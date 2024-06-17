package scada.gui;

import javafx.scene.control.Button;

public class Addetto extends GuiController{

    public Button strButton;
    public Button stpButton;
    public Button weatherButton;
    public Button reporButton;
    public Button refreshButton;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Addetto newInstance(){
        return GuiConstructor.createInstance("/addetto.fxml");
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

    public void weather() {
        //TODO: Apre una nuova finestra che mostra il meteo
    }

    public void generateReport() {
        //TODO: Genera un file csv con le statistiche degli impianti
    }

    public void refresh() {
        //TODO: Ricarica la informazioni su impianti e macchinari
    }
}
