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
import javax.xml.bind.JAXBException;

import ManualLayout.common.GraphConverter2;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.graph.layout.algorithm.MutablePolyEdgeGraphLayout;
import cytoscape.graph.layout.impl.RotationLayouter;
import cytoscape.layout.CyLayouts;
import cytoscape.visual.VisualStyle;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.FileReader.FileReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.PidFileReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.AffymetrixRegexReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.IlluminaRegexReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.SubgraphExtraction;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.EntrezGeneIdforEntrezGeneWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ExtPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.IdWithPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ModificationsWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.NodeTypeAttributeForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PidForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PreferredSymbolForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.SifFileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.UniprotIdForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.EntrezGeneIdforEntrezGeneWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion.SifFileExpandMolWriter;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import edu.ucsd.bioeng.coreplugin.tableImport.reader.AttributeMappingParameters;
import edu.ucsd.bioeng.coreplugin.tableImport.reader.DefaultAttributeTableReader;
import edu.ucsd.bioeng.coreplugin.tableImport.ui.ImportAttributeTableTask;
import edu.ucsd.bioeng.coreplugin.tableImport.ui.ImportTextTableDialog;

/**


/**
 * This class is responsible for the Actions and the methods they call.
 * @author Hadi Kang
 *
 */
@SuppressWarnings("serial")
public class Controller extends JFrame implements ActionListener{
	
	// boolean to set if convertbutton is pressed
	private boolean isConverted = false;
	private MainFrame mainframe;
	private AffymetrixView affymetrixview;
	private IlluminaView illuminaview;
	private String inputfilepath;
	private static String inputbarcode1;
	private static String inputbarcode2;
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
	private static String targetIDCytoUniProtFilepath;
	private static String targetIDCytoEntrezGeneFilepath;
	private static String targetUniqueUniProtFilepath;
	private static String targetUniqueEntrezGeneFilepath;
	private static String targetUniProtToGeneIDMapFilepath;
	private static String targetEntrezGeneToGeneIDMapFilepath;
	private static String targetGeneIDtoAffymetrixMapFilepath;
	private static String targetGeneIDtoIlluminaMapFilepath;
	private File curFile = null; 
	private File barcode1File = null;
	private File barcode2File = null;
	private File genesourceFile = null;
	private File genetargetFile = null;
	private File sigmolsourceFile = null;
	private File sigmoltargetFile = null;
	private File currentDirectory = null;
	
	//creation of the File Chooser for the controller
	private JFileChooser fc = new JFileChooser(".");
	
	// the file name of the VIZMAP property file
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";
	// the file concatenation of the (filtered_absent_proteins)
	private static final String ABSENT_PROTEINS_CONCATENATION = "(filtered_absent_proteins)";
	private static final String SUBGRAPHED = "(subgraphed)";
	/**
	 * The constructor for the mainframe controller
	 * @param mainframe
	 */
	public Controller(MainFrame mainframe)
	{
		this.mainframe = mainframe;
	}
	
