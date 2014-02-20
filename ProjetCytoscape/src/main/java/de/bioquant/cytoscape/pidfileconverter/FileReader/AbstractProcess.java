package de.bioquant.cytoscape.pidfileconverter.FileReader;

public abstract class AbstractProcess implements Runnable{
	protected boolean continueThread=true;
	
	public boolean isContinueThread() {
		return continueThread;
	}

	public void setContinueThread(boolean continueThread) {
		this.continueThread = continueThread;
	}
}
