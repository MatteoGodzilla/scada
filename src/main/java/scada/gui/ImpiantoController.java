package scada.gui;

import javafx.scene.control.TitledPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class ImpiantoController implements GuiController{

    public TitledPane pane;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static ImpiantoController newInstance(){
        return GuiConstructor.createInstance("/addetti/impianto-row.fxml",(ImpiantoController instance, TitledPane pane)->{
            instance.pane = pane;
        });
    }
}
