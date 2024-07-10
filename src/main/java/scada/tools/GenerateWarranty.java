package scada.tools;

import java.sql.SQLException;
import java.util.*;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import scada.dao.DAO;

public class GenerateWarranty {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DAO.connectToDB();

        var statement = DAO.getDB().prepareStatement("SELECT azienda, nome FROM MODELLO;");
        var response = statement.executeQuery();

        List<Pair<String,String>> res = new ArrayList<>();

        while(response.next()){
            res.add(new Pair<String,String>(response.getString(1), response.getString(2)));
        }

        Random r = new Random();
        boolean allGood = true;
        for (Pair<String,String> pair : res) {
            int anni = 2;
            String descrizione = "Garanzia di 2 anni";
            if(r.nextFloat() < 0.1){
                anni = 5;
                descrizione = "Garanzia estesa di 5 anni";
            }

            var stmt = DAO.getDB().prepareStatement("INSERT INTO GARANZIA VALUE (?,?,?,?,?);");
            stmt.setString(1, pair.left);
            stmt.setString(2, pair.right);
            stmt.setInt(3, anni);
            stmt.setInt(4,r.nextInt(1000, 2500)*100);
            stmt.setString(5, descrizione);
            int update = stmt.executeUpdate();
            allGood = allGood && update > 1;
        }
        DAO.getDB().commit();
    }
}
