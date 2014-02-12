package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.bioquant.cytoscape.pidfileconverter.Analyzer.Rule1SingleModification;
import de.bioquant.cytoscape.pidfileconverter.Analyzer.Rule2ImportantComplexModification;
import de.bioquant.cytoscape.pidfileconverter.Analyzer.RuleApplicator;
import de.bioquant.cytoscape.pidfileconverter.Analyzer.RuleGraph;
import de.bioquant.cytoscape.pidfileconverter.Analyzer.RuleGraphNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public final class SifFileRuleGraphWriter implements FileWriter {

	private static SifFileRuleGraphWriter instance = null;

	private SifFileRuleGraphWriter() {
	}

	public static SifFileRuleGraphWriter getInstance() {
		if (instance == null)
			instance = new SifFileRuleGraphWriter();
		return instance;
	}

	@Override
	public void write(String path, NodeManagerImpl manager) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(path);
		try {
			RuleGraph graph = new RuleGraph(manager);
			RuleApplicator ruleMan = new RuleApplicator();
			ruleMan.addRule(new Rule1SingleModification());
			ruleMan.addRule(new Rule2ImportantComplexModification());
			ruleMan.applyRulesOnGraph(manager, graph);

			Collection<RuleGraphNode> nodes = graph.getAllNodes();
			for (RuleGraphNode node : nodes) {
				Map<RuleGraphNode, String> connections = node.getOutgoingConnections();
				Set<Entry<RuleGraphNode, String>> set = connections.entrySet();
				for (Entry<RuleGraphNode, String> entry : set) {
					TupelWriter.printTriple(writer, node.getName(), entry.getValue(), entry.getKey().getName());
				}
			}
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
	}
}
