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
                .filter(tx -> tx.getInputs().isEmpty())
                .collect(Collectors.summingDouble(Transaction::sumOutputs));
    }

    public List<TransactionOutput> unspent(Recipient recipient) {
        if (recipient == null) {
            throw new IllegalArgumentException("recipient must not be null");
        }

        List<TransactionOutput> unspent = new ArrayList<>();
        List<TransactionInput> allInputs = new ArrayList<>();

        for (Transaction tx : transactions) {
            allInputs.addAll(tx.getInputs());
        }

        for (Transaction tx : transactions) {
            List<TransactionOutput> outputs = tx.getOutputs();
            for (int outputIndex = 0; outputIndex < outputs.size(); outputIndex++) {
                TransactionOutput output = outputs.get(outputIndex);
                if (recipient.equals(output.getRecipient()) &&
                        !allInputs.contains(TransactionInput.of(tx.getId(), outputIndex))) {
                    unspent.add(output);
                }
            }
        }

        return unspent;
    }

    @Override
    public double balance(Recipient recipient) {
        return unspent(recipient).stream().collect(Collectors.summingDouble(TransactionOutput::getAmount));
    }
}
