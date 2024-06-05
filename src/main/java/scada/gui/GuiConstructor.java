package scada.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GuiConstructor {
    public static <T extends GuiController> T createInstance(String path){
        var res = GuiConstructor.class.getResource(path);
        FXMLLoader loader = new FXMLLoader(res);
        try {
            //the following lines must be in this specific order
            //1: loader.load()
            Parent p = loader.load();
            //2: access controller
            T controller = loader.getController();
            controller.setScene(new Scene(p));
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
