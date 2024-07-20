package scada.gui.responsabili;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.SQLCreazioneFotovoltaico;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class FotovoltaicoCreateController extends StageController {
    public Runnable onCloseRunnable;
    //gui
    public TextField textCodImpianto;
    public TextField textProvinciaImpianto;
    public TextField textSpazioDisponibile;
    public TextField textTipologiaImpianto;

    public ComboBox<String> comboAziendaMacchinario;
    public ComboBox<String> comboModelloMacchinario;
    public ComboBox<Integer> comboDurataGaranzia;

    public TextField textAreaOccupata;
    public TextField textCostoGaranzia;
    public TextArea textDescrizioneGaranzia;

    public TextField textCelle;
    public TextField textAngoloPannello;
    public TextField textKWHMassimi;
    public Button btnInstallation;

    //internal variables
    private Impianto impianto;
    private float remainingSpace = -1;
    private Map<String, Float> spaceForModello = new HashMap<>();
    private static record GaranzieData(String description, int price){}
    private Map<Integer, GaranzieData> infoGaranzie = new HashMap<>();

    public static FotovoltaicoCreateController newInstance(int codiceImpianto, String provinciaImpianto){
        return GuiConstructor.createInstance("/responsabili/FotovoltaicoCreate.fxml",(FotovoltaicoCreateController instance, Stage stage) ->{
            instance.stage = stage;
            instance.initialLoad(codiceImpianto, provinciaImpianto);
        });
    }
    private void initialLoad(int codiceImpianto, String provinciaImpianto){
        //initial load
        this.impianto = Impianto.findFromCodiceProvincia(codiceImpianto, provinciaImpianto);
        textCodImpianto.setText(impianto.getCodice() + "");
        textProvinciaImpianto.setText(impianto.getProvincia() + "");
        textTipologiaImpianto.setText(Tipologia.fromCode(impianto.getTipologia()));

        //calculate remaining space
        try (var spazioRimanente = DAO.getDB().prepareStatement(SQLResponsabili.CALCOLO_SPAZIO_DISPONIBILE_FOTOVOLTAICO)) {
            spazioRimanente.setInt(1, codiceImpianto);
            ResultSet result = spazioRimanente.executeQuery();
            result.next();
            remainingSpace = impianto.getArea() - result.getFloat(1);
            textSpazioDisponibile.setText(remainingSpace + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (var stmt = DAO.getDB().prepareStatement(SQLCreazioneFotovoltaico.GET_AZIENDE)) {
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                comboAziendaMacchinario.getItems().add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void filterByAzienda() {
        String chosenAzienda = comboAziendaMacchinario.getSelectionModel().getSelectedItem();
        try (var stmt = DAO.getDB().prepareStatement(SQLCreazioneFotovoltaico.GET_MODELLO_FROM_AZIENDA)) {
            stmt.setString(1, chosenAzienda);
            stmt.setFloat(2, remainingSpace);
            ResultSet result = stmt.executeQuery();
            spaceForModello.clear();
            while(result.next()){
                spaceForModello.put(result.getString(1), result.getFloat(2));
            }
            comboModelloMacchinario.getItems().clear();
            comboModelloMacchinario.getItems().addAll(spaceForModello.keySet());
            comboModelloMacchinario.setDisable(false);
            comboDurataGaranzia.setDisable(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void filterByModello() {
        String azienda = comboAziendaMacchinario.getSelectionModel().getSelectedItem();
        String modello = comboModelloMacchinario.getSelectionModel().getSelectedItem();

        textAreaOccupata.setText(spaceForModello.get(modello) + "");

        try (var stmt = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_GARANZIE)) {
            stmt.setString(1, azienda);
            stmt.setString(2, modello);
            ResultSet result = stmt.executeQuery();
            infoGaranzie.clear();
            while(result.next()){
                infoGaranzie.put(result.getInt(1), new GaranzieData(result.getString(2), result.getInt(3)));
            }
            comboDurataGaranzia.getItems().clear();
            comboDurataGaranzia.getItems().addAll(infoGaranzie.keySet());
            comboDurataGaranzia.setDisable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showInfoGaranzia() {
        int years = comboDurataGaranzia.getSelectionModel().getSelectedItem();
        GaranzieData info = infoGaranzie.get(years);

        textDescrizioneGaranzia.setText(info.description);
        textCostoGaranzia.setText((float)(info.price)/100 + "");

        textCelle.setDisable(false);
        textAngoloPannello.setDisable(false);
        textKWHMassimi.setDisable(false);
        btnInstallation.setDisable(false);
    }

    public void createPannello() {
        try (var stmt = DAO.getDB().prepareStatement(SQLResponsabili.CREAZIONE_MACCHINARIO, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
            stmt.setInt(2, impianto.getTipologia());
            stmt.setString(3, comboAziendaMacchinario.getSelectionModel().getSelectedItem());
            stmt.setString(4, comboModelloMacchinario.getSelectionModel().getSelectedItem());
            stmt.setInt(5, comboDurataGaranzia.getSelectionModel().getSelectedItem());
            stmt.setInt(6, 2); //di default il macchinario è spento

            if(stmt.executeUpdate() <= 0)
                return;
            var generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            int codiceInstallazione = generatedKeys.getInt(1);
            //part 2
            var stmt2 = DAO.getDB().prepareStatement(SQLCreazioneFotovoltaico.GET_CODICE_INTERNO);
            stmt2.setInt(1, impianto.getCodice());
            stmt2.setString(2, impianto.getProvincia());
            ResultSet res = stmt2.executeQuery();
            res.next();
            int codiceInterno = res.getInt(1);

            //part 3
            var stmt3 = DAO.getDB().prepareStatement(SQLCreazioneFotovoltaico.CREAZIONE_FOTOVOLTAICO);
            stmt3.setInt(1, impianto.getCodice());
            stmt3.setString(2, impianto.getProvincia());
            stmt3.setInt(3, codiceInstallazione);
            stmt3.setInt(4, codiceInterno);
            stmt3.setInt(5, Integer.parseInt(textCelle.getText()));
            stmt3.setFloat(6, Float.parseFloat(textKWHMassimi.getText()));
            stmt3.setFloat(7, Float.parseFloat(textAngoloPannello.getText()));

            if(stmt3.executeUpdate() > 0){
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
