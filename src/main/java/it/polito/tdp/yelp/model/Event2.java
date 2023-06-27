package it.polito.tdp.yelp.model;

public class Event2  implements Comparable<Event2>{

	
	public enum EventType{
		INTERVISTA,
		PAUSA
	}
	
	EventType type; 
	int intervistatore; 
	User u; 
	int giorno;
	
	public Event2(EventType type, int intervistatore, User u, int giorno) {
		
		this.type = type;
		this.intervistatore = intervistatore;
		this.u = u;
		this.giorno = giorno;
	}
	
	

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getIntervistatore() {
		return intervistatore;
	}

	public void setIntervistatore(int intervistatore) {
		this.intervistatore = intervistatore;
	}

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}


	@Override
	public int compareTo(Event2 o) {
		return this.giorno-o.getGiorno();
	}
	
	
	
	
	
	
	
}
