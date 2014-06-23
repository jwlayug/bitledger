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

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Chris Beams
 */
public class Tests {

    @Test
    public void test() {

        AtomicInteger txid = new AtomicInteger(0);

        // the ledger starts out empty.

        Ledger ledger = new InMemoryLedger();
        assertThat(ledger.total(), equalTo(0d));

        Wallet alice = new Wallet(ledger);
        Wallet bob = new Wallet(ledger);


        // 100 units of new money is generated and spent to address alice_a1

        Recipient alice_1 = Recipient.of("alice_1");
        Recipient alice_2 = Recipient.of("alice_2"); // use this for a change address below
        alice.add(alice_1);
        alice.add(alice_2);

        Transaction tx1 = Transaction.of(
                txid.incrementAndGet(),
                Collections.emptyList(),
                Arrays.asList(
                        TransactionOutput.of(100, alice_1)
                ));
        ledger.add(tx1);

        assertThat(ledger.balance(alice_1), equalTo(100d));
        assertThat(ledger.total(), equalTo(100d));

        // 25 of those units are spent from alice_a1 to bob_1

        Recipient bob_1 = Recipient.of("bob_1");
        bob.add(bob_1);

        Transaction tx2 = Transaction.of(
                txid.incrementAndGet(),
                Arrays.asList(TransactionInput.of(tx1.getId(), 0)), // spend the original coinbase tx output
                Arrays.asList(
                        TransactionOutput.of(25d, bob_1),           // pay bob 25 units
                        TransactionOutput.of(75d, alice_2)          // spend remainder to change address
                )
        );
        ledger.add(tx2);

        // crawl the ledger and sum the value of all transactions

        assertThat(ledger.balance(alice_1), equalTo(0d));
        assertThat(ledger.balance(alice_2), equalTo(75d));
        assertThat(ledger.balance(bob_1), equalTo(25d));

        // total amount in the ledger is still 100 units
        assertThat(ledger.total(), equalTo(100d));

        assertThat(alice.balance(), equalTo(75d));
        assertThat(bob.balance(), equalTo(25d));
    }
}
