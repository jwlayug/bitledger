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
import java.util.stream.Stream;

/**
 * @author Chris Beams
 * @since 0.1.0
 */
@ToString
@EqualsAndHashCode
public class Transaction {

    private final int id;
    private final List<TransactionInput> inputs;
    private final List<TransactionOutput> outputs;

    private Transaction(int id, List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public int getId() {
        return id;
    }

    public Stream<TransactionInput> inputs() {
        return inputs.stream();
    }

    public Stream<TransactionOutput> outputs() {
        return outputs.stream();
    }

    public static Transaction of(int id, List<TransactionInput> inputs, List<TransactionOutput.Data> outputs) {
        return new Transaction(id, inputs, indexed(outputs, id));
    }

    private static List<TransactionOutput> indexed(List<TransactionOutput.Data> outputs, int txId) {
        List<TransactionOutput> indexedOutputs = new ArrayList<>();

        for (int i = 0; i < outputs.size(); i++) {
            indexedOutputs.add(TransactionOutput.of(txId, i, outputs.get(i)));
        }

        return indexedOutputs;
    }
}
