package de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions;

@SuppressWarnings("serial")
public class UnknownOntologyException extends Exception {

	public UnknownOntologyException() {
		super("This ontology is not known!");

	}

	public UnknownOntologyException(String message, Throwable cause) {
		super(message, cause);

	}

	public UnknownOntologyException(String message) {
		super(message);

	}

	public UnknownOntologyException(Throwable cause) {
		super(cause);
	}
}
