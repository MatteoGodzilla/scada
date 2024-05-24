package scada.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test {
    private Scene scene;

    //this should be moved to another class, since it's basically the same in Test and Test2
	public static Test newInstance(){
        var res = Test.class.getResource("/test.fxml");
        FXMLLoader loader = new FXMLLoader(res);
		try {
            //the following lines must be in this specific order
            //1: loader.load()
            Parent p = loader.load();
            //2: access controller
            Test instance =  loader.getController();
            //3: set up controller class
            instance.scene = new Scene(p);
            return instance;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }

    public void switchToSecond() {
        var inst = Test2.newInstance();
        Stage stage = new Stage();
        stage.setScene(inst.getScene());
        stage.show();
    }

    public Scene getScene() {
		return scene;
	}
}