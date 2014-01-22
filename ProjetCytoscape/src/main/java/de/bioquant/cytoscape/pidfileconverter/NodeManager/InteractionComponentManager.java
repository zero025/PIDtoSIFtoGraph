package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;

public interface InteractionComponentManager {
	
	public boolean addInteractionComponent(InteractionComponent component) throws InvalidInteractionIdException, InvalidArgumentException;
	
	public boolean containsInteractionComponent(InteractionComponent component);
	
	public InteractionComponent getEqualInteractionComponent(InteractionComponent component);
	
	public boolean deleteInteractionComponent(InteractionComponent component);
	
	public Collection<InteractionComponent> getAllInteractionComponents();
	
	public int getInteractionComponentCount();

	void deleteAllInteractionComponents();
	
	

}
