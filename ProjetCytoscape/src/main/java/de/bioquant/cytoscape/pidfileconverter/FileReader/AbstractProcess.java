/**
 * Abstract class for a process in a separate thread
 * @author Thomas Brunel & Yamei Sun
 */
package de.bioquant.cytoscape.pidfileconverter.FileReader;

public abstract class AbstractProcess implements Runnable{
	protected boolean continueThread=true;
	
	/**
	 * 
	 * @return false if the thread has to stop
	 */
	public boolean isContinueThread() {
		return continueThread;
	}
	
	/**
	 * Asks the thread to stop if the parameter is false.
	 * @param continueThread
	 */
	public void setContinueThread(boolean continueThread) {
		this.continueThread = continueThread;
	}
}
