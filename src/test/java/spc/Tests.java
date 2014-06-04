package spc;

import org.apache.commons.codec.binary.Hex;

import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Tests {

    @Test
    public void test() throws NoSuchAlgorithmException {

        // Bob creates a public/private keypair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = SecureRandom.getInstanceStrong();
        keyGen.initialize(1024, random);

        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Bob creates a public key hash using SHA256(pubkey)
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        String pubKeyHash = String.valueOf(Hex.encodeHex(sha256.digest(publicKey.getEncoded())));
        System.out.println("pubKeyHash = " + pubKeyHash);

        Address address = BasicAddress.of(pubKeyHash);

        assertThat(address.getValue(), equalTo(pubKeyHash));
    }
}
