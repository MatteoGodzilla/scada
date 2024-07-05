package scada.dao;

public class SQLLogin {

    public static String LOGIN_TECNICI = """
        SELECT username, password FROM USR_TECNICO
        WHERE username = ?
    """;

    public static String LOGIN_ADDETTI = """
        SELECT username, password FROM USR_ADDETTO
        WHERE username = ?
    """;

    public static final String LOGIN_RESPONSABILI = """
        SELECT username, password FROM USR_RESPONSABILE
        WHERE username = ?
    """;
}
