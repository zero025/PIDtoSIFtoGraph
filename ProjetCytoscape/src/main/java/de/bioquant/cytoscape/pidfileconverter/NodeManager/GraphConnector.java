package de.bioquant.cytoscape.pidfileconverter.NodeManager;

public interface GraphConnector {

	public void connectIntCompsToMolecules(MoleculeNodeManager molManager, InteractionComponentManager intCompManager);

	public void connectInteractionsToInteractionComponents(InteractionComponentManager intCompManager,
			InteractionNodeManager interactionManager);
}
