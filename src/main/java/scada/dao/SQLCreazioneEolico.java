package scada.dao;

public class SQLCreazioneEolico {
    public static String GET_AZIENDE = """
        SELECT DISTINCT(azienda) FROM MODELLO;
    """;

    public static String GET_MODELLO_FROM_AZIENDA = """
        SELECT nome, area FROM MODELLO
        WHERE azienda = ? AND area <= ?;
    """;

    public static String GET_CODICE_INTERNO = """
        SELECT MAX(me.codiceInterno)+1 FROM MACC_EOLICO me
        WHERE me.codiceImpianto = ? && me.siglaProvincia = ?
    """;

    public static String CREAZIONE_EOLICO = """
        INSERT INTO MACC_EOLICO (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno) VALUES (?, ?, ?, ?);
    """;
}
