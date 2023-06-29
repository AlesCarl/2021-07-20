package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.model.Event.EventType;



public class Simulator {

	
	//paramentri di INPUT: 
    private int intervistatori  ; 
    private int userDaIntervistare;
    List<User> listDaIntervistare; 
	
	
   //Stato del sistema:
  	private SimpleWeightedGraph<User, DefaultWeightedEdge> graph;
   //private Map <String,Integer> numShare;  //tiene conto degli share in real time

  	int giorniTrascorsi; 

	
  	
    //paramentri di OUTPUT:
  	 Map <Integer,Integer>mapIntervistati=  new HashMap<Integer,Integer>();
 	

	//coda eventi
	private PriorityQueue<Event> queue; 
    
		 
	
	
	public Simulator(SimpleWeightedGraph<User, DefaultWeightedEdge> graph, int intervistatori, int userDaIntervistare) {
		
		this.graph= graph;
		this.intervistatori=intervistatori;
		this.userDaIntervistare=userDaIntervistare; 
		
	}
	

	public void initialize() {
		this.queue= new PriorityQueue<Event>();
		
		int cont=0; 
		giorniTrascorsi=0;
		listDaIntervistare= new ArrayList<>(); 
		
		
		for(User u: this.graph.vertexSet()) {
			if(cont <= userDaIntervistare) {
				listDaIntervistare.add(u);
				cont++; 
			}
		}

		//Assegno a ciascun intervistatore un utente: 
		
		for(int t=0; t<intervistatori; t++) {
			
			User u = userCasuale(); 
			                            // t: è l'intervistatore
			this.queue.add(new Event(1,EventType.INTERVISTA,u, t)) ; 
			listDaIntervistare.remove(u);
			mapIntervistati.put(t, 1);
			giorniTrascorsi++;
			
		}
	}


	public void run() {
        
		 while (!this.queue.isEmpty()) {
			
			Event e= this.queue.poll();
			
			
			if(this.listDaIntervistare.size()<=0)
				break; 
			
			User u1= e.getU();
			int t= e.getGiornalista();  // giornalista
			int giorno= e.getGiorno(); 
			
			
			switch ( e.getType()) {

			
			/** 1o CASO:  **/ 
			case INTERVISTA:
				
				double prob = Math.random();
				
				
				// new intervista domani
				if(prob<0.6) {
					
					 User newU = sceltaUser(u1); 
					    if(newU==null)
					    	newU= this.userCasuale(); 
				
				this.queue.add(new Event(giorno+1, EventType.INTERVISTA,newU, t)) ; 
				listDaIntervistare.remove(newU);
				mapIntervistati.put( t, mapIntervistati.get(t)+1 ); 
				giorniTrascorsi++;
				}
				
				
				// domani vacanza
				if(prob>=0.6 && prob<0.8) {
				this.queue.add(new Event(giorno+1, EventType.PAUSA,u1, t)) ; 
				//mapIntervistati.put( t, mapIntervistati.get(t)+1 ); 
				giorniTrascorsi++;
				}
				
				// domani CI RIPROVA con lo stesso user
				if(prob>=0.8 && prob<1) {
					this.queue.add(new Event(giorno+1,EventType.INTERVISTA,u1, t)) ; 
					giorniTrascorsi++;
					
				}
				
				
				
			/** 2o CASO:  **/ 
			 case PAUSA:
				 
				    User newU = sceltaUser(u1); 
				    if(newU==null)
				    	newU= this.userCasuale(); 
					
					this.queue.add(new Event(giorno+1, EventType.INTERVISTA,newU, t)) ;

					listDaIntervistare.remove(newU);	
					mapIntervistati.put( t, mapIntervistati.get(t)+1 ); 
					giorniTrascorsi++;
			}
		 }
	}
	

	public Map<Integer,Integer>  getSimulationMap() {
		System.out.println("giorni per concludere analisi  = " + this.giorniTrascorsi);
		return mapIntervistati; 
	}
	public int getGiorniTrascorsiSim() { 
		return giorniTrascorsi;
	}
	
	
	
	private User sceltaUser(User u1) {
		
		/** 
		    NON PUOI USARE LO STESSO METODO DEL MODEL,
		    prima di calcolare il grado MAX, devo togliere quelli già scelti 
		 */ 
		
	//	List<User> listGradoSimiliarati= new ArrayList<>();  // qui metto quelli con il grado più alto
	//  double gradoMax=0; 
		
	/*	 
	for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(u1) ) {
				
				User uu= Graphs.getOppositeVertex(this.graph, e, u1);
				
				if( this.graph.getEdgeWeight(e) ==gradoMax ) {
					listGradoSimiliarati.add(uu);
				}
				if( this.graph.getEdgeWeight(e) > gradoMax ) {
					listGradoSimiliarati.clear();
					gradoMax= this.graph.getEdgeWeight(e); 
					listGradoSimiliarati.add(uu);
				}
		 } */ 
		 
		List<User> vicini = Graphs.neighborListOf(this.graph, u1);
		
		
		//tolgo da vicini tutti gli utenti già usati:
		for(User uu: vicini) {
			if(!this.listDaIntervistare.contains(uu)) 
				vicini.remove(uu); 
		}
		
		if(vicini.size()==0) {
			// vertice isolato
			// oppure tutti adiacenti già intervistati
			return null ;
			// se null poi andrai a sceglierlo tra tutto il grafo, e non tra i vicini.
		} 
		
		double gradoMax=0;
		
		//vicini è la lista depurata
		for(User v: vicini) {  //senza usare " for(DefaultWeightedEdge e: ...)", non lavori con ARCHI ma solo con i NODI 
			double peso = this.graph.getEdgeWeight(this.graph.getEdge(u1, v)); 
		
			if(peso > gradoMax)
				gradoMax = peso ;
		}
		
		List<User> migliori = new ArrayList<>();
		for(User v: vicini) {
			double peso = this.graph.getEdgeWeight(this.graph.getEdge(u1, v)); 
			
			if(peso == gradoMax) {
				migliori.add(v) ;
			}
		}
		
		

	if(migliori.size()==0) 
		 return userCasuale();
	
	if(migliori.size()==1) 
		return migliori.get(0);
	
	if(migliori.size()>1) 
		return userCasualeList(migliori);
	
	
	return null;
	}
	 

	private User userCasuale() {
		if(listDaIntervistare.size()==0)
			return null; 
		int n = (int)(Math.random()*this.listDaIntervistare.size());
		return listDaIntervistare.get(n) ;
	}
	
	
	private User userCasualeList(List<User> list) {
		int n = (int)(Math.random()*list.size());
		return list.get(n) ;
	}


}
