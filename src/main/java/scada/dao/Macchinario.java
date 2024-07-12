package scada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Macchinario {
    private int codiceInstallazione;
    private String dataInstallazione;
    private int tipologia;
    private String azienda;
    private String nomeModello;
    private int durataGaranzia;
    private int status; //enum?

    private static String SQL_INSTALLAZIONE = """
        SELECT * FROM MACCHINARIO
        WHERE codiceInstallazione = ?;
    """;


    private Macchinario(int codice, String dataInstallazione, int tipologia, String azienda, String nomeModello, int garanzia, int status) {
        this.codiceInstallazione = codice;
        this.dataInstallazione = dataInstallazione;
        this.tipologia = tipologia;
        this.azienda = azienda;
        this.nomeModello = nomeModello;
        this.durataGaranzia = garanzia;
        this.status = status;
    }

    public static Macchinario findFromInstCode(int codiceInstallazione){
        try (var stmt = DAO.getDB().prepareStatement(SQL_INSTALLAZIONE)) {
            stmt.setInt(1, codiceInstallazione);
            ResultSet result = stmt.executeQuery();
            result.next();
            return new Macchinario(result.getInt(1), result.getString(2), result.getInt(3), result.getString(4),
                result.getString(5), result.getInt(6), result.getInt(7));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //getters

    public int getCodiceInstallazione() {
        return codiceInstallazione;
    }

    public String getDataInstallazione() {
        return dataInstallazione;
    }

    public int getTipologia() {
        return tipologia;
    }

    public String getAzienda() {
        return azienda;
    }

    public String getNomeModello() {
        return nomeModello;
    }

    public int getDurataGaranzia() {
        return durataGaranzia;
    }

    public int getStatus() {
        return status;
    }



}
