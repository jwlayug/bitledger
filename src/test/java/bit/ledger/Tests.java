/*
 * Copyright 2014 Bitledger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bit.ledger;

import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertThat;

/**
 * @author Chris Beams
 */
public class Tests {

    @Test
    public void test() {

        final AtomicInteger txid = new AtomicInteger(0);
        final Address bobAddress;
        final Address aliceAddress;
        final Wallet bobWallet;
        final Wallet aliceWallet;

        Ledger ledger = new InMemoryLedger();

        {
            // Alice creates a keypair
            KeyPair keyPair = SimpleDSAKeyPairGenerator.generate();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Alice creates a public key hash using SHA256(pubkey)
            KeyHash pubKeyHash = SHA256KeyHash.of(publicKey);

            aliceAddress = SimpleAddress.of(pubKeyHash);
        }

        {
            // Alice is the recipient of a generation (coinbase) transaction
            Transaction coinbaseTx = Transaction.of(
                    txid.getAndIncrement(),
                    Collections.emptyList(),
                    Arrays.asList(TransactionOutput.of(10.00, aliceAddress.getPubKeyHash())));

            ledger.add(coinbaseTx);
        }

        {
            // Bob creates a public/private keypair
            KeyPair keyPair = SimpleDSAKeyPairGenerator.generate();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Bob creates a public key hash using SHA256(pubkey)
            KeyHash pubKeyHash = SHA256KeyHash.of(publicKey);

            bobAddress = SimpleAddress.of(pubKeyHash);
        }

        // ...Bob shares the address with Alice...

        {
            // And alice pays Bob the 1 SPC she owes him:

            // Alice creates a transaction input that spends 1.00 SPC to Bob's address
            TransactionInput fromAlice = TransactionInput.of(0, 0);

            // Alice creates a transaction output that spends 1.00 SPC to Bob's address
            TransactionOutput toBob = TransactionOutput.of(1.00, bobAddress.getPubKeyHash());

            // Alice creates a transaction output that spends 8.99 SPC to Alice's change address
            TransactionOutput toAlice = TransactionOutput.of(8.99, aliceAddress.getPubKeyHash());

            // Alice creates a transaction with the given inputs and outputs
            Transaction tx = Transaction.of(
                    txid.getAndIncrement(),
                    Arrays.asList(fromAlice),
                    Arrays.asList(toBob, toAlice));

            ledger.add(tx);
        }

        System.out.println(ledger);
    }
}
