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
    /*TODO*/
    public void filterByAzienda() {
        return;
    }
    /*TODO*/
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
