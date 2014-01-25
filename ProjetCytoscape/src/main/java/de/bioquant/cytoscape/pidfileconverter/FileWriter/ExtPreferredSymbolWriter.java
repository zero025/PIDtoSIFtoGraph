package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.util.regex.Pattern;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;

public final class ExtPreferredSymbolWriter extends AbstractPreferredSymbolWriter {

	private static ExtPreferredSymbolWriter instance = null;

	private ExtPreferredSymbolWriter() {
	}

	public static ExtPreferredSymbolWriter getInstance() {
		if (null == instance)
			instance = new ExtPreferredSymbolWriter();
		return instance;
	}

	@Override
	String getPreferredSymbol(CompMolMember molMember) {
		StringBuffer result = new StringBuffer(molMember.getMolecule()
				.getPreferredSymbol());
		if (molMember.hasModification()) {
			String activityState = molMember.getModification()
					.getActivityState();
			if (activityState.equals("active"))
				result.append("{plus}");
			else if (activityState.equals("inactive"))
				result.append("{-}");
			String location = molMember.getModification().getLocation();
			if (!location.isEmpty()) {
				String[] words = location.split(" |_");

				result.append('[');
				for (String word : words) {
					if (!word.isEmpty())
						result.append(word.charAt(0));
				}
				result.append(']');
			}
		}
		return result.toString();
	}

	@Override
	public String getAttributeName() {
		return "PREFERRED_SYMBOL_EXT";
	}

}
