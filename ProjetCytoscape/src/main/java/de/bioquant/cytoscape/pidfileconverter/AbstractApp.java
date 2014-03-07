/**
 * Abstract application that defines the run() function
 * @author Florian Dittmann
 * @author Hadi Adisurya Kang (extensions)
 * @contributor Thomas Brunel & Yamei Sun
 */

package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.Step1;

public abstract class AbstractApp {
	private Controller controller;
	private Step1 step1;

	/**
	 * Start the plugin
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		if (step1 == null) {
			step1 = new Step1(controller);
		} else {
			step1.setVisible(true);
		}
	}

	public abstract void extraOutput(BufferedReader reader,
			NodeManagerImpl manager, String path) throws FileNotFoundException;

}
