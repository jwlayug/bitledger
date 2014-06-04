package spc;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class BasicAddress implements Address {

    private final KeyHash keyHash;

    private BasicAddress(KeyHash keyHash) {
        this.keyHash = keyHash;
    }

    @Override
    public String getValue() {
        return keyHash.getValue();
    }

    public static BasicAddress of(KeyHash keyHash) {
        return new BasicAddress(keyHash);
    }
}
