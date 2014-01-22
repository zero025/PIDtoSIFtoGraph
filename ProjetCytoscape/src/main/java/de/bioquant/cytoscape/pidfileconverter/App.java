package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;

import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

/**
 * 
 * @author Florian Dittmann
 * 
 */
public class App extends AbstractApp {

	
	public static void main(String[] args) throws Exception{
		App app = new App();
		app.run();

		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.AbstractApp#extraOutput(java
	 * .io.BufferedReader,
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.NodeManager.NodeManagerImpl,
	 * java.lang.String)
	 */
	@Override
	public void extraOutput(BufferedReader reader, NodeManagerImpl manager,
			String path) {

	}
}
