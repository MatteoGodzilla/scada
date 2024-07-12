package scada.gui.addetti;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class Meteo extends StageController {

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Meteo newInstance(){
        return GuiConstructor.createInstance("/addetti/meteo.fxml",(Meteo instance, Stage stage)->{
            instance.stage = stage;
        });
    }
}
