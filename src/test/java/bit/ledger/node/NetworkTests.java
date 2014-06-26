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

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

public class NetworkTests {

    @Test
    public void discoverAddress() {
        Network network = new Network();
        String address1 = network.discoverAddress();
        String address2 = network.discoverAddress();
        assertThat(address1, startsWith("ipc://node"));
        assertThat(address2, startsWith("ipc://node"));
        assertThat(address1, not(equalTo(address2)));
    }

    @Test
    public void discoverPeers() {
        Network network = new Network();

        List<String> peersAtT1 = network.discoverPeers();
        assertThat(peersAtT1.size(), equalTo(0));

        String peerAddress1 = network.discoverAddress();

        List<String> peersAtT2 = network.discoverPeers();
        assertThat(peersAtT2.size(), equalTo(1));
        assertThat(peersAtT2.contains(peerAddress1), is(true));

        String peerAddress2 = network.discoverAddress();

        List<String> peersAtT3 = network.discoverPeers();
        assertThat(peersAtT3.size(), equalTo(2));
        assertThat(peersAtT3.contains(peerAddress1), is(true));
        assertThat(peersAtT3.contains(peerAddress2), is(true));
    }
}
