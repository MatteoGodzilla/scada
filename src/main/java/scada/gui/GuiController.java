package scada.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class GuiController {
    private Scene scene;
    private Stage stage;

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    //THIS METHOD SHOULD ONLY BE CALLED BY GUICONSTRUCTOR WHEN INITIALIZING THE INSTANCE
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
