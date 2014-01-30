package de.bioquant.cytoscape.pidfileconverter.Naming;

import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;

public final class CreatorPreferredSymbolWithModification extends
		AbstractNameCreatorWithModification {

	private static CreatorPreferredSymbolWithModification instance = null;
	
	
	public static CreatorPreferredSymbolWithModification getInstance() {
		if (null == instance)
			instance = new CreatorPreferredSymbolWithModification();
		return instance;
	}

	private CreatorPreferredSymbolWithModification() {
	}
	
	/* (non-Javadoc)
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Naming.CreatorIDWithModification#getId(de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.MoleculeNode)
	 */
	@Override
	public String getId(MoleculeNode molecule) {
		if (molecule.hasPreferredSymbol())
			return molecule.getPreferredSymbol();
		else
			return molecule.getFullPid();
	}

}
