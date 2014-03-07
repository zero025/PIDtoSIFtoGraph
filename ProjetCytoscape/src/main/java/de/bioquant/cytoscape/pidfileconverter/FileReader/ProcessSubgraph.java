/**
 * Separate thread for the step 3
 * @contributor Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.Cytoscape;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;
import de.bioquant.cytoscape.pidfileconverter.View.Step3;

public class ProcessSubgraph extends AbstractProcess {

	private SplashFrame subgraphFrame;
	private Controller controller;
	private static String targetSIFpath;
	private Step3 step3;
	private String targetsubgraphedSIFpath;

	// the file name of the VIZMAP property file
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";

	/**
	 * Constructor
	 * @param sp
	 * 			the splash frame
	 * @param controller
	 * 			the controller
	 * @param step3
	 * 			window of the step 3
	 * @param targetSIFpath
	 * 			path for the new SIF file created
	 * @param targetsubgraphedSIFpath
	 */
	public ProcessSubgraph(SplashFrame sp, Controller controller, Step3 step3,
			String targetSIFpath, String targetsubgraphedSIFpath) {
		this.subgraphFrame = sp;
		this.controller = controller;
		ProcessSubgraph.targetSIFpath = targetSIFpath;
		this.step3 = step3;
		this.targetsubgraphedSIFpath = targetsubgraphedSIFpath;
	}

	@Override
	public void run() {
		try {

			subgraphFrame.getStart().setEnabled(false);

			// setfocus on the splash frame
			subgraphFrame.requestFocus();

			// TODO: instead of getting targetSIFpath, get actual network worked upon
			String currentNetworkFilepath = targetSIFpath;

			SubgraphExtraction sgex;

			//For the debugging of the .jar
			//JOptionPane.showMessageDialog(new JFrame(),
			//		"The subgraph extraction will be created", "Info",
			//		JOptionPane.INFORMATION_MESSAGE);

			// Stuck here in the .jar
			sgex = new SubgraphExtraction(step3);
			// Test for .jar file :
			// sgex=new SubgraphExtraction();

			//For the debugging of the .jar
			//JOptionPane.showMessageDialog(new JFrame(),
			//		"The subgraph extraction will begin", "Info",
			//		JOptionPane.INFORMATION_MESSAGE);

			// sgex.readGeneSourceFile();
			sgex.readGeneTargetFile(currentNetworkFilepath,
					controller.getTargetNODE_TYPEpath());

			sgex.readSigmolSourceFile();
			sgex.readSigmolTargetFile();
			sgex.readCytoSourceText();
			sgex.readCytoTargetText();

			// then draw the graph from the read files/text long part
			sgex.drawJungGraph(subgraphFrame, this);
			if (!isContinueThread()) {
				return;
			}

			try {
				sgex.SIFreaderAndNewCreator(currentNetworkFilepath);

				String[] temporarypath = currentNetworkFilepath.split(".sif");
				targetsubgraphedSIFpath = temporarypath[0].concat(Controller
						.getSubgraphed() + ".sif");
				// draw graph of new SIF and create a network
				Cytoscape.createNetworkFromFile(targetsubgraphedSIFpath);// load the NODE_TYPE .NA file

				// load the VIZMAP props file
				//TODO : add a checkbox to ask if the user wants to have a hierarchical graph
				controller.mapVisually(VIZMAP_PROPS_FILE_NAME, null);
				JOptionPane.showMessageDialog(new JFrame(),
						"Subgraph successful! Files converted are located in the directory:"
								+ "\n" + targetsubgraphedSIFpath, "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				// delete the splashframe
				subgraphFrame.dispose();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(new JFrame(), "Error :  "
					+ ex.getClass().getName(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

}
