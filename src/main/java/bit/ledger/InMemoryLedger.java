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
import java.util.stream.Stream;

/**
 * @author Chris Beams
 * @since 0.1.0
 */
@ToString
@EqualsAndHashCode
public class InMemoryLedger implements Ledger {

    private final List<Transaction> transactions = new ArrayList<>();

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(validated(transaction));
    }

    @Override
    public double total() {
        return unspentOutputs().mapToDouble(TransactionOutput::getAmount).sum();
    }

    private Stream<TransactionOutput> outputs() {
        return transactions().flatMap(Transaction::outputs);
    }

    private Stream<TransactionOutput> unspentOutputs() {
        return outputs().filter(this::isUnspent);
    }

    public Stream<TransactionOutput> unspentOutputs(Recipient recipient) {
        return unspentOutputs().filter(output -> output.getRecipient().equals(recipient));
    }

    @Override
    public double balance(Recipient recipient) {
        return unspentOutputs(recipient).mapToDouble(TransactionOutput::getAmount).sum();
    }

    private Transaction validated(Transaction transaction) {
        if (transaction.inputs().anyMatch(this::isSpent)) {
            throw new InvalidTransactionException("double spend");
        }
        return transaction;
    }

    private boolean isSpent(TransactionInput input) {
        return transactions().flatMap(Transaction::inputs).anyMatch(input::equals);
    }

    private boolean isUnspent(TransactionOutput output) {
        return !isSpent(TransactionInput.of(output.getTxId(), output.getIndex()));
    }
}
