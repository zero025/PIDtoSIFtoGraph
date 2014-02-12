package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;

public class LocationOntology extends Ontology {

	public final static String NAME = SpecialOntologies.LOCATION.toString();

	public LocationOntology(int id) {
		super(id, NAME);
	}

	public LocationOntology(String id) {
		super(id, NAME);
	}
}
