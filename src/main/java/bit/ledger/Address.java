package bit.ledger;

public interface Address {

    String getValue();

    KeyHash getPubKeyHash();
}
