/**
 * @author Yamei & Thomas
 */
package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

	public ProcessConvert(SplashFrame sp, Controller controller, Step1 step1,
			String inputfilepath, File curFile) {

		this.convertFrame = sp;
		this.controller = controller;
		this.step1 = step1;
		this.inputfilepath = inputfilepath;
		this.curFile = curFile;
	}

	public void run() {

		convertFrame.getStart().setEnabled(false);
		
		// do a read of the inputtextfield to determine file
		String filetobeconverted = step1.getInputTextFieldText().trim();
		inputfilepath = filetobeconverted;
		// set this filepath in the File object
		curFile = new File(inputfilepath);

		// -------------------------------------------------------
		if (curFile.getAbsolutePath().endsWith("xml")) {
			// targetSIFpath is set here by default
			String[] temporarypath = curFile.getAbsolutePath().split(".xml");
			controller.setTargetSIFpath(temporarypath[0].concat(".sif"));
		}
		if (curFile.getAbsolutePath().endsWith("sif")) {
			// targetSIFpath is set here by default
			controller.setTargetSIFpath(curFile.getAbsolutePath());
		}

		try {
			// converting a file which is displayed in the inputtextfield
			// JTextfield field in mainframe
			
			//This part is 85% of the processing time
			controller.convertFile(filetobeconverted, convertFrame, this);
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
			controller.mapVisually(VIZMAP_PROPS_FILE_NAME);
			
			// read the nodetype file and then parse it accordingly,
			// generating the 3 targetfiles
			AffymetrixRegexReader.readAndWriteFiles(
					controller.getTargetNODE_TYPEpath(),
					controller.getTargetCytoIDtoIDFilepath(),
					controller.getTargetUniqueIDFilepath(),
					controller.getTargetUniProtToGeneIDMapFilepath(),
					controller.getTargetGeneIDtoAffymetrixMapFilepath(),
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

}