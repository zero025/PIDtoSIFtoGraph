/**
 * @author Florian Dittmann
 * @author Hadi Adisurya Kang (extensions)
 * 
 */
package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.MainFrame;
import de.bioquant.cytoscape.pidfileconverter.View.Step1;

public abstract class AbstractApp {
	private Controller controller;

	public void run() throws Exception
	{
		Step1 step1 = new Step1(controller);
	}

	public abstract void extraOutput(BufferedReader reader,
			NodeManagerImpl manager, String path) throws FileNotFoundException;

}
