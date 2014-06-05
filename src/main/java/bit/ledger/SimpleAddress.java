package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SimpleAddress extends AbstractAddress {

    private SimpleAddress(KeyHash keyHash) {
        super(keyHash);
    }

    @Override
    public String getValue() {
        return pubKeyHash.getValue();
    }

    public static SimpleAddress of(KeyHash keyHash) {
        return new SimpleAddress(keyHash);
    }
}
