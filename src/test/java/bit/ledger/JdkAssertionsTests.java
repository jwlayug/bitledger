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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

/**
 * Test ensuring that JDK assertions are enabled.
 *
 * @author Chris Beams
 */
public class JdkAssertionsTests {

    @Test
    public void viaDesiredAssertionStatus() {
        assertAssertionsEnabled(this.getClass().desiredAssertionStatus());
    }

    @Test
    @SuppressWarnings({ "ConstantConditions", "AssertWithSideEffects", "UnusedAssignment" })
    public void viaActualAssert() {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        assertAssertionsEnabled(assertionsEnabled);
    }

    private void assertAssertionsEnabled(boolean assertionsEnabled) {
        assertThat("Please enable assertions with -ea VM flag", assertionsEnabled, is(true));
    }
}
