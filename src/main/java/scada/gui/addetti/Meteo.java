package scada.gui.addetti;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class Meteo extends StageController {

    public Label labelImpianto;
    public TextField txtVento;
    public TextField txtUV;
    public Label labelTime;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Meteo newInstance(String nomeImpianto){
        return GuiConstructor.createInstance("/addetti/meteo.fxml",(Meteo instance, Stage stage)->{
            instance.stage = stage;
            instance.labelImpianto.setText(nomeImpianto);
        });
    }
}
