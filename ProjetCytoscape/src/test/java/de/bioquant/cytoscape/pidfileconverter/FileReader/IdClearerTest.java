package de.bioquant.cytoscape.pidfileconverter.FileReader;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidPathwayIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.IdClearer;

public class IdClearerTest {

	@Test
	public void testGetSource() {
		String myUrl="http:://mytestUrl.de";
		String pid=myUrl+"#aPid";
		assertEquals(myUrl,IdClearer.getSource(pid));
	}
	
	@Test
	public void testGetSourceNoSource() {
		String myUrl="mytestUrl.de";
		String pid=myUrl+"#aPid";
		assertNull(IdClearer.getSource(pid));
	}



	@Test
	public void testClearUpPidI() throws InvalidInteractionIdException {
		String myUrl="http:://mytestUrl.de";
		String myPid="test";
		String pid=myUrl+"#pid_i_"+myPid;
		assertEquals(myPid,IdClearer.clearUpPidI(pid));
	}
	
	@Test
	public void testClearUpPidINoUrl() throws InvalidInteractionIdException{
		String myPid="test";
		String pid="pid_i_"+myPid;
		assertEquals(myPid,IdClearer.clearUpPidI(pid));
	}
	
	@Test(expected=InvalidIdException.class)
	public void testClearUpPidINoUrlInvalidPid() throws InvalidInteractionIdException {
		String myPid="test";
		String pid=""+myPid;
		IdClearer.clearUpPidI(pid);
	}
	
	@Test(expected=InvalidIdException.class)
	public void testClearUpPidIWithUrlInvalidPid() throws InvalidInteractionIdException {
		String myUrl="http:://mytestUrl.de";
		String myPid="test";
		String pid=myUrl+"#pid_"+myPid;
		IdClearer.clearUpPidI(pid);
	}

	@Test
	public void testClearUpPidP() throws InvalidInteractionIdException, InvalidPathwayIdException {
		String myUrl="http:://mytestUrl.de";
		String myPid="test";
		String pid=myUrl+"#pid_p_"+myPid;
		assertEquals(myPid,IdClearer.clearUpPidP(pid));
	}
	
	@Test
	public void testClearUpPidPNoUrl() throws InvalidInteractionIdException, InvalidPathwayIdException {
		String myPid="test";
		String pid="pid_p_"+myPid;
		assertEquals(myPid,IdClearer.clearUpPidP(pid));
	}
	
	@Test(expected=InvalidIdException.class)
	public void testClearUpPidPNoUrlInvalidPid() throws InvalidInteractionIdException, InvalidPathwayIdException{
		String myPid="test";
		String pid=""+myPid;
		IdClearer.clearUpPidP(pid);
	}
	
	@Test(expected=InvalidIdException.class)
	public void testClearUpPidPWithUrlInvalidPid() throws InvalidInteractionIdException, InvalidPathwayIdException{
		String myUrl="http:://mytestUrl.de";
		String myPid="test";
		String pid=myUrl+"#pid_"+myPid;
		IdClearer.clearUpPidP(pid);
	}

}
