package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class BiogasCreateController extends StageController {
    public static BiogasCreateController newInstance(int codiceImpianto, String provinciaImpianto){
        return GuiConstructor.createInstance("/responsabili/BiogasCreate.fxml",(BiogasCreateController instance, Stage stage) ->{
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
    public void createDigestore() {
        return;
    }
    /*TODO*/
    public void showInfoGaranzia() {
        return;
    }
}
