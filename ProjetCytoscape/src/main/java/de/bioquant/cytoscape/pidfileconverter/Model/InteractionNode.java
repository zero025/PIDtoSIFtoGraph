/**
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyElementException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.ProcessTypeOntology;

/**
 * This class represents an interaction. It has an ID, a type and eventually a
 * positive condition. It also contains connection to pathways and the component
 * of this interaction.
 * 
 * @author Florian Dittmann
 * 
 */
public class InteractionNode extends AbstractGraphNode {

	public final static String PREFIX = "pid_i_";
	private OntologyElement type;
	private String posCondition = "";
	private Collection<InteractionComponent> interactionComponents = new ArrayList<InteractionComponent>();
	private Collection<PathwayNode> pathways = new ArrayList<PathwayNode>();

	public InteractionNode(final String pid) throws InvalidIdException {
		super(pid);
	}

	public OntologyElement getType() {
		return type;
	}

	/**
	 * Sets the interaction type.
	 * 
	 * @param type
	 *            must not be null
	 * @throws InvalidArgumentException
	 */
	public void setType(final OntologyElement type)
			throws InvalidArgumentException {
		if (type == null)
			throw new InvalidArgumentException();
		this.type = type;
	}

	/**
	 * Sets the interaction type.
	 * 
	 * @param type
	 * @throws UnknownOntologyException
	 *             if process-type ontology is not known
	 * @throws UnknownOntologyElementException
	 *             if ontology element with this element does not exist
	 */
	public void setType(final String type) throws UnknownOntologyException,
			UnknownOntologyElementException {
		Ontology onto = OntologyManager.getInstance().getOntology(
				ProcessTypeOntology.NAME);
		if (null == onto)
			throw new UnknownOntologyException(
					"Process-type ontology is not set!");
		OntologyElement el = onto.getElement(type);
		try {
			setType(el);
		} catch (InvalidArgumentException e) {
			throw new UnknownOntologyElementException();
		}
	}

	/**
	 * Adds the specified interaction component to this interaction.
	 * 
	 * @param newIntComp
	 * @return true - if it is a new interaction component; false - component is
	 *         not added to interaction
	 * @throws InvalidArgumentException
	 */
	public boolean addInteractionComponent(InteractionComponent newIntComp)
			throws InvalidArgumentException {
		if (newIntComp == null)
			throw new InvalidArgumentException();
		if (interactionComponents.contains(newIntComp))
			return false;
		else
			return interactionComponents.add(newIntComp);
	}

	/**
	 * Returns an equal interactioncomponent object, if the interaction contains
	 * such a component.
	 * 
	 * @param newIntComp
	 *            component to compare
	 * @return the equal component object; null otherwise
	 */
	public InteractionComponent getEqualInteractionComponentNodeInInteraction(
			final InteractionComponent newIntComp) {
		for (InteractionComponent comp : interactionComponents) {
			if (comp.equals(newIntComp))
				return comp;
		}
		return null;
	}

	@Override
	public String getClearID(final String pid) throws InvalidIdException {
		return IdClearer.clearUpPidI(pid);
	}

	public Collection<InteractionComponent> getIncomingInteractionComponents()
			throws InconsistentOntologyException, UnknownOntologyException {
		return this
				.getSpecialEdgeInteractionComponents(EdgeTypeOntology.INCOMINGNAME);
	}

	public Collection<InteractionComponent> getOutgoingInteractionComponents()
			throws UnknownOntologyException, InconsistentOntologyException {
		return this
				.getSpecialEdgeInteractionComponents(EdgeTypeOntology.OUTGOINGNAME);
	}

