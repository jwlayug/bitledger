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

/**
 * @author Chris Beams
 * @since 0.1.0
 */
@ToString
@EqualsAndHashCode
public class TransactionOutput {

    private final int txId;
    private final int index;
    private final double amount;
    private final Recipient recipient;

    public TransactionOutput(int txId, int index, double amount, Recipient recipient) {
        this.txId = txId;
        this.index = index;
        this.amount = amount;
        this.recipient = recipient;
    }

    public int getTxId() {
        return txId;
    }

    public int getIndex() {
        return index;
    }

    public double getAmount() {
        return amount;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public static TransactionOutput.Data of(double amount, Recipient recipient) {
        return new TransactionOutput.Data(amount, recipient);
    }


    public static TransactionOutput of(int txId, int index, TransactionOutput.Data data) {
        return new TransactionOutput(txId, index, data.amount, data.recipient);
    }


    public static class Data {
        private final double amount;
        private final Recipient recipient;

        private Data(double amount, Recipient recipient) {
            this.amount = amount;
            this.recipient = recipient;
        }
    }

}
