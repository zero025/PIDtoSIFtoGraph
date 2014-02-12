package de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;

public enum SpecialOntologies {

	ACTIVITYSTATE("activity-state"), 
	EDGETYPE("edge-type"), 
	MOLECULETYPE("molecule-type"), 
	PROCESSTYPE("process-type"), 
	LOCATION("location"), 
	PTM("ptm"), 
	UNKNOWN("");

	private String name;

	private SpecialOntologies(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	public static SpecialOntologies getValue(String role) {
		try {
			return valueOf(role.toUpperCase().replace("-", ""));
		} catch (Exception e) {
			return UNKNOWN;
		}
	}

	public Ontology newSpecialOntology(int id, String name) {
		switch (this) {
		case ACTIVITYSTATE:
			return new ActivityStateOntology(id);
		case EDGETYPE:
			return new EdgeTypeOntology(id);
		case LOCATION:
			return new LocationOntology(id);
		case MOLECULETYPE:
			return new MoleculeTypeOntology(id);
		case PROCESSTYPE:
			return new ProcessTypeOntology(id);
		case PTM:
			return new PtmOntology(id);
		default:
			return new Ontology(id, name);
		}
	}

	public Ontology newSpecialOntology(String id, String name) {
		return newSpecialOntology(Integer.valueOf(id).intValue(), name);
	}
}
