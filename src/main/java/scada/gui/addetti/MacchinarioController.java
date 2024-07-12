package scada.gui.addetti;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import scada.dao.DAO;
import scada.dao.SQLAddetti;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class MacchinarioController implements GuiController{

    public Button strButton;
    public Button stpButton;
    public AnchorPane macchinarioPane;
    public Label nomeMacchinario;
    public int codiceInstallazione;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static MacchinarioController newInstance(int codInstallazione){
        return GuiConstructor.createInstance("/addetti/macchinario-row.fxml",(MacchinarioController instance, AnchorPane pane)->{
            instance.macchinarioPane = pane;
            instance.codiceInstallazione = codInstallazione;
        });
    }

    public void setLabelText(String name) {
        nomeMacchinario.setText(name);
    }

    /**
     * Se clicco START, disabilito il pulsante di START e attivo quello di STOP modificando il database
     * @throws SQLException
     */
    public void startMacchinario() {
        try (PreparedStatement state = DAO.getDB().prepareStatement(SQLAddetti.START)) {
            state.setInt(1, codiceInstallazione);
            int res = state.executeUpdate();
            if (res == 0) {
                System.out.println("UPDATE DI START FALLITO!!!");
                DAO.getDB().rollback();
            } else {
                System.out.println("UPDATE DI START RIUSCITO!!!");
                DAO.getDB().commit();
                strButton.setDisable(true);
                stpButton.setDisable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se clicco STOP, disabilito il pulsante di STOP e attivo quello di START modificando il database
     */
    public void stopMacchinario() {
        try (PreparedStatement state = DAO.getDB().prepareStatement(SQLAddetti.STOP)) {
            state.setInt(1, codiceInstallazione);
            int res = state.executeUpdate();
            if (res == 0) {
                System.out.println("UPDATE DI STOP FALLITO!!!");
                DAO.getDB().rollback();
            } else {
                System.out.println("UPDATE DI STOP RIUSCITO!!!");
                DAO.getDB().commit();
                stpButton.setDisable(true);
                strButton.setDisable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se il macchinario è in manutenzione, sono disabilitati i pulsanti di START e di STOP
     */
    public void underMaintenance() {
        stpButton.setDisable(true);
        strButton.setDisable(true);
    }
}
