package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public interface IntCompAnalyzer {
	
	public boolean hasPredecessors(InteractionComponent intComp) throws InconsistentOntologyException, UnknownOntologyException;
	
	public Collection<String> getPredecessors(InteractionComponent intComp) throws InconsistentOntologyException, UnknownOntologyException;
	
	public boolean hasSuccessors(InteractionComponent intComp) throws UnknownOntologyException, InconsistentOntologyException;
	
	public Collection<String> getSuccessors(InteractionComponent intComp) throws UnknownOntologyException, InconsistentOntologyException;
	
	/**
	 * 
	 * @param intComp
	 * @return true - has neither predecessors nor successors; false -else
	 */
	public boolean isIsolatedNode(InteractionComponent intComp);
	
	
	
	

}
