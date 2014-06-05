package bit.ledger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SimpleDSAKeyPairGenerator {

    public static KeyPair generate() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = SecureRandom.getInstanceStrong();
            keyGen.initialize(1024, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
