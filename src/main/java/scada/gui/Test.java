package scada.gui;

import javafx.scene.control.TextField;

public class Test extends GuiController {

    public TextField textField;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
	public static Test newInstance(){
        return GuiConstructor.createInstance("/test.fxml");
    }

    public void switchToSecond() {
        // var inst = Test2.newInstance();
        // Stage stage = new Stage();
        // stage.setScene(inst.getScene());
        // stage.show();
        System.out.println(textField.textProperty().getValue());
    }

    public void textFieldAction(){
        System.out.println(textField.textProperty().getValue());
    }
}