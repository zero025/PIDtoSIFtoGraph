package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import de.bioquant.cytoscape.pidfileconverter.Model.Modification;

public interface ModificationManager {

	public boolean addModificationforPid(String pid, Modification modification);

	public Modification getEqualModification(String pid, Modification modification);

	public boolean containsModification(String pid, Modification modification);
}
