package scada.dao;

public class SQLImpiantoCreate {
    public static String GET_TIPOLOGIE = """
        SELECT codice FROM TIPOLOGIA;
    """;

    public static String CREAZIONE_IMPIANTO = """
        INSERT INTO IMPIANTO (siglaProvincia, indirizzo, area, tipologia)
        VALUES (?, ?, ?, ?);
    """;
}
