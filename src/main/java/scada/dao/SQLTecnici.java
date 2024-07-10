package scada.dao;

public class SQLTecnici {
    //manca il controllo sulla provincia
    public static String INTERVENTI_SCELTI = """
        SELECT codice, usernameResponsabile, tipo FROM INTERVENTO
        WHERE usernameTecnico = ?;
    """;

    //manca il controllo sulla provincia
    public static String INTERVENTI_DISPONIBILI = """
        SELECT codice, usernameResponsabile, tipo FROM INTERVENTO
        where usernameTecnico = NULL;
    """;
}
