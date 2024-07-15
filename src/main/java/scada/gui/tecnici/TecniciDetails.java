package scada.gui.tecnici;

import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.Macchinario;
import scada.dao.MacchinarioStatus;
import scada.dao.SQLTecniciDetails;
import scada.dao.Tipologia;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class TecniciDetails extends StageController{

    public TextArea detailsImpianto;
    public TextArea detailsMacchinario;
    public TextArea note;
    //ci deve essere un modo per fare questo direttamente da javafx
    public Runnable closingCallback;

    private int codiceIntervento;
    private int tipoIntervento;
    private Impianto impianto;
    private Macchinario macchinario;

    public static TecniciDetails newInstance(int codiceIntervento, int tipoIntervento, Impianto impianto, Macchinario macchinario){
        return GuiConstructor.createInstance("/tecnici/tecnici-details.fxml",(TecniciDetails instance, Stage stage) ->{
            instance.stage = stage;
            instance.codiceIntervento = codiceIntervento;
            instance.tipoIntervento = tipoIntervento;
            instance.impianto = impianto;
            instance.macchinario = macchinario;
            instance.loadDetailsImpianto();
            instance.loadDetailsMacchinario();
        });
    }

    public void onStep0(){
        try (var stmt = DAO.getDB().prepareStatement(SQLTecniciDetails.SQL_UOMO_IN_SITO)) {
            stmt.setInt(1, impianto.getCodice());
            stmt.setString(2, impianto.getProvincia());
            if(stmt.executeUpdate() > 0){
                impianto = Impianto.refresh(impianto);
                loadDetailsImpianto();
                DAO.getDB().commit();
            } else {
                DAO.getDB().rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onStartIntervento(){
        if(tipoIntervento != 3){
            try (var stmt = DAO.getDB().prepareStatement(SQLTecniciDetails.SQL_INIZIO_INT_MACCHINARIO)) {
                stmt.setInt(1, macchinario.getCodiceInstallazione());
                if(stmt.executeUpdate() > 0){
                    macchinario = Macchinario.refresh(macchinario);
                    loadDetailsMacchinario();
                    DAO.getDB().commit();
                } else {
                    DAO.getDB().rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //non deve fare niente se è un intervento di dismissione impianto
    }

    public void onStopIntervento(){
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Conferma");
        a.setHeaderText("Confermare la chiusura dell'intervento?");
        a.showAndWait();
        System.out.println(a.getResult());

        if(a.getResult() != ButtonType.OK)
            return;
        //completa intervento
        try (var stmt = DAO.getDB().prepareStatement(SQLTecniciDetails.SQL_FINE_INTERVENTO)) {
            stmt.setString(1, note.getText());
            stmt.setInt(2, codiceIntervento);
            if(stmt.executeUpdate() > 0) {
                DAO.getDB().commit();
            } else {
                DAO.getDB().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //modifica valori necessari
        if(tipoIntervento != 3){
            try (var stmt = DAO.getDB().prepareStatement(SQLTecniciDetails.SQL_FINE_INT_MACCHINARIO)) {
                stmt.setInt(1, macchinario.getCodiceInstallazione());
                if(stmt.executeUpdate() > 0){
                    DAO.getDB().commit();
                } else {
                    DAO.getDB().rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            //dismissione impianto
            // try (var stmt = DAO.getDB().prepareStatement(SQLTecniciDetails.SQL_FINE_INT_IMPIANTO)) {
            //     stmt.setInt(1, macchinario.getCodiceInstallazione());
            //     if(stmt.executeUpdate() > 0){
            //         DAO.getDB().commit();
            //     } else {
            //         DAO.getDB().rollback();
            //     }
            // } catch (SQLException e) {
            //     e.printStackTrace();
            // }
        }
        closingCallback.run();
        stage.hide();
    }

    private void loadDetailsImpianto() {
        if(impianto == null){
            detailsImpianto.setText("Nessuna informazione aggiuntiva");
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
            detailsImpianto.setText(builder.toString());
        }
    }

    private void loadDetailsMacchinario() {
        if(macchinario == null){
            detailsMacchinario.setText("Nessuna informazione aggiuntiva");
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
            builder.append('\n');
            builder.append("Status:");
            builder.append(MacchinarioStatus.fromCode(macchinario.getStatus()));
            builder.append('\n');
            detailsMacchinario.setText(builder.toString());
        }
    }
}
