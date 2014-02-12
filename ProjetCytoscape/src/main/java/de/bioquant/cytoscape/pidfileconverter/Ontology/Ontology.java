/**
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Ontology;

import java.util.Collection;
import java.util.TreeMap;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InvalidParentException;

public class Ontology {

	private int id;
	private TreeMap<Integer, OntologyElement> onto = new TreeMap<Integer, OntologyElement>();
	private String name;
	private boolean connectedTree = true;
	private int inValidIds = -1;

	public Ontology(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Ontology(String id, String name) {
		this.id = Integer.valueOf(id).intValue();
		this.name = name;
	}

	public void connectOntologyElements() throws InconsistentOntologyException {
		if (!connectedTree) {
			Collection<OntologyElement> elements = onto.values();
			for (OntologyElement el : elements) {
				OntologyElement parent = onto.get(el.getParentId());
				try {
					el.setParent(parent);
				} catch (InvalidParentException e) {
					// throw new InconsistentOntologyException(el.getName()+
					// "is not connected to ontology graph of "+name+". will be added to root...");
					// TODO replace dirty bug fix
					System.out.println(el.getName() + " is not connected to ontology graph of " + name
							+ ". will be added to root...");
					parent = getRoot();
					el.setParentId(parent.getId());
					try {
						el.setParent(getRoot());
					} catch (InvalidParentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			this.connectedTree = true;
		}
	}

	public boolean addElement(OntologyElement element) throws InvalidArgumentException {
		if (null == element)
			throw new InvalidArgumentException("Element mustn't be null!");
		if (onto.containsKey(element.getId()))
			return false;
		OntologyElement old = onto.put(element.getId(), element);
		if (null != old) {
			onto.put(old.getId(), element);
			return false;
		} else {
			OntologyElement parent = onto.get(element.getParentId());
			if (null == parent) {
				connectedTree = false;
			}
			else
				try {
					element.setParent(parent);
				} catch (InvalidParentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return true;
		}
	}

	public OntologyElement getElement(int id) {
		return onto.get(id);
	}

	public OntologyElement getElement(String elementName) {
		Collection<OntologyElement> elements = onto.values();
		for (OntologyElement el : elements) {
			if (elementName.equals(el.getName())) {
				return el;
			}
		}
		return null;
	}

	public boolean isParentOf(int parent, int child) throws InconsistentOntologyException {

		this.connectOntologyElements();
		OntologyElement childElement = onto.get(child);
		if (null == childElement) {
			return false;
		}
		if (parent == child) {
			return true;
		}

		do {
			int parentID = childElement.getParentId();
			if (parentID == parent) {
				return true;
			}
			childElement = childElement.getParent();
			if (null == childElement) {
				throw new InconsistentOntologyException();
			}
		} while (!childElement.isRoot());
		return false;
	}

	public boolean isParentOf(String idOfParent, String idOfChild) throws InconsistentOntologyException {

		this.connectOntologyElements();
		int cId = Integer.valueOf(idOfChild).intValue();
		OntologyElement childElement = onto.get(cId);
		int pID = Integer.valueOf(idOfParent).intValue();
		if (null == childElement) {
			return false;
		}
		if (pID == cId) {
			return true;
		}

		do {
			int parentID = childElement.getParentId();
			if (parentID == pID) {
				return true;
			}
			childElement = onto.get(parentID);
			// if (null == childElement)
			// throw new InconsistentOntologyException();
		} while (!childElement.isRoot());
		return false;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	public int getElementCount() {
		return onto.size();
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	public OntologyElement getElementByName(String name) {
		Collection<OntologyElement> allOntos = onto.values();
		for (OntologyElement el : allOntos) {
			if (el.getName().equals(name)) {
				return el;
			}
		}
		return null;
	}

	public OntologyElement getRoot() {
		Collection<OntologyElement> allOntos = onto.values();
		for (OntologyElement el : allOntos) {
			if (el.isRoot()) {
				return el;
			}
		}
		return null;
	}

	public boolean addInvalidElementToRoot(String element) throws InconsistentOntologyException {
		OntologyElement root = this.getRoot();
		if (null == root) {
			throw new InconsistentOntologyException("Ontology '" + name + "' has no root!");
		}
		try {
			for (int i = 0; i < 1000; i++) {
				OntologyElement newElement = new OntologyElement(inValidIds, root.getId(), element);
				inValidIds--;
				if (this.addElement(newElement)) {
					return true;
				}
			}
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}
}
