/**
 * @author Florian Dittmann
 *
 */
package de.bioquant.cytoscape.pidfileconverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.SifFileRuleGraphWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.StatisticsWriter;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public class AppWithStatAndRules extends AbstractApp {

	public static void main( String[] args ) throws Exception {
		AppWithStatAndRules app=new AppWithStatAndRules();
		app.run();
	}

	/* (non-Javadoc)
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.AbstractApp#extraOutput(java.io.BufferedReader, de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.NodeManager.NodeManagerImpl, java.lang.String)
	 */
	@Override
	public void extraOutput(BufferedReader reader, NodeManagerImpl manager,
			String path) throws FileNotFoundException {
		FileWriter statiticsWriter=StatisticsWriter.getInstance();
		statiticsWriter.write(path+"statistics.csv", manager);

		FileWriter ruleSifWriter=SifFileRuleGraphWriter.getInstance();
		System.out.println("Applying rules...");
		ruleSifWriter.write(path+"graphWithAppliedRules.sif", manager);
	}
}
