/**
 * Interface for each graph object which has a unique pid (from the pid-xml files).
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;

public interface PidNode {

	public String getPid();

	/**
	 * Returns the full pid. For interactions "pid_i_", for pathways "pid_p_", for molecules "pid_m_" is added at the beginning of the ID
	 * 
	 * @return
	 */
	public String getFullPid();

	/**
	 * Tests whether the full-pid is correct and returns a clear id without prefix.
	 * 
	 * @param pid
	 *            pid to test
	 * @return clear id without prefix
	 * @throws InvalidIdException
	 *             thrown if pid is invalid
	 */
	public String getClearID(final String pid) throws InvalidIdException;
}
