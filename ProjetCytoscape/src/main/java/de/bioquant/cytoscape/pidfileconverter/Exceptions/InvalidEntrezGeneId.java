/**
 * @author Yamei & Thomas
 */
package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class InvalidEntrezGeneId extends Exception {

	public InvalidEntrezGeneId(String id) {
		super("Invalid EntrezGene-ID:"+id);
		// TODO Auto-generated constructor stub
	}

	public InvalidEntrezGeneId(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidEntrezGeneId(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
