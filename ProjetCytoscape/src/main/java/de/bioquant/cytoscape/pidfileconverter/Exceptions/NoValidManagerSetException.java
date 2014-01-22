package de.bioquant.cytoscape.pidfileconverter.Exceptions;

public class NoValidManagerSetException extends Exception {
	
	public NoValidManagerSetException(){
		super("There is no valid manager set!");		
	}
	
	public NoValidManagerSetException(String message){
		super(message);		
	}
	
	public NoValidManagerSetException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
