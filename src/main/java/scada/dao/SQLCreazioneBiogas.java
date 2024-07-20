package scada.dao;

public class SQLCreazioneBiogas {
    public static String GET_AZIENDE = """
        SELECT DISTINCT(azienda) FROM MODELLO;
    """;

    public static String GET_MODELLO_FROM_AZIENDA = """
        SELECT nome, area FROM MODELLO
        WHERE azienda = ? AND area <= ?;
    """;

    public static String GET_CODICE_INTERNO = """
        SELECT MAX(mb.codiceInterno)+1 FROM MACC_BIOGAS mb
        WHERE mb.codiceImpianto = ? && mb.siglaProvincia = ?;
    """;

    public static String CREAZIONE_BIOGAS = """
        INSERT INTO MACC_BIOGAS (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno, kwhOttimo, kgBatteri, kgUmido)
        VALUES (?, ?, ?, ?, ?, ?, ?);
    """;
}
