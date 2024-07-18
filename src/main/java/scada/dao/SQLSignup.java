package scada.dao;

public class SQLSignup {
    public static String PROVINCIE = """
        SELECT sigla FROM PROVINCIA;
    """;

    public static String REGIONI = """
        SELECT DISTINCT(regione) FROM PROVINCIA;
    """;

    public static String CHECK_TECNICO = """
        SELECT COUNT(username) FROM USR_TECNICO
        where username = ?;
    """;

    public static String CHECK_ADDETTO = """
        SELECT COUNT(username) FROM USR_ADDETTO
        where username = ?;
    """;

    public static String CHECK_RESPONSABILI = """
        SELECT COUNT(username) FROM USR_RESPONSABILE
        where username = ?;
    """;

    public static String REGISTER_TECNICO = """
        INSERT INTO USR_TECNICO(username, password, nome, cognome, siglaProvincia)
        VALUE (?, ?, ?, ?, ?);
    """;

    public static String REGISTER_ADDETTO = """
        INSERT INTO USR_ADDETTO(username, password, nome, cognome, regione)
        VALUE (?, ?, ?, ?, ?);
    """;

    public static String REGISTER_RESPONSABILE = """
        INSERT INTO USR_RESPONSABILE(username, password, nome, cognome, regione)
        VALUE (?, ?, ?, ?, ?);
    """;
}
