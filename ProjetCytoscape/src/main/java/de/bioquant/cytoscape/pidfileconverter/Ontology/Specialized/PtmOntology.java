/**
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;

public class PtmOntology extends Ontology {

	public final static String NAME = SpecialOntologies.PTM.toString();

	public PtmOntology(int id) {
		super(id, NAME);
	}

	public PtmOntology(String id) {
		super(id, NAME);
	}
}
