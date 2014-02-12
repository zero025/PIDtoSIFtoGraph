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

public abstract class AbstractApp {
	private Controller controller;
	
	private MainFrame mainframe;

	public void run() throws Exception
	{
		mainframe = new MainFrame(controller);
		mainframe.setTitle("Plugin");
		mainframe.setLocation(10, 10);
		mainframe.setSize(800, 400);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
		mainframe.pack();
	}

	public abstract void extraOutput(BufferedReader reader,
			NodeManagerImpl manager, String path) throws FileNotFoundException;

}
