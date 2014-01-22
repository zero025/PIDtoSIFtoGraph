package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;

/**
 * This class represents a part relation to a parent molecule. The attributes of
 * this object contain the ID of the parent and the start and end position of
 * the part molecule.
 * 
 * @author Florian Dittmann
 */
public class PartRelation {

	private String parent;
	private String start;
	private String end;

	/**
	 * Initializes new part relation with given parent and empty start and end
	 * position.
	 * 
	 * @param parent
	 *            pid of parent molecule, must not ne null or empty
	 * @throws InvalidArgumentException
	 *             thrown if parent is null or empty thrown
	 */
	public PartRelation(String parent) throws InvalidArgumentException {
		super();
		if (null == parent || parent.isEmpty())
			throw new InvalidArgumentException();
		this.parent = parent;
		start = "";
		end = "";
	}

	/**
	 * Initializes new part relation with given parent and start and end
	 * position.
	 * 
	 * @param parent
	 *            pid of parent molecule, must not ne null or empty
	 * @param start
	 *            start position of part, must not be null
	 * @param end
	 *            end position of part, must not be null
	 * @throws InvalidArgumentException
	 *             thrown if parent is null or empty, start is null or end is
	 *             null
	 */
	public PartRelation(String parent, String start, String end)
			throws InvalidArgumentException {
		super();
		if (null == parent || null == start || null == end || parent.isEmpty())
			throw new InvalidArgumentException();
		this.parent = parent;
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public final String getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public final String getEnd() {
		return end;
	}

	/**
	 * @return the parent
	 */
	public final String getParent() {
		return parent;
	}

}
