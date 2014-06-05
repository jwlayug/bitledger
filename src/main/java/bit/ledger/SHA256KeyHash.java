package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.codec.binary.Hex;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ToString
@EqualsAndHashCode
public class SHA256KeyHash implements KeyHash {

    private static final MessageDigest sha256;

    private final String value;

    static {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private SHA256KeyHash(Key key) {
        this.value = String.valueOf(Hex.encodeHex(sha256.digest(key.getEncoded())));
    }

    @Override
    public String getValue() {
        return value;
    }

    public static KeyHash of(Key key) {
        return new SHA256KeyHash(key);
    }
}
