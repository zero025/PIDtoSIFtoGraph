package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class InvalidUniProtId extends Exception {

	public InvalidUniProtId(String id) {
		super("Invalid UniProt-ID:"+id);
		// TODO Auto-generated constructor stub
	}

	public InvalidUniProtId(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidUniProtId(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
