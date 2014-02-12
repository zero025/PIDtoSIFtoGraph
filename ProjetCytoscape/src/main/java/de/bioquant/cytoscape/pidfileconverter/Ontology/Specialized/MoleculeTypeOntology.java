package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;

public class MoleculeTypeOntology extends Ontology {

	public final static String NAME = SpecialOntologies.MOLECULETYPE.toString();
	public final static String PROTEIN_NAME = "protein";
	public final static String RNA_NAME = "rna";

	public MoleculeTypeOntology(int id) {
		super(id, NAME);
	}

	public MoleculeTypeOntology(String id) {
		super(id, NAME);
	}

	public boolean isProtein(OntologyElement edge) throws InconsistentOntologyException {
		OntologyElement protein = this.getElement(PROTEIN_NAME);
		return this.isParentOf(protein.getId(), edge.getId());
	}

	public boolean isRna(OntologyElement edge) throws InconsistentOntologyException {
		OntologyElement rna = this.getElement(RNA_NAME);
		return this.isParentOf(rna.getId(), edge.getId());
	}
}
