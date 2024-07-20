package scada.gui.responsabili;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLCreazioneImpianto;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class ImpiantoCreateController extends StageController {
    public Runnable onCloseRunnable;
    public ComboBox<String> comboProvincia;
    public ComboBox<String> comboTipologia;
    public TextField textIndirizzo;
    public TextField textAreaImpianto;

    private String regione;
    private List<Integer> realTipologiaValues = new ArrayList<>();
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
        try (var stmt = DAO.getDB().prepareStatement(SQLCreazioneImpianto.GET_TIPOLOGIE)) {
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                int val = result.getInt(1);
                realTipologiaValues.add(val);
                comboTipologia.getItems().add(Tipologia.fromCode(val));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createImpianto() {
        try (var stmt = DAO.getDB().prepareStatement(SQLCreazioneImpianto.CREAZIONE_IMPIANTO)) {
            stmt.setString(1, comboProvincia.getSelectionModel().getSelectedItem());
            stmt.setString(2, textIndirizzo.getText());
            stmt.setFloat(3, Float.parseFloat(textAreaImpianto.getText()));
            int chosenIndex = comboTipologia.getSelectionModel().getSelectedIndex();
            stmt.setInt(4, realTipologiaValues.get(chosenIndex));
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
