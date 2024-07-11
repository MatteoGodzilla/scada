package scada.gui.tecnici;

public class TecniciMainRowData {
    public String userResponsabile;
    public int code;
    public String type;

    public TecniciMainRowData(int code, String usernameResponsabile, String type){
        this.code = code;
        this.userResponsabile = usernameResponsabile;
        this.type = type;
    }
}
