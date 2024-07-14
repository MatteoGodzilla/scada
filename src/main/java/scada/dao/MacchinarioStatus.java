package scada.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//this class is only used to get
public class MacchinarioStatus {
    private static String SQL_FROM_CODE = """
        SELECT Descrizione FROM MACC_STATUS
        WHERE tipo = ?;
    """;

    private MacchinarioStatus(){}

    private static Map<Integer, String> cache = new HashMap<>();

    public static String fromCode(int code){
        if(cache.containsKey(code)){
            return cache.get(code);
        } else {
            try (var stmt = DAO.getDB().prepareStatement(SQL_FROM_CODE)) {
                stmt.setInt(1, code);
                var result = stmt.executeQuery();
                result.next();
                String value = result.getString(1);
                cache.put(code, value);
                return value;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