	/**
	 * The constructor for the affymetrixview controller
	 * @param affymetrixview
	 */
	public Controller(AffymetrixView affymetrixview)
	{
		this.affymetrixview = affymetrixview;
		// load up the static hashmap mappings once the window opens
		try
		{
			AffymetrixRegexReader.makeHashMapUniProtToGeneID();
			AffymetrixRegexReader.makeHashMapGeneIDtoAffyID();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The constructor for the illuminaview controller
	 * @param illuminaview
	 */
	public Controller(IlluminaView illuminaview)
	{
		this.illuminaview = illuminaview;
		// TODO : make hashmap From IlluminaID to CytoID
	}

	/**
	 * This method receives a set of ActionEvents denoted in the viewframes (mainframe, affy and illumina views).
	 * The ActionEvents then define what sort of action is to be carried out
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		// if user clicks browse button
		if(command.equals("Browse"))
		{
			browseInputFile();
		}

		// if user clicks output button
		if(command.equals("Output"))
		{
			browseOutputFilePath();
		}

		// if user clicks choose button
		if(command.equals("Choose"))
		{
			if(!isConverted)
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"Please convert first in Step 1 above before continuing.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			if(isConverted)
			{
				runChoose();				
			}
		}

		// if user clicks browse condition 1 button
		if(command.equals("Browse Condition 1"))
		{
			browseBarcode1File();
			//TODO: check if file really exists!!
		}
		
		// if user clicks browse condition 2 button        
		if(command.equals("Browse Condition 2"))
		{
			browseBarcode2File();
		}

		// if user clicks apply filter button        
		if(command.equals("Apply Filter"))
		{
			// checks if the user has selected 2 files in the two fields. if not, show the warning message.
			if(affymetrixview.getInputcondition1field().getText().equals("") || affymetrixview.getInputcondition2field().getText().equals(""))
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"Please choose 2 barcode files using the browse buttons",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(!affymetrixview.getInputcondition1field().getText().equals("") && !affymetrixview.getInputcondition2field().getText().equals(""))
			{
				// only if the two fields are not the same then try to read the files. Else warning message!
				if (affymetrixview.getInputcondition1field().getText().equals(affymetrixview.getInputcondition2field().getText()))
				{
					JOptionPane
					.showMessageDialog(new JFrame(),
							"Please choose 2 DIFFERENT barcode files",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					try
					{
						SplashFrame sp = new SplashFrame();
						sp.setTitle("Please Wait a Moment...");
						sp.setLocation(100, 100);
						sp.setSize(500, 100);
						sp.setResizable(false);
						sp.setVisible(true);
						//setfocus on the splash frame
						sp.requestFocus();
						
						//read the inputfields
						inputbarcode1 = affymetrixview.getInputcondition1field().getText();
						inputbarcode2 = affymetrixview.getInputcondition2field().getText();
						// read the barcodes and compare them
						AffymetrixRegexReader.barcode1Reader(inputbarcode1);
						AffymetrixRegexReader.barcode2Reader(inputbarcode2);
						AffymetrixRegexReader.compareBarcodes();
						// create new SIF
						//TODO: If can please create a separate method here
						AffymetrixRegexReader.SIFreaderAndNewCreator(targetSIFpath);
						String[] temporarypath = targetSIFpath.split(".sif");
						targetfilteredSIFpath = temporarypath[0].concat(ABSENT_PROTEINS_CONCATENATION+".sif");
						//draw graph of new SIF and create a network
						Cytoscape.createNetworkFromFile(targetfilteredSIFpath);// load the NODE_TYPE .NA file

						//setfocus on the splash frame
						sp.requestFocus();
						
						loadNodeAttributeFile(getTargetNODE_TYPEpath());
						
						// load the UNIPROT .NA file
						loadNodeAttributeFile(getTargetUNIPROTpath());
						
						// load the ENTREZGENE .NA file
						loadNodeAttributeFile(getTargetENTREZGENEpath());
						
						// load the MODIFICATIONS .NA file
						loadNodeAttributeFile(getTargetMODIFICATIONSpath());
						
						// load the PREFERRED_SYMBOL .NA file
						loadNodeAttributeFile(getTargetPREFERRED_SYMBOLpath());

						// load the PREFERRED_SYMBOL_EXT .NA file
						loadNodeAttributeFile(getTargetPREFERRED_SYMBOL_EXTpath());
						
//						// load the PREFERRED_SYMBOL_EXT .NA file
//						loadNodeAttributeFileFromGraph(getTargetPREFERRED_SYMBOL_EXTpath());
						
						// load the PID .NA file
						loadNodeAttributeFile(getTargetPIDpath());
						
						// load the ID_PREF .NA file
						loadNodeAttributeFile(getTargetID_PREFpath());
						// change the title of the splash frame
						sp.setTitle("Network loaded, now loading visualisation...");
						// load the VIZMAP props file
						mapVisually(VIZMAP_PROPS_FILE_NAME);

						// change the title of the splash frame
						sp.setTitle("Visualisation loaded, this window closes automatically.");
						//delete the splashframe
						sp.dispose();
					}
					catch (IOException e1)
					{
						JOptionPane
						.showMessageDialog(new JFrame(),
								"Invalid Barcode files detected. Please check!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		}

		// if user clicks help button        
		if(command.equals("BarcodeHelp"))
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please input two files in barcode format, one for the experiment, the other the control."
							+ "\n" + "An example of the barcode format:"
							+ "\n" + "\"1007_s_at\",1"
							+ "\n" + "\"1053_at\",0",
					"Take Note!", JOptionPane.INFORMATION_MESSAGE);
		}
		if(command.equals("Check to expand help"))
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Check to automatically add into the graph the nodes which"
							+ "\n" + "are each families of protein complex without predecessors.",
					"What's this button?", JOptionPane.INFORMATION_MESSAGE);
		}
		// if user runs the convert button
		if(command.equals("Convert"))
		{
			//checks if the user has typed a file path into the input textfield. if not, alert!
			if(mainframe.getInputTextfieldText().equals(""))
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"Please enter a file using the browse button above",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(!mainframe.getInputTextfieldText().equals(""))
			{
				// do a read of the inputtextfield to determine file
				String filetobeconverted = mainframe.getInputTextfieldText().trim();
				inputfilepath = filetobeconverted;
				// set this filepath in the File object
				curFile = new File(inputfilepath);
				
				//-------------------------------------------------------
				if(curFile.getAbsolutePath().endsWith("xml"))
				{
					// targetSIFpath is set here by default
					String[] temporarypath = curFile.getAbsolutePath().split(".xml");
					setTargetSIFpath(temporarypath[0].concat(".sif"));					
				}
				if(curFile.getAbsolutePath().endsWith("sif"))
				{
					// targetSIFpath is set here by default
					setTargetSIFpath(curFile.getAbsolutePath());
				}
				
				// create the splashframe
				SplashFrame sp = new SplashFrame();
				sp.setTitle("Please Wait a Moment...");
				sp.setLocation(100, 100);
				sp.setSize(500, 100);
				sp.setResizable(false);
				sp.setVisible(true);
				try
				{
					//setfocus on this frame
					sp.requestFocus();
					// converting a file which is displayed in the inputtextfield JTextfield field in mainframe
					convertFile(filetobeconverted);
					isConverted = true;
				}
				catch (NullPointerException exp)
				{
					JOptionPane
					.showMessageDialog(new JFrame(),
							"Please enter a file using the browse button above",
							"Warning", JOptionPane.WARNING_MESSAGE);
					exp.printStackTrace();
				}
				try
				{
					//set focus on this frame
					sp.requestFocus();
					// draw the network graph of the SIF file
					drawGraphFromSIF(getTargetSIFpath());
					//set focus on this frame again
					sp.requestFocus();					
					// load the NODE_TYPE .NA file
					loadNodeAttributeFile(getTargetNODE_TYPEpath());					
					// load the UNIPROT .NA file
					loadNodeAttributeFile(getTargetUNIPROTpath());		
					// load the ENTREZGENE .NA file
					loadNodeAttributeFile(getTargetENTREZGENEpath());	
					// load the MODIFICATIONS .NA file
					loadNodeAttributeFile(getTargetMODIFICATIONSpath());					
					// load the PREFERRED_SYMBOL .NA file
					loadNodeAttributeFile(getTargetPREFERRED_SYMBOLpath());
					// load the PREFERRED_SYMBOL_EXT .NA file
					loadNodeAttributeFile(getTargetPREFERRED_SYMBOL_EXTpath());					
//					// load the PREFERRED_SYMBOL_EXT .NA file
//					loadNodeAttributeFileFromGraph(getTargetPREFERRED_SYMBOL_EXTpath());					
					// load the PID .NA file
					loadNodeAttributeFile(getTargetPIDpath());					
					// load the ID_PREF .NA file
					loadNodeAttributeFile(getTargetID_PREFpath());
					// change the title of the splash frame
					sp.setTitle("Network loaded, now loading visualisation...");
					// load the VIZMAP props file
					mapVisually(VIZMAP_PROPS_FILE_NAME);
										
					// read the nodetype file and then parse it accordingly, generating the 3 targetfiles
					AffymetrixRegexReader.readAndWriteFiles(targetNODE_TYPEpath, targetIDCytoUniProtFilepath,
							targetUniqueUniProtFilepath, targetUniProtToGeneIDMapFilepath, targetGeneIDtoAffymetrixMapFilepath
							/*,targetIDCytoEntrezGeneFilepath, targetUniqueEntrezGeneFilepath, targetEntrezGeneToGeneIDMapFilepath*/);

					// change the title of the splash frame
					sp.setTitle("Visualisation loaded, this window closes automatically.");
					
					// displaying a successful conversion message
					String filepath = getTargetSIFpath();
					JOptionPane
					.showMessageDialog(new JFrame(),
							"Conversion successful! Files converted are located in the directory:"
									+ "\n" + filepath,
							"Success", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception exp)
				{				
					JOptionPane
					.showMessageDialog(new JFrame(),
							"The graph cannot be read!",
							"Warning", JOptionPane.WARNING_MESSAGE);
					exp.printStackTrace();
				}
				finally
				{
					mainframe.requestFocus();
					// delete the SplashFrame
					sp.dispose();
				}
			}
			
		}
		if (command.equals("Subgraph"))
		{
			if(!isConverted)
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"Please convert first in Step 1 above before continuing.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			if(isConverted)
			{
				// check the source and target fields for emptiness
				if(mainframe.getSigmolsourcetextfield().getText().trim().equals("")&&
//						mainframe.getGenesourcetextfield().getText().trim().equals("")&&
						mainframe.getCytoidSourceTextArea().getText().trim().equals(""))
				{
					JOptionPane
					.showMessageDialog(new JFrame(),
							"Please enter at least one file in SOURCE",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
				else if(mainframe.getGenetargettextfield().getText().trim().equals("")&&
						mainframe.getSigmoltargettextfield().getText().trim().equals("")&&
						mainframe.getCytoidTargetTextArea().getText().trim().equals(""))
				{
					JOptionPane
					.showMessageDialog(new JFrame(),
							"Please enter at least one file in TARGET",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					SplashFrame sp = new SplashFrame();
					sp.setTitle("Please Wait a Moment...");
					sp.setLocation(100, 100);
					sp.setSize(500, 100);
					sp.setResizable(false);
					sp.setVisible(true);
					//setfocus on the splash frame
					sp.requestFocus();

					//TODO: instead of getting targetSIFpath, get actual network worked upon
					String currentNetworkFilepath = targetSIFpath;
					
					SubgraphExtraction sgex = new SubgraphExtraction(mainframe);
//					sgex.readGeneSourceFile();
					sgex.readGeneTargetFile(currentNetworkFilepath, targetNODE_TYPEpath);
					sgex.readSigmolSourceFile();
					sgex.readSigmolTargetFile();
					sgex.readCytoSourceText();
					sgex.readCytoTargetText();
					//then draw the graph from the read files/text
					sgex.drawJungGraph();
					try
					{
						sgex.SIFreaderAndNewCreator(currentNetworkFilepath);
						String[] temporarypath = currentNetworkFilepath.split(".sif");
						targetsubgraphedSIFpath = temporarypath[0].concat(SUBGRAPHED+".sif");
						//draw graph of new SIF and create a network
						Cytoscape.createNetworkFromFile(targetsubgraphedSIFpath);// load the NODE_TYPE .NA file

						// load the VIZMAP props file
						mapVisually(VIZMAP_PROPS_FILE_NAME);
						JOptionPane
						.showMessageDialog(new JFrame(),
								"Subgraph successful! Files converted are located in the directory:"
										+ "\n" + targetsubgraphedSIFpath,
								"Success", JOptionPane.INFORMATION_MESSAGE);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					finally
					{
						//delete the splashframe
						sp.dispose();						
					}
				}
			}
		}
		if (command.equals("Gene Source Browse"))
		{
			browseGeneSource();
		}
		if (command.equals("Gene Target Browse"))
		{
			browseGeneTarget();
		}
		if (command.equals("Sigmol Source Browse"))
		{
			browseSigmolSource();
		}
		if (command.equals("Sigmol Target Browse"))
		{
			browseSigmolTarget();
		}
		if(command.equals("Step3Help"))
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"For gene/signalling molecule source and target, input a text file in the following format:"
							+ "\n" + "'P12345"
							+ "\n" + " Q67890"
							+ "\n" + " O12345"
							+ "\n" + " ..............'"
							+ "\n" + "For CytoID source and target text areas, just Copy (Ctrl+C) the node IDs,"
							+ "\n" + "then Paste (Ctrl+V) in the text areas.",
					"Take Note!", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * This method checks the states of the affymetrix and illumina checkboxes
	 * in mainframe and runs the appropriate commands to open the respective window.
	 * This method checks if neither checkboxes are checked, or both are checked,
	 * and gives a warning message to select only one checkbox.
	 */
	private void runChoose()
	{
		boolean isaffymetrixchecked = mainframe.isAffymetrixChecked();
		boolean isilluminachecked = mainframe.isIlluminaChecked();
		
		// if neither checkboxes are checked
		if(!isaffymetrixchecked && !isilluminachecked)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select one single option.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		// if both checkboxes are checked
		if(isaffymetrixchecked && isilluminachecked)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select only one single option.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		// if only affymetrix is checked, not illumina
		if(isaffymetrixchecked && !isilluminachecked)
		{
			affymetrixview = new AffymetrixView(this);
			affymetrixview.setTitle("Import Barcode Output");
			affymetrixview.setLocation(30, 30);
			affymetrixview.setSize(400, 300);
			affymetrixview.setResizable(false);
			affymetrixview.setVisible(true);
			affymetrixview.pack();
		}
		
		// if only illumina is checked, not affymetrix
		if(isilluminachecked && !isaffymetrixchecked)
		{
			illuminaview = new IlluminaView(this);
			illuminaview.setTitle("Import files");
			illuminaview.setLocation(80, 80);
			illuminaview.setSize(400, 300);
			illuminaview.setResizable(false);
			illuminaview.setVisible(true);
			illuminaview.pack();
		}
		
	}

	/**
	 * This method rotates the graph created by 180 degrees.
	 * Suitable for rotating upside-down hierarchical graphs.
	 * source:
	 * http://chianti.ucsd.edu/svn/coreplugins/tags/cyto-2_3/ManualLayout/src/ManualLayout/rotate/RotateAction.java
	 */
	@SuppressWarnings("unused")
	private void rotateGraph()
	{
		MutablePolyEdgeGraphLayout[] nativeGraph =
			      new MutablePolyEdgeGraphLayout[] {
			        GraphConverter2.getGraphReference(16.0d, true, false) };
		RotationLayouter[] rotation = new RotationLayouter[]
			      { new RotationLayouter(nativeGraph[0]) };
		
		//the actual rotation
        rotation[0].rotateGraph(1.0d * Math.PI);
        
        //refreshing the view
        Cytoscape.getCurrentNetworkView().updateView();
	}

	/**
	 * This method creates a network graph from the string 's',
	 * which should be a path to a Cytoscape readable file, e.g. SIF
	 * @param s the path of the file to be read.
	 */
	private void drawGraphFromSIF(String s)
	{
		// read the sif file and create the network
		Cytoscape.createNetworkFromFile(s);
		Cytoscape.getCurrentNetworkView().redrawGraph(false, true);
		
	}
	
	/**
	 * This method loads the attribute NA files
	 * @param s the path of the NA file to be loaded
	 */
	private void loadNodeAttributeFile(String s)
	{
		Cytoscape.loadAttributes(new String[] { s },
                new String[] {});
		Cytoscape.getCurrentNetworkView().updateView();
		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);
	}

	/**
	 * This method loads the attribute NA files
	 * @param s the path of the NA file to be loaded
	 */
	private void loadNodeAttributeFileFromGraph(String s)
	{
		Cytoscape.loadAttributes(new String[] { s },
                new String[] {  });
		Cytoscape.getCurrentNetworkView().updateView();
		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);
		
		//TODO: big construction site here, to find out how to import network file from table
		ImportAttributeTableTask task = new ImportAttributeTableTask(new DefaultAttributeTableReader(null, null,
				   0, null, true), s);
		task.run();
		//http://chianti.ucsd.edu/svn/cytoscape/trunk/coreplugins/TableImport/src/main/java/edu/ucsd/bioeng/coreplugin/tableImport/ui/ImportTextTableDialog.java
//		AttributeMappingParameters mapping;
//		mapping = new AttributeMappingParameters(objType, del,
//				 listDelimiter, keyInFile,
//				 mappingAttribute, aliasList,
//				 attributeNames, attributeTypes,
//				 listDataTypes, importFlag, caseSensitive);
		Cytoscape.firePropertyChange(Cytoscape.NEW_ATTRS_LOADED, null, null);

//		try {
//			ImportTextTableDialog ittd = new ImportTextTableDialog(Cytoscape.getDesktop(), true, 1);
//			ittd.pack();
//			ittd.setLocationRelativeTo(Cytoscape.getDesktop());
//			ittd.setVisible(true);
//			
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}


	/**
	 * This method loads the vizmap property file and redraws the graph
	 * Also does the layout hierarchically and rotates the graph 180 degrees
	 * @param s the name of the vizmap property file
	 */
	private void mapVisually(String s)
	{
		//load the vizmap file
		Cytoscape.firePropertyChange(Cytoscape.VIZMAP_LOADED, null, s);
		VisualStyle vs = Cytoscape.getVisualMappingManager().getCalculatorCatalog().getVisualStyle("netView");
        Cytoscape.getCurrentNetworkView().setVisualStyle(vs.getName()); // not strictly necessary

        // actually apply the visual style
        Cytoscape.getVisualMappingManager().setVisualStyle(vs);
        Cytoscape.getCurrentNetworkView().redrawGraph(true,true);

		// TODO: should ask user whether he wants this? set the layout as hierarchical
		CyLayouts.getLayout("hierarchical").doLayout();
		if(Cytoscape.getCurrentNetwork().getNodeCount()<1000)
		{
			// TODO: sometimes rotating the graph causes some crash errors. thus only rotate if hierarchichal layout!
			rotateGraph();
		}
	}
	
	/**
	 * This method gets the filepath from the input file text area and then converts that xml file into SIF files
	 * @param filepath
	 */
	private void convertFile(String filepath)
	{
		
		// if the outputfiletextfield is empty, output file folder is same as input file's
		if(mainframe.getOutputTextfieldText().trim().equals(""))
		{
			String[] temporarypath = null;
			// if a file with xml ending
			if(curFile.getAbsolutePath().endsWith("xml"))
			{
				// set the SIF file path
				temporarypath = curFile.getAbsolutePath().split(".xml");
				setTargetSIFpath(temporarypath[0].concat(".sif"));
			}
			if(curFile.getAbsolutePath().endsWith("sif"))
			{
				temporarypath = curFile.getAbsolutePath().split(".sif");
				setTargetSIFpath(curFile.getAbsolutePath());
				// for loading of pre-filtered SIF files
				if(curFile.getAbsolutePath().contains(getAbsentProteinsConcatenation()))
				{
					temporarypath[0] = temporarypath[0].replace(getAbsentProteinsConcatenation(), "");
				}
				// for loading of pre-subgraphed SIF files
				if(curFile.getAbsolutePath().contains(getSubgraphed()))
				{
					temporarypath[0] = temporarypath[0].replace(getSubgraphed(), "");
				}
			}
			// set the target node type NA path
			setTargetNODE_TYPEpath(temporarypath[0].concat(".NODE_TYPE.NA"));
			// set the UNIPROT NA path
			setTargetUNIPROTpath(temporarypath[0].concat(".UNIPROT.NA"));
			// set the ENTREZGENE NA path
			setTargetENTREZGENEpath(temporarypath[0].concat(".ENTREZGENE.NA"));
			// set the MODIFICATIONS NA path
			setTargetMODIFICATIONSpath(temporarypath[0].concat(".MODIFICATIONS.NA"));
			// set the PREFERRED_SYMBOL NA path
			setTargetPREFERRED_SYMBOLpath(temporarypath[0].concat(".PREFERRED_SYMBOL.NA"));
			// set the PREFERRED_SYMBOL_EXT NA path
			setTargetPREFERRED_SYMBOL_EXTpath(temporarypath[0].concat(".PREFERRED_SYMBOL_EXT.NA"));
			// set the PID NA path
			setTargetPIDpath(temporarypath[0].concat(".PID.NA"));
			// set the ID_PREF NA path
			setTargetID_PREFpath(temporarypath[0].concat(".ID_PREF.NA"));
			// set the IDCytoUniProtFile path
			setTargetIDCytoUniProtFilepath(temporarypath[0].concat(".IDCytoToUniprot.NA"));
			// set the IDCytEntrezGeneFile path
			setTargetIDCytoEntrezGeneFilepath(temporarypath[0].concat(".IDCytoToEntrezGene.NA"));
			// set the UniqueUniProtFile path
			setTargetUniqueUniProtFilepath(temporarypath[0].concat(".UNIQUEUNIPROT.NA"));
			// set the UniqueEntrezGeneFile path
			setTargetUniqueEntrezGeneFilepath(temporarypath[0].concat(".UNIQUEENTREZGENE.NA"));
			// set the UniProt to GeneID map file path
			setTargetUniProtToGeneIDMapFilepath(temporarypath[0].concat(".UPToGeneIDMap.NA"));
			// set the EntrezGene to GeneID map file path
			setTargetEntrezGeneToGeneIDMapFilepath(temporarypath[0].concat(".UPToGeneIDMap2.NA"));
			// set the GeneID to Affymetrix map file path
			setTargetGeneIDtoAffymetrixMapFilepath(temporarypath[0].concat(".GeneIDToAffyMap.NA"));
		}
		this.inputfilepath = filepath;
		if(inputfilepath.endsWith("xml"))
		{
			NodeManagerImpl manager = NodeManagerImpl.getInstance();
			FileReader reader = PidFileReader.getInstance();
			try
			{
				reader.read(inputfilepath);
			}
			catch (NoValidManagerSetException e1)
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"Program error, please contact hadi.kang@bioquant.uni-heidelberg.de for assistance.",
						"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}
			catch (FileParsingException e1)
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
						"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}
			
			FileWriter writer = SifFileWriter.getInstance();
			try
			{
				writer.write(getTargetSIFpath(), manager);	
				if (mainframe.isExpandChecked())
				{
					writer = SifFileExpandMolWriter.getInstance();
					writer.write(getTargetSIFpath(), manager);
				}
			}
			catch (FileNotFoundException e)
			{
				JOptionPane
				.showMessageDialog(new JFrame(),
						"File write error",
						"Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
			
			FileWriter nWriter = NodeTypeAttributeForUniprotWithModWriter.getInstance();
			try
			{
				nWriter.write(getTargetNODE_TYPEpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter uWriter = UniprotIdForUniprotWithModWriter.getInstance();
			try
			{
				uWriter.write(getTargetUNIPROTpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter eWriter = EntrezGeneIdforEntrezGeneWithModWriter.getInstance();
			try
			{
				eWriter.write(getTargetENTREZGENEpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			
			FileWriter modiWriter = ModificationsWriter.getInstance();
			try
			{
				modiWriter.write(getTargetMODIFICATIONSpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter pWriter = PreferredSymbolForUniprotWithModWriter.getInstance();
			try
			{
				pWriter.write(getTargetPREFERRED_SYMBOLpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			pWriter = ExtPreferredSymbolWriter.getInstance();
			try
			{
				pWriter.write(getTargetPREFERRED_SYMBOL_EXTpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter pidWriter = PidForUniprotWithModWriter.getInstance();
			try
			{
				pidWriter.write(getTargetPIDpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter prefIdWriter = IdWithPreferredSymbolWriter.getInstance();
			try
			{
				prefIdWriter.write(getTargetID_PREFpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		}
		else if(inputfilepath.endsWith("sif"))
		{
			
		}
		else // not an xml or SIF file
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	/**
	 * This method opens a file chooser dialog box and sets the input text field string to the absolute path of the file
	 */
	private void browseInputFile()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose an XML file");
			FileNameExtensionFilter xmldata = new FileNameExtensionFilter("XML", "xml");
			FileNameExtensionFilter sifdata = new FileNameExtensionFilter("SIF", "sif");
			fc.addChoosableFileFilter(xmldata);
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				curFile = fc.getSelectedFile();
				currentDirectory = fc.getCurrentDirectory();
				if(curFile.getAbsolutePath().endsWith("xml"))
				{
					inputfilepath = curFile.getAbsolutePath();
					mainframe.setInputFileText(inputfilepath);
					
					// targetSIFpath is set here by default
					String[] temporarypath = curFile.getAbsolutePath().split(".xml");
					setTargetSIFpath(temporarypath[0].concat(".sif"));
				}
				if(curFile.getAbsolutePath().endsWith("sif"))
				{
					inputfilepath = curFile.getAbsolutePath();
					mainframe.setInputFileText(inputfilepath);
				}
			}
			
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a valid xml file downloaded from Pathway Interaction Database, or a converted SIF file.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		
	}

	/**
	 * This method selects the folder in which the output file should be placed.
	 * if there are same named files present, they will be overwrite!
	 */
	private void browseOutputFilePath()
	{
		String filedirectory = "";
		String filepath = "";
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //select directories or files
			fc.setDialogTitle("Please choose a directory to save the converted file(s)");
			fc.setCurrentDirectory(currentDirectory);
			
			FileNameExtensionFilter sifdata = new FileNameExtensionFilter("SIF", "sif");
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				// if the selected is a directory
				if(fc.getSelectedFile().isDirectory())
				{
					// get the absolute path of the directory in which the file lies
					filepath = fc.getSelectedFile().getAbsolutePath();
					// split the file name
					String[] temporarypath = curFile.getName().split(".xml");
					
					// set the target SIF path
					setTargetSIFpath(filepath.concat("\\").concat(temporarypath[0].concat(".sif")));
					
					// set the target node type NA path
					setTargetNODE_TYPEpath(filepath.concat("\\").concat(temporarypath[0].concat(".NODE_TYPE.NA")));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filepath.concat("\\").concat(temporarypath[0].concat(".UNIPROT.NA")));
					
					// set the target entrezGene NA path
					setTargetENTREZGENEpath(filepath.concat("\\").concat(temporarypath[0].concat(".ENTREZGENE.NA")));

					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filepath.concat("\\").concat(temporarypath[0].concat(".MODIFICATIONS.NA")));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filepath.concat("\\").concat(temporarypath[0].concat(".PREFERRED_SYMBOL.NA")));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filepath.concat("\\").concat(temporarypath[0].concat(".PREFERRED_SYMBOL_EXT.NA")));

					// set the target PID NA path
					setTargetPIDpath(filepath.concat("\\").concat(temporarypath[0].concat(".PID.NA")));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filepath.concat("\\").concat(temporarypath[0].concat(".ID_PREF.NA")));

					// set the IDCytoUniProtFile path
					setTargetIDCytoUniProtFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".IDCytoToUniProt.NA")));

					// set the IDCytoEntrezGeneFile path
					setTargetIDCytoEntrezGeneFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".IDCytoToEntrezGene.NA")));
					
					// set the UniqueUniProtFile path
					setTargetUniqueUniProtFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UNIQUEUNIPROT.NA")));	

					// set the UniqueEntrezGeneFile path
					setTargetUniqueEntrezGeneFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UNIQUEENTREZGENE.NA")));	
					
					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap.NA")));
					
					// set the EntrezGene to GeneID map file path
					setTargetEntrezGeneToGeneIDMapFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap2.NA")));
					
					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".GeneIDToAffyMap.NA")));
					
				}
				else // if the selected is a file
				{
					// get the directory path
					filedirectory = fc.getCurrentDirectory().getAbsolutePath();
					// split the current file into one without .xml
					String[] temporarypath = curFile.getName().split(".xml");
					
					// set the target SIF path
					setTargetSIFpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".sif"));

					// set the target node type NA path
					setTargetNODE_TYPEpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".NODE_TYPE.NA"));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".UNIPROT.NA"));

					// set the target entrezGene NA path
					setTargetENTREZGENEpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".ENTREZGENE.NA"));
					
					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".MODIFICATIONS.NA"));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PREFERRED_SYMBOL.NA"));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PREFERRED_SYMBOL_EXT.NA"));

					// set the target PID NA path
					setTargetPIDpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PID.NA"));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".ID_PREF.NA"));
					
					// set the IDCytoUniProtFile path
					setTargetIDCytoUniProtFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".IDCytoToUniProt.NA")));

					// set the IDCytoEntrezGeneFile path
					setTargetIDCytoEntrezGeneFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".IDCytoToEntrezGene.NA")));
					
					// set the UniqueUniProtFile path
					setTargetUniqueUniProtFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UNIQUEUNIPROT.NA")));

					// set the UniqueEntrezGeneFile path
					setTargetUniqueEntrezGeneFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UNIQUEENTREZGENE.NA")));
					
					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap.NA")));
					
					// set the EntrezGene to GeneID map file path
					setTargetEntrezGeneToGeneIDMapFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap2.NA")));
					
					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".GeneIDToAffyMap.NA")));
				}
				mainframe.setOutputTextfieldText(targetSIFpath);
			}
			currentDirectory = fc.getCurrentDirectory();
			
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a valid xml file downloaded from Protein Interaction Database.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}
	
	/**
	 * this method browses for the first barcode file
	 */
	public void browseBarcode1File()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose Barcode file of Condition 1");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				barcode1File = fc.getSelectedFile();
				// set the input barcode 1
				setInputbarcode1(barcode1File.getAbsolutePath());
				// put the absolute path in the textfield
				affymetrixview.setInput1FieldText(inputbarcode1);	
			}
			currentDirectory = fc.getCurrentDirectory();
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Barcode file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * this method browses for the second barcode file
	 */
	public void browseBarcode2File()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose Barcode file of Condition 2");
			fc.setCurrentDirectory(currentDirectory);
			
			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				barcode2File = fc.getSelectedFile();
				// set the input barcode 2
				setInputbarcode2(barcode2File.getAbsolutePath());
				// put the absolute path in the textfield
				affymetrixview.setInput2FieldText(inputbarcode2);
			}
			currentDirectory = fc.getCurrentDirectory();
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Barcode file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a gene source file
	 */
	private void browseGeneSource()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				genesourceFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setGenesourcetextfieldText(genesourceFile.getAbsolutePath());	
			}
			currentDirectory = fc.getCurrentDirectory();
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Text file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a gene target file
	 */
	private void browseGeneTarget()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				genetargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setGenetargettextfieldText(genetargetFile.getAbsolutePath());	
			}
			currentDirectory = fc.getCurrentDirectory();
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Text file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a sigmol source file
	 */
	private void browseSigmolSource()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				sigmolsourceFile = fc.getSelectedFile();
				currentDirectory = fc.getCurrentDirectory();
				// put the absolute path in the textfield
				mainframe.setSigmolsourcetextfieldText(sigmolsourceFile.getAbsolutePath());	
			}
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Text file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a s file
	 */
	private void browseSigmolTarget()
	{
		try
		{
			//JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");
			fc.setCurrentDirectory(currentDirectory);
			
			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				sigmoltargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setSigmoltargettextfieldText(sigmoltargetFile.getAbsolutePath());	
			}
			currentDirectory = fc.getCurrentDirectory();
		}
		catch (Exception e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"Please select a Text file",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public String getTargetSIFpath()
	{
		return targetSIFpath;
	}

	public void setTargetSIFpath(String targetSIFpath)
	{
		this.targetSIFpath = targetSIFpath;
	}

	public String getTargetNODE_TYPEpath()
	{
		return targetNODE_TYPEpath;
	}

	public void setTargetNODE_TYPEpath(String targetNODE_TYPEpath)
	{
		this.targetNODE_TYPEpath = targetNODE_TYPEpath;
	}

	public String getTargetUNIPROTpath()
	{
		return targetUNIPROTpath;
	}
	
	public String getTargetENTREZGENEpath()
	{
		return targetENTREZGENEpath;
	}

	public void setTargetUNIPROTpath(String targetUNIPROTpath)
	{
		this.targetUNIPROTpath = targetUNIPROTpath;
	}
	
	public void setTargetENTREZGENEpath(String targetENTREZGENEpath)
	{
		this.targetENTREZGENEpath = targetENTREZGENEpath;
	}

	public String getTargetMODIFICATIONSpath()
	{
		return targetMODIFICATIONSpath;
	}

	public void setTargetMODIFICATIONSpath(String targetMODIFICATIONSpath)
	{
		this.targetMODIFICATIONSpath = targetMODIFICATIONSpath;
	}

	public String getTargetPREFERRED_SYMBOLpath()
	{
		return targetPREFERRED_SYMBOLpath;
	}

	public void setTargetPREFERRED_SYMBOLpath(String targetPREFERRED_SYMBOLpath)
	{
		this.targetPREFERRED_SYMBOLpath = targetPREFERRED_SYMBOLpath;
	}

	public String getTargetPREFERRED_SYMBOL_EXTpath()
	{
		return targetPREFERRED_SYMBOL_EXTpath;
	}

	public void setTargetPREFERRED_SYMBOL_EXTpath(String targetPREFERRED_SYMBOL_EXTpath)
	{
		this.targetPREFERRED_SYMBOL_EXTpath = targetPREFERRED_SYMBOL_EXTpath;
	}

	public String getTargetPIDpath()
	{
		return targetPIDpath;
	}

	public void setTargetPIDpath(String targetPIDpath)
	{
		this.targetPIDpath = targetPIDpath;
	}

	public String getTargetID_PREFpath()
	{
		return targetID_PREFpath;
	}

	public void setTargetID_PREFpath(String targetID_PREFpath)
	{
		this.targetID_PREFpath = targetID_PREFpath;
	}

	public String getTargetIDCytoUniProtFilepath()
	{
		return targetIDCytoUniProtFilepath;
	}
	
	public String getTargetIDCytoEntrezGeneFilepath()
	{
		return targetIDCytoEntrezGeneFilepath;
	}
	
	public void setTargetIDCytoUniProtFilepath(String targetIDCytoUniProtFilepath)
	{
		this.targetIDCytoUniProtFilepath = targetIDCytoUniProtFilepath;
	}
	
	public void setTargetIDCytoEntrezGeneFilepath(String targetIDCytoEntrezGeneFilepath)
	{
		this.targetIDCytoEntrezGeneFilepath = targetIDCytoEntrezGeneFilepath;
	}

	public String getTargetUniqueUniProtFilepath()
	{
		return targetUniqueUniProtFilepath;
	}

	public String getTargetUniqueEntrezGeneFilepath()
	{
		return targetUniqueEntrezGeneFilepath;
	}

	public void setTargetUniqueUniProtFilepath(String targetUniqueUniProtFilepath)
	{
		this.targetUniqueUniProtFilepath = targetUniqueUniProtFilepath;
	}

	public void setTargetUniqueEntrezGeneFilepath(String targetUniqueEntrezGeneFilepath)
	{
		this.targetUniqueEntrezGeneFilepath = targetUniqueEntrezGeneFilepath;
	}
	
	public String getTargetUniProtToGeneIDMapFilepath()
	{
		return targetUniProtToGeneIDMapFilepath;
	}

	public String getTargetEntrezGeneToGeneIDMapFilepath()
	{
		return targetEntrezGeneToGeneIDMapFilepath;
	}
	
	public void setTargetUniProtToGeneIDMapFilepath(String targetUniProtToGeneIDMapFilepath)
	{
		this.targetUniProtToGeneIDMapFilepath = targetUniProtToGeneIDMapFilepath;
	}

	public void setTargetEntrezGeneToGeneIDMapFilepath(String targetEntrezGeneToGeneIDMapFilepath)
	{
		this.targetEntrezGeneToGeneIDMapFilepath = targetEntrezGeneToGeneIDMapFilepath;
	}
	
	public String getTargetGeneIDtoAffymetrixMapFilepath()
	{
		return targetGeneIDtoAffymetrixMapFilepath;
	}

	public void setTargetGeneIDtoAffymetrixMapFilepath(String targetGeneIDtoAffymetrixMapFilepath)
	{
		this.targetGeneIDtoAffymetrixMapFilepath = targetGeneIDtoAffymetrixMapFilepath;
	}

	public String getInputbarcode1()
	{
		return inputbarcode1;
	}

	public void setInputbarcode1(String inputbarcode1)
	{
		this.inputbarcode1 = inputbarcode1;
	}

	public String getInputbarcode2()
	{
		return inputbarcode2;
	}

	public void setInputbarcode2(String inputbarcode2)
	{
		this.inputbarcode2 = inputbarcode2;
	}

	public static String getAbsentProteinsConcatenation() {
		return ABSENT_PROTEINS_CONCATENATION;
	}

	public static String getSubgraphed() {
		return SUBGRAPHED;
	}
	
}
