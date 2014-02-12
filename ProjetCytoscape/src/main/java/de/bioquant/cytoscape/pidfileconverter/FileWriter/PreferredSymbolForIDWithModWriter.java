package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;

public final class PreferredSymbolForIDWithModWriter extends AbstractPreferredSymbolWriter implements FileWriter {

	private static PreferredSymbolForIDWithModWriter instance = null;

	private PreferredSymbolForIDWithModWriter() {
	}

	public static PreferredSymbolForIDWithModWriter getInstance() {
		if (null == instance) {
			instance = new PreferredSymbolForIDWithModWriter();
		}
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
