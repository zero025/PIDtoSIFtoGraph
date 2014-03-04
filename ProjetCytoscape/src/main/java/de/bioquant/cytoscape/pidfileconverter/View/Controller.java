/** This class is responsible for the Actions and the methods they call.
 * 
 * @author Hadi Kang
 * @contributor Yamei & Thomas
 */

package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ManualLayout.common.GraphConverter2;
import cytoscape.Cytoscape;
import cytoscape.graph.layout.algorithm.MutablePolyEdgeGraphLayout;
import cytoscape.graph.layout.impl.RotationLayouter;
import cytoscape.layout.CyLayouts;
import cytoscape.visual.VisualStyle;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.FileReader.AffymetrixRegexReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.PidFileReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.ProcessAffymetrix;
import de.bioquant.cytoscape.pidfileconverter.FileReader.ProcessConvert;
import de.bioquant.cytoscape.pidfileconverter.FileReader.ProcessIllumina;
import de.bioquant.cytoscape.pidfileconverter.FileReader.ProcessSubgraph;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.EntrezGeneIdforEntrezGeneWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ExtPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.IdWithPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ModificationsWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.NodeTypeAttributeForIDWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PidForIDWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PreferredSymbolForIDWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.SifFileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.UniprotIdForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion.SifFileExpandMolWriter;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import edu.ucsd.bioeng.coreplugin.tableImport.reader.DefaultAttributeTableReader;
import edu.ucsd.bioeng.coreplugin.tableImport.ui.ImportAttributeTableTask;

@SuppressWarnings("serial")
public class Controller extends JFrame implements ActionListener {

	// boolean to set if convertbutton is pressed
	private static boolean isConverted = false;
	private static Step1 step1;
	private static Step2 step2;
	private static Step3 step3;
	private AffymetrixView affymetrixview;
	private IlluminaView illuminaview;
	private SplashFrame convertFrame;
	private SplashFrame affymetrixApplyFilterFrame;
	private SplashFrame illuminaApplyFilterFrame;
	private SplashFrame subgraphFrame;
	private String inputfilepath;
	private static String inputbarcode1;
	private static String inputbarcode2;
	private static String inputFileIllumina;
	private static String targetSIFpath;
	private static String targetfilteredSIFpath;
	private String targetsubgraphedSIFpath;
	private static String targetNODE_TYPEpath;
	private static String targetUNIPROTpath;
	private static String targetENTREZGENEpath;
	private static String targetMODIFICATIONSpath;
	private static String targetPREFERRED_SYMBOLpath;
	private static String targetPREFERRED_SYMBOL_EXTpath;
	private static String targetPIDpath;
	private static String targetID_PREFpath;
	private static String targetCytoIDtoIDFilepath;
	private static String targetUniqueIDFilepath;
	private static String targetUniProtToGeneIDMapFilepath;
	private static String targetGeneIDtoAffymetrixMapFilepath;
	private File curFile = null;
	private File barcode1File = null;
	private File barcode2File = null;
	private File fileIllumina = null;
	private File genetargetFile = null;
	private File sigmolsourceFile = null;
	private File sigmoltargetFile = null;
	private File currentDirectory = null;

	// creation of the File Chooser for the controller
	private JFileChooser fc = new JFileChooser(".");

	// the file concatenation of the (filtered_absent_proteins)
	private static final String ABSENT_PROTEINS_CONCATENATION = "(filtered_absent_proteins)";
	private static final String SUBGRAPHED = "(subgraphed)";
	
	/**
	 * The constructor for the step 1 controller
	 * 
	 * @param mainframe
	 */
	public Controller(Step1 step1) {
		Controller.step1 = step1;
	}
	
	/**
	 * The constructor for the step 2 controller
	 * 
	 * @param mainframe
	 */
	public Controller(Step2 step2) {
		Controller.step2 = step2;
		
	}
	
