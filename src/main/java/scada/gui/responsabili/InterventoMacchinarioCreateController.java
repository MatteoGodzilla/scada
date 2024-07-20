package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.Macchinario;
import scada.dao.MacchinarioStatus;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class InterventoMacchinarioCreateController extends StageController {

    public ComboBox<Integer> comboTipologia;
    public ComboBox<Integer> comboCodiceInstallazione;
    public TextArea textMacchinarioInfo;
    private String regione;
    private String username;
    private Macchinario macchinario;

    public static InterventoMacchinarioCreateController newInstance(String regione, String username){
        return GuiConstructor.createInstance("/responsabili/InterventiMacchinarioCreate.fxml",(InterventoMacchinarioCreateController instance, Stage stage) ->{
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
            //Aggiunge i codici dei macchinari fotovoltaici alla combobox
            try (PreparedStatement statement1 = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_MACCHINARI_FOTOVOLTAICI_PER_REGIONE)) {
                statement1.setString(1, instance.regione);
                ResultSet result1 = statement1.executeQuery();
                while (result1.next()) {
                    instance.comboCodiceInstallazione.getItems().add(result1.getInt("MA.codiceInstallazione"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Aggiunge i codici dei macchinari eolici alla combobox
            try (PreparedStatement statement2 = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_MACCHINARI_EOLICI_PER_REGIONE)) {
                statement2.setString(1, instance.regione);
                ResultSet result2 = statement2.executeQuery();
                while (result2.next()) {
                    instance.comboCodiceInstallazione.getItems().add(result2.getInt("MA.codiceInstallazione"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Aggiunge i codici dei macchinari biogas alla combobox
            try (PreparedStatement statement3 = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_MACCHINARI_BIOGAS_PER_REGIONE)) {
                statement3.setString(1, instance.regione);
                ResultSet result3 = statement3.executeQuery();
                while (result3.next()) {
                    instance.comboCodiceInstallazione.getItems().add(result3.getInt("MA.codiceInstallazione"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Inserisce le informazioni del macchinario
     */
    public void showMacchinarioInfo() {
        macchinario = Macchinario.findFromInstCode(this.comboCodiceInstallazione.getValue());
        if(macchinario == null){
            textMacchinarioInfo.setText("Nessuna informazione aggiuntiva");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("Codice Installazione:");
            builder.append(macchinario.getCodiceInstallazione());
            builder.append('\n');
            builder.append("Data Installazione:");
            builder.append(macchinario.getDataInstallazione());
            builder.append('\n');
            builder.append("Tipologia:");
            builder.append(Tipologia.fromCode(macchinario.getTipologia()));
            builder.append('\n');
            builder.append("Azienda:");
            builder.append(macchinario.getAzienda());
            builder.append('\n');
            builder.append("Nome Modello:");
            builder.append(macchinario.getNomeModello());
            builder.append('\n');
            builder.append("Garanzia:");
            builder.append(macchinario.getDurataGaranzia());
            builder.append(" anni\n");
            builder.append("Status:");
            builder.append(MacchinarioStatus.fromCode(macchinario.getStatus()));
            builder.append('\n');
            textMacchinarioInfo.setText(builder.toString());
        }
    }

    /**
     * Crea l'intervento per il macchinario selezionato
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
        //Aggiunto l'intervento a INT_MACCHINARIO
        try (PreparedStatement statement1 = DAO.getDB().prepareStatement(SQLResponsabili.CREAZIONE_INTERVENTO_MACCHINARIO)) {
            statement1.setInt(1, codice);
            statement1.setInt(2, this.comboCodiceInstallazione.getValue());
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
