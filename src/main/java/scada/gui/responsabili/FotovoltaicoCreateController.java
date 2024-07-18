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
    /*TODO PRIMA SI SCEGLIE AZIENDA, QUESTO SBLOCCA MODELLO*/
    public void filterByAzienda() {
        return;
    }
    /*TODO SCELTO ANCHE IL MODELLO SI PUO' SCEGLIERE LA GARANZIA*/
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
