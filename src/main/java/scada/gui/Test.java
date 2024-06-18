package scada.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class Test extends GuiController {

    public TreeView<AnchorPane> table;

    public void initialize(){

        // root.getChildren().add(new TreeItem<String>("Figlio 1"));
        // root.getChildren().add(new TreeItem<String>("Figlio 2"));

        try {
            TreeItem<AnchorPane> root = new TreeItem<AnchorPane>(
                FXMLLoader.load(this.getClass().getResource("/row.fxml"))
            );

            root.getChildren().add(new TreeItem<AnchorPane>(
                FXMLLoader.load(this.getClass().getResource("/row.fxml"))
            ));


            table.setRoot(root);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

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
        // System.out.println(textField.textProperty().getValue());
    }

    public void textFieldAction(){
        // System.out.println(textField.textProperty().getValue());
    }
}