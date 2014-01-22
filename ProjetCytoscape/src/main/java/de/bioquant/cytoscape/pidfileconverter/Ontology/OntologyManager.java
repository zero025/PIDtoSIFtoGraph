package de.bioquant.cytoscape.pidfileconverter.Ontology;

import java.util.TreeMap;

import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public final class OntologyManager {
	
	private static OntologyManager instance=null;
	private TreeMap<String,Ontology> ontos= new TreeMap<String, Ontology>();
	
	private OntologyManager(){}
	
	public static OntologyManager getInstance()
	{
		if (instance==null)
			instance=new OntologyManager();
		return instance;
	}
	
	public boolean containsSuchOntology(String name)
	{
		return ontos.containsKey(name);
	}
	
	public Ontology getOntology(String name)
	{
		return ontos.get(name);
	}
	
	public boolean addOntology(Ontology onto) throws UnknownOntologyException
	{
		if (null==onto)
			throw new UnknownOntologyException("Ontology is null - could not be added!");
		Ontology old=ontos.put(onto.getName(), onto);
		if (null==old)
			return true;
		else
			return false;
	}
	
	public void deleteAllOntologies()
	{
		this.ontos.clear();
	}
	
	public int getOntologieCount()
	{
		return this.ontos.size();
	}

}
