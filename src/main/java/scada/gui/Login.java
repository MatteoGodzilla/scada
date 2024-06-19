package scada.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;
import java.sql.Connection;

public class Login extends StageController {
    public ComboBox<String> type;
    public TextField username;
    public PasswordField password;

    public void initialize(){
        type.getItems().addAll("Tecnico","Addetto", "Responsabile");
        type.getSelectionModel().select(0);
    }

    public static Login newInstance() {
        return GuiConstructor.createInstance("/login.fxml", (Login inst, Stage stage) -> {
            inst.stage = stage;
        });
    }

    //Event Handlers

    public void submit(){
        //send to database
        
    }
}
