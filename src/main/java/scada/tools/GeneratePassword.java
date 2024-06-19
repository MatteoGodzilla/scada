package scada.tools;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class GeneratePassword {
    public static void main(String[] args) {
        String text = System.console().readLine();
        System.out.println("Reading " + text);
        byte[] hash = BCrypt.withDefaults().hash(12, text.getBytes());
        System.out.println(new String(hash));

        // Check
        // String hash = "<insert hash here>";
        // String text = System.console().readLine();
        // Result r = BCrypt.verifyer().verify(text.toCharArray(), hash);
        // if(r.verified){
        //     System.out.println("Good job");
        // }
    }
}
