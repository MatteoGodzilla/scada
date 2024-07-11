package scada.tools;

import javafx.application.Platform;
import scada.gui.tecnici.TecniciMainRow;
import scada.gui.tecnici.TecniciMainRowData;

public class Test {
    public static void main(String[] args) {
        Platform.startup(()->{
            TecniciMainRow.newInstance(new TecniciMainRowData(0, "gianfranco.babbei", 1));
        });
    }
}
