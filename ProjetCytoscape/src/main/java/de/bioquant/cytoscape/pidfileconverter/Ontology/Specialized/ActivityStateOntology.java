package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;

public class ActivityStateOntology extends Ontology {

	public final static String NAME = SpecialOntologies.ACTIVITYSTATE.toString();

	public ActivityStateOntology(final int id) {
		super(id, NAME);
	}

	public ActivityStateOntology(final String id) {
		super(id, NAME);
	}
}
