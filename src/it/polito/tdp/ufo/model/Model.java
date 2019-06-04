package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

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
	public List<String> getRaggiungibili(String partenza){
			
			List<String> raggiungibili = new ArrayList<>();
			
			//CREO ITERATORE E LO ASSOCIO AL GRAFO      
			//GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo,source); //in ampiezza
			GraphIterator<String, DefaultEdge> it = new DepthFirstIterator<>(this.grafo,partenza); //in profondita'
			
			while(it.hasNext()) {
				raggiungibili.add(it.next());
			}
			
			//Pulisco il primo valore della lista che è lo stato stesso
			return raggiungibili.subList(1, raggiungibili.size());
		}
	
	//RICORSIONE
	private List<String> best;
		
	public List<String> ricorsione(String partenza){
		
		this.best = new ArrayList<>();
		
		//Creo soluzione parziale vuota
		List<String> partial = new ArrayList<>();
		
		//Il risultato deve partire dal nodo selezionato
		partial.add(partenza);
					
		//Start recursion (level 0)
        sub_ricorsione(partial, 0);
				
		//ritorno la miglior sequenza trovata 
		return best;
	}
	
	public void sub_ricorsione(List<String> partial, int livello) {
		
		//CONDIZIONE TERMINAZIONE (Cammino più lungo)
		if ( partial.size()>best.size() ) {
			
			this.best = new ArrayList<>(partial);
			
		}
		
		//CASO INTERMEDIO
		
		List<String> candidati = Graphs.successorListOf(grafo, partial.get(partial.size()-1));
		
		for (String s : candidati) {
			
			//Solo una volta può comparire
			if (!partial.contains(s)) {
			
				partial.add(s);
			
				sub_ricorsione(partial, livello+1);
			
				partial.remove(partial.size()-1);
			}
		}
		
		
		
	}
}
