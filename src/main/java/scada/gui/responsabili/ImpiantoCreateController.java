package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class ImpiantoCreateController extends StageController {
    public Runnable closingCallback;

    public static ImpiantoCreateController newInstance(String regione){
        return GuiConstructor.createInstance("/responsabili/ImpiantoCreate.fxml",(ImpiantoCreateController instance, Stage stage) ->{
            instance.stage = stage;
        });
    }

    /*TODO*/
    public void createImpianto() {
        return;
    }
}
