package scada.gui.tecnici;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class TecniciMainRow implements GuiController {
    public AnchorPane root;
    public Label code;
    public Label username;
    public Label type;

    public static TecniciMainRow newInstance(TecniciMainRowData data){
        return GuiConstructor.createInstance("/tecnici/tecnici-main-row.fxml", (TecniciMainRow instance, AnchorPane row) -> {
            instance.root = row;
            instance.code.setText("" + data.code);
            instance.username.setText(data.userResponsabile);
            instance.type.setText(data.type);
        });
    }
}
