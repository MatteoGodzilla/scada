package scada.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DAO {
    private static Connection dbConnection = null;

    public static void connectToDB() throws SQLException, ClassNotFoundException{
        //forces the loading of the mysql driver, so it is registered by the time we create the connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        Dotenv dotenv = Dotenv.load();
        String address = "jdbc:mysql://" + dotenv.get("DB_ADDRESS") + ":" + dotenv.get("DB_PORT");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        dbConnection = DriverManager.getConnection(address, username, password);

        PreparedStatement statement = dbConnection.prepareStatement("USE SCADA;");
        System.out.println(statement);
        statement.execute();
    }

    public static Connection getDB(){
        return dbConnection;
    }
}
