package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class InterventoMacchinarioCreateController extends StageController {
    public static InterventoMacchinarioCreateController newInstance(){
        return GuiConstructor.createInstance("/responsabili/BiogasCreate.fxml",(InterventoMacchinarioCreateController instance, Stage stage) ->{
            instance.stage = stage;
        });
    }
    /*TODO*/
    public void showMacchinarioInfo() {
        return;
    }
    /*TODO*/
    public void createIntervento() {
        return;
    }
}
