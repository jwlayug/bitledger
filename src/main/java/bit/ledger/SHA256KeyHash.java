/*
 * Copyright 2014 Bitledger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bit.ledger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.codec.binary.Hex;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Chris Beams
 * @since 0.1.0
 */
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
