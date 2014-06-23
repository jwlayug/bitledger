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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chris Beams
 * @since 0.1.0
 */
@ToString
@EqualsAndHashCode
public class InMemoryLedger implements Ledger {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void add(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public double total() {
        // count up all the coinbase / generation transactions
        return transactions.stream()
                .filter(tx -> tx.getFrom() == null)
                .collect(Collectors.summingDouble(Transaction::getAmount));
    }

    @Override
    public double balance(Address address) {
        return transactions.stream()
                .filter(tx -> tx.getTo().equals(address))
                .collect(Collectors.summingDouble(Transaction::getAmount)) -

                transactions.stream()
                        .filter(tx -> tx.getFrom() != null && tx.getFrom().equals(address))
                        .collect(Collectors.summingDouble(Transaction::getAmount));
    }
}
