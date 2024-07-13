package scada.gui.addetti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLAddetti;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class Addetto extends StageController{

    private String username;
    public List<ImpiantoController> listaImpController = new ArrayList<>();
    public Button weatherButton;
    public Button reportButton;
    public Button refreshButton;
    public Accordion accordionPane;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Addetto newInstance(String username){
        return GuiConstructor.createInstance("/addetti/addetto.fxml",(Addetto instance, Stage stage)->{
            instance.stage = stage;
            instance.username = username;
            instance.addettiInfo();
        });
    }

    /**
     * Mostra la finestra con le informazioni sul meteo dell'impianto selezionato
     */
    public void weather() {
        for (ImpiantoController impController : listaImpController) {
            var impianto = impController.impiantoPane;
            if (impianto.isExpanded()) {
                try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLAddetti.METEO)) {
                    statement.setInt(1, impController.getCodImpianto());
                    statement.setString(2, impController.getProvincia());
                    ResultSet result = statement.executeQuery();
                    result.next();
                    Meteo m = Meteo.newInstance(impController.nomeImpianto());
                    m.txtVento.setText(result.getString("IR.vento"));
                    m.txtUV.setText(result.getString("IR.uv"));
                    m.labelTime.setText(result.getString("IR.ts"));
                    m.getStage().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Genera un file .csv con le statistiche di un impianto
     * @throws IOException
     */
    public void generateReport() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("ReportImpianto");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File filePath = fileChooser.showSaveDialog(getStage());
        //In content ci va ciò che voglio salvare nel file
        String content = "Ciao Mondo" + "!!!";
        SaveFile(content, filePath);
    }

    /**
     * Ricarica la informazioni su impianti e macchinari
     */
    public void refresh() {
        accordionPane.getPanes().clear();
        addettiInfo();
    }

    /**
     * Metodo per salvare un file
     * @param content il contenuto del file
     * @param file il percorso in cui salvare il file
     * @throws IOException
     */
    private void SaveFile(String content, File file) throws IOException{
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Mostra gli impianti che sono affidati all'addetto identificato dall'username
     */
    private void addettiInfo() {
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLAddetti.IMPIANTI)) {
           //Il setString sostituisce i ? nella query
           statement.setString(1, username);
           ResultSet result = statement.executeQuery();
            while (result.next()) {
                ImpiantoController  impController = ImpiantoController.newInstance(result.getInt("M.codiceImpianto"), result.getString("M.siglaProvincia"));
                listaImpController.add(impController);
                TitledPane impianto = impController.impiantoPane;
                var nomeImpianto = impController.nomeImpianto();
                if (result.getBoolean("I.uomoInSito")) {
                    nomeImpianto = nomeImpianto + " (Uomo In Sito)";
                }
                impController.setTitle(nomeImpianto);

                try (PreparedStatement statement2 = DAO.getDB().prepareStatement(SQLAddetti.MACCHINARI)) {
                    statement2.setInt(1, result.getInt("codiceImpianto"));
                    statement2.setString(2, result.getString("siglaProvincia"));
                    ResultSet result2 = statement2.executeQuery();
                    result2.next();
                    var tipologia = result2.getInt("tipologia");
                    switch (tipologia) {
                        case 1:
                            macchinariFotovoltaiciInfo(impController);
                            break;

                        case 2:
                            macchinariEoliciInfo(impController);
                            break;

                        case 3:
                            macchinariBiogasInfo(impController);
                            break;
                   }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                accordionPane.getPanes().add(impianto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   /**
    * Estrae le informazioni sui macchinari fotovoltaici dal database
    * @param impController il controller dell'impianto in cui si trovano i macchinari
    */
   private void macchinariFotovoltaiciInfo(ImpiantoController impController) {
       try (PreparedStatement statement3 = DAO.getDB().prepareStatement(SQLAddetti.MACCHINARI_FOTOVOLTAICI)) {
           ResultSet result3 = statement3.executeQuery();
           while (result3.next()) {
               var nomeMacchinario = result3.getString("M.nomeModello") + "-" + result3.getInt("M.codiceInstallazione") + "-" + result3.getString("azienda");
               MacchinarioController maccController = MacchinarioController.newInstance(result3.getInt("M.codiceInstallazione"));
               AnchorPane macchinario = maccController.macchinarioPane;
               nomeMacchinario = updateButtonStatus(result3, maccController, nomeMacchinario);
               maccController.setLabelText(nomeMacchinario);
               impController.macchinariBox.getChildren().add(macchinario);
           }
       } catch (SQLException e2) {
           e2.printStackTrace();
       }
   }

   /**
    * Estrae le informazioni sui macchinari eolici dal database
    * @param impController il controller dell'impianto in cui si trovano i macchinari
    */
   private void macchinariEoliciInfo(ImpiantoController impController) {
       try (PreparedStatement statement3 = DAO.getDB().prepareStatement(SQLAddetti.MACCHINARI_EOLICI)) {
           ResultSet result3 = statement3.executeQuery();
           while (result3.next()) {
               var nomeMacchinario = result3.getString("M.nomeModello") + "-" + result3.getInt("M.codiceInstallazione") + "-" + result3.getString("azienda");
               MacchinarioController maccController = MacchinarioController.newInstance(result3.getInt("M.codiceInstallazione"));
               AnchorPane macchinario = maccController.macchinarioPane;
               nomeMacchinario = updateButtonStatus(result3, maccController, nomeMacchinario);
               maccController.setLabelText(nomeMacchinario);
               impController.macchinariBox.getChildren().add(macchinario);
           }
       } catch (SQLException e2) {
           e2.printStackTrace();
       }
   }

   /**
    * Estrae le informazioni sui macchinari biogas dal database
    * @param impController il controller dell'impianto in cui si trovano i macchinari
    */
   private void macchinariBiogasInfo(ImpiantoController impController) {
       try (PreparedStatement statement3 = DAO.getDB().prepareStatement(SQLAddetti.MACCHINARI_BIOGAS)) {
           ResultSet result3 = statement3.executeQuery();
           while (result3.next()) {
               var nomeMacchinario = result3.getString("M.nomeModello") + "-" + result3.getInt("M.codiceInstallazione") + "-" + result3.getString("azienda");
               MacchinarioController maccController = MacchinarioController.newInstance(result3.getInt("M.codiceInstallazione"));
               AnchorPane macchinario = maccController.macchinarioPane;
               nomeMacchinario = updateButtonStatus(result3, maccController, nomeMacchinario);
               maccController.setLabelText(nomeMacchinario);
               impController.macchinariBox.getChildren().add(macchinario);
           }
       } catch (SQLException e2) {
           e2.printStackTrace();
       }
   }

   /**
    * Si occupa di impostare i button in modo corretto solo all'avvio dell'applicazione
    * @param result3 il ResultSet contenente i dati estratti dal database
    * @param maccController il controller del macchinario
    * @param nomeMacchinario il nome del macchinario
    * @return il nome del macchinario, eventualmente modificato
    * @throws SQLException
    */
    private String updateButtonStatus(ResultSet result3, MacchinarioController maccController, String nomeMacchinario) throws SQLException {
        if (result3.getInt("M.status") == 1) {
            maccController.strButton.setDisable(true);
            maccController.stpButton.setDisable(false);
        } else if (result3.getInt("M.status") == 2) {
            maccController.stpButton.setDisable(true);
            maccController.strButton.setDisable(false);
        } else {
            maccController.underMaintenance();
            nomeMacchinario = nomeMacchinario + " (in manutenzione)";
        }
        return nomeMacchinario;
    }
}
