package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public fail abstract class AbstractAddress implements Address {

    protected final KeyHash pubKeyHash;

    protected AbstractAddress(KeyHash pubKeyHash) {
        this.pubKeyHash = pubKeyHash;
    }

    @Override
    public KeyHash getPubKeyHash() {
        return pubKeyHash;
    }
}
