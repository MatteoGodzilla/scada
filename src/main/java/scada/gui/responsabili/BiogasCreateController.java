package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class BiogasCreateController extends StageController {
    public static BiogasCreateController newInstance(){
        return GuiConstructor.createInstance("/responsabili/BiogasCreate.fxml",(BiogasCreateController instance, Stage stage) ->{
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
    public void createDigestore() {
        return;
    }
    /*TODO*/
    public void showInfoGaranzia() {
        return;
    }
}
