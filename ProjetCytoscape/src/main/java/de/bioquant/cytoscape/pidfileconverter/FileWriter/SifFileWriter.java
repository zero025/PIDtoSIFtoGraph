package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.InteractionNodeManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

public final class SifFileWriter implements FileWriter {

	private static SifFileWriter instance = null;
	
	private NameCreator naming = CreatorIDWithModification.getInstance();

	private SifFileWriter() {
	}

	public static SifFileWriter getInstance() {
		if (instance == null)
			instance = new SifFileWriter();
		return instance;
	}

	@Override
	public void write(String path, NodeManagerImpl manager) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(path);

		try {
			this.writeInteractions(writer, manager);
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
	}

	public void writeInteractions(PrintWriter out, InteractionNodeManager manager)
			throws InconsistentOntologyException, UnknownOntologyException {
		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode inter : interactions) {

			try {
				String interID = naming.getNameForInteraction(inter);
				String interPid = inter.getFullPid();

				Ontology onto = OntologyManager.getInstance().getOntology(EdgeTypeOntology.NAME);
				if (onto != null) {
					if (onto.getClass() == EdgeTypeOntology.class) {

						EdgeTypeOntology eOnto = (EdgeTypeOntology) onto;

						Collection<InteractionComponent> intComponents = inter.getInteractionComponents();
						for (InteractionComponent node : intComponents) {
							String component = naming.getNameForCompMolMember(node);
							Collection<OntologyElement> roles;

							roles = node.getRolesTypeForInteraction(interPid);

							for (OntologyElement role : roles) {
								String roleString = role.getName();
								if (eOnto.isSpecialEdge(role, EdgeTypeOntology.INCOMINGNAME))  {
									TupelWriter.printTriple(out, component, roleString, interID);
								}
								else {
									TupelWriter.printTriple(out, interID, roleString, component);
								}
							}
						}
					}
				}

				Collection<PathwayNode> pathways = inter.getPatways();
				for (PathwayNode pathway : pathways) {
					String right = naming.getNameForPathway(pathway);
					String middle = "hasPathway";
					String left = interID;
					TupelWriter.printTriple(out, left, middle, right);
				}

				if (inter.hasPosCondition()) {
					String left = inter.getPosCondition();
					String middle = "positiveCond";
					String right = interID;
					TupelWriter.printTriple(out, left, middle, right);
				}
			} catch (InvalidInteractionIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
