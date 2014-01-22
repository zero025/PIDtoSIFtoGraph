package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

public final class IntCompAnalyzerImpl implements IntCompAnalyzer {

	private static IntCompAnalyzerImpl instance=null;
	
	private IntCompAnalyzerImpl(){}
	
	public static IntCompAnalyzerImpl getInstance(){
		if (null==instance)
			instance=new IntCompAnalyzerImpl();
		return instance;
	}
	
	
	@Override
	public boolean hasPredecessors(InteractionComponent intComp)
			throws InconsistentOntologyException, UnknownOntologyException {
		Collection<Collection<OntologyElement>> roles = intComp
				.getRolesForInteraction().values();

		EdgeTypeOntology eOnto = this.getOntology();
		for (Collection<OntologyElement> rolesForInter : roles) {
			for (OntologyElement role : rolesForInter) {
				if (eOnto.isSpecialEdge(role, EdgeTypeOntology.OUTGOINGNAME))
					return true;
			}
		}
		return false;
	}

	@Override
	public Collection<String> getPredecessors(InteractionComponent intComp)
			throws InconsistentOntologyException, UnknownOntologyException {
		Collection<String> predecessors = new ArrayList<String>();
		Map<String, Collection<OntologyElement>> roles = intComp
				.getRolesForInteraction();
		EdgeTypeOntology eOnto = this.getOntology();
		for (Entry<String, Collection<OntologyElement>> interaction : roles.entrySet()) {
			Collection<OntologyElement> rolForInt = roles.get(interaction.getKey());
			for (OntologyElement role : rolForInt) {
				if (eOnto.isSpecialEdge(role, EdgeTypeOntology.OUTGOINGNAME)) {
					predecessors.add(interaction.getKey());
					break;
				}

			}
		}
		return predecessors;
	}

	@Override
	public boolean hasSuccessors(InteractionComponent intComp)
			throws UnknownOntologyException, InconsistentOntologyException {
		Collection<Collection<OntologyElement>> roles = intComp
				.getRolesForInteraction().values();

		EdgeTypeOntology eOnto = this.getOntology();
		for (Collection<OntologyElement> rolesForInter : roles) {
			for (OntologyElement role : rolesForInter) {
				if (eOnto.isSpecialEdge(role, EdgeTypeOntology.INCOMINGNAME))
					return true;
			}
		}
		return false;
	}

	@Override
	public Collection<String> getSuccessors(InteractionComponent intComp) throws UnknownOntologyException, InconsistentOntologyException {
		Collection<String> successors = new ArrayList<String>();
		Map<String, Collection<OntologyElement>> roles = intComp
				.getRolesForInteraction();
		EdgeTypeOntology eOnto = this.getOntology();
		for (Entry<String, Collection<OntologyElement>> interaction : roles.entrySet()) {
			Collection<OntologyElement> rolForInt = interaction.getValue();
			for (OntologyElement role : rolForInt) {
				if (eOnto.isSpecialEdge(role, EdgeTypeOntology.INCOMINGNAME)) {
					successors.add(interaction.getKey());
					break;
				}

			}
		}
		return successors;
	}

	@Override
	public boolean isIsolatedNode(InteractionComponent intComp) {
		Collection<String> interactions = intComp.getInteractionsIds();
		return interactions.isEmpty();
	}

	private EdgeTypeOntology getOntology() throws UnknownOntologyException {
		Ontology onto = OntologyManager.getInstance().getOntology(
				EdgeTypeOntology.NAME);
		if (onto != null) {
			if (onto.getClass() == EdgeTypeOntology.class)
				return (EdgeTypeOntology) onto;
		}
		throw new UnknownOntologyException("Edge-Type ontology is not set!");
	}

}
