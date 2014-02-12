package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;

public interface Rule {
	
	/**
	 * Applies the rule on the given interaction, if it fulfills the conditions of the rule.
	 * @param interaction where the rule should be applied
	 * @param graph where the modifications by rule should be applied 
	 * @return true - if the given interaction fulfills the conditions of the rule; false - else
	 */
	public boolean processModificationsIFConditionFullfilled(InteractionNode interaction, RuleGraph graph);
	
	public String getRuleName();
}
