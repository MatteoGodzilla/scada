package scada.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class Addetto extends StageController{

    public Button strButton;
    public Button stpButton;
    public Button weatherButton;
    public Button reportButton;
    public Button refreshButton;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Addetto newInstance(){
        return GuiConstructor.createInstance("/addetto.fxml",(Addetto instance, Stage stage)->{
            instance.stage = stage;
        });
    }

    /**
     * Se clicco START, disabilito il pulsante di START e attivo quello di STOP
     */
    public void startMacchinario() {
        strButton.setDisable(true);
        stpButton.setDisable(false);
    }

    /**
     * Se clicco STOP, disabilito il pulsante di STOP e attivo quello di START
     */
    public void stopMacchinario() {
        stpButton.setDisable(true);
        strButton.setDisable(false);
    }

    public void weather(String impianto) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meteo.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        //TODO: La nuova finestra mostra il meteo dell'impianto selezionato
        //NON HO IDEA DI COME CAPIRE CHE IMPIANTO SELEZIONO (SEMPRE CHE SIA UNA COSA POSSIBILE DA FARE)

        /*
        Teoricamente dovrebbe bastare una cosa del genere

        Meteo m = Meteo.newInstance();
        m.getStage().show();

        Poi avendo accesso al controller puoi passargli i parametri che serve

        */
    }

    public void generateReport(String impianto) {
        //TODO: Genera un file csv con le statistiche degli impianti
    }

    public void refresh() {
        //TODO: Ricarica la informazioni su impianti e macchinari
    }
}
