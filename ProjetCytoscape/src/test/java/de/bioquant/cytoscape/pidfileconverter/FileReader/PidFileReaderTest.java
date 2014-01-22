package de.bioquant.cytoscape.pidfileconverter.FileReader;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.FileReader.PidFileReader;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponentImpl;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public class PidFileReaderTest extends AbstractFileReaderTest{

	private NodeManagerImpl manager=NodeManagerImpl.getInstance();
	private PidFileReader reader=PidFileReader.getInstance();
	private String testFilePath=this.getTestFilepath();
	private OntologyManager ontoman=OntologyManager.getInstance();

	
	@Before
	public void newNodeManager() throws NoValidManagerSetException
	{
		manager.reset();
		ontoman.deleteAllOntologies();
	}
	
	@Test
	public void testReadOntologies() throws NoValidManagerSetException, FileParsingException, InconsistentOntologyException
	{
		reader.read(testFilePath+"interaction2.xml");
		assertEquals(7,ontoman.getOntologieCount());
		
		Ontology onto= ontoman.getOntology("process-type");
		assertEquals(16,onto.getElementCount());
		assertTrue(onto.isParentOf(19000, 209));
		assertTrue(onto.isParentOf(6, 209));
		assertFalse(onto.isParentOf(19000, 6));
		
		assertEquals(3,ontoman.getOntology("activity-state").getElementCount());
	}
	/*
	@Test
	public void testNewInteractions() throws NoValidManagerSetException, FileParsingException, InvalidIdException {
		reader.read(testFilePath+"interaction.xml");
		assertEquals(3,manager.getInteractionCount());
		InteractionNode i1=new InteractionNode("pid_i_206564");
		i1=manager.getEqualInteractionNodeInManager(i1);
		assertNotNull(i1);
		assertEquals("transcription",i1.getType().getName());
		Collection<InteractionComponent> comp=i1.getInteractionComponents();
		assertEquals(3,comp.size());
		for (InteractionComponent compNode:comp)
		{
			/*if (compNode.getFullPid().equals("pid_m_205115"))
				assertEquals("agent", compNode.getRoleType().getName());
			else if (compNode.getFullPid().equals("pid_m_203895"))
				assertEquals("agent", compNode.getRoleType().getName());
			else if (compNode.getFullPid().equals("pid_m_202990"))
				assertEquals("output", compNode.getRoleType().getName());
			else
				fail("unknown Interaction Component!");			
		}
		assertEquals(8, manager.getMoleculeCount());
	}
	
	@Test
	public void testOneInteractionsWith3Molecules() throws NoValidManagerSetException, FileParsingException, InvalidIdException, InconsistentOntologyException, UnknownOntologyException {
		reader.read(testFilePath+"interaction2.xml");
		assertEquals(1,manager.getInteractionCount());
		assertEquals(3,manager.getMoleculeCount());
		assertEquals(0,manager.getPathwayCount());
		InteractionNode interaction=new InteractionNode(InteractionNode.PREFIX+"201685");
		interaction=manager.getEqualInteractionNodeInManager(interaction);
		assertNotNull(interaction);
		Collection<InteractionComponentInterface> input=interaction.getIncomingInteractionComponents();
		assertEquals(2, input.size());
		MoleculeNode in=input.get(0).getMolecule();
		MoleculeNode mol=new MoleculeNode(MoleculeNode.PREFIX+"200055");
		assertSame(in,manager.getEqualMoleculeNodeInManager(mol));
		
		Collection<InteractionComponentInterface> output=interaction.getOutgoingInteractionComponents();
		assertEquals(1, output.size());
		MoleculeNode out=output.get(0).getMolecule();
		MoleculeNode mol2=new MoleculeNode(MoleculeNode.PREFIX+"200056");
		assertSame(out,manager.getEqualMoleculeNodeInManager(mol2));
		
	}
	
	@Test
	public void test3InteractionsWithOverlappingMoleculesAndOneDuplicateInteractionComponent() throws NoValidManagerSetException, FileParsingException, InvalidIdException, InconsistentOntologyException, UnknownOntologyException {
		reader.read(testFilePath+"interaction3.xml");
		assertEquals(3,manager.getInteractionCount());
		assertEquals(5,manager.getMoleculeCount());
		assertEquals(0,manager.getPathwayCount());
		InteractionNode interaction=new InteractionNode(InteractionNode.PREFIX+"206536");
		interaction=manager.getEqualInteractionNodeInManager(interaction);
		assertNotNull(interaction);
		Collection<InteractionComponentInterface> input=interaction.getIncomingInteractionComponents();
		assertEquals(1, input.size());
		MoleculeNode in=input.get(0).getMolecule();
		MoleculeNode mol=new MoleculeNode(MoleculeNode.PREFIX+"201105");
		assertSame(in,manager.getEqualMoleculeNodeInManager(mol));
		Collection<InteractionComponentInterface> output=interaction.getOutgoingInteractionComponents();
		assertEquals(1, output.size());
		
	}
	
	@Test
	public void testOneInteractionsWithPathway() throws NoValidManagerSetException, FileParsingException, InvalidIdException, UnknownOntologyException, InconsistentOntologyException {
		reader.read(testFilePath+"interaction4WithPathway.xml");
		assertEquals(1,manager.getInteractionCount());
		assertEquals(1,manager.getMoleculeCount());
		assertEquals(1,manager.getPathwayCount());
		InteractionNode interaction=new InteractionNode(InteractionNode.PREFIX+"201776");
		interaction=manager.getEqualInteractionNodeInManager(interaction);
		assertNotNull(interaction);
		
		Collection<InteractionComponentInterface> input=interaction.getIncomingInteractionComponents();
		assertEquals(1, input.size());
		MoleculeNode in=input.get(0).getMolecule();
		MoleculeNode mol=new MoleculeNode(MoleculeNode.PREFIX+"205458");
		assertSame(in,manager.getEqualMoleculeNodeInManager(mol));
		
		Collection<InteractionComponentInterface> output=interaction.getOutgoingInteractionComponents();
		assertEquals(0, output.size());
		
		Collection<PathwayNode> pathways=interaction.getPatways();
		PathwayNode pathway=pathways.get(0);
		PathwayNode path=new PathwayNode(PathwayNode.PREFIX+"200103");
		path=manager.getEqualPathwayNodeInManager(path);
		assertSame(pathway,path);
	}
	
	@Test
	public void testPositiveCondition() throws NoValidManagerSetException, FileParsingException, InvalidIdException {
		reader.read(testFilePath+"interaction3.xml");
		assertEquals(3,manager.getInteractionCount());
		assertEquals(5,manager.getMoleculeCount());
		assertEquals(0,manager.getPathwayCount());
		InteractionNode interaction=new InteractionNode(InteractionNode.PREFIX+"206568");
		interaction=manager.getEqualInteractionNodeInManager(interaction);
		assertNotNull(interaction);
		assertEquals("Plasma Cell Myeloma",interaction.getPosCondition());
	}
	*/
}
