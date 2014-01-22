package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyElementException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

/**
 * 
 * @author Florian
 *
 */
public class InteractionComponentImpl extends CompMolMemberImpl implements InteractionComponent{
	
	private Map<String, Collection<OntologyElement>> interactions=new HashMap<String, Collection<OntologyElement>>();
		
	public InteractionComponentImpl(String pid) throws InvalidIdException
	{
		super(pid);
	}
	
	public InteractionComponentImpl(MoleculeNode node) throws InvalidIdException, InvalidArgumentException
	{
		super(node);
	}

	@Override
	public Collection<OntologyElement> getRolesTypeForInteraction(
			String interactionID) throws InvalidInteractionIdException {
		String id =IdClearer.clearUpPidI(interactionID);
		return interactions.get(id);
		
	}

	@Override
	public boolean setRoleTypeForInteraction(String interactionID,
			OntologyElement roleType) throws InvalidArgumentException,
			InvalidInteractionIdException {
		String id =IdClearer.clearUpPidI(interactionID);
		Collection<OntologyElement> roleList;
		if (!interactions.containsKey(interactionID))
		{
			roleList=new ArrayList<OntologyElement>();
			interactions.put(id,roleList);
		}else
			roleList=interactions.get(id);
		if (roleList.contains(roleType))
			return false;
		else
			return roleList.add(roleType);
	}

	@Override
	public boolean setRoleType(String roleTypeName, String interaction)
			throws InvalidArgumentException, UnknownOntologyElementException,
			InvalidInteractionIdException {
		Ontology onto=AbstractGraphNode.ONTOMANAGER.getOntology(EdgeTypeOntology.NAME);
		OntologyElement role=onto.getElement(roleTypeName);
		return this.setRoleTypeForInteraction(interaction, role);
		
	}

	@Override
	public Collection<String> getInteractionsIds() {
		return interactions.keySet();
	}

	@Override
	public Map<String, Collection<OntologyElement>> getRolesForInteraction() {
		return Collections.unmodifiableMap(this.interactions);
	}

	@Override
	public boolean setRoleTypesForInteractions(
			Map<String, Collection<OntologyElement>> roles) throws InvalidInteractionIdException, InvalidArgumentException {
		for (Entry<String, Collection<OntologyElement>> interaction:roles.entrySet())
		{
			String interactionId=interaction.getKey();
			Collection<OntologyElement> currRoles=interaction.getValue();
			for (OntologyElement role:currRoles)
			{
				if(!this.setRoleTypeForInteraction(InteractionNode.PREFIX+interactionId, role))
					return false;
			}
		}
		return false;
	}


}
