package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public class EdgeTypeOntology extends Ontology {

	public final static String NAME = SpecialOntologies.EDGETYPE.toString();
	public final static String INCOMINGNAME = "incoming-edge";
	public final static String OUTGOINGNAME = "outgoing-edge";
	public final static String AGENTNAME = "agent";
	public final static String INHIBITOR = "inhibitor";
	public final static String INPUT = "input";
	public final static String OUTPUT = "output";

	public EdgeTypeOntology(int id) {
		super(id, NAME);
	}

	public EdgeTypeOntology(String id) {
		super(id, NAME);
	}

	public boolean isSpecialEdge(OntologyElement edge, String name) throws UnknownOntologyException,
			InconsistentOntologyException {
		OntologyElement element = this.getElement(name);
		if (element == null) {
			throw new UnknownOntologyException("'" + name + "' is not known in edgeType ontology!");
		}
		return this.isParentOf(element.getId(), edge.getId());
	}
}
