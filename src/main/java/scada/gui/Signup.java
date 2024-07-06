package scada.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLSignup;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Signup extends StageController {
    public ComboBox<String> type;
    public TextField firstName;
    public TextField lastName;
    public TextField username;
    public PasswordField password;
    public ComboBox<String> extra;

    private List<String> provincie = new ArrayList<String>();
    private List<String> regioni = new ArrayList<String>();

    public void initialize(){
        type.getItems().addAll("Tecnico", "Addetto", "Responsabile");
        type.getSelectionModel().select(0);
    }

    public static Signup newInstance(){
        return GuiConstructor.createInstance("/signup.fxml", (Signup instance, Stage stage) -> {
            instance.stage = stage;
            instance.loadData();
            instance.updateExtra();
        });
    }

    // Event Handlers
    public void updateExtra(){
        extra.getItems().clear();
        if (type.getSelectionModel().getSelectedIndex() == 0){
            // i tecnici lavorano a livello di provincia
            extra.getItems().setAll(provincie);
        } else {
            // gli addetti e i responsabili lavorano a livello di regione
            extra.getItems().setAll(regioni);
        }
    }

    public void submit(){
        //check that every parameter has a value
        boolean all = true;
        all = firstName.getText() != "" && all;
        all = lastName.getText() != "" && all;
        all = username.getText() != "" && all;
        all = password.getText() != "" && all;
        all = extra.getSelectionModel().getSelectedIndex() >= 0 && all;
        if( all ){
            try {
                PreparedStatement statement = null;
                switch(type.getSelectionModel().getSelectedIndex()){
                    default:
                    case 0:
                        statement = DAO.getDB().prepareStatement(SQLSignup.REGISTER_TECNICO);
                        break;
                    case 1:
                        statement = DAO.getDB().prepareStatement(SQLSignup.REGISTER_TECNICO);
                        break;
                    case 2:
                        statement = DAO.getDB().prepareStatement(SQLSignup.REGISTER_RESPONSABILE);
                        break;
                }
                statement.setString(1, username.getText());
                byte[] pwd = BCrypt.withDefaults().hash(12, password.getText().getBytes());
                statement.setString(2, new String(pwd));
                statement.setString(3, firstName.getText());
                statement.setString(4, lastName.getText());
                statement.setString(5, extra.getSelectionModel().getSelectedItem());
                int updatedRows = statement.executeUpdate();
                if(updatedRows > 0){
                    DAO.getDB().commit();
                } else {
                    DAO.getDB().rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(AlertType.ERROR, "Inserire tutti i campi necessari");
            a.show();
        }

    }

    //Util functions
    private void loadData() {
        //load provincie
        try (var statement = DAO.getDB().prepareStatement(SQLSignup.PROVINCIE)) {
            var result = statement.executeQuery();
            while(result.next()){
                provincie.add(result.getString(1));
            }
            System.out.println(provincie.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //load regioni
        try (var statement = DAO.getDB().prepareStatement(SQLSignup.REGIONI)) {
            var result = statement.executeQuery();
            while(result.next()){
                regioni.add(result.getString(1));
            }
            System.out.println(regioni.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
