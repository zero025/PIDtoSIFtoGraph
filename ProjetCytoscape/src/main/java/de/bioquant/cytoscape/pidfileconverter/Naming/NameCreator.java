package de.bioquant.cytoscape.pidfileconverter.Naming;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;

public interface NameCreator {
	
	public String getNameForInteraction(final InteractionNode interaction);
	
	public String getNameForCompMolMember(final CompMolMember node);
	
	public String getNameForPathway(final PathwayNode pathway);
	
	public String getNameForMolecule(final MoleculeNode molecule);

	/**
	 * @param component
	 * @return
	 */
	String getNameForCompMolMemberManagement(CompMolMember component);
	

}
