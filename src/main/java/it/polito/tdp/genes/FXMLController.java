/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jheaps.monotone.DoubleRadixAddressableHeap;

import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnContaArchi"
    private Button btnContaArchi; // Value injected by FXMLLoader

    @FXML // fx:id="btnRicerca"
    private Button btnRicerca; // Value injected by FXMLLoader

    @FXML // fx:id="txtSoglia"
    private TextField txtSoglia; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doContaArchi(ActionEvent event) {
    	Double soglia;
    	List<Double>minMax=this.model.pesoMinimoEMassimo();
    	try {
			soglia=Double.parseDouble(txtSoglia.getText());
			if(soglia<minMax.get(0)||soglia>minMax.get(1)) {
				txtResult.setText("Inserire un numero decimale compreso tra il peso minimo ed il peso massimo");
				return;
			}
			List<Integer>conta=this.model.contaArchi(soglia);
			txtResult.appendText("Soglia: "+soglia+"-----> Maggiori: "+conta.get(1)+", minori:" +conta.get(0)+"\n");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			txtResult.setText("Inserire un numero decimale");
			return;
		}
    }

    @FXML
    void doRicerca(ActionEvent event) {
    	txtResult.clear();
    	
    	Double soglia;
    	List<Double>minMax=this.model.pesoMinimoEMassimo();
    	try {
			soglia=Double.parseDouble(txtSoglia.getText());
			if(soglia<minMax.get(0)||soglia>minMax.get(1)) {
				txtResult.setText("Inserire un numero decimale compreso tra il peso minimo ed il peso massimo");
				return;
			}
			List<Integer>result=this.model.cerca(soglia);
			txtResult.setText("Sequenza di cromosomi di lunghezza massima: \n");
			for(Integer i:result) {
				txtResult.appendText(i+"\n");
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			txtResult.setText("Inserire un numero decimale");
			return;
		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnContaArchi != null : "fx:id=\"btnContaArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSoglia != null : "fx:id=\"txtSoglia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.appendText("#VERTICI: "+this.model.nVertici()+"\n");
    	txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    	List<Double>output=this.model.pesoMinimoEMassimo();
    	txtResult.appendText("Peso minimo: "+output.get(0)+" , Peso massimo: "+output.get(1)+"\n");
    	
	}
}
