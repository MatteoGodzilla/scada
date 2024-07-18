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
}