package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.InterventiTipologia;
import scada.dao.SQLCreazioneImpianto;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class InterventoImpiantoCreateController extends StageController {
    public Runnable onCloseRunnable;
    public ComboBox<String> comboTipologia;
    public ComboBox<String> comboProvincia;
    public ComboBox<Integer> comboCodiceImpianto;
    public TextArea textImpiantoInfo;

    private String regione;
    private String username;
    private Impianto impianto;
    private List<Integer> realTipologiaCode = new ArrayList<>();
    public static InterventoImpiantoCreateController newInstance(String regione, String username){
        return GuiConstructor.createInstance("/responsabili/InterventiImpiantoCreate.fxml",(InterventoImpiantoCreateController instance, Stage stage) ->{
            instance.stage = stage;
            instance.regione = regione;
            instance.username = username;
            //Aggiunge le tipologie di intervento alla combobox
            try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_INTERVENTO_TIPI)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    int tipologia = result.getInt("T.tipo");
                    instance.realTipologiaCode.add(tipologia);
                    instance.comboTipologia.getItems().add(InterventiTipologia.fromCode(tipologia));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Aggiunge le provincie alla combobox
            try (PreparedStatement statement1 = DAO.getDB().prepareStatement(SQLResponsabili.GET_PROVINCE_FROM_REGIONE)) {
                statement1.setString(1, instance.regione);
                ResultSet result1 = statement1.executeQuery();
                while (result1.next()) {
                    instance.comboProvincia.getItems().add(result1.getString("P.sigla"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Aggiunge i codici degli impianti di quella regione
            try (PreparedStatement statement2 = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_REGIONALI_ATTIVI)) {
                statement2.setString(1, instance.regione);
                ResultSet result2 = statement2.executeQuery();
                while (result2.next()) {
                    instance.comboCodiceImpianto.getItems().add(result2.getInt("I.codiceImpianto"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void filterCodiciImpianto() {
        String provincia = this.comboProvincia.getSelectionModel().getSelectedItem();
        if(provincia != null) {
             this.comboCodiceImpianto.setDisable(false);
             this.comboCodiceImpianto.getItems().clear();
             try (PreparedStatement statementCodici = DAO.getDB().prepareStatement(SQLCreazioneImpianto.IMPIANTI_IN_PROVINCIA)) {
                statementCodici.setString(1, provincia);
                ResultSet result = statementCodici.executeQuery();
                while (result.next()) {
                    this.comboCodiceImpianto.getItems().add(result.getInt("I.codiceImpianto"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserisce le informazioni dell'impianto
     */
    public void showImpiantoInfo() {
        Integer codice = this.comboCodiceImpianto.getSelectionModel().getSelectedItem();
        String provincia = this.comboProvincia.getSelectionModel().getSelectedItem();
        if(codice != null && provincia != null) {
            impianto = Impianto.findFromCodiceProvincia(codice, provincia);
            if(impianto == null){
                textImpiantoInfo.setText("Nessuna informazione aggiuntiva");
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("Codice Impianto:");
                builder.append(impianto.getCodice());
                builder.append('\n');
                builder.append("Provincia:");
                builder.append(impianto.getProvincia());
                builder.append('\n');
                builder.append("Indirizzo:");
                builder.append(impianto.getIndirizzo());
                builder.append('\n');
                builder.append("Area:");
                builder.append(impianto.getArea());
                builder.append('\n');
                builder.append("Uomo In Sito:");
                builder.append(impianto.isUomoInSito() ? "Presente" : "Assente");
                builder.append('\n');
                builder.append("Tipologia:");
                builder.append(Tipologia.fromCode(impianto.getTipologia()));
                builder.append('\n');
                textImpiantoInfo.setText(builder.toString());
            }
        } else {
            this.textImpiantoInfo.setText("Nessuna informazione aggiuntiva");
        }
    }

    /**
     * Crea l'intervento per l'impianto selezionato
     */
    public void createIntervento() {
        //Aggiungo l'intervento a INTERVENTO
        int codice = 0;
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLResponsabili.CREAZIONE_INTERVENTI, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.username);
            int chosenIndex = comboTipologia.getSelectionModel().getSelectedIndex();
            statement.setInt(2, realTipologiaCode.get(chosenIndex));
            int res = statement.executeUpdate();
            if (res == 0) {
                System.out.println("INSERIMENTO DI INTERVENTO FALLITO!!!");
                DAO.getDB().rollback();
            } else {
                System.out.println("INSERIMENTO DI INTERVENTO RIUSCITO!!!");
                DAO.getDB().commit();
            }
            var cod = statement.getGeneratedKeys();
            cod.next();
            codice = cod.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Aggiungo l'intervento a INT_IMPIANTO
        try (PreparedStatement statement1 = DAO.getDB().prepareStatement(SQLResponsabili.CREAZIONE_INTERVENTO_IMPIANTO)) {
            statement1.setInt(1, codice);
            statement1.setInt(2, this.comboCodiceImpianto.getSelectionModel().getSelectedItem());
            statement1.setString(3, this.comboProvincia.getSelectionModel().getSelectedItem());
            int res1 = statement1.executeUpdate();
            if (res1 == 0) {
                System.out.println("INSERIMENTO DI INTERVENTO FALLITO!!!");
                DAO.getDB().rollback();
            } else {
                System.out.println("INSERIMENTO DI INTERVENTO RIUSCITO!!!");
                DAO.getDB().commit();
            }
            this.onCloseRunnable.run();
            this.stage.hide();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
