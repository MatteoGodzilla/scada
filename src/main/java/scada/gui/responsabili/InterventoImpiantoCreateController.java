package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class InterventoImpiantoCreateController extends StageController {

    public ComboBox<Integer> comboTipologia;
    public ComboBox<String> comboProvincia;
    public ComboBox<Integer> comboCodiceImpianto;
    public TextArea textImpiantoInfo;
    private String regione;
    private String username;
    private Impianto impianto;

    public static InterventoImpiantoCreateController newInstance(String regione, String username){
        return GuiConstructor.createInstance("/responsabili/InterventiImpiantoCreate.fxml",(InterventoImpiantoCreateController instance, Stage stage) ->{
            instance.stage = stage;
            instance.regione = regione;
            instance.username = username;
            //Aggiunge le tipologie di intevento alla combobox
            try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_INTERVENTO_TIPI)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    instance.comboTipologia.getItems().add(result.getInt("T.tipo"));
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
            try (PreparedStatement statement2 = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_REGIONALI)) {
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

    /**
     * Inserisce le informazioni dell'impianto
     */
    public void showImpiantoInfo() {
        impianto = Impianto.findFromCodiceProvincia(this.comboCodiceImpianto.getValue(), this.comboProvincia.getValue());
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
    }

    /**
     * Crea l'intervento per l'impianto selezionato
     */
    public void createIntervento() {
        //Aggiungo l'intervento a INTERVENTO
        int codice = 0;
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLResponsabili.CREAZIONE_INTERVENTI, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.username);
            statement.setInt(2, this.comboTipologia.getValue());
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
            statement1.setInt(2, this.comboCodiceImpianto.getValue());
            statement1.setString(3, this.comboProvincia.getValue());
            int res1 = statement1.executeUpdate();
            if (res1 == 0) {
                System.out.println("INSERIMENTO DI INTERVENTO FALLITO!!!");
                DAO.getDB().rollback();
            } else {
                System.out.println("INSERIMENTO DI INTERVENTO RIUSCITO!!!");
                DAO.getDB().commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