	/**
	 * Returns the interactions component with a special edge type name.
	 * 
	 * @param edgeName
	 * @return an unmodifiable list of the interaction components with the given
	 *         edge name.
	 * @throws UnknownOntologyException
	 * @throws InconsistentOntologyException
	 */
	public Collection<InteractionComponent> getSpecialEdgeInteractionComponents(
			final String edgeName) throws UnknownOntologyException,
			InconsistentOntologyException {
		ArrayList<InteractionComponent> set = new ArrayList<InteractionComponent>();
		Ontology onto = OntologyManager.getInstance().getOntology(
				EdgeTypeOntology.NAME);
		if (onto != null) {
			if (onto instanceof EdgeTypeOntology) {
				try {
					EdgeTypeOntology eOnto = (EdgeTypeOntology) onto;
					for (InteractionComponent intComp : interactionComponents) {
						Collection<OntologyElement> roles = intComp
								.getRolesTypeForInteraction(getFullPid());
						for (OntologyElement role : roles) {
							if (eOnto.isSpecialEdge(role, edgeName)) {
								set.add(intComp);
								break;
							}
						}
					}
				} catch (InvalidInteractionIdException e) {
					e.printStackTrace();
				}
			} else
				throw new UnknownOntologyException(
						"Edge-Type ontology is not set!");
		} else
			throw new UnknownOntologyException("Edge-Type ontology is not set!");
		return Collections.unmodifiableList(set);
	}

	/**
	 * Returns an unmodifiable collection of the added pathways.
	 * 
	 * @return
	 */
	public Collection<PathwayNode> getPatways() {
		return Collections.unmodifiableCollection(this.pathways);
	}

	/**
	 * Adds a pathway to the interaction.
	 * 
	 * @param pathway
	 * @return
	 */
	public boolean addPathway(final PathwayNode pathway) {
		return pathways.add(pathway);
	}

	/**
	 * Returns an unmodifiable collection of all components of this interaction.
	 * 
	 * @return the interactionComponents
	 */
	public Collection<InteractionComponent> getInteractionComponents() {
		return Collections.unmodifiableCollection(interactionComponents);
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}

	/**
	 * @return the posConditions
	 */
	public final String getPosCondition() {
		return posCondition;
	}

	public boolean hasPosCondition() {
		if (null == posCondition || posCondition.isEmpty())
			return false;
		else
			return true;
	}

	/**
	 * @param posCondition
	 *            the posConditions to set
	 */
	public final void setPosConditions(String posCondition) {
		this.posCondition = posCondition.replaceAll(" ", "_");
	}

	public boolean isCellularProcess() throws UnknownOntologyException,
			InconsistentOntologyException {
		Ontology onto = OntologyManager.getInstance().getOntology(
				ProcessTypeOntology.NAME);
		if (onto == null)
			throw new UnknownOntologyException(
					"Process type ontology is not set!");
		else if (onto.getClass() == ProcessTypeOntology.class) {
			ProcessTypeOntology newOnto = (ProcessTypeOntology) onto;
			if (newOnto.isCellularProcess(type))
				return true;
		}
		return false;
	}

	public boolean isBiologicalProcess() throws UnknownOntologyException,
			InconsistentOntologyException {
		Ontology onto = OntologyManager.getInstance().getOntology(
				ProcessTypeOntology.NAME);
		if (onto == null)
			throw new UnknownOntologyException(
					"Process type ontology is not set!");
		else if (onto.getClass() == ProcessTypeOntology.class) {
			ProcessTypeOntology newOnto = (ProcessTypeOntology) onto;
			if (newOnto.isBiologicalProcess(type))
				return true;
		}
		return false;
	}

	public boolean hasOutgoingComponent(InteractionComponentImpl component)
			throws UnknownOntologyException, InconsistentOntologyException {
		Collection<InteractionComponent> components = this
				.getOutgoingInteractionComponents();
		return components.contains(component);
	}

	public boolean hasIncomingComponent(InteractionComponentImpl component)
			throws UnknownOntologyException, InconsistentOntologyException {
		Collection<InteractionComponent> components = this
				.getIncomingInteractionComponents();
		return components.contains(component);
	}

}
