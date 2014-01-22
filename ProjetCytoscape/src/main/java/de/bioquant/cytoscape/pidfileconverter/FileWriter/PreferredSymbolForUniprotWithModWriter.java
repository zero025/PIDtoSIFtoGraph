package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;

public final class PreferredSymbolForUniprotWithModWriter extends AbstractPreferredSymbolWriter implements FileWriter {

	private static PreferredSymbolForUniprotWithModWriter instance = null;
	
	private PreferredSymbolForUniprotWithModWriter() {
	}

	public static PreferredSymbolForUniprotWithModWriter getInstance() {
		if (null == instance)
			instance = new PreferredSymbolForUniprotWithModWriter();
		return instance;
	}

	@Override
	public String getAttributeName() {
		return "PREFERRED_SYMBOL";
	}

	@Override
	public String getPreferredSymbol(CompMolMember molMember) {
		return molMember.getMolecule().getPreferredSymbol();
	}

}
