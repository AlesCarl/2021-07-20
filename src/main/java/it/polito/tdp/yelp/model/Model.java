package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	YelpDao dao; 
    private SimpleWeightedGraph<User, DefaultWeightedEdge> graph;  // SEMPLICE, PESATO, NON ORIENTATO
    private List<User> allUtenti ; 
   // private List<Arco> listArchi ; 
    List<User> listGradoSimilarita;
    int giorniTrascorsiSim ;
    

	
	public Model() {
		
		this.dao= new YelpDao(); 
    	this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    	this.allUtenti= new ArrayList<>();
    	listGradoSimilarita= new ArrayList<>(); 
		
	}
	
	private void loadAllNodes(int nRecensioni) {
			
		allUtenti= dao.getAllUsersRecensioni(nRecensioni);
 				
 		}
	
	 public void creaGrafo( int nRecensioni, int anno) {
		 
		 
		 loadAllNodes(nRecensioni); 
	 		//System.out.println("size: " +this.allNACode.size());

 		 /** VERTICI */
	    	Graphs.addAllVertices(this.graph, allUtenti);
	 		System.out.println("NUMERO vertici GRAFO: " +this.graph.vertexSet().size());
	 		System.out.println("vertici GRAFO: " +this.graph.vertexSet());
	 		
	 		
	 		/** ARCHI */
	 		
	 		/*
	 		 almeno una volta, essi abbiano pubblicato una recensione per uno stesso locale 
	 		 commerciale nell’anno a selezionato	 		 
	 		*/
	 		
	 		for(User u1: this.allUtenti) {
 	 			for(User u2: this.allUtenti) {
 	 				
 	 				if(u1.getUserId().compareTo(u2.getUserId())!=0) { 
 	 					double peso= calcoloPeso(u1,u2,anno); 
 	 			 
 	 			 				if( peso>0) {
 	 			 					
 	 		 						Graphs.addEdgeWithVertices(this.graph, u1, u2, peso);
 	 		 						
 	 		        }
 	 			}
 	 		}
    	 }
	 		System.out.println("NUMERO ARCHI GRAFO: " +this.graph.edgeSet().size());
	
	 }
	 
	
	 
	 public double getGradoSimilarita(User u) {
		 
		 double gradoMax=0; 
		 listGradoSimilarita.clear();
		 
		 for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(u) ) {
				
				User uu= Graphs.getOppositeVertex(this.graph, e, u);
				
				if( this.graph.getEdgeWeight(e) ==gradoMax ) {
					listGradoSimilarita.add(uu);
				}
				
				if( this.graph.getEdgeWeight(e) > gradoMax ) {
					
					listGradoSimilarita.clear();
					gradoMax= this.graph.getEdgeWeight(e); 
					listGradoSimilarita.add(uu);
					
				}
		 }
		 
		 return gradoMax; 
	 }
	 

	 public List<User> getListSimilarita() {
		return listGradoSimilarita;
	 }

	 
	 

	private double calcoloPeso(User u1, User u2, int anno) {		
// calcolo le recensioni nell'anno  di  ciascun  utente 
		
		
		List<String> listRec1= dao.getRecensioniAnno(anno, u1);
		List<String> listRec2= dao.getRecensioniAnno(anno,u2);

		double contatore=0.0; 
		
		for(String s1:listRec1 ) {
			for(String s2:listRec2 ) {
				if(s1.compareTo(s2)==0) {
					contatore++; 
				}	
			}
		}
		
		return contatore;
	}

	
	
	public int getVertici() {
		return graph.vertexSet().size();
	}
    
	public int getNumEdges() {
	     return graph.edgeSet().size(); 
	 }
	
	public List<User> getVerticiCmb() {
		return this.allUtenti; 
	}
	
	
	


/***************  COLLEGA SIMULAZIONE E MODEL   **************/ 
public  Map<Integer, Integer>  simula(int intervistarori, int userIntervistare ) 
{
		
	Simulator sim = new Simulator(this.graph, intervistarori,userIntervistare);
	
	sim.initialize();
	sim.run();
	
	giorniTrascorsiSim= sim.getGiorniTrascorsiSim();
	
	return sim.getSimulationMap(); 
	
}

	
	public int getGiorniTrascorsiSim() {
		return giorniTrascorsiSim; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
