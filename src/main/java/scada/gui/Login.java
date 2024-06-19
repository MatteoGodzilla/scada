package scada.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

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
        String hash = "<get from database>";
        Result r = BCrypt.verifyer().verify(password.getText().toCharArray(), hash);
        // if(r.verified){
            switch (type.getSelectionModel().getSelectedIndex()) {
                case 0:
                    //open window Tecnici
                    break;
                case 1:
                    //open window Addetti
                    Addetto newWindow = Addetto.newInstance();
                    newWindow.getStage().show();
                    break;
                case 2:
                    //open window Responsabili
                    break;
            }
        // }
    }
}
