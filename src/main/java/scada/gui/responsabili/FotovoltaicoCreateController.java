package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class FotovoltaicoCreateController extends StageController {
     public static FotovoltaicoCreateController newInstance(){
        return GuiConstructor.createInstance("/responsabili/FotovoltaicoCreate.fxml",(FotovoltaicoCreateController instance, Stage stage) ->{
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
    public void createPannello() {
        return;
    }
    /*TODO*/
    public void showInfoGaranzia() {
        return;
    }
}
