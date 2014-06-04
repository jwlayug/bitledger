package spc;

public class TransactionOutput {

    private final int amount;
    private final KeyHash pubKeyHash;

    private TransactionOutput(int amount, KeyHash pubKeyHash) {
        this.amount = amount;
        this.pubKeyHash = pubKeyHash;
    }

    public static TransactionOutput of(double amount, KeyHash pubKeyHash) {
        return new TransactionOutput(amount, pubKeyHash);
    }
}
