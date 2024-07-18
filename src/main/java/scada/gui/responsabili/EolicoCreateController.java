package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class EolicoCreateController extends StageController {
     public static EolicoCreateController newInstance(){
        return GuiConstructor.createInstance("/responsabili/EolicoCreate.fxml",(EolicoCreateController instance, Stage stage) ->{
            instance.stage = stage;
        });
    }
    /*TODO PRIMA SI SCEGLIE AZIENDA, QUESTO SBLOCCA MODELLO*/
    public void filterByAzienda() {
        return;
    }
    /*TODO SCELTO ANCHE IL MODELLO SI PUO' SCEGLIERE LA GARANZIA*/
    public void filterByModello() {
        return;
    }
    /*TODO*/
    public void createTurbina() {
        return;
    }
    /*TODO*/
    public void showInfoGaranzia() {
        return;
    }

}
