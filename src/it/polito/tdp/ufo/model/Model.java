package it.polito.tdp.ufo.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	Graph<String,DefaultEdge> grafo;
	SightingsDAO dao;
	
	public Model() {
		dao = new SightingsDAO();
	}
	
	public List<Year> getAllYear(){
		return dao.getAllYear();
	}
	
	public void creaGrafo(int year) {
		
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//Aggiungo vertici
		Graphs.addAllVertices(grafo, dao.getStati(year));
		
		//Aggiungo archi
		for (String s1 : this.grafo.vertexSet()) {
			for (String s2 : this.grafo.vertexSet()){
				if (!s1.equals(s2)) {
					if (this.dao.esisteArco(s1,s2,year)) {
						this.grafo.addEdge(s1, s2);
					}
				}
			}
		}
	

		System.out.println("GRAFO CREATO");
		System.out.println("#VERTICI: "+grafo.vertexSet().size());
		System.out.println("#ARCHI: "+grafo.edgeSet().size()+"\n");
	}

	public Graph<String, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	//Ottengo successori e predecessori di quello stato
	
	public List<String> getSuccessori(String stato){
		return Graphs.successorListOf(grafo, stato);
	}
	
	public List<String> getPredecessori(String stato){
		return Graphs.predecessorListOf(grafo, stato);
	}
	
	//Visita
	
	public List<String> getRaggiungibili(){
		
		List<String> raggiungibili = new LinkedList<>();
		
		DepthFirstIterator<String, DefaultEdge> dp = new DepthFirstIterator<>(this.grafo);
		
		dp.
		
		return raggiungibili;
	}

}
