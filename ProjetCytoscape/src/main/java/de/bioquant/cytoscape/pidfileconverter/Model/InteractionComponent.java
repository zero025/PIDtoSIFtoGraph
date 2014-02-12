/**
 * This interface represents a component of an interaction.
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.Collection;
import java.util.Map;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyElementException;

public interface InteractionComponent extends CompMolMember {

	/**
	 * Returns the roles of this interaction component for a special interaction.
	 * 
	 * @param interactionID
	 * @return empty - if there is no such connected interaction for this component; else - all roles for specified connection
	 */
	public Collection<OntologyElement> getRolesTypeForInteraction(final String interactionID)
			throws InvalidInteractionIdException;

	/**
	 * Sets the role type of this interaction component for a special interaction.
	 * 
	 * @param interactionID
	 * @param roleType
	 * @return true if successful
	 * @throws InvalidArgumentException
	 *             if one of the arguments is null or empty
	 * @throws InvalidInteractionIdException
	 *             if interactionID is invalid
	 */
	public boolean setRoleTypeForInteraction(final String interactionID, final OntologyElement roleType)
			throws InvalidArgumentException, InvalidInteractionIdException;

	/**
	 * Sets the role type of this interaction component for multiple interactions.
	 * 
	 * @param roles
	 * @return
	 * @throws InvalidInteractionIdException
	 * @throws InvalidArgumentException
	 */
	public boolean setRoleTypesForInteractions(Map<String, Collection<OntologyElement>> roles)
			throws InvalidInteractionIdException, InvalidArgumentException;

	/**
	 * Sets the role type of this interaction component for multiple interactions.
	 * 
	 * @param roleTypeName
	 * @param interaction
	 * @return
	 * @throws InvalidArgumentException
	 * @throws UnknownOntologyElementException
	 *             if an ontology element with roleTypeName is not available
	 * @throws InvalidInteractionIdException
	 */
	public boolean setRoleType(final String roleTypeName, final String interaction) throws InvalidArgumentException,
	UnknownOntologyElementException, InvalidInteractionIdException;

	/**
	 * Returns the IDs of all interactions the component is connected.
	 * 
	 * @return IDs of all Interaction this component is Connected with
	 */
	public Collection<String> getInteractionsIds();

	/**
	 * Returns an unmodifiable map of the roles of this interaction component. The map contains the pid of the interactions mapped to a collection of
	 * roles.
	 * 
	 * @return
	 */
	public Map<String, Collection<OntologyElement>> getRolesForInteraction();
}
