package scada.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import scada.dao.DAO;

public class GenerateCompModello {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DAO.connectToDB();
        var statement = DAO.getDB().prepareStatement("SELECT azienda,codice FROM COMPONENTE;");
        var result = statement.executeQuery();
        //dump componente
        List<Pair<String, String>> comp = new ArrayList<>();
        while(result.next()){
            comp.add(new Pair<String,String>(result.getString(1), result.getString(2)));
        }
        //dump modello
        statement = DAO.getDB().prepareStatement("SELECT azienda, nome FROM MODELLO;");
        result = statement.executeQuery();
        List<Pair<String, String>> modello = new ArrayList<>();
        while(result.next()){
            modello.add(new Pair<String,String>(result.getString(1), result.getString(2)));
        }
        System.out.println(comp.size());
        System.out.println(modello.size());

        int rounds = 30;
        int COMP_PER_MODELLO = 4;
        Random r = new Random();
        boolean allgood = true;
        while(rounds > 0){
            int choiceModello = r.nextInt(modello.size());
            var chosenModello = modello.get(choiceModello);
            modello.remove(choiceModello);

            Set<Pair<String,String>> compPerModello = new HashSet<>();
            for (int i = 0; i < COMP_PER_MODELLO; i++){
                int choiceComp = r.nextInt(comp.size());
                compPerModello.add(comp.get(choiceComp));
            }

            for (var chosenComp : compPerModello){
                var stmt = DAO.getDB().prepareStatement("INSERT INTO COMP_MODELLO VALUES (?,?,?,?);");
                stmt.setString(1, chosenComp.left);
                stmt.setString(2, chosenComp.right);
                stmt.setString(3, chosenModello.left);
                stmt.setString(4, chosenModello.right);
                allgood = allgood && stmt.executeUpdate() > 0;
            }

            rounds--;
        }
        if(allgood)
            DAO.getDB().commit();
    }
}
