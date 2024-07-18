package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class InterventoImpiantoCreateController extends StageController {
    public static InterventoImpiantoCreateController newInstance(){
        return GuiConstructor.createInstance("/responsabili/BiogasCreate.fxml",(InterventoImpiantoCreateController instance, Stage stage) ->{
            instance.stage = stage;
        });
    }
    /*TODO*/
    public void showImpiantoInfo() {
        return;
    }

    /*TODO*/
    public void createIntervento() {
        return;
    }
}
