package scada.dao;

public class SQLCreazioneImpianto {
    public static String GET_TIPOLOGIE = """
        SELECT codice FROM TIPOLOGIA;
    """;

    public static String IMPIANTI_IN_PROVINCIA = """
        SELECT I.codiceImpianto FROM IMPIANTO I WHERE I.siglaProvincia = ?;
    """;

    public static String CREAZIONE_IMPIANTO = """
        INSERT INTO IMPIANTO (siglaProvincia, indirizzo, area, tipologia)
        VALUES (?, ?, ?, ?);
    """;

    public static String DISMISSIONE_IMPIANTO = """
        UPDATE IMPIANTO I 
        SET inOperazione = 0
        WHERE I.codiceImpianto = ? AND  I.siglaProvincia = ?;
    """;
}
