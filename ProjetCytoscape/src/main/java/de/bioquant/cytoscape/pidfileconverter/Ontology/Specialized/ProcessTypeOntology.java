package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;

public class ProcessTypeOntology extends Ontology {

	public final static String NAME = SpecialOntologies.PROCESSTYPE.toString();
	public final static String CELL_PROCESS = "cell-process";
	public final static String BIOLOGICAL_PROCESS = "biological_process";

	public ProcessTypeOntology(int id) {
		super(id, NAME);
	}

	public ProcessTypeOntology(String id) {
		super(id, NAME);
	}

	public boolean isCellularProcess(OntologyElement edge) throws InconsistentOntologyException {
		OntologyElement incomeElement = this.getElement(CELL_PROCESS);
		if (null == incomeElement) {
			// TODO temporary bug fix
			System.out.println(CELL_PROCESS + " is not known in '" + NAME + "'-Ontology!");
			return false;
		}
		return this.isParentOf(incomeElement.getId(), edge.getId());
	}

	public boolean isBiologicalProcess(OntologyElement edge) throws InconsistentOntologyException {
		OntologyElement incomeElement = this.getElement(BIOLOGICAL_PROCESS);
		if (null == incomeElement) {
			// TODO temporary bug fix
			System.out.println(BIOLOGICAL_PROCESS + " is not knowsn in '" + NAME + "'-Ontology!");
			return false;
		}
		return this.isParentOf(incomeElement.getId(), edge.getId());
	}
}
