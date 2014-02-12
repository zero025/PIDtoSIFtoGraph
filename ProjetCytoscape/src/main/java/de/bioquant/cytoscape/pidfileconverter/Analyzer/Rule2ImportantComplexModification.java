/**
 * @author Florian Dittmann
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.Modification;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

public class Rule2ImportantComplexModification implements Rule {

	public final static String RULENAME = "Rule2ImportantComplexModification";

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
			if (outgoing.size() != 1) {
				return false;
			}
			InteractionComponent out = outgoing.iterator().next();
			if (!out.getMolecule().hasComplexComponents()) {
				return false;
			}
			Collection<InteractionComponent> incoming = interaction
					.getSpecialEdgeInteractionComponents(EdgeTypeOntology.INCOMINGNAME);

			Set<CompMolMember> outComps = new TreeSet<CompMolMember>(out
					.getMolecule().getComplexComponents());
			Set<CompMolMember> inComps = this.decomposeIntCompList(incoming);
			outComps.removeAll(inComps);
			if (outComps.size() != 1) {
				return false;
			}
			CompMolMember outMem = outComps.iterator().next();
			if (this.containsMemberWithDifferentModification(inComps, outMem)) {
				String newName = naming.getNameForCompMolMember(outMem);
				graph.getNodeForName(naming.getNameForCompMolMember(out))
				.setName(newName);
				return true;
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

	private Set<CompMolMember> decomposeIntCompList(
			Collection<InteractionComponent> incoming) {
		Set<CompMolMember> result = new TreeSet<CompMolMember>();
		for (CompMolMember intComp : incoming) {
			if (intComp.getMolecule().hasComplexComponents()){
				result.addAll(intComp.getMolecule().getComplexComponents());
			}
			else {
				result.add(intComp);
			}
		}
		return result;
	}

	private boolean containsMemberWithDifferentModification(
			Collection<CompMolMember> collection, CompMolMember member) {
		Modification memMod=this.getModificationsFromMember(member);
		for (CompMolMember compMem : collection) {
			if (compMem.getMolecule().equals(member.getMolecule())) {
				Modification compMod=this.getModificationsFromMember(compMem);
				if (memMod!=null) {
					return !memMod.equals(compMod);
				}
				else {
					if (compMod!=null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private Modification getModificationsFromMember(CompMolMember member){
		if (member.hasModification()){
			ComponentModification compMod=member.getModification();
			if (compMod.hasAnyModifications()) {
				return compMod.getModification();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bioquant.cytoscape.pidfileconverter.Analyzer.Rule#getRuleName()
	 */
	@Override
	public String getRuleName() {
		return RULENAME;
	}
}
