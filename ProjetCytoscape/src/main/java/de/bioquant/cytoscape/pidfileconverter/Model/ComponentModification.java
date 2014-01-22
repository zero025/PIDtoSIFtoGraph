package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.Collections;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;

/**
 * This class represents the modification of a component (of an interaction, a
 * family or complex)
 * 
 * @author Florian Dittmann
 * 
 */
public class ComponentModification implements Comparable<ComponentModification> {

	private Modification modification = new Modification();
	private OntologyElement activityState = null;
	private OntologyElement location = null;

	/**
	 * @return the activityState
	 */
	public final String getActivityState() {
		if (null == activityState)
			return "";
		else
			return activityState.getName();
	}

	/**
	 * @param activityState
	 *            the activityState to set
	 */
	public final void setActivityState(OntologyElement activityState) {
		this.activityState = activityState;
	}

	public boolean hasSpecifiedActivityState() {
		if (activityState == null)
			return false;
		else
			return true;
	}

	/**
	 * @return the location
	 */
	public final String getLocation() {
		if (null == location)
			return "";
		else
			return location.getName();
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public final void setLocation(OntologyElement location) {
		this.location = location;
	}

	public boolean hasSpecifiedLocation() {
		if (location == null)
			return false;
		else
			return true;
	}

	public boolean addModification(PTMterm term) {
		return modification.addModification(term);

	}

	public boolean hasAnyModifications() {
		if (modification!=null)
			return modification.hasAnyModifications();
		return false;
	}

	public long getModID() {
		return modification.getModID();
	}

	public List<PTMterm> getModifications() {
		return Collections.unmodifiableList(modification.getModifications());
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
				+ ((activityState == null) ? 0 : activityState.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((modification == null) ? 0 : modification.hashCode());
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
		if (!(obj instanceof ComponentModification)) {
			return false;
		}
		ComponentModification other = (ComponentModification) obj;
		if (activityState == null) {
			if (other.activityState != null) {
				return false;
			}
		} else if (!activityState.equals(other.activityState)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (modification == null) {
			if (other.modification != null) {
				return false;
			}
		} else if (!modification.equals(other.modification)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the modification
	 */
	public final Modification getModification() {
		return modification;
	}

	/**
	 * @param modification
	 *            the modification to set
	 */
	public final void setModification(Modification modification) {
		this.modification = modification;
	}

	@Override
	public int compareTo(ComponentModification o) {
		int result = modification.compareTo(o.modification);
		if (0 != result)
			return result;
		if (null != activityState && null != o.activityState)
			result = activityState.compareTo(o.activityState);
		else {
			if (null == activityState)
				result = -1;
			else
				result = 1;

		}
		if (0 != result)
			return result;
		if (null != location && null != o.location)
			result = location.compareTo(o.location);
		else {
			if (null == location)
				result = -1;
			else
				result = 1;
		}
		return result;
	}

}
