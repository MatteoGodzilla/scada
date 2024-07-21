package scada.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLLogin;
import scada.gui.addetti.Addetto;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;
import scada.gui.responsabili.ResponsabiliMain;
import scada.gui.tecnici.TecniciMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class Login extends StageController {
    public ComboBox<String> type;
    public TextField username;
    public PasswordField password;
    public Label error;

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
        try {
            PreparedStatement statement = null;
            switch(type.getSelectionModel().getSelectedIndex()){
                default:
                case 0:
                    statement = DAO.getDB().prepareStatement(SQLLogin.LOGIN_TECNICI);
                    break;
                case 1:
                    statement = DAO.getDB().prepareStatement(SQLLogin.LOGIN_ADDETTI);
                    break;
                case 2:
                    statement = DAO.getDB().prepareStatement(SQLLogin.LOGIN_RESPONSABILI);
                    break;
            }
            statement.setString(1, username.getText());
            ResultSet hashResponse = statement.executeQuery();
            if(!hashResponse.next()){
                error.setText("Username/Password invalida");
                return;
            }
            String hash = hashResponse.getString("password");
            Result r = BCrypt.verifyer().verify(password.getText().toCharArray(), hash);
            if(!r.verified){
                error.setText("Username/Password invalida");
                return;
            }
            switch (type.getSelectionModel().getSelectedIndex()) {
                case 0:
                    //open window Tecnici
                    TecniciMain tecnici = TecniciMain.newInstance(username.getText());
                    tecnici.getStage().show();
                    break;
                case 1:
                    //open window Addetti
                    Addetto newWindow = Addetto.newInstance(username.getText());
                    newWindow.getStage().show();
                    break;
                case 2:
                    //open window Responsabili
                    ResponsabiliMain responsabili = ResponsabiliMain.newInstance(username.getText());
                    responsabili.getStage().show();
                    break;
            }
            stage.hide();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void openSignup(){
        Signup signup = Signup.newInstance();
        signup.getStage().show();
    }
}
