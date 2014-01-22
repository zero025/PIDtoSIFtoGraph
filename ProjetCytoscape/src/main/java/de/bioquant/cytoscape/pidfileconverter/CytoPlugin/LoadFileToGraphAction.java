package de.bioquant.cytoscape.pidfileconverter.CytoPlugin;

import java.awt.event.ActionEvent;

import cytoscape.util.CytoscapeAction;
import de.bioquant.cytoscape.pidfileconverter.App;

public class LoadFileToGraphAction extends CytoscapeAction{

	/**
	 * The constructor of this class.
	 * Sets the name of the menu entry and puts it in the preferred menu.
	 * @param name
	 */
	public LoadFileToGraphAction(String name)
	{
		super(name);
		// set the preferred menu
		setPreferredMenu("Plugins");
		
	}
	
	/**
	 * When the menu item is clicked, this action runs
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		App app = new App();
		try {
			app.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
