package de.bioquant.cytoscape.pidfileconverter.FileReader;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;

public interface FileReader {
	
	public void read(String path) throws NoValidManagerSetException, FileParsingException;

}
