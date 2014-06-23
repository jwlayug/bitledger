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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Chris Beams
 */
public class Tests {

    @Test
    public void test() {

        // the ledger starts out empty.

        Ledger ledger = new InMemoryLedger();
        assertThat(ledger.total(), equalTo(0d));

        // 100 units of new money is generated and spent to address alice_a1

        Address alice_a1 = Address.of("alice_a1");
        Transaction tx1 = Transaction.of(100, null, alice_a1); // amount, from, to
        ledger.add(tx1);

        assertThat(ledger.balance(alice_a1), equalTo(100d));
        assertThat(ledger.total(), equalTo(100d));

        // 25 of those units are spent from alice_a1 to bob_a1

        Address bob_a1 = Address.of("bob_a1");
        Transaction tx2 = Transaction.of(25, alice_a1, bob_a1);
        ledger.add(tx2);

        // crawl the ledger and sum the value of all transactions for alice_a1

        assertThat(ledger.balance(alice_a1), equalTo(75d));
        assertThat(ledger.balance(bob_a1), equalTo(25d));

        // total amount in the ledger is still 100 units
        assertThat(ledger.total(), equalTo(100d));
    }
}
