package scada.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class MacchinarioController implements GuiController{

    public Button strButton;
    public Button stpButton;
    public AnchorPane pane;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static MacchinarioController newInstance(){
        return GuiConstructor.createInstance("/addetti/macchinario-row.fxml",(MacchinarioController instance, AnchorPane pane)->{
            instance.pane = pane;
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
}
