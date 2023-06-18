/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    
    @FXML
    void doCreaGrafo(ActionEvent event) {

    	int anno= this.cmbAnno.getValue(); 
    	int nRecensioni= Integer.parseInt(this.txtN.getText());
    	
    	model.creaGrafo(nRecensioni, anno);
    	
    	this.txtResult.appendText("VERTICI E ARCHI: "+model.getVertici()+" -- "+model.getNumEdges());
    	
    	// try catch ... 
    	
    	this.cmbUtente.getItems().addAll(model.getVerticiCmb()); 
    	
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	
    	User u= this.cmbUtente.getValue(); 

    	this.txtResult.appendText("\n\ngrado massimo: "+model.getGradoSimilarita(u));
    	
    	for(User uu: model.getListSimilarita()) {
        	this.txtResult.appendText("\n"+uu);

    	}
    	
    	
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	
    	int intervistarori= Integer.parseInt(this.txtX1.getText());
    	int userIntervistare= Integer.parseInt(this.txtX1.getText()); 
    	int cont=0; 
    	
    	this.txtResult.appendText("\nGiorni per concludere analisi mercato:"+model.getGiorniTrascorsiSim());
    	
    	this.txtResult.appendText("\nUtenti intervistati da ciascun intervistatore");

    	
    	for(int i: model.simula(intervistarori, userIntervistare).values()) {
    		this.txtResult.appendText("\nIntervistatore "+cont++ +"ha intervistato questo numero di utenti: "+i);
    	}
    	
    	

    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.setCmbx();
    	
    }
    public void setCmbx() {
    	
    	for(int i=2005; i<2014; i++) {
      	     this.cmbAnno.getItems().add(i); 
      	 }	
       	
    }
}
