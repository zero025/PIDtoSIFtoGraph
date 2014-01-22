package de.bioquant.cytoscape.pidfileconverter.Naming;

import java.util.ArrayList;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PTMterm;

public class ModificationExtractor {
	
	public static String getModificationString(
			ComponentModification modification) {

		StringBuffer string = new StringBuffer();

		if (modification.hasAnyModifications())
			string.append("|Mod" + modification.getModID());
		if (modification.hasSpecifiedActivityState())
			string.append("|" + modification.getActivityState());
		if (modification.hasSpecifiedLocation())
		{
			string.append("@" + modification.getLocation());
		}
		return string.toString().replaceAll(" ", "_");
	}
	
	public static String getDetailedModifactionString(
			ComponentModification modification)
	{
		StringBuffer string = new StringBuffer();
		if (modification.hasAnyModifications())
		{
			List<PTMterm> mods=modification.getModifications();
			for (PTMterm term:mods)
			{
				String pos=term.getPosition();
				String side=term.getAa();
				String name=term.getModification().getName();
				string.append(name);
				string.append("@"+pos);
				string.append("@"+side+":");
			}
			string.deleteCharAt(string.length()-1);
		}
		return string.toString().replaceAll(" ", "_");
	}
	
	public static String getDetailledModificationStringForMember(ComponentModification modification)
	{
		StringBuffer string = new StringBuffer();

		if (modification.hasAnyModifications())
			string.append("|" + getDetailedModifactionString(modification));
		if (modification.hasSpecifiedActivityState())
			string.append("|" + modification.getActivityState());
		if (modification.hasSpecifiedLocation())
		{
			string.append("@" + modification.getLocation());
		}
		return string.toString().replaceAll(" ", "_");
		
	}
	
	public static String getDetailledModificatonStringForComplexMolecules(List<CompMolMember> members,String memberSeperator)
	{
		
		StringBuffer finalString=new StringBuffer();
		ArrayList<String> memberStrings=new ArrayList<String>();
		for (CompMolMember member:members)
		{
			if (!member.hasModification()) continue;
			StringBuffer string = new StringBuffer();
			String mod=getDetailledModificationStringForMember(member.getModification());
			if (!mod.isEmpty())
			{
				MoleculeNode memberMolecule=member.getMolecule();
				String id=CreatorUniprotWithModification.getInstance().getId(memberMolecule);
				string.append(id);
				if (memberMolecule.hasPreferredSymbol())
						string.append("("+memberMolecule.getPreferredSymbol()+")");
				string.append(mod+memberSeperator);
				String finalMemberString=string.toString();
				if (!memberStrings.contains(finalMemberString))
				{
					finalString.append(finalMemberString);
					memberStrings.add(finalMemberString);
				}
			}
		}
		if (finalString.length()>0)
			finalString.deleteCharAt(finalString.length()-1);
		return finalString.toString();
	}

}
