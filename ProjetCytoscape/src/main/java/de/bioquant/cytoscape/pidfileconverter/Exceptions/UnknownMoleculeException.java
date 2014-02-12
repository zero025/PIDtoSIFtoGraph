package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class UnknownMoleculeException extends Exception {

	public UnknownMoleculeException(String id) {
		super("molecule "+id+" is not known in manager. Interaction Component can not be created!");
		// TODO Auto-generated constructor stub
	}

	public UnknownMoleculeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownMoleculeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
