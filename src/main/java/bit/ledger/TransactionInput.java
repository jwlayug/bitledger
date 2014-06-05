package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TransactionInput {

    private final int txid;
    private final int outputIndex;

    private TransactionInput(int txid, int outputIndex) {
        this.txid = txid;
        this.outputIndex = outputIndex;
    }

    public static TransactionInput of(int txid, int outputIndex) {
        return new TransactionInput(txid, outputIndex);
    }
}
