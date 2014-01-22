package de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions;

public class InconsistentOntologyException extends Exception {

	public InconsistentOntologyException() {
		super("Invalid connection to parent element!");
	}

	public InconsistentOntologyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InconsistentOntologyException(String message) {
		super(message);
	}

	public InconsistentOntologyException(Throwable cause) {
		super(cause);
	}
	
	
}
