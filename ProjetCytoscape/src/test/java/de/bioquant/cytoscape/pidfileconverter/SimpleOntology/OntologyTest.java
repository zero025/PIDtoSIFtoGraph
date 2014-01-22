package de.bioquant.cytoscape.pidfileconverter.SimpleOntology;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;

public class OntologyTest {

	private Ontology onto;
	
	@Before
	public void initOnto() throws InvalidArgumentException
	{
		onto=new Ontology(1,"test");
		createSimpleOntology();
	}
	
	private void createSimpleOntology() throws InvalidArgumentException
	{
		OntologyElement element=new OntologyElement(4, 1, "child1");
		onto.addElement(element);
		element=new OntologyElement(1, 1, "parent");
		onto.addElement(element);
		element=new OntologyElement(3, 4, "childchild");
		onto.addElement(element);		
	}
	
	@Test
	public void testAddElement(){
		
		assertEquals(3, onto.getElementCount());
	}

	@Test
	public void testGetElement(){
		OntologyElement element=onto.getElement(4);
		assertNotNull(element);
		assertEquals("child1",element.getName());
		element=onto.getElement(1);
		assertNotNull(element);
		assertEquals("parent",element.getName());
		element=onto.getElement(3);
		assertNotNull(element);
		assertEquals("childchild",element.getName());
		
	}
	
	@Test
	public void testGetElementNoSuchElement(){
		OntologyElement element=onto.getElement(100);
		assertNull(element);
	}

	@Test
	public void testIsParentOf() throws InconsistentOntologyException {
		assertTrue(onto.isParentOf(1, 3));
		assertTrue(onto.isParentOf(1, 4));
	}
	
	@Test
	public void testGetElementByNameNoSuchElement(){
		assertNull(onto.getElementByName("fake"));
	}
	
	@Test
	public void testGetElementByNameWithSuchElement(){
		OntologyElement el=onto.getElementByName("child1");
		assertNotNull(el);
		assertEquals(4,el.getId());
	}

}
