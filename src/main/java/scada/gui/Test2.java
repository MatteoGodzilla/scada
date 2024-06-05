package scada.gui;

public class Test2 extends GuiController {
    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
	public static Test2 newInstance(){
        return GuiConstructor.createInstance("/test2.fxml");
    }

    public void switchToMain(){
        Test window = Test.newInstance();
        window.getScene();
    }
}
