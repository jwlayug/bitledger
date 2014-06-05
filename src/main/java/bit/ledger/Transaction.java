package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

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

    public static Transaction of(int id, List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        return new Transaction(id, inputs, outputs);
    }
}
