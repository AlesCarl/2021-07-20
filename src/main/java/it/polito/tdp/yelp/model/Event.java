package it.polito.tdp.yelp.model;


public class Event implements Comparable<Event> {
	
	public enum EventType{
		INTERVISTA,
		PAUSA
	}

	private EventType type ;
	
	/** giorno: l'ho messo giusto per*/
	private int giorno;   //--->  mi serve per tenere il conto dei giorni, ma è abbastanza inutile
	private User u; 
	private int giornalista; 
	
	
	public Event(int giorno, EventType type, User u, int giornalista) {
		
		this.giorno=giorno; 
		this.type = type;
		this.u = u;
		this.giornalista = giornalista;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno-o.giorno;
	}

	



	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public EventType getType() {
		return type;
	}






	public void setType(EventType type) {
		this.type = type;
	}






	public User getU() {
		return u;
	}






	public void setU(User u) {
		this.u = u;
	}






	public int getGiornalista() {
		return giornalista;
	}


	public void setGiornalista(int giornalista) {
		this.giornalista = giornalista;
	}






	
}
