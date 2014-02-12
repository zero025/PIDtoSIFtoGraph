/**
 * This class represents a pathway in the graph. It is represented by his pid and name.
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;

public class PathwayNode extends AbstractGraphNode {

	public final static String PREFIX = "pid_p_";
	String name = "";

	/**
	 * Initializes a new pathway with the given pid.
	 * 
	 * @param pid
	 *            has to start with "pid_p_"
	 * @throws InvalidIdException
	 *             thrown if pid is invalid (is null or doesn't start with "pid_p"
	 */
	public PathwayNode(String pid) throws InvalidIdException {
		super(pid);
	}

	public String getName() {
		return name;
	}

	/**
	 * Sets the name for the pathway.
	 * 
	 * @param name
	 *            name to set
	 * @throws InvalidArgumentException
	 *             thrown if name is null or empty
	 */
	public void setName(String name) throws InvalidArgumentException {
		if (name == null || name.isEmpty()) {
			throw new InvalidArgumentException();
		}
		else {
			this.name = name;
		}
	}

	@Override
	public String getClearID(final String pid) throws InvalidIdException {
		return IdClearer.clearUpPidP(pid);
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}
}
