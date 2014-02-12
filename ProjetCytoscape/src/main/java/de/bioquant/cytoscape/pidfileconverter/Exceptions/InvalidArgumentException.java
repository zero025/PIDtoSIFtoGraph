package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class InvalidArgumentException extends Exception {
	
	public InvalidArgumentException(){
		super("Argument String must not be null or empty!");		
	}
	
	public InvalidArgumentException(String message){
		super(message);		
	}
	
	public InvalidArgumentException(String message, Throwable cause){
		super(message, cause);
	}
}
