package de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.NodeManager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public class NodeManagerTest {

	private NodeManagerImpl manager=NodeManagerImpl.getInstance();
	
	@Before
	public void clearManager()
	{
		manager.reset();
	}
	
	@Test
	public void testAddPathway() throws InvalidArgumentException, InvalidIdException {
		
		String testId="pid_p_testID";
		PathwayNode pathway=new PathwayNode(testId);
		assertTrue(manager.addPathway(pathway));
		assertEquals(1,manager.getPathwayCount());
				
		PathwayNode pathway2=new PathwayNode(testId);
		assertFalse(manager.addPathway(pathway2));
		
		String testId2="pid_p_testID2";
		PathwayNode pathway3=new PathwayNode(testId2);
		assertTrue(manager.addPathway(pathway3));
		assertEquals(2,manager.getPathwayCount());
		
	}

	@Test
	public void testDeletePathway() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_p_testID";
		PathwayNode pathway=new PathwayNode(testId);
		assertFalse(manager.deletePathway(pathway));
		
		assertTrue(manager.addPathway(pathway));
		assertEquals(1,manager.getPathwayCount());
		
		PathwayNode pathway2=new PathwayNode(testId);
		assertTrue(manager.deletePathway(pathway2));
		
		assertEquals(0,manager.getPathwayCount());	
			
	}

	@Test
	public void testAddInteraction() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_i_testID";
		InteractionNode interaction=new InteractionNode(testId);
		assertTrue(manager.addInteraction(interaction));
		assertEquals(1,manager.getInteractionCount());
		
		InteractionNode interaction2=new InteractionNode(testId);
		assertFalse(manager.addInteraction(interaction2));
		
		String testId2="pid_i_testID2";
		InteractionNode interaction3=new InteractionNode(testId2);
		assertTrue(manager.addInteraction(interaction3));
		assertEquals(2,manager.getInteractionCount());
			
	}

	@Test
	public void testDeleteInteraction() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_i_testID";
		InteractionNode interaction=new InteractionNode(testId);
		assertFalse(manager.deleteInteraction(interaction));
		
		assertTrue(manager.addInteraction(interaction));
		assertEquals(1,manager.getInteractionCount());
		
		InteractionNode interaction2=new InteractionNode(testId);
		assertTrue(manager.deleteInteraction(interaction2));
		
		assertEquals(0,manager.getInteractionCount());
	}

	@Test
	public void testDeleteAllInteractions() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_i_testID";
		InteractionNode interaction=new InteractionNode(testId);
		assertTrue(manager.addInteraction(interaction));
		
		String testId2="pid_i_testID2";
		InteractionNode interaction3=new InteractionNode(testId2);
		assertTrue(manager.addInteraction(interaction3));
		assertEquals(2,manager.getInteractionCount());
		
		manager.deleteAllInteractions();
		
		assertEquals(0,manager.getInteractionCount());
	}

	@Test
	public void testContainsPathway() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_p_testID";
		PathwayNode pathway=new PathwayNode(testId);
		assertTrue(manager.addPathway(pathway));
		
		assertTrue(manager.containsPathway(pathway));
		PathwayNode pathway2=new PathwayNode(testId);
		assertTrue(manager.containsPathway(pathway2));
	}

	@Test
	public void testContainsInteraction() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_i_testID";
		InteractionNode interaction=new InteractionNode(testId);
		assertTrue(manager.addInteraction(interaction));
		
		assertTrue(manager.containsInteraction(interaction));
		InteractionNode interaction2=new InteractionNode(testId);
		assertTrue(manager.containsInteraction(interaction2));
	}

	@Test
	public void testGetEqualPathwayNodeInManager() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_p_testID";
		PathwayNode pathway=new PathwayNode(testId);
		assertTrue(manager.addPathway(pathway));
		
		PathwayNode pathway2=new PathwayNode(testId);
		PathwayNode returned=manager.getEqualPathwayNodeInManager(pathway2);
		assertEquals(pathway, returned);
		assertTrue(pathway==returned);
	}

	@Test
	public void testGetEqualInteractionNodeInManager() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_i_testID";
		InteractionNode interaction=new InteractionNode(testId);
		assertTrue(manager.addInteraction(interaction));
		
		InteractionNode interaction2=new InteractionNode(testId);
		InteractionNode returned=manager.getEqualInteractionNodeInManager(interaction2);
		assertEquals(interaction, returned);
		assertTrue(interaction==returned);
	}

	@Test
	public void testDeleteAllPathways() throws InvalidArgumentException, InvalidIdException {
		String testId="pid_p_testID";
		PathwayNode pathway=new PathwayNode(testId);
		manager.addPathway(pathway);				
		String testId2="pid_p_testID2";
		PathwayNode pathway3=new PathwayNode(testId2);
		manager.addPathway(pathway3);
		assertEquals(2,manager.getPathwayCount());
		
		manager.deleteAllPathways();
		assertEquals(0,manager.getPathwayCount());
		
	}


}
