/**
 * 
 * @author Florian Dittmann
 * 
 */
package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;

import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public class App extends AbstractApp {

	public static void main(String[] args) throws Exception{
		App app = new App();
		app.run();
	}

	@Override
	public void extraOutput(BufferedReader reader, NodeManagerImpl manager,
			String path) {
	}
}
