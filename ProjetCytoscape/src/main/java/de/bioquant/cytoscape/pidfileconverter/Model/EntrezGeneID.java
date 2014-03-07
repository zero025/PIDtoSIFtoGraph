/**
 * @author Yamei Sun & Thomas Brunel
 * This class represents one EntrezGene ID. It handles also the part molecules which have the same ID like their parent
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.HashMap;
import java.util.Map;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;

public class EntrezGeneID {

	private String id;
	private static Map<String, Long> partMolCounter = new HashMap<String, Long>();

	/**
	 * Creates a new EntrezGene ID object with specified EntrezGene ID. For part molecules "-P" and a consecutive number is added to have a unique
	 * EntrezGene-ID for each molecule.
	 * 
	 * @param entrezGeneID
	 * @param partmolecule
	 *            flag, whether its an EntrezGene for a part molecule or not
	 * @throws InvalidArgumentException
	 *             thrown if first parameter is null
	 */
	public EntrezGeneID(String entrezGeneID, boolean partmolecule) throws InvalidArgumentException {
		if (null == entrezGeneID)
			throw new InvalidArgumentException("EntrezGene-ID must not be null!");
		if (partmolecule) {
			if (partMolCounter.containsKey(entrezGeneID)) {
				long count = partMolCounter.get(entrezGeneID) + 1;
				this.setEntrezGenePart(entrezGeneID, count);
				partMolCounter.put(entrezGeneID, count);

			} else {
				long startValue = 1;
				partMolCounter.put(entrezGeneID, startValue);
				this.setEntrezGenePart(entrezGeneID, startValue);
			}
		} else {
			id = entrezGeneID;
		}
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	private void setEntrezGenePart(String entrezGeneID, long part) {
		this.id = entrezGeneID + "-P" + part;
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
		if (!(obj instanceof EntrezGeneID)) {
			return false;
		}
		EntrezGeneID other = (EntrezGeneID) obj;
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
