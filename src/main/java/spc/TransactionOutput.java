package spc;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TransactionOutput {

    private final double amount;
    private final KeyHash pubKeyHash;

    private TransactionOutput(double amount, KeyHash pubKeyHash) {
        this.amount = amount;
        this.pubKeyHash = pubKeyHash;
    }

    public static TransactionOutput of(double amount, KeyHash pubKeyHash) {
        return new TransactionOutput(amount, pubKeyHash);
    }
}
