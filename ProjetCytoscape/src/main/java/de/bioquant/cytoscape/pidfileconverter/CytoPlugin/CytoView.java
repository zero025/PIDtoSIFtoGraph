package de.bioquant.cytoscape.pidfileconverter.CytoPlugin;

import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.util.CytoscapeAction;
import de.bioquant.cytoscape.pidfileconverter.CytoPlugin.LoadFileToGraphAction;

public class CytoView extends CytoscapePlugin
{	
	public CytoView()
	{
		initMenuItem();
	}

	protected void initMenuItem()
	{
		LoadFileToGraphAction loadwindowaction = new LoadFileToGraphAction("PIDtoSIFtoGraph");
		// add this to the menu
		Cytoscape.getDesktop().getCyMenus().addCytoscapeAction((CytoscapeAction) loadwindowaction);
	}
}