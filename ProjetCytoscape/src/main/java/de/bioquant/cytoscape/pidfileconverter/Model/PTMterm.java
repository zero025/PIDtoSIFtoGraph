/**
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;

/**
 * @author Florian Dittmann
 * 
 */
public class PTMterm {

	private String position = "";
	private String aa = "";
	private OntologyElement modification;

	/**
	 * Initilizes a new PTM term object with the given position, site and
	 * modification type.
	 * 
	 * @param position of the modification
	 * @param aa side of the modification
	 * @param modification type of the modification
	 * @throws InvalidArgumentException throw if when of the arguments is null
	 */
	public PTMterm(String position, String aa, OntologyElement modification)
			throws InvalidArgumentException {
		if (position == null || aa == null || modification == null)
			throw new InvalidArgumentException("Arguments must not be null!");
		this.position = position;
		this.aa = aa;
		this.modification = modification;
	}

	/**
	 * @return the position of the modification
	 */
	public final String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public final void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the side of the modification
	 */
	public final String getAa() {
		return aa;
	}

	/**
	 * @param aa
	 *            the aa to set
	 */
	public final void setAa(String aa) {
		this.aa = aa;
	}

	/**
	 * @return the type of the modification represented by an ontology element
	 */
	public final OntologyElement getModification() {
		return modification;
	}

	/**
	 * @param modification
	 *            the modification type to set
	 */
	public final void setModification(OntologyElement modification) {
		this.modification = modification;
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
		result = prime * result + ((aa == null) ? 0 : aa.hashCode());
		result = prime * result
				+ ((modification == null) ? 0 : modification.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
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
		if (!(obj instanceof PTMterm)) {
			return false;
		}
		PTMterm other = (PTMterm) obj;
		if (aa == null) {
			if (other.aa != null) {
				return false;
			}
		} else if (!aa.equals(other.aa)) {
			return false;
		}
		if (modification == null) {
			if (other.modification != null) {
				return false;
			}
		} else if (!modification.equals(other.modification)) {
			return false;
		}
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!position.equals(other.position)) {
			return false;
		}
		return true;
	}

}
