package scada.gui.fxml;

import java.io.IOException;
import java.util.function.BiConsumer;

import javafx.fxml.FXMLLoader;

public class GuiConstructor {
    public static <T extends GuiController, U> T createInstance(String path, BiConsumer<T,U> callback){
        var res = GuiConstructor.class.getResource(path);
        FXMLLoader loader = new FXMLLoader(res);
        try {
            //the following lines must be in this specific order
            //1: loader.load()
            U p = loader.load();
            //2: access controller
            T controller = loader.getController();
            callback.accept(controller,p);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
