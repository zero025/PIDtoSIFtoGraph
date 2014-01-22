package de.bioquant.cytoscape.pidfileconverter.Model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;

public class PathwayNodeTest {

	@Test
	public void testPathwayNode() throws InvalidIdException {
		String pid="pid_p_test";
		PathwayNode p=new PathwayNode(pid);
		assertEquals(pid,p.getFullPid());
	}
	
	@Test(expected=InvalidIdException.class)
	public void testPathwayNodeInvalidId() throws InvalidIdException {
		String pid="test";
		new PathwayNode(pid);
		
	}
	
	@Test
	public void testPathwayNodeWithSource() throws InvalidIdException {
		String url="http://myUrl.de";
		String pid="pid_p_test";
		PathwayNode p=new PathwayNode(url+"#"+pid);
		assertEquals(pid,p.getFullPid());
	}

}
