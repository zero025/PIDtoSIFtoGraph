package de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions;

@SuppressWarnings("serial")
public class UnknownOntologyElementException extends Exception {

	public UnknownOntologyElementException() {
		super("This onotology element is not known!");
	}

	public UnknownOntologyElementException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownOntologyElementException(String message) {
		super(message);

	}

	public UnknownOntologyElementException(Throwable cause) {
		super(cause);
	}
}
