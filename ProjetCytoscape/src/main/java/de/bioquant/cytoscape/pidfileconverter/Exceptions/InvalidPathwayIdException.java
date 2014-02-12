package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class InvalidPathwayIdException extends InvalidIdException {

	public InvalidPathwayIdException() {
		super("Invalid Pathway ID!");
	}

	public InvalidPathwayIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPathwayIdException(String message) {
		super(message);
	}

	public InvalidPathwayIdException(Throwable cause) {
		super(cause);
	}
}
