package de.bioquant.cytoscape.pidfileconverter.Exceptions;

@SuppressWarnings("serial")
public class FileParsingException extends Exception {
	
	public FileParsingException(){
		super("Error during reading file!");		
	}
	
	public FileParsingException(String message){
		super(message);		
	}
	
	public FileParsingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
