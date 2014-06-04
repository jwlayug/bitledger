package spc;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class SimpleAddress implements Address {

    private final KeyHash keyHash;

    private SimpleAddress(KeyHash keyHash) {
        this.keyHash = keyHash;
    }

    @Override
    public String getValue() {
        return keyHash.getValue();
    }

    public static SimpleAddress of(KeyHash keyHash) {
        return new SimpleAddress(keyHash);
    }
}
