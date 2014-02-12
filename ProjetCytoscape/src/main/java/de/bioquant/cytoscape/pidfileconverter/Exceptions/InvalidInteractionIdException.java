package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class InvalidInteractionIdException extends InvalidIdException {

	public InvalidInteractionIdException() {
		super("Invalid Interaction-ID");
	}

	public InvalidInteractionIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidInteractionIdException(String message) {
		super(message);
	}

	public InvalidInteractionIdException(Throwable cause) {
		super(cause);
	}
}
