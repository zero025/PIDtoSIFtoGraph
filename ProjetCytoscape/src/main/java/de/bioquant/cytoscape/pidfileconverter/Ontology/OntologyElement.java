package de.bioquant.cytoscape.pidfileconverter.Ontology;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InvalidParentException;

public class OntologyElement implements Comparable<OntologyElement> {

	private int id;
	private int parentId;
	private OntologyElement parent = null;
	private String name;

	public OntologyElement(int id, int parentId, String name) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public OntologyElement(String id, String parentId, String name) {
		super();
		this.id = Integer.valueOf(id).intValue();
		this.parentId = Integer.valueOf(parentId).intValue();
		this.name = name;
	}

	public boolean isRoot() {
		return (id == parentId);
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the parentId
	 */
	public final int getParentId() {
		return parentId;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	public void setParent(OntologyElement parent) throws InvalidParentException {
		if (null == parent || parentId != parent.getId()) {
			throw new InvalidParentException();
		}
		this.parent = parent;

	}

	/**
	 * @return the parent
	 */
	public final OntologyElement getParent() {
		return parent;
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
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + parentId;
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
		if (!(obj instanceof OntologyElement))  {
			return false;
		}
		OntologyElement other = (OntologyElement) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentId != other.parentId)  {
			return false;
		}
		return true;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public final void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public int compareTo(OntologyElement o) {
		return ((Integer) id).compareTo(o.getId());
	}

	public boolean isRootChild() {
		if (null == parent) {
			return false;
		}
		else {
			return parent.isRoot();
		}
	}
}
