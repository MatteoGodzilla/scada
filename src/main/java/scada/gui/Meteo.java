package scada.gui;

import javafx.scene.control.Button;

public class Meteo extends GuiController {

    public Button closeButton;
    private GuiController controller;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Meteo newInstance(){
        return GuiConstructor.createInstance("/meteo.fxml");
    }

    /**
     * Metodo per chiudere la finestra del meteo
     */
    public void close() {
        this.controller.getStage().close();
    }
}
