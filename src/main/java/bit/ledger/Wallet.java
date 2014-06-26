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

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final Ledger ledger;
    private final List<Recipient> recipients = new ArrayList<>();

    public Wallet(Ledger ledger) {
        this.ledger = ledger;
    }

    public void add(Recipient recipient) {
        recipients.add(recipient);
    }

    public double balance() {
        return recipients.stream().mapToDouble(ledger::balance).sum();
    }
}
