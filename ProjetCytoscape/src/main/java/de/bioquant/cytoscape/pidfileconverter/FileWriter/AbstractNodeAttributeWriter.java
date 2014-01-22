package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public abstract class AbstractNodeAttributeWriter implements FileWriter {
	
	public abstract String getAttributeName();
	
	public abstract void writeAttributes(PrintWriter writer,NodeManagerImpl manager);

	/* (non-Javadoc)
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.FileWriter.FileWriter#write(java.lang.String, de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.NodeManager.NodeManagerImpl)
	 */
	@Override
	public void write(final String path, final NodeManagerImpl manager)
			throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(path);
		writer.println(getAttributeName());
		writeAttributes(writer,manager);		
		writer.close();
		
	}

}
