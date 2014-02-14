/**
 * @author Yamei & Thomas
 */
package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.Cytoscape;
import de.bioquant.cytoscape.pidfileconverter.View.AffymetrixView;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;

public class ProcessAffymetrix extends AbstractProcess {
	private Controller controller;
	private SplashFrame affymetrixFrame;
	private AffymetrixView affymetrixview;
	private static String targetSIFpath;
	private static String targetfilteredSIFpath;
	private static final String ABSENT_PROTEINS_CONCATENATION = "(filtered_absent_proteins)";
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";
	
	public ProcessAffymetrix(SplashFrame sp, Controller controller,
			AffymetrixView affymetrixview, String targetSIFpath,
			String targetfilteredSIFpath) {
		this.affymetrixFrame = sp;
		this.controller = controller;
		this.affymetrixview = affymetrixview;
		ProcessAffymetrix.targetSIFpath = targetSIFpath;
		ProcessAffymetrix.targetfilteredSIFpath = targetfilteredSIFpath;
	}

	public void run() {
		try {
			
			affymetrixFrame.getStart().setEnabled(false);
			// read the inputfields
			controller.setInputbarcode1(affymetrixview
					.getInputcondition1field().getText());

			controller.setInputbarcode2(affymetrixview
					.getInputcondition2field().getText());

			// read the barcodes and compare them
			AffymetrixRegexReader.barcode1Reader(controller.getInputbarcode1());

			AffymetrixRegexReader.barcode2Reader(controller.getInputbarcode2());

			AffymetrixRegexReader.compareBarcodes(affymetrixFrame, this);	
			if(!isContinueThread()){
				return;
			}

			// create new SIF
			// TODO: If can please create a separate method here
			AffymetrixRegexReader.SIFreaderAndNewCreator(targetSIFpath);

			String[] temporarypath = targetSIFpath.split(".sif");
			targetfilteredSIFpath = temporarypath[0].concat(ABSENT_PROTEINS_CONCATENATION + ".sif");
		
			// draw graph of new SIF and create a network
			Cytoscape.createNetworkFromFile(targetfilteredSIFpath);// load
			// the
			// NODE_TYPE
			// .NA
			// file

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
			affymetrixFrame
					.setTitle("Network loaded, now loading visualisation...");

			// load the VIZMAP props file
			controller.mapVisually(VIZMAP_PROPS_FILE_NAME);

			// change the title of the splash frame
			affymetrixFrame
					.setTitle("Visualisation loaded, this window closes automatically.");

			// delete the splashframe
			affymetrixFrame.dispose();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Invalid Barcode files detected. Please check!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			e1.printStackTrace();
		}
	}


}
