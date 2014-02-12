package de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions;

@SuppressWarnings("serial")
public class InvalidParentException extends Exception {

	public InvalidParentException() {
		super();
	}

	public InvalidParentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParentException(String message) {
		super(message);
	}

	public InvalidParentException(Throwable cause) {
		super(cause);
	}
}
