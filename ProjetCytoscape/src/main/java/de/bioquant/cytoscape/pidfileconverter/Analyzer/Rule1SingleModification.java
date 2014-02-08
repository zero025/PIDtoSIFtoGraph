/**
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

/**
 * @author Florian Dittmann
 * 
 */
public class Rule1SingleModification implements Rule {

	public final static String RULENAME="Rule1SingleModification";
	private NameCreator naming = CreatorIDWithModification.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Analyzer.Rule#
	 * processModificationsIFConditionFullfilled
	 * (de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.InteractionNode,
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Analyzer.RuleGraph)
	 */

	@Override
	public boolean processModificationsIFConditionFullfilled(
			InteractionNode interaction, RuleGraph graph) {
		try {
			Collection<InteractionComponent> outgoing = interaction
					.getSpecialEdgeInteractionComponents(EdgeTypeOntology.OUTPUT);
			if (outgoing.size() != 1)
				return false;
			InteractionComponent out = outgoing.iterator().next();
			Collection<InteractionComponent> input = interaction
					.getSpecialEdgeInteractionComponents(EdgeTypeOntology.INPUT);
			Collection<InteractionComponent> agents = interaction
					.getSpecialEdgeInteractionComponents(EdgeTypeOntology.AGENTNAME);
			for (InteractionComponent inter : input) {
				if (inter.getInteractionsIds().size() == 1)
					if (out.getMolecule().equals(inter.getMolecule()))
						if (out.hasModification())
							if (!out.getModification().equals(
									inter.getModification())) {
								setNewAgents(graph, interaction, agents, out);
								String intName = naming
										.getNameForInteraction(interaction);
								graph.deleteNodeFromGraph(intName);
								graph.deleteNodeFromGraph(naming
										.getNameForCompMolMember(inter));

								return true;
							}

			}
			return false;
		} catch (UnknownOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void setNewAgents(RuleGraph graph,
			final InteractionNode interaction,
			Collection<InteractionComponent> agents, InteractionComponent output) {
		String out = naming.getNameForCompMolMember(output);
		for (InteractionComponent agent : agents) {
			String agentName = naming.getNameForCompMolMember(agent);
			String interName = naming.getNameForInteraction(interaction);
			String connection = "new_"
					+ graph.getConnectionForNodes(agentName, interName);
			graph.addConnection(agentName, connection, out);
		}
	}

	/* (non-Javadoc)
	 * @see de.bioquant.cytoscape.pidfileconverter.Analyzer.Rule#getRuleName()
	 */
	@Override
	public String getRuleName() {
		return RULENAME;
	}

}
