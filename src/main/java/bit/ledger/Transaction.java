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

import java.util.List;

/**
 * @author Chris Beams
 * @since 0.1.0
 */
@ToString
@EqualsAndHashCode
public class Transaction {

    private final double amount;
    private final Account from;
    private final Account to;

    private Transaction(double amount, Account from, Account to) {
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public static Transaction of(double amount, Account from, Account to) {
        return new Transaction(amount, from, to);
    }
}
