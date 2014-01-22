package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents all the modifications (ptm-terms) of one single
 * molecule. Additionally each object of this class has an unique id.
 * 
 * @author Florian Dittmann
 * 
 */
public class Modification implements Comparable<Modification> {

	private ArrayList<PTMterm> modifications = new ArrayList<PTMterm>();
	private static long count = 0;
	private final long modID;

	public Modification() {
		modID = count;
		count++;
	}

	/**
	 * Returns an unmodifiable list of the modifications represented by the
	 * ptm-terms.
	 * 
	 * @return the modifications
	 */
	public final List<PTMterm> getModifications() {
		return Collections.unmodifiableList(modifications);
	}

	public boolean addModification(PTMterm modification) {
		return modifications.add(modification);
	}

	public boolean hasAnyModifications() {
		return !modifications.isEmpty();
	}

	/**
	 * @return the modID
	 */
	public final long getModID() {
		return modID;
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
		result = prime * result
				+ ((modifications == null) ? 0 : modifications.hashCode());
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
		if (!(obj instanceof Modification)) {
			return false;
		}
		Modification other = (Modification) obj;
		if (modifications == null) {
			if (other.modifications != null) {
				return false;
			}
		} else if (!modifications.equals(other.modifications)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Modification o) {
		return ((Long) modID).compareTo(o.getModID());
	}

}
