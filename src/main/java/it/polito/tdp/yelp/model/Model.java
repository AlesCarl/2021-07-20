package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		 

	    	Graphs.addAllVertices(this.graph, allUtenti);
	 		System.out.println("vertici GRAFO: " +this.graph.vertexSet().size());
	 		
	 		/*
	 		 
	arco se almeno una volta, essi abbiano pubblicato una recensione 
	per uno stesso locale commerciale nell’anno a selezionato. 
	
	Il peso dell’arco, sempre positivo, è dato dal numero di 
	volte che questo accade, e rappresenta il grado di similarità 
	tra i due utenti coinvolti. 
	 		   
	 		 */
	 		
	 		
	 // dato un user prendi elenco di tutte le review nell' ANNO
	 		
	 		for(User s1: allUtenti) {
	 			for(User s2: allUtenti) {
	 				
	 				if(!s1.equals(s2)) { 
	 		
	 					
	 				double peso= gradoSimilarita(s1,s2,anno); 
	 				if(peso>0) {
						Graphs.addEdgeWithVertices(this.graph, s1, s2, peso);
						//this.listPesi.add(peso); //abbastanza inutile, usa i GRAFI
	 				}
	 			}
	 				
	 		  }
	 		}
			System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());

		 
		 
	 }

	 // prendo tutte le review di user in un anno
	 
	private double gradoSimilarita(User s1, User s2,int  anno) {
		
		List<Business> list1= dao.getAllReviewsUser(anno, s1);
		List<Business> list2= dao.getAllReviewsUser(anno, s2);
		
		double cont= 0; 
		
		for(Business r1: list1) {
			for(Business r2: list2) {
				if(r1.getBusinessId().compareTo(r2.getBusinessId())==0)
				   cont++; 
			}
			
		}

		return cont; // grado di similarità
	}

	
	
	
	
	public int getVertici() {
		return this.graph.vertexSet().size();
	}
	public int getEdge() {
		return this.graph.edgeSet().size();
	}

	public List<User> getVerticiCmb() {
		Collections.sort(allUtenti);
		return this.allUtenti;
	}

	
	
	//stampare, l’utente collegato più simile ad u, ovvero quello 
	// con grado di similarità maggiore. In caso ci sia più di un utente che 
	// abbia lo stesso grado di similarità con u, stamparli tutti.
	
	public List<User> getGradoSimilarita(User u) {
		
		
		List<User> uMax= new ArrayList<>(); 
		double pesoMax= 0; 
		
		for( DefaultWeightedEdge ee: this.graph.outgoingEdgesOf(u)) {
			
			if(this.graph.getEdgeWeight(ee)== pesoMax) {
				uMax.add(Graphs.getOppositeVertex(this.graph, ee, u)); 

			}
			
			if(this.graph.getEdgeWeight(ee)>pesoMax) {
				uMax.clear();
				pesoMax= this.graph.getEdgeWeight(ee); 
				uMax.add(Graphs.getOppositeVertex(this.graph, ee, u)); 
				
			}
		}
		
		
		return uMax;
	}
		
	
	
	

/***************  COLLEGA SIMULAZIONE E MODEL   **************/ 
public  void simula(int x1, int x2 ) 
{
	Simulator2 sim = new Simulator2(this.graph, x1,x2);
	
	sim.initialize();
	sim.run();
	
//	return sim.getIdMapTotShare(); 
	//this.nPassiSimulatore= sim.getnPassi();
	//return sim.getStanziali() ;
}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
