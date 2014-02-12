package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;

/**
 * This class represents an object in the graph,characterized by a pid.
 * 
 * @author Florian Dittmann
 * 
 */
public abstract class AbstractGraphNode implements Comparable<AbstractGraphNode>, PidNode {

	private final String pid;
	protected final static OntologyManager ONTOMANAGER = OntologyManager.getInstance();

	/**
	 * Initializes a an object with the given pid.
	 * 
	 * @param pid
	 *            must not be null or empty; has to be correct pid
	 * @throws InvalidIdException
	 *             if pid is incorrect
	 */
	public AbstractGraphNode(final String pid) throws InvalidIdException {
		if (pid == null || pid.isEmpty())
			throw new InvalidIdException();
		this.pid = this.getClearID(pid);
	}

	@Override
	public String getPid() {
		return pid;
	}

	@Override
	public String getClearID(final String pid) throws InvalidIdException {
		return pid;
	}

	public int compareTo(AbstractGraphNode compObject) {
		return pid.compareTo(compObject.getPid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	public abstract String getPrefix();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.PidNode#getFullPid()
	 */
	@Override
	public String getFullPid() {
		return this.getPrefix() + this.getPid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractGraphNode)) {
			return false;
		}
		AbstractGraphNode other = (AbstractGraphNode) obj;
		if (pid == null) {
			if (other.pid != null) {
				return false;
			}
		} else if (!getFullPid().equals(other.getFullPid())) {
			return false;
		}
		return true;
	}
}
