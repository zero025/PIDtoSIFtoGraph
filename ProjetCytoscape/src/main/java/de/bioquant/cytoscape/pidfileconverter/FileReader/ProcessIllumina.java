/**
 * separate thread for step 2 Illumina
 * @author Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.Cytoscape;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.IlluminaView;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;

public class ProcessIllumina extends AbstractProcess {

	private Controller controller;
	private SplashFrame illuminaFrame;
	private IlluminaView illuminaview;
	private static String targetSIFpath;
	private static String targetfilteredSIFpath;
	private static final String ABSENT_PROTEINS_CONCATENATION = "(filtered_absent_proteins)";
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";

	/**
	 * Constructor
	 * @param sp
	 * 			the splash frame
	 * @param controller
	 * 			the controller
	 * @param illuminaview
	 * 			window of illumina
	 * @param targetSIFpath
	 * 			path for the new SIF file created
	 * @param targetfilteredSIFpath
	 */
	public ProcessIllumina(SplashFrame sp, Controller controller,
			IlluminaView illuminaview, String targetSIFpath,
			String targetfilteredSIFpath) {
		this.illuminaFrame = sp;
		this.controller = controller;
		this.illuminaview = illuminaview;
		ProcessIllumina.targetSIFpath = targetSIFpath;
		ProcessIllumina.targetfilteredSIFpath = targetfilteredSIFpath;

	}

	@Override
	public void run() {
		try {
			
			illuminaFrame.getStart().setEnabled(false);
			
			// read the inputfield
			controller.setInputFileIllumina(illuminaview.getInputFileField()
					.getText());
			// read the fil and compare the conditions
			IlluminaRegexReader.fileReader(controller.getInputFileIllumina(),
					illuminaview.getInputcondition1Field().getText(),
					illuminaview.getInputcondition2Field().getText());
			IlluminaRegexReader.compareConditions(illuminaFrame, this);
			if(!isContinueThread()){
				return;
			}
			
			// create new SIF
			// TODO: If can please create a separate method here
			IlluminaRegexReader.SIFreaderAndNewCreator(targetSIFpath);
			String[] temporarypath = targetSIFpath.split(".sif");
			targetfilteredSIFpath = temporarypath[0]
					.concat(ABSENT_PROTEINS_CONCATENATION + ".sif");
			// draw graph of new SIF and create a network
			Cytoscape.createNetworkFromFile(targetfilteredSIFpath);// load the NODE_TYPE .NA file

			// setfocus on the splash frame
			illuminaFrame.requestFocus();

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

			// load the PID .NA file
			controller.loadNodeAttributeFile(controller.getTargetPIDpath());

			// load the ID_PREF .NA file
			controller.loadNodeAttributeFile(controller.getTargetID_PREFpath());
			// change the title of the splash frame
			illuminaFrame
					.setTitle("Network loaded, now loading visualisation...");
			// load the VIZMAP props file
			//TODO : add a checkbox to ask if the user wants to have a hierarchical graph
			controller.mapVisually(VIZMAP_PROPS_FILE_NAME, null);

			// change the title of the splash frame
			illuminaFrame
					.setTitle("Visualisation loaded, this window closes automatically.");
			// delete the splashframe
			illuminaFrame.dispose();
			
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Invalid Barcode files detected. Please check!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e1.printStackTrace();
		}
	}

}
