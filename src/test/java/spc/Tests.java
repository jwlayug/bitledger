package spc;

import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Tests {

    @Test
    public void test() {

        // Bob creates a public/private keypair
        KeyPair keyPair = SimpleDSAKeyPairGenerator.generate();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Bob creates a public key hash using SHA256(pubkey)
        KeyHash pubKeyHash = SHA256KeyHash.of(publicKey);

        Address address = BasicAddress.of(pubKeyHash);

        assertThat(address.getValue(), equalTo(pubKeyHash.getValue()));
    }
}
