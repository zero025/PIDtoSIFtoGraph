/**
 * Separate thread for the step 1
 * @contributor Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
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
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;
import de.bioquant.cytoscape.pidfileconverter.View.Step1;

public class ProcessConvert extends AbstractProcess {

	private SplashFrame convertFrame;
	private Step1 step1;
	private String inputfilepath;
	private File curFile;
	private Controller controller;

	// the file name of the VIZMAP property file
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";

	/**
	 * Constructor
	 * @param sp
	 * 			the splash frame
	 * @param controller
	 * 			the controller
	 * @param step1
	 * 			the window of the step1
	 * @param inputfilepath
	 * 		
	 * @param curFile
	 */
	public ProcessConvert(SplashFrame sp, Controller controller, Step1 step1) {

		this.convertFrame = sp;
		this.controller = controller;
		this.step1 = step1;
	}

	public void run() {

		convertFrame.getStart().setEnabled(false);
		
		// do a read of the inputtextfield to determine file
		String filetobeconverted = step1.getInputTextFieldText().trim();
		inputfilepath = filetobeconverted;
		// set this filepath in the File object
		curFile = new File(inputfilepath);

		try {
			// converting a file which is displayed in the inputtextfield
			// JTextfield field in mainframe
			
			//This part is 85% of the processing time
			convertFile(filetobeconverted);
			if(!isContinueThread()){
				return;
			}
			
		} catch (NullPointerException exp) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Null pointer exception", "Error",
					JOptionPane.ERROR_MESSAGE);
			exp.printStackTrace();
		}
		try {
			controller.setConverted(true);
			// set focus on this frame
			convertFrame.requestFocus();
			// draw the network graph of the SIF file
			controller.drawGraphFromSIF(controller.getTargetSIFpath());
			// set focus on this frame again
			convertFrame.requestFocus();
			// load the NODE_TYPE .NA file
			controller.loadNodeAttributeFile(controller
					.getTargetNODE_TYPEpath());
			// load the UNIPROT .NA file
			controller.loadNodeAttributeFile(controller.getTargetUNIPROTpath());
			// load the ENTREZGENE .NA file
			controller.loadNodeAttributeFile(controller
					.getTargetENTREZGENEpath());
			// load the MODIFICATIONS .NA file
			controller.loadNodeAttributeFile(controller
					.getTargetMODIFICATIONSpath());
			// load the PREFERRED_SYMBOL .NA file
			controller.loadNodeAttributeFile(controller
					.getTargetPREFERRED_SYMBOLpath());
			// load the PREFERRED_SYMBOL_EXT .NA file
			controller.loadNodeAttributeFile(controller
					.getTargetPREFERRED_SYMBOL_EXTpath());
			// // load the PREFERRED_SYMBOL_EXT .NA file
			// loadNodeAttributeFileFromGraph(getTargetPREFERRED_SYMBOL_EXTpath());
			// load the PID .NA file
			controller.loadNodeAttributeFile(controller.getTargetPIDpath());
			// load the ID_PREF .NA file
			controller.loadNodeAttributeFile(controller.getTargetID_PREFpath());
			// change the title of the splash frame
			convertFrame
					.setTitle("Network loaded, now loading visualisation...");
			
			// load the VIZMAP props file
			controller.mapVisually(VIZMAP_PROPS_FILE_NAME, step1);
			
			// read the nodetype file and then parse it accordingly,
			// generating the 3 targetfiles
			AffymetrixRegexReader.readAndWriteFiles(
					controller.getTargetNODE_TYPEpath(),
					controller.getTargetCytoIDtoIDFilepath(),
					controller.getTargetUniqueIDFilepath(),
					controller.getTargetUniProtToGeneIDMapFilepath(),
					convertFrame, this);
			if (!isContinueThread()) {
				return;
			}

			// change the title of the splash frame
			convertFrame
					.setTitle("Visualisation loaded, this window closes automatically.");

			// displaying a successful conversion message
			String filepath = controller.getTargetSIFpath();
			JOptionPane.showMessageDialog(new JFrame(),
					"Conversion successful! Files converted are located in the directory:"
							+ "\n" + filepath, "Success",
					JOptionPane.INFORMATION_MESSAGE);

			step1.getNext().setEnabled(true);
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(new JFrame(),
					"The graph cannot be read! Exception :"
							+ exp.getClass().getName(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			exp.printStackTrace();
		} finally {
			step1.requestFocus();
			// delete the SplashFrame when the work is done
			convertFrame.dispose();
		}
	}
	
	/**
	 * This method gets the filepath from the input file text area and then
	 * converts that xml file into SIF files
	 * 
	 * @param filepath
	 */
	public void convertFile(String filepath) {
		
		// if the outputfiletextfield is empty, output file folder is same as
		// input file's
		if (step1.getOutputTextFieldText().trim().equals("")) {
			String[] temporarypath = null;
			// if a file with xml ending
			if (curFile.getAbsolutePath().endsWith("xml")) {
				// set the SIF file path
				temporarypath = curFile.getAbsolutePath().split(".xml");
				controller.setTargetSIFpath(temporarypath[0].concat(".sif"));
			} else if (curFile.getAbsolutePath().endsWith("sif")) {
				temporarypath = curFile.getAbsolutePath().split(".sif");
				controller.setTargetSIFpath(curFile.getAbsolutePath());
				// for loading of pre-filtered SIF files
				if (curFile.getAbsolutePath().contains(
						Controller.getAbsentProteinsConcatenation())) {
					temporarypath[0] = temporarypath[0].replace(
							Controller.getAbsentProteinsConcatenation(), "");
				}
				// for loading of pre-subgraphed SIF files
				if (curFile.getAbsolutePath().contains(Controller.getSubgraphed())) {
					temporarypath[0] = temporarypath[0].replace(
							Controller.getSubgraphed(), "");
				}
			}
			// set the target node type NA path
			controller.setTargetNODE_TYPEpath(temporarypath[0].concat(".NODE_TYPE.NA"));
			// set the UNIPROT NA path
			controller.setTargetUNIPROTpath(temporarypath[0].concat(".UNIPROT.NA"));
			// set the ENTREZGENE NA path
			controller.setTargetENTREZGENEpath(temporarypath[0].concat(".ENTREZGENE.NA"));
			// set the MODIFICATIONS NA path
			controller.setTargetMODIFICATIONSpath(temporarypath[0]
					.concat(".MODIFICATIONS.NA"));
			// set the PREFERRED_SYMBOL NA path
			controller.setTargetPREFERRED_SYMBOLpath(temporarypath[0]
					.concat(".PREFERRED_SYMBOL.NA"));
			// set the PREFERRED_SYMBOL_EXT NA path
			controller.setTargetPREFERRED_SYMBOL_EXTpath(temporarypath[0]
					.concat(".PREFERRED_SYMBOL_EXT.NA"));
			// set the PID NA path
			controller.setTargetPIDpath(temporarypath[0].concat(".PID.NA"));
			// set the ID_PREF NA path
			controller.setTargetID_PREFpath(temporarypath[0].concat(".ID_PREF.NA"));
			// set the CytoIDtoIDFile path
			controller.setTargetCytoIDtoIDFilepath(temporarypath[0]
					.concat(".CytoIDToID.NA"));
			// set the UniqueID path
			controller.setTargetUniqueIDFilepath(temporarypath[0].concat(".UNIQUEID.NA"));
			// set the UniProt to GeneID map file path
			controller.setTargetUniProtToGeneIDMapFilepath(temporarypath[0]
					.concat(".UPToGeneIDMap.NA"));
			// set the GeneID to Affymetrix map file path
			controller.setTargetGeneIDtoAffymetrixMapFilepath(temporarypath[0]
					.concat(".GeneIDToAffyMap.NA"));
		}
		this.inputfilepath = filepath;
		if (inputfilepath.endsWith("xml")) {
			NodeManagerImpl manager = NodeManagerImpl.getInstance();
			PidFileReader reader = PidFileReader.getInstance();
			reader.setSplashFrameAndProcess(convertFrame, this);
			try {
				
				//The longest part of the function (in processing time)
				reader.read(inputfilepath);
				if(!isContinueThread()){
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
				writer.write(controller.getTargetSIFpath(), manager);
				convertFrame.getBar().setValue(85);
				if(!isContinueThread()){
					return;
				}
				if (step1.isExpandChecked()) {
					writer = SifFileExpandMolWriter.getInstance();
					writer.write(controller.getTargetSIFpath(), manager);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(new JFrame(), "File write error",
						"Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}

			FileWriter nWriter = NodeTypeAttributeForIDWithModWriter
					.getInstance();
			try {
				nWriter.write(controller.getTargetNODE_TYPEpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter uWriter = UniprotIdForUniprotWithModWriter.getInstance();
			try {
				uWriter.write(controller.getTargetUNIPROTpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter eWriter = EntrezGeneIdforEntrezGeneWithModWriter
					.getInstance();
			try {
				eWriter.write(controller.getTargetENTREZGENEpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter modiWriter = ModificationsWriter.getInstance();
			try {
				modiWriter.write(controller.getTargetMODIFICATIONSpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter pWriter = PreferredSymbolForIDWithModWriter
					.getInstance();
			try {
				pWriter.write(controller.getTargetPREFERRED_SYMBOLpath(), manager);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			pWriter = ExtPreferredSymbolWriter.getInstance();
			try {
				pWriter.write(controller.getTargetPREFERRED_SYMBOL_EXTpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter pidWriter = PidForIDWithModWriter.getInstance();
			try {
				pidWriter.write(controller.getTargetPIDpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			FileWriter prefIdWriter = IdWithPreferredSymbolWriter.getInstance();
			try {
				prefIdWriter.write(controller.getTargetID_PREFpath(), manager);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} else if (inputfilepath.endsWith("sif")) {
			convertFrame.getBar().setValue(85);
			
		} else // not an xml or SIF file
		{
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
							"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

}