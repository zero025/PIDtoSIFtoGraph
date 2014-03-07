/**
 * This class represents one Uniprot ID. It handles also the part molecules which have the same ID like their parent
 * 
 * @author Florian Dittmann
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.HashMap;
import java.util.Map;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;

public class UniprotID {

	private String id;
	private static Map<String, Long> partMolCounter = new HashMap<String, Long>();

	/**
	 * Creates a new Uniprot ID object with specified Uniprot ID. For part molecules "-P" and a consecutive number is added to have a unique
	 * Uniprot-ID for each molecule.
	 * 
	 * @param uniprotID
	 * @param partmolecule
	 *            flag, whether its a Uniprot for a part molecule or not
	 * @throws InvalidArgumentException
	 *             thrown if first parameter is null
	 */
	public UniprotID(String uniprotID, boolean partmolecule) throws InvalidArgumentException {
		if (null == uniprotID)
			throw new InvalidArgumentException("Uniprot-ID must not be null!");
		if (partmolecule) {
			if (partMolCounter.containsKey(uniprotID)) {
				long count = partMolCounter.get(uniprotID) + 1;
				this.setUniProtPart(uniprotID, count);
				partMolCounter.put(uniprotID, count);
			} else {
				long startValue = 1;
				partMolCounter.put(uniprotID, startValue);
				this.setUniProtPart(uniprotID, startValue);
			}
		} else {
			id = uniprotID;
		}
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	private void setUniProtPart(String uniprotID, long part) {
		this.id = uniprotID + "-P" + part;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof UniprotID)) {
			return false;
		}
		UniprotID other = (UniprotID) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
