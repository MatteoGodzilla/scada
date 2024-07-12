package scada.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Impianto {
    private int codiceImpianto;
    private String siglaProvincia;
    private String indirizzo;
    private float area;
    private boolean uomoInSito;
    private int tipologia; //enum?

    private static String SQL_CODICE_PROVINCIA = """
        SELECT * FROM IMPIANTO
        WHERE codiceImpianto = ? AND siglaProvincia = ?;
    """;

    public static String SQL_MACC_FOTO = """
        SELECT i.* FROM MACC_FOTOVOLTAICO mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;

    public static String SQL_MACC_EOLICO = """
        SELECT i.* FROM MACC_EOLICO mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;

    public static String SQL_MACC_BIOGAS = """
        SELECT i.* FROM MACC_BIOGAS mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;


    private Impianto(int codice, String provincia, String indirizzo, float area, boolean uomoInSito, int tipologia) {
        this.codiceImpianto = codice;
        this.siglaProvincia = provincia;
        this.indirizzo = indirizzo;
        this.area = area;
        this.uomoInSito = uomoInSito;
        this.tipologia = tipologia;
    }

    public static Impianto findFromCodiceProvincia(int codice, String provincia){
        try (var statement = DAO.getDB().prepareStatement(SQL_CODICE_PROVINCIA)) {
            statement.setInt(1, codice);
            statement.setString(2, provincia);
            ResultSet result = statement.executeQuery();
            result.next();
            return new Impianto(result.getInt(1), result.getString(2), result.getString(3),
                result.getFloat(4), result.getBoolean(5), result.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Impianto findFromMacchinario(Macchinario macchinario){
        try {
            PreparedStatement stmt;
            switch (macchinario.getTipologia()) {
                case 1:
                    stmt = DAO.getDB().prepareStatement(SQL_MACC_FOTO);
                    break;
                case 2:
                    stmt = DAO.getDB().prepareStatement(SQL_MACC_EOLICO);
                    break;
                case 3:
                    stmt = DAO.getDB().prepareStatement(SQL_MACC_EOLICO);
                    break;
                default:
                    return null;
                }
            stmt.setInt(1, macchinario.getCodiceInstallazione());
            ResultSet result = stmt.executeQuery();
            result.next();
            return new Impianto(result.getInt(1), result.getString(2), result.getString(3),
                result.getFloat(4), result.getBoolean(5), result.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Getters

    public int getCodice() {
        return codiceImpianto;
    }

    public String getProvincia() {
        return siglaProvincia;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public float getArea() {
        return area;
    }

    public boolean isUomoInSito() {
        return uomoInSito;
    }

    public int getTipologia() {
        return tipologia;
    }
}
