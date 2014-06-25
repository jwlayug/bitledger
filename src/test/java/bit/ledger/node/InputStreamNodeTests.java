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

package bit.ledger.node;

import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link InputStreamNode}.
 */
public class InputStreamNodeTests {

    @Test
    public void lifecycle() {
        InputStream input = System.in;

        Node node = new InputStreamNode(input);
        assertThat(node.getStatus(), is(Node.Status.NEW));

        node.start();
        assertThat(node.getStatus(), is(Node.Status.STARTED));

        node.stop();
        assertThat(node.getStatus(), is(Node.Status.STOPPED));
    }
}
