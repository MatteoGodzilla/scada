package scada.tools;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class GeneratePassword {
    public static void main(String[] args) {
        generatePassword();
    }

    static void generatePassword(){
        System.out.println("Inserisci password");
        String text = System.console().readLine();
        byte[] hash = BCrypt.withDefaults().hash(12, text.getBytes());
        System.out.println(new String(hash));
    }

    static void testPassword(){
        String hash = "<insert hash here>";
        String text = System.console().readLine();
        Result r = BCrypt.verifyer().verify(text.toCharArray(), hash);
        if(r.verified){
            System.out.println("Good job");
        }
    }
}