	/**
	 * The constructor for the step 2 controller
	 * 
	 * @param mainframe
	 */
	public Controller(Step3 step3) {
		Controller.step3 = step3;
		// load up the static hashmap uniprotToGeneID if it has not been done yet.
		//Used to match uniprotID and EntrezGeneID in the files of source and target molecules.
		if (AffymetrixRegexReader.getUniprottogeneidFullhashmap().size() == 0){
			try {
				AffymetrixRegexReader.makeHashMapUniProtToGeneID();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * The constructor for the affymetrixview controller
	 * 
	 * @param affymetrixview
	 */
	public Controller(AffymetrixView affymetrixview) {
		this.affymetrixview = affymetrixview;
		// load up the static hashmap mappings once the window opens
		try {
			AffymetrixRegexReader.makeHashMapUniProtToGeneID();
			AffymetrixRegexReader.makeHashMapGeneIDtoAffyID();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The constructor for the illuminaview controller
	 * 
	 * @param illuminaview
	 */
	public Controller(IlluminaView illuminaview) {
		this.illuminaview = illuminaview;
		try {
			AffymetrixRegexReader.makeHashMapUniProtToGeneID();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method receives a set of ActionEvents denoted in the viewframes
	 * (mainframe, affy and illumina views). The ActionEvents then define what
	 * sort of action is to be carried out
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		// if user clicks browse button
		if (command.equals("Browse")) {
			browseInputFile();
		}

		// if user clicks output button
		if (command.equals("Output")) {
			browseOutputFilePath();
		}
		if (command.equals("Help Step 1")) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Check to automatically add into the graph the nodes which"
									+ "\n"
									+ "are each families of protein complex without predecessors.",
							"What's this CheckBox?",
							JOptionPane.INFORMATION_MESSAGE);
		}
		if (command.equals("Next 1")) {
			if (!isConverted) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"Please convert first in Step 1 above before continuing.",
								"Warning", JOptionPane.WARNING_MESSAGE);
			}
			if (isConverted) {
				if (step2 == null){
					try {
						step1.setVisible(false);
						step2= new Step2(this);
						step2.setLocationRelativeTo((JFrame)step1);
						//step2.setSize(step1.getSize());
						step2.requestFocus();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					step1.setVisible(false);
					step2.setVisible(true);
					step2.requestFocus();
				}
			}
		}
		// if user clicks choose button
		if (command.equals("Choose")) {
				runChoose();
		}
		if (command.equals("Help Step 2")) {
			JOptionPane
			.showMessageDialog(
					new JFrame(),
					"To filter the graph, choose between an Affymetrix file or "
					+ "an Illumina file",
					"What's this CheckBox?",
					JOptionPane.INFORMATION_MESSAGE);
		}
		if (command.equals("Back 1")) {
			
			step2.setVisible(false);
			step1.setVisible(true);
			step1.requestFocus();
		}
		if (command.equals("Next 2")) {
			if (step3==null){

			try {
				step3 = new Step3(this);
				step3.setLocationRelativeTo(step2);
				step3.requestFocus();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			else{
				step2.setVisible(false);
				step3.setVisible(true);
				step3.requestFocus();
			}
		}
		// if user clicks browse condition 1 button
		if (command.equals("Affymetrix Browse Condition 1")) {
			browseBarcode1File();
			// TODO: check if file really exists!!
		}

		// if user clicks browse condition 2 button
		if (command.equals("Affymetrix Browse Condition 2")) {
			browseBarcode2File();
		}

		// if user clicks browse condition 1 button
		if (command.equals("Illumina Browse File")) {
			browseFileIllumina();
			// TODO: check if file really exists!!
		}

		// if user clicks apply filter button
		if (command.equals("Affymetrix Apply Filter")) {

			// not, show the warning message.
			if (affymetrixview.getInputcondition1field().getText().equals("")
					|| affymetrixview.getInputcondition2field().getText()
							.equals("")) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"Please choose 2 barcode files using the browse buttons",
								"Warning", JOptionPane.WARNING_MESSAGE);
			} else if (!affymetrixview.getInputcondition1field().getText()
					.equals("")
					&& !affymetrixview.getInputcondition2field().getText()
							.equals("")) {
				// only if the two fields are not the same then try to read the
				// files. Else warning message!
				if (affymetrixview
						.getInputcondition1field()
						.getText()
						.equals(affymetrixview.getInputcondition2field()
								.getText())) {
					JOptionPane.showMessageDialog(new JFrame(),
							"Please choose 2 DIFFERENT barcode files",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {

					// create the splashframe
					affymetrixApplyFilterFrame = new SplashFrame(
							"Affymetrix Apply Filter", this);
				}
			}
		}

		if (command.equals("Start Affymetrix Apply Filter")) {

			// Start a processus to do the filtering

			ProcessAffymetrix process = new ProcessAffymetrix(
					affymetrixApplyFilterFrame, this, affymetrixview,
					targetSIFpath, targetfilteredSIFpath);

			Thread t = new Thread(process);
			affymetrixApplyFilterFrame.setProcess(process);
			t.start();
		}
		
		if (command.equals("Stop Affymetrix Apply Filter")) {
			if (affymetrixApplyFilterFrame.getProcess() != null){
				affymetrixApplyFilterFrame.getProcess().setContinueThread(false);
			}
			affymetrixApplyFilterFrame.dispose();
		}
		
		// if user clicks help button
		if (command.equals("Affymetrix Help")) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Please input two files in barcode format, one for the experiment, the other the control."
									+ "\n"
									+ "An example of the barcode format:"
									+ "\n"
									+ "\"1007_s_at\",1"
									+ "\n"
									+ "\"1053_at\",0", "Take Note!",
							JOptionPane.INFORMATION_MESSAGE);
		}
		
		// if user clicks apply filter button
		if (command.equals("Illumina Apply Filter")) {
			// checks if the user has selected 2 files in the two fields. if
			// not, show the warning message.
			if (illuminaview.getInputFileField().getText().equals("")) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Please choose a file using the browse buttons",
						"Warning", JOptionPane.WARNING_MESSAGE);
			} else if (!illuminaview.getInputFileField().getText().equals("")) {
				// create the splashframe
				illuminaApplyFilterFrame = new SplashFrame(
						"Illumina Apply Filter", this);
			}
		}

		if (command.equals("Start Illumina Apply Filter")) {

			// Start a processus to do the filtering

			ProcessIllumina process = new ProcessIllumina(
					illuminaApplyFilterFrame, this, illuminaview,
					targetSIFpath, targetfilteredSIFpath);

			Thread t = new Thread(process);
			illuminaApplyFilterFrame.setProcess(process);
			t.start();

		}
		
		if (command.equals("Stop Illumina Apply Filter")) {
			if (illuminaApplyFilterFrame.getProcess() !=null){
			illuminaApplyFilterFrame.getProcess().setContinueThread(false);
			}
			illuminaApplyFilterFrame.dispose();
		}
		
		if (command.equals("Illumina Help")) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Please input a .csv Illumina file, and the name of two experiments."
									+ "\n"
									+ "A Illumina condition file has lots of columns."
									+ "\n"
									+ "In the \"DD\" column you should find the EntrezGeneID"
									+ "\n"
									+ "The columns from \"C\" to \"CT\" are conditions, with several sets of .mean, .sd, .p and .nbeads columns"
									+ "An example of experiment name is  : My4 6 P_5753685072_B.",
							"Help", JOptionPane.INFORMATION_MESSAGE);
		}
		// if user runs the convert button
		if (command.equals("Convert")) {
			// checks if the user has typed a file path into the input
			// textfield. if not, alert!
			if (step1.getInputTextFieldText().equals("")) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Please enter a file using the browse button above",
						"Warning", JOptionPane.WARNING_MESSAGE);
			} else if (!step1.getInputTextFieldText().equals("")) {

				// create the splashframe
				convertFrame = new SplashFrame("convert", this);
			}
		}
		if (command.equals("Start convert")) {

			// Start a processus to do the conversion

			ProcessConvert process = new ProcessConvert(convertFrame, this,
					step1, inputfilepath, curFile);
			Thread t = new Thread(process);
			convertFrame.setProcess(process);
			t.start();

		}
		
		if (command.equals("Stop convert")) {
			if (convertFrame.getProcess() != null){
				convertFrame.getProcess().setContinueThread(false);
			}
			convertFrame.dispose();
		}
		
		if (command.equals("Subgraph")) {
			try {
				if (!isConverted) {
					JOptionPane
							.showMessageDialog(
									new JFrame(),
									"Please convert first in Step 1 above before continuing.",
									"Warning", JOptionPane.WARNING_MESSAGE);
				}
				if (isConverted) {
					// check the source and target fields for emptiness
					if (step3.getSourceSourceTextField().getText().trim()
							.equals("")
							&&
							// mainframe.getGenesourcetextfield().getText().trim().equals("")&&
							step3.getCytoidSourceTextArea().getText()
									.trim().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Please enter at least one file in SOURCE",
								"Warning", JOptionPane.WARNING_MESSAGE);
					} else if (step3.getGeneTargetTextField().getText()
							.trim().equals("")
							&& step3.getSourceTargetTextField().getText()
									.trim().equals("")
							&& step3.getCytoidTargetTextArea().getText()
									.trim().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Please enter at least one file in TARGET",
								"Warning", JOptionPane.WARNING_MESSAGE);
					} else {
						// create the splashframe
						subgraphFrame = new SplashFrame("subgraph", this);
					}
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Error :  "
						+ ex.getClass().getName(), "Warning",
						JOptionPane.WARNING_MESSAGE);
			}

		}
		if (command.equals("Start subgraph")) {

			// Start a processus to subgraph

			ProcessSubgraph process = new ProcessSubgraph(subgraphFrame, this,
					step3, targetSIFpath, targetsubgraphedSIFpath);
			Thread t = new Thread(process);
			subgraphFrame.setProcess(process);
			t.start();

		}
		if (command.equals("Stop subgraph")) {
			if (subgraphFrame.getProcess() != null){
			subgraphFrame.getProcess().setContinueThread(false);
			}
			subgraphFrame.dispose();
		}
		if (command.equals("Gene Target Browse")) {
			browseGeneTarget();
		}
		if (command.equals("Source Source Browse")) {
			browseSigmolSource();
		}
		if (command.equals("Source Target Browse")) {
			browseSigmolTarget();
		}
		if (command.equals("Help Step 3")) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"For gene/signalling molecule source and target, input a text file in the following format:"
									+ "\n"
									+ "'P12345"
									+ "\n"
									+ " Q67890"
									+ "\n"
									+ " O12345"
									+ "\n"
									+ " ..............'"
									+ "\n"
									+ "For CytoID source and target text areas, just Copy (Ctrl+C) the node IDs,"
									+ "\n"
									+ "then Paste (Ctrl+V) in the text areas.",
							"Take Note!", JOptionPane.INFORMATION_MESSAGE);
		}
		if (command.equals("Back 2")) {
			step3.setVisible(false);
			step2.setVisible(true);
			step2.requestFocus();
			
		}
		if (command.equals("Quit")) {
			step1.dispose();
			if (step2 != null){
				step2.dispose();
			}
			if (step3 != null){
				step3.dispose();
			}
		}
	}

	public void setConverted(boolean isConverted) {
		Controller.isConverted = isConverted;
	}

	/**
	 * This method checks the states of the affymetrix and illumina checkboxes
	 * in mainframe and runs the appropriate commands to open the respective
	 * window. This method checks if neither checkboxes are checked, or both are
	 * checked, and gives a warning message to select only one checkbox.
	 */
	private void runChoose() {
		boolean isaffymetrixselected = step2.isAffymetrixSelected();
		boolean isilluminaselected = step2.isIlluminaSelected();

		// if neither checkboxes are checked
		if (!isaffymetrixselected && !isilluminaselected) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select one single option.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}

		// if only affymetrix is checked
		if (isaffymetrixselected) {
			affymetrixview = new AffymetrixView(this);
			affymetrixview.setTitle("Import Barcode Output");
			affymetrixview.setLocation(30, 30);
			affymetrixview.setSize(400, 300);
			affymetrixview.setResizable(false);
			affymetrixview.setVisible(true);
			affymetrixview.pack();
		}

		// if only illumina is checked
		if (isilluminaselected) {
			illuminaview = new IlluminaView(this);
			illuminaview.setTitle("Illumina View");
			illuminaview.setLocation(80, 80);
			illuminaview.setSize(400, 300);
			illuminaview.setResizable(false);
			illuminaview.setVisible(true);
			illuminaview.pack();
		}

	}

	/**
	 * This method rotates the graph created by 180 degrees. Suitable for
	 * rotating upside-down hierarchical graphs. source:
	 * http://chianti.ucsd.edu/svn/coreplugins/tags/cyto-
	 * 2_3/ManualLayout/src/ManualLayout/rotate/RotateAction.java
	 */
	private void rotateGraph() {
		MutablePolyEdgeGraphLayout[] nativeGraph = new MutablePolyEdgeGraphLayout[] { GraphConverter2
				.getGraphReference(16.0d, true, false) };
		RotationLayouter[] rotation = new RotationLayouter[] { new RotationLayouter(
				nativeGraph[0]) };

		// the actual rotation
		rotation[0].rotateGraph(1.0d * Math.PI);

		// refreshing the view
		Cytoscape.getCurrentNetworkView().updateView();
	}

	/**
	 * This method creates a network graph from the string 's', which should be
	 * a path to a Cytoscape readable file, e.g. SIF
	 * 
	 * @param s
	 *            the path of the file to be read.
	 */
	public void drawGraphFromSIF(String s) {
		// read the sif file and create the network
		Cytoscape.createNetworkFromFile(s);
		Cytoscape.getCurrentNetworkView().redrawGraph(false, true);
	}

	/**
	 * This method loads the attribute NA files
	 * 
	 * @param s
	 *            the path of the NA file to be loaded
	 */
	public void loadNodeAttributeFile(String s) {
		Cytoscape.loadAttributes(new String[] { s }, new String[] {});
		Cytoscape.getCurrentNetworkView().updateView();
		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);
	}

	/**
	 * This method loads the attribute NA files
	 * 
	 * @param s
	 *            the path of the NA file to be loaded
	 */
	@SuppressWarnings("unused")
	private void loadNodeAttributeFileFromGraph(String s) {
		Cytoscape.loadAttributes(new String[] { s }, new String[] {});
		Cytoscape.getCurrentNetworkView().updateView();
		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);

		// TODO: big construction site here, to find out how to import network
		// file from table
		ImportAttributeTableTask task = new ImportAttributeTableTask(
				new DefaultAttributeTableReader(null, null, 0, null, true), s);
		task.run();
		// http://chianti.ucsd.edu/svn/cytoscape/trunk/coreplugins/TableImport/src/main/java/edu/ucsd/bioeng/coreplugin/tableImport/ui/ImportTextTableDialog.java
		// AttributeMappingParameters mapping;
		// mapping = new AttributeMappingParameters(objType, del,
		// listDelimiter, keyInFile,
		// mappingAttribute, aliasList,
		// attributeNames, attributeTypes,
		// listDataTypes, importFlag, caseSensitive);
		Cytoscape.firePropertyChange(Cytoscape.NEW_ATTRS_LOADED, null, null);

		// try {
		// ImportTextTableDialog ittd = new
		// ImportTextTableDialog(Cytoscape.getDesktop(), true, 1);
		// ittd.pack();
		// ittd.setLocationRelativeTo(Cytoscape.getDesktop());
		// ittd.setVisible(true);
		//
		// } catch (JAXBException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * This method loads the vizmap property file and redraws the graph Also
	 * does the layout hierarchically and rotates the graph 180 degrees
	 * 
	 * @param s
	 *            the name of the vizmap property file
	 */
	public void mapVisually(String s) {
		// load the vizmap file
		Cytoscape.firePropertyChange(Cytoscape.VIZMAP_LOADED, null, s);
		VisualStyle vs = Cytoscape.getVisualMappingManager()
				.getCalculatorCatalog().getVisualStyle("netView");
		Cytoscape.getCurrentNetworkView().setVisualStyle(vs.getName()); // not
																		// strictly
																		// necessary

		// actually apply the visual style
		Cytoscape.getVisualMappingManager().setVisualStyle(vs);
		Cytoscape.getCurrentNetworkView().redrawGraph(true, true);

		// TODO: should ask user whether he wants this? set the layout as
		// hierarchical
		CyLayouts.getLayout("hierarchical").doLayout();
		if (Cytoscape.getCurrentNetwork().getNodeCount() < 1000) {
			// TODO: sometimes rotating the graph causes some crash errors. thus
			// only rotate if hierarchichal layout!
			rotateGraph();
		}
	}

	/**
	 * This method gets the filepath from the input file text area and then
	 * converts that xml file into SIF files
	 * 
	 * @param filepath
	 */
	public void convertFile(String filepath, SplashFrame sp, ProcessConvert process) {
		
		// if the outputfiletextfield is empty, output file folder is same as
		// input file's
		if (step1.getOutputTextFieldText().trim().equals("")) {
			String[] temporarypath = null;
			// if a file with xml ending
			if (curFile.getAbsolutePath().endsWith("xml")) {
				// set the SIF file path
				temporarypath = curFile.getAbsolutePath().split(".xml");
				setTargetSIFpath(temporarypath[0].concat(".sif"));
			} else if (curFile.getAbsolutePath().endsWith("sif")) {
				temporarypath = curFile.getAbsolutePath().split(".sif");
				setTargetSIFpath(curFile.getAbsolutePath());
				// for loading of pre-filtered SIF files
				if (curFile.getAbsolutePath().contains(
						getAbsentProteinsConcatenation())) {
					temporarypath[0] = temporarypath[0].replace(
							getAbsentProteinsConcatenation(), "");
				}
				// for loading of pre-subgraphed SIF files
				if (curFile.getAbsolutePath().contains(getSubgraphed())) {
					temporarypath[0] = temporarypath[0].replace(
							getSubgraphed(), "");
				}
			}
			// set the target node type NA path
			setTargetNODE_TYPEpath(temporarypath[0].concat(".NODE_TYPE.NA"));
			// set the UNIPROT NA path
			setTargetUNIPROTpath(temporarypath[0].concat(".UNIPROT.NA"));
			// set the ENTREZGENE NA path
			setTargetENTREZGENEpath(temporarypath[0].concat(".ENTREZGENE.NA"));
			// set the MODIFICATIONS NA path
			setTargetMODIFICATIONSpath(temporarypath[0]
					.concat(".MODIFICATIONS.NA"));
			// set the PREFERRED_SYMBOL NA path
			setTargetPREFERRED_SYMBOLpath(temporarypath[0]
					.concat(".PREFERRED_SYMBOL.NA"));
			// set the PREFERRED_SYMBOL_EXT NA path
			setTargetPREFERRED_SYMBOL_EXTpath(temporarypath[0]
					.concat(".PREFERRED_SYMBOL_EXT.NA"));
			// set the PID NA path
			setTargetPIDpath(temporarypath[0].concat(".PID.NA"));
			// set the ID_PREF NA path
			setTargetID_PREFpath(temporarypath[0].concat(".ID_PREF.NA"));
			// set the CytoIDtoIDFile path
			setTargetCytoIDtoIDFilepath(temporarypath[0]
					.concat(".CytoIDToID.NA"));
			// set the UniqueID path
			setTargetUniqueIDFilepath(temporarypath[0].concat(".UNIQUEID.NA"));
			// set the UniProt to GeneID map file path
			setTargetUniProtToGeneIDMapFilepath(temporarypath[0]
					.concat(".UPToGeneIDMap.NA"));
			// set the GeneID to Affymetrix map file path
			setTargetGeneIDtoAffymetrixMapFilepath(temporarypath[0]
					.concat(".GeneIDToAffyMap.NA"));
		}
		this.inputfilepath = filepath;
		if (inputfilepath.endsWith("xml")) {
			NodeManagerImpl manager = NodeManagerImpl.getInstance();
			PidFileReader reader = PidFileReader.getInstance();
			reader.setSplashFrameAndProcess(sp, process);
			try {
				
				//The longest part of the function (in processing time)
				reader.read(inputfilepath);
				if(!process.isContinueThread()){
					return;
				}
				
			} catch (NoValidManagerSetException e1) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"Program error, please contact hadi.kang@bioquant.uni-heidelberg.de for assistance.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			} catch (FileParsingException e1) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}

			FileWriter writer = SifFileWriter.getInstance();
			try {
				writer.write(getTargetSIFpath(), manager);
				sp.getBar().setValue(85);
				if(!process.isContinueThread()){
					return;
				}
				if (step1.isExpandChecked()) {
					writer = SifFileExpandMolWriter.getInstance();
					writer.write(getTargetSIFpath(), manager);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(new JFrame(), "File write error",
						"Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}

			FileWriter nWriter = NodeTypeAttributeForIDWithModWriter
					.getInstance();
			try {
				nWriter.write(getTargetNODE_TYPEpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter uWriter = UniprotIdForUniprotWithModWriter.getInstance();
			try {
				uWriter.write(getTargetUNIPROTpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter eWriter = EntrezGeneIdforEntrezGeneWithModWriter
					.getInstance();
			try {
				eWriter.write(getTargetENTREZGENEpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter modiWriter = ModificationsWriter.getInstance();
			try {
				modiWriter.write(getTargetMODIFICATIONSpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter pWriter = PreferredSymbolForIDWithModWriter
					.getInstance();
			try {
				pWriter.write(getTargetPREFERRED_SYMBOLpath(), manager);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			pWriter = ExtPreferredSymbolWriter.getInstance();
			try {
				pWriter.write(getTargetPREFERRED_SYMBOL_EXTpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter pidWriter = PidForIDWithModWriter.getInstance();
			try {
				pidWriter.write(getTargetPIDpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter prefIdWriter = IdWithPreferredSymbolWriter.getInstance();
			try {
				prefIdWriter.write(getTargetID_PREFpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} else if (inputfilepath.endsWith("sif")) {

		} else // not an xml or SIF file
		{
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
							"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * This method opens a file chooser dialog box and sets the input text field
	 * string to the absolute path of the file
	 */
	private void browseInputFile() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose an XML file");
			FileNameExtensionFilter xmldata = new FileNameExtensionFilter(
					"XML", "xml");
			FileNameExtensionFilter sifdata = new FileNameExtensionFilter(
					"SIF", "sif");
			fc.addChoosableFileFilter(xmldata);
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name und path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				curFile = fc.getSelectedFile();
				currentDirectory = fc.getCurrentDirectory();
				if (curFile.getAbsolutePath().endsWith("xml")) {
					inputfilepath = curFile.getAbsolutePath();
					step1.setInputTextField(inputfilepath);

					// targetSIFpath is set here by default
					String[] temporarypath = curFile.getAbsolutePath().split(
							".xml");
					setTargetSIFpath(temporarypath[0].concat(".sif"));
				}
				if (curFile.getAbsolutePath().endsWith("sif")) {
					inputfilepath = curFile.getAbsolutePath();
					step1.setInputTextField(inputfilepath);
				}
			}

		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Please select a valid xml file downloaded from Pathway Interaction Database, or a converted SIF file.",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * This method selects the folder in which the output file should be placed.
	 * if there are same named files present, they will be overwrite!
	 */
	private void browseOutputFilePath() {
		String filedirectory = "";
		String filepath = "";
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // select directories or files
			fc.setDialogTitle("Please choose a directory to save the converted file(s)");
			fc.setCurrentDirectory(currentDirectory);

			FileNameExtensionFilter sifdata = new FileNameExtensionFilter(
					"SIF", "sif");
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			
			// get name and path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				//Adapt the separator in the path, according to the operating system : / for Linux and Mac, \ for Windows.
				String separator;
				if (System.getProperty("os.name").contains("Windows")){
					separator = "\\";
				}else{
					separator = "/";
				}
				
				// if the selected is a directory
				if (fc.getSelectedFile().isDirectory()) {
					// get the absolute path of the directory in which the file lies
					filepath = fc.getSelectedFile().getAbsolutePath();
					// split the file name
					String[] temporarypath = curFile.getName().split(".xml");

					// set the target SIF path
					setTargetSIFpath(filepath.concat("\\").concat(
							temporarypath[0].concat(".sif")));

					// set the target node type NA path
					setTargetNODE_TYPEpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".NODE_TYPE.NA")));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".UNIPROT.NA")));

					// set the target entrezGene NA path
					setTargetENTREZGENEpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".ENTREZGENE.NA")));

					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".MODIFICATIONS.NA")));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".PREFERRED_SYMBOL.NA")));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filepath.concat(separator)
							.concat(temporarypath[0]
									.concat(".PREFERRED_SYMBOL_EXT.NA")));

					// set the target PID NA path
					setTargetPIDpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".PID.NA")));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filepath.concat(separator).concat(
							temporarypath[0].concat(".ID_PREF.NA")));

					// set the CytoIDtoIDFile path
					setTargetCytoIDtoIDFilepath(filepath.concat(separator).concat(
							temporarypath[0].concat(".CytoIDToID.NA")));

					// set the UniqueIDFile path
					setTargetUniqueIDFilepath(filepath.concat(separator).concat(
							temporarypath[0].concat(".UNIQUEID.NA")));

					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filepath.concat(separator)
							.concat(temporarypath[0]
									.concat(".UPToGeneIDMap.NA")));

					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filepath
							.concat(separator).concat(
									temporarypath[0]
											.concat(".GeneIDToAffyMap.NA")));

				} else // if the selected is a file
				{
					// get the directory path
					filedirectory = fc.getCurrentDirectory().getAbsolutePath();
					// split the current file into one without .xml
					String[] temporarypath = curFile.getName().split(".xml");

					// set the target SIF path
					setTargetSIFpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".sif"));

					// set the target node type NA path
					setTargetNODE_TYPEpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".NODE_TYPE.NA"));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".UNIPROT.NA"));

					// set the target entrezGene NA path
					setTargetENTREZGENEpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".ENTREZGENE.NA"));

					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filedirectory.concat(separator)
							.concat(temporarypath[0])
							.concat(".MODIFICATIONS.NA"));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filedirectory.concat(separator)
							.concat(temporarypath[0])
							.concat(".PREFERRED_SYMBOL.NA"));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filedirectory
							.concat(separator).concat(temporarypath[0])
							.concat(".PREFERRED_SYMBOL_EXT.NA"));

					// set the target PID NA path
					setTargetPIDpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".PID.NA"));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filedirectory.concat(separator)
							.concat(temporarypath[0]).concat(".ID_PREF.NA"));

					// set the CytoIDtoIDFile path
					setTargetCytoIDtoIDFilepath(filedirectory.concat(separator)
							.concat(temporarypath[0].concat(".CytoIDToID.NA")));

					// set the UniqueIDFile path
					setTargetUniqueIDFilepath(filedirectory.concat(separator)
							.concat(temporarypath[0].concat(".UNIQUEID.NA")));

					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filedirectory.concat(
							separator).concat(
							temporarypath[0].concat(".UPToGeneIDMap.NA")));

					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filedirectory
							.concat(separator).concat(
									temporarypath[0]
											.concat(".GeneIDToAffyMap.NA")));
				}
				step1.setOutputTextFieldText(targetSIFpath);
			}
			currentDirectory = fc.getCurrentDirectory();

		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Please select a valid xml file downloaded from Protein Interaction Database.",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * this method browses for the first barcode file
	 */
	public void browseBarcode1File() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose Barcode file of Condition 1");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name and path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				barcode1File = fc.getSelectedFile();
				// set the input barcode 1
				setInputbarcode1(barcode1File.getAbsolutePath());
				// put the absolute path in the textfield
				affymetrixview.setInput1FieldText(inputbarcode1);
			}
			currentDirectory = fc.getCurrentDirectory();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select a Barcode file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * this method browses for the second barcode file
	 */
	public void browseBarcode2File() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose Barcode file of Condition 2");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name and path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				barcode2File = fc.getSelectedFile();
				// set the input barcode 2
				setInputbarcode2(barcode2File.getAbsolutePath());
				// put the absolute path in the textfield
				affymetrixview.setInput2FieldText(inputbarcode2);
			}
			currentDirectory = fc.getCurrentDirectory();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select a Barcode file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * this method browses for the first file of Illumina
	 */
	public void browseFileIllumina() {
		try {
			fc.setDialogTitle("Please choose an Illumina file");
			fc.setCurrentDirectory(currentDirectory);
			
			FileNameExtensionFilter csvdata = new FileNameExtensionFilter(
					"CSV", "csv");
			fc.addChoosableFileFilter(csvdata);
			
			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name and path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileIllumina = fc.getSelectedFile();
				// set the input file
				setInputFileIllumina(fileIllumina.getAbsolutePath());
				// put the absolute path in the textfield
				illuminaview.setInputFileFieldText(inputFileIllumina);
			}
			currentDirectory = fc.getCurrentDirectory();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select an Illumina file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a gene target
	 * file
	 */
	private void browseGeneTarget() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name und path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				genetargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				step3.setGeneTargetTextFieldText(genetargetFile
						.getAbsolutePath());
			}
			currentDirectory = fc.getCurrentDirectory();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select a Text file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a sigmol
	 * source file
	 */
	private void browseSigmolSource() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name und path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sigmolsourceFile = fc.getSelectedFile();
				currentDirectory = fc.getCurrentDirectory();
				// put the absolute path in the textfield
				step3.setSourceSourceTextFieldText(sigmolsourceFile
						.getAbsolutePath());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select a Text file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a s file
	 */
	private void browseSigmolTarget() {
		try {
			// JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the
														// file browser
			// get name und path
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sigmoltargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				step3.setSourceTargetTextFieldText(sigmoltargetFile
						.getAbsolutePath());
			}
			currentDirectory = fc.getCurrentDirectory();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please select a Text file", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	public String getTargetSIFpath() {
		return targetSIFpath;
	}

	public void setTargetSIFpath(String targetSIFpath) {
		Controller.targetSIFpath = targetSIFpath;
	}

	public String getTargetNODE_TYPEpath() {
		return targetNODE_TYPEpath;
	}

	public void setTargetNODE_TYPEpath(String targetNODE_TYPEpath) {
		Controller.targetNODE_TYPEpath = targetNODE_TYPEpath;
	}

	public String getTargetUNIPROTpath() {
		return targetUNIPROTpath;
	}

	public String getTargetENTREZGENEpath() {
		return targetENTREZGENEpath;
	}

	public void setTargetUNIPROTpath(String targetUNIPROTpath) {
		Controller.targetUNIPROTpath = targetUNIPROTpath;
	}

	public void setTargetENTREZGENEpath(String targetENTREZGENEpath) {
		Controller.targetENTREZGENEpath = targetENTREZGENEpath;
	}

	public String getTargetMODIFICATIONSpath() {
		return targetMODIFICATIONSpath;
	}

	public void setTargetMODIFICATIONSpath(String targetMODIFICATIONSpath) {
		Controller.targetMODIFICATIONSpath = targetMODIFICATIONSpath;
	}

	public String getTargetPREFERRED_SYMBOLpath() {
		return targetPREFERRED_SYMBOLpath;
	}

	public void setTargetPREFERRED_SYMBOLpath(String targetPREFERRED_SYMBOLpath) {
		Controller.targetPREFERRED_SYMBOLpath = targetPREFERRED_SYMBOLpath;
	}

	public String getTargetPREFERRED_SYMBOL_EXTpath() {
		return targetPREFERRED_SYMBOL_EXTpath;
	}

	public void setTargetPREFERRED_SYMBOL_EXTpath(
			String targetPREFERRED_SYMBOL_EXTpath) {
		Controller.targetPREFERRED_SYMBOL_EXTpath = targetPREFERRED_SYMBOL_EXTpath;
	}

	public String getTargetPIDpath() {
		return targetPIDpath;
	}

	public void setTargetPIDpath(String targetPIDpath) {
		Controller.targetPIDpath = targetPIDpath;
	}

	public String getTargetID_PREFpath() {
		return targetID_PREFpath;
	}

	public void setTargetID_PREFpath(String targetID_PREFpath) {
		Controller.targetID_PREFpath = targetID_PREFpath;
	}

	public String getTargetCytoIDtoIDFilepath() {
		return targetCytoIDtoIDFilepath;
	}

	public void setTargetCytoIDtoIDFilepath(String targetCytoIDtoIDFilepath) {
		Controller.targetCytoIDtoIDFilepath = targetCytoIDtoIDFilepath;
	}

	public String getTargetUniqueIDFilepath() {
		return targetUniqueIDFilepath;
	}

	public void setTargetUniqueIDFilepath(String targetUniqueIDFilepath) {
		Controller.targetUniqueIDFilepath = targetUniqueIDFilepath;
	}

	public String getTargetUniProtToGeneIDMapFilepath() {
		return targetUniProtToGeneIDMapFilepath;
	}

	public void setTargetUniProtToGeneIDMapFilepath(
			String targetUniProtToGeneIDMapFilepath) {
		Controller.targetUniProtToGeneIDMapFilepath = targetUniProtToGeneIDMapFilepath;
	}

	public String getTargetGeneIDtoAffymetrixMapFilepath() {
		return targetGeneIDtoAffymetrixMapFilepath;
	}

	public void setTargetGeneIDtoAffymetrixMapFilepath(
			String targetGeneIDtoAffymetrixMapFilepath) {
		Controller.targetGeneIDtoAffymetrixMapFilepath = targetGeneIDtoAffymetrixMapFilepath;
	}

	public String getInputbarcode1() {
		return inputbarcode1;
	}

	public void setInputbarcode1(String inputbarcode1) {
		Controller.inputbarcode1 = inputbarcode1;
	}

	public String getInputbarcode2() {
		return inputbarcode2;
	}

	public void setInputbarcode2(String inputbarcode2) {
		Controller.inputbarcode2 = inputbarcode2;
	}

	public String getInputFileIllumina() {
		return inputFileIllumina;
	}

	public void setInputFileIllumina(String inputfile) {
		Controller.inputFileIllumina = inputfile;
	}

	public static String getAbsentProteinsConcatenation() {
		return ABSENT_PROTEINS_CONCATENATION;
	}

	public static String getSubgraphed() {
		return SUBGRAPHED;
	}

}
