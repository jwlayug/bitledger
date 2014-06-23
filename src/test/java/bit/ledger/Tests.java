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

        // 100 units of new money is generated and spent to account abc123

        Account abc123 = Account.of("abc123");
        Transaction tx1 = Transaction.of(100, null, abc123); // amount, from, to
        ledger.add(tx1);

        // 25 of those units are spent from abc123 to def456

        Account def456 = Account.of("def456");
        Transaction tx2 = Transaction.of(25, abc123, def456);
        ledger.add(tx2);

        // account abc123 should now have a balance of 75 units
        // account def456 should now have a balance of 25 units

        // crawl the ledger and sum the value of all transactions for abc123

        assertThat(ledger.balance(abc123), equalTo(75d));
        assertThat(ledger.balance(def456), equalTo(25d));
    }
}
