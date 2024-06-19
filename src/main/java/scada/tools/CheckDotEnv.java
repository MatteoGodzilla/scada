package scada.tools;

import io.github.cdimascio.dotenv.Dotenv;

public class CheckDotEnv {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.out.println(dotenv.get("DB_ADDRESS"));
        System.out.println(dotenv.get("DB_PORT"));
        System.out.println(dotenv.get("DB_USER"));
        System.out.println(dotenv.get("DB_PASSWORD"));
    }
}
