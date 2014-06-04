package spc;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class BasicAddress implements Address {

    private final String value;

    private BasicAddress(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static BasicAddress of(String pubKeyHash) {
        return new BasicAddress(pubKeyHash);
    }
}
