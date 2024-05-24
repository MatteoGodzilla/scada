package scada.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Test2 {
    private Scene scene;

    //this should be moved to another class, since it's basically the same in Test and Test2
	public static Test2 newInstance(){
        var res = Test2.class.getResource("/test2.fxml");
        FXMLLoader loader = new FXMLLoader(res);
        try {
            //the following lines must be in this specific order
            //1: loader.load()
            Parent p = loader.load();
            //2: access controller
            Test2 instance = loader.getController();
            //3: set up controller class
            instance.scene = new Scene(p);
            return instance;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void switchToMain(){

    }

    public Scene getScene() {
		return scene;
	}
}
