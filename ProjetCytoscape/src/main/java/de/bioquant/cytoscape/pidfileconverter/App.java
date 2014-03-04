/**
 * 
 * @author Florian Dittmann
 * 
 */
package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;

import de.bioquant.cytoscape.pidfileconverter.Analyzer.IntCompAnalyzerImpl;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public class App extends AbstractApp {
	
	private static App instance=null;

	private App(){}
	
	public static App getInstance(){
		if (null==instance) {
			instance=new App();
		}
		return instance;
	}
	
	public static void main(String[] args) throws Exception{
		App app = App.getInstance();
		app.run();
	}
	
	@Override
	public void extraOutput(BufferedReader reader, NodeManagerImpl manager,
			String path) {
	}
}
