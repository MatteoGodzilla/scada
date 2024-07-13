package scada.gui.addetti;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class ImpiantoController implements GuiController{

    public TitledPane impiantoPane;
    public VBox macchinariBox;
    private int codImpianto;
    private String provincia;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static ImpiantoController newInstance(int codImpianto, String provincia){
        return GuiConstructor.createInstance("/addetti/impianto-row.fxml",(ImpiantoController instance, TitledPane pane)->{
            instance.impiantoPane = pane;
            instance.codImpianto = codImpianto;
            instance.provincia = provincia;
        });
    }

    public void setTitle(String name) {
        impiantoPane.setText(name);
    }

    public int getCodImpianto() {
        return codImpianto;
    }

    public String getProvincia() {
        return provincia;
    }

    public String nomeImpianto() {
        return "Impianto" + getCodImpianto() + " (" + getProvincia() + ")";
    }
}
