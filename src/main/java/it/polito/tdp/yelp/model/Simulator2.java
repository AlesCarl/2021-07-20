package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.model.Event2.EventType;


public class Simulator2 {
	
	//paramentri di INPUT: 
	
	private int x1; // intervistatori disponibili
    private int x2; // user da intervistare

    
    //Stato del sistema:
   	private SimpleWeightedGraph<User, DefaultWeightedEdge> graph;
   //private Map <String,Integer> numShare;  
	int contUserIntervistati;

   	
   	
  //paramentri di OUTPUT: 
	int giorniTotali=0; 
	private Map <Integer,Integer> userIntervistati;  
	
 // #key: idGiornalisti, #values: contUserIntervistati


  //coda eventi
  	private PriorityQueue<Event2> queue; 
  	
	private List <User> listVertici; 
	
      
  	
   	
   	
	public Simulator2(SimpleWeightedGraph<User, DefaultWeightedEdge> graph, int x1, int x2) {
		
		this.graph= graph;
		this.x1=x1;
		this.x2=x2;
	}
   	
	public void initialize() {
		
		this.queue= new PriorityQueue<Event2>();
		
		this.listVertici= new ArrayList<>(graph.vertexSet()); 
		
		Map <Integer,Integer> userIntervistati= new HashMap<>(); 
		contUserIntervistati= 0; 

		
		/** CREO EVENTI INIZIALI, inizializzando la CODA degli eventi **/ 
	
		for(int i=0; i<x1; i++) {
			
			User userCas= getCasuale() ; 
			
				this.queue.add(new Event2(EventType.INTERVISTA, i,userCas, 1));
				listVertici.remove(userCas);
				// " i " = intervistatore
				
				userIntervistati.put(i, 1); 
				contUserIntervistati++; 
				
		}

		
	}

	private User getCasuale() {
			 int n = (int) (Math.random() * this.listVertici.size());
			   return listVertici.get(n);
	}

	public void run() {
	
		
	      while (!this.queue.isEmpty()) {
				
				Event2 e= this.queue.poll();
				
				//condizione di uscita:
				if(contUserIntervistati==x2) {
					giorniTotali= e.getGiorno(); 
					break; 
				}
				
				// le  3 variabili dell'evento:
				int intervistatore= e.getIntervistatore();
				User u= e.getU();
				int giorno = e.getGiorno(); 
				//DURATA è importante perchè mi dice quando sta per scadere  la condivisione su un NTA
				
				
				switch ( e.getType()) {
				
				
				/** 1o CASO:  **/ 
				case INTERVISTA:
					

					double prob= Math.random(); 
					
					
					if(prob<= 0.6) {
						// intervista finita, nuovo user per domani
						
						User newUser= gradoSimilaritaMaggiore(u); 
						
						if(newUser==null) {
							// sceglie a caso tra list vertici   
							 newUser= getCasuale() ; 
						}
						this.queue.add(new Event2(EventType.INTERVISTA, intervistatore,newUser, giorno+1));
						listVertici.remove(newUser);
						contUserIntervistati++; 
						userIntervistati.put(intervistatore, this.userIntervistati.get(intervistatore)+1); 
					}
					
					
					
					
					if(prob<= 0.8) {
						// intervista fatta e ferie
						this.queue.add(new Event2(EventType.PAUSA, intervistatore,u, giorno+1));
					}
					
					
					
					if(prob<= 1) {
						// intervista da ripetere ... 
						this.queue.add(new Event2(EventType.INTERVISTA, intervistatore,u, giorno+1));
					 }
					
					
				 break;
				 
				 
				 
				 /** 2o CASO:  **/ 
				  case PAUSA:
					  
					  User newUser= gradoSimilaritaMaggiore(u); 
						
						if(newUser==null) {
							// sceglie a caso ytra list verici   
							 newUser= getCasuale() ; 
						}
						
					this.queue.add(new Event2(EventType.INTERVISTA, intervistatore,newUser, giorno+1));
					listVertici.remove(newUser);
					contUserIntervistati++; 
					userIntervistati.put(intervistatore, this.userIntervistati.get(intervistatore)+1); 
			
				}
	 }
   	
	}

	private User gradoSimilaritaMaggiore(User u) {
		
		List<User> listUsMax= new ArrayList<>(); 
		double pesoMax= 0; 
		User usMax= null; 
		
		for( DefaultWeightedEdge ee: this.graph.outgoingEdgesOf(u)) {
			
			User nu= Graphs.getOppositeVertex(this.graph, ee, u); 
			
			if(this.listVertici.contains(nu)) {
			//vuol dire che non è stato ancora usato quell'User
				
			if(this.graph.getEdgeWeight(ee) == pesoMax) {
				listUsMax.add(nu); 
			}
			
			if(this.graph.getEdgeWeight(ee)>pesoMax) {
				listUsMax.clear();
				pesoMax= this.graph.getEdgeWeight(ee); 
				listUsMax.add(Graphs.getOppositeVertex(this.graph, ee, u)); 
			}
		  }
			
		}
		// se c'è piu di un vertice con grado max
		if(listUsMax.size()>1) {
			usMax= getCasuale(listUsMax) ; 
			return usMax;
		}
		
		return listUsMax.get(0);
		
	}

	private User getCasuale(List<User> listUsMax) {
		 int n = (int) (Math.random() * listUsMax.size()) ;
		   return listUsMax.get(n);
	}

	public Map<String, Integer> getIdMapTotShare() {
		// TODO Auto-generated method stub
		return null;
	}
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
    
    
}
