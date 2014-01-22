package de.bioquant.cytoscape.pidfileconverter.Naming;

public final class CreatorUniprotWithModification extends AbstractNameCreatorWithModification {

	private static CreatorUniprotWithModification instance = null;
	

	private CreatorUniprotWithModification() {
	}

	public static CreatorUniprotWithModification getInstance() {
		if (null == instance)
			instance = new CreatorUniprotWithModification();
		return instance;
	}

	

}
