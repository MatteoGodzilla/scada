package scada.gui.responsabili;

import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class ResponsabiliMain extends StageController {
    private String username;

    public static ResponsabiliMain newInstance(String username) {
        return GuiConstructor.createInstance("/responsabili/ResponsabileDashboard.fxml", (ResponsabiliMain instance, Stage stage)-> {
            instance.stage = stage;
            instance.username = username;
        });
    }

    /*TODO*/
    public void openImpiantoCreate() {
        return;
    }
    /*TODO*/
    public void openMacchinarioCreate() {
        return;
    }
    /*TODO*/
    public void assignImpiantoToAddetto() {
        return;
    }
    /*TODO*/
    public void filterByTipologiaInterventi() {
        return;
    }
    /*TODO*/
    public void filterByTechnicianInterventi() {
        return;
    }
    /*TODO*/
    public void showOnlyCompletedInterventi() {
        return;
    }
    /*TODO*/
    public void openInterventiImpiantocreate() {
        return;
    }

    /*TODO*/
    public void openInterventiMacchinarioCreate() {
        return;
    }
}