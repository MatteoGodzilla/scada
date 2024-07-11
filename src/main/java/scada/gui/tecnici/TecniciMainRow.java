package scada.gui.tecnici;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class TecniciMainRow implements GuiController {
    public AnchorPane root;
    public Label code;
    public Label int_type;
    public Label imp_id;
    public Label address;

    public static TecniciMainRow newInstance(TecniciMainRowData data){
        return GuiConstructor.createInstance("/tecnici/tecnici-main-row.fxml", (TecniciMainRow instance, AnchorPane row) -> {
            instance.root = row;
            instance.code.setText("" + data.code);
            instance.int_type.setText(data.int_type);
            instance.imp_id.setText("Impianto " + data.imp_id + "(" + data.imp_sigla + ")");
            instance.address.setText(data.imp_via);
        });
    }
}
