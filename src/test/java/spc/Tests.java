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

        Address address = SimpleAddress.of(pubKeyHash);

        // ...Bob shares the address with Alice...

        // Alice creates a transaction input that spends 1.00 SPC to Bob's address
        TransactionInput fromAlice = TransactionInput.of(0, 0);

        // Alice creates a transaction output that spends 1.00 SPC to Bob's address
        TransactionOutput toBob = TransactionOutput.of(1.00, address.getPubKeyHash());

        // Alice creates a transaction output that spends 8.99 SPC to Alice's change address
        TransactionOutput toAlice = TransactionOutput.of(8.99, address.getPubKeyHash());

        assertThat(address.getValue(), equalTo(pubKeyHash.getValue()));
    }
}
