package scada.gui;

import javafx.scene.Scene;

public abstract class GuiController {
    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    //THIS METHOD SHOULD ONLY BE CALLED BY GUICONSTRUCTOR WHEN INITIALIZING THE INSTANCE
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
