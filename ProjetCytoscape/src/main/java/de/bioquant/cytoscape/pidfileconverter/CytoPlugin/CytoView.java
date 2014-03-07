/**
 * Add the menu entry of the plugin to Cytoscape
 *
 */

package de.bioquant.cytoscape.pidfileconverter.CytoPlugin;

import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.util.CytoscapeAction;

public class CytoView extends CytoscapePlugin {	
	public CytoView() {
		initMenuItem();
	}

	protected void initMenuItem() {
		LoadFileToGraphAction loadwindowaction = new LoadFileToGraphAction("PIDtoSIFtoGraph");
		// add this to the menu
		Cytoscape.getDesktop().getCyMenus().addCytoscapeAction((CytoscapeAction) loadwindowaction);
	}
}