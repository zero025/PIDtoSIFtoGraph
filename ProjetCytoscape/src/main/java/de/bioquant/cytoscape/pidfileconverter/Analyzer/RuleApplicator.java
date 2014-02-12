/**
 * @author Florian Dittmann
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.InteractionNodeManager;

public class RuleApplicator {

	private List<Rule> rules = new ArrayList<Rule>();

	/**
	 * Initializes a new RuleApplicator without any rules.
	 */
	public RuleApplicator() {
		super();
	}

	/**
	 * Adds rule to rule list.
	 * 
	 * @param rule
	 * @return
	 */
	public boolean addRule(Rule rule) {
		return rules.add(rule);
	}

	/**
	 * Applies all the added rules to the given rule graph based on the
	 * information about the interactions in the interaction node manager
	 * 
	 * @param manager
	 *            which contains all the information about the interactions in
	 *            the graph
	 * @param graph graph where the modifications by the rules should be persisted
	 */
	public final void applyRulesOnGraph(InteractionNodeManager manager,
			RuleGraph graph) {
		for (Rule rule : rules) {
			Collection<InteractionNode> interactions = manager
					.getAllInteractions();
			for (InteractionNode interaction : interactions) {
				if (rule.processModificationsIFConditionFullfilled(interaction,
						graph))
					System.out.println("Rule '" + rule.getRuleName()
							+ "' was applied on " + interaction.getFullPid());
			}
			graph.persistDeletingNodes();
		}
	}
}
