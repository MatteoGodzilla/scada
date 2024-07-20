package scada.gui.responsabili;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLImpiantoCreate;
import scada.dao.SQLResponsabili;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class ImpiantoCreateController extends StageController {
    public Runnable onCloseRunnable;
    public ComboBox<String> comboProvincia;
    public ComboBox<Integer> comboTipologia;
    public TextField textIndirizzo;
    public TextField textAreaImpianto;

    private String regione;
    public static ImpiantoCreateController newInstance(String regione){
        return GuiConstructor.createInstance("/responsabili/ImpiantoCreate.fxml",(ImpiantoCreateController instance, Stage stage) ->{
            instance.stage = stage;
            instance.regione = regione;
            instance.initialLoad();
        });
    }

    private void initialLoad(){
        //load provincie
        try (var stmt = DAO.getDB().prepareStatement(SQLResponsabili.GET_PROVINCE_FROM_REGIONE)) {
            stmt.setString(1, regione);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                comboProvincia.getItems().addAll(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //load tipologie
        try (var stmt = DAO.getDB().prepareStatement(SQLImpiantoCreate.GET_TIPOLOGIE)) {
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                comboTipologia.getItems().add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createImpianto() {
        try (var stmt = DAO.getDB().prepareStatement(SQLImpiantoCreate.CREAZIONE_IMPIANTO)) {
            stmt.setString(1, comboProvincia.getSelectionModel().getSelectedItem());
            stmt.setString(2, textIndirizzo.getText());
            stmt.setFloat(3, Float.parseFloat(textAreaImpianto.getText()));
            stmt.setInt(4, comboTipologia.getSelectionModel().getSelectedItem());
            if(stmt.executeUpdate() > 0){
                DAO.getDB().commit();
                onCloseRunnable.run();
                stage.hide();
            } else {
                DAO.getDB().rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
