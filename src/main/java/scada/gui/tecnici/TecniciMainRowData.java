package scada.gui.tecnici;

import scada.dao.Impianto;
import scada.dao.Macchinario;

public class TecniciMainRowData {
    public int code;
    public int type;
    public String int_type;
    public int imp_id;
    public String imp_sigla;
    public String imp_via;

    private Impianto impiantoRef;
    private Macchinario macchinarioRef;

    public TecniciMainRowData(int code, int type, String int_type, Impianto impianto, Macchinario macchinario){
        this.code = code;
        this.type = type;
        this.int_type = int_type;
        this.imp_id = impianto.getCodice();
        this.imp_sigla = impianto.getProvincia();
        this.imp_via = impianto.getIndirizzo();

        this.impiantoRef = impianto;
        this.macchinarioRef = macchinario;
    }

    public Impianto getImpiantoRef() {
        return impiantoRef;
    }
    public Macchinario getMacchinarioRef() {
        return macchinarioRef;
    }
}
