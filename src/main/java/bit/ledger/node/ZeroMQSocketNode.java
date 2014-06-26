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

import lombok.ToString;

import org.zeromq.ZMQ;

import java.nio.channels.ClosedSelectorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * A {@link Node} implementation that accepts its messages via a ZeroMQ {@link
 * org.zeromq.ZMQ.Socket Socket}.
 */
@ToString(of = "address")
public class ZeroMQSocketNode extends AbstractNode {

    private final ZMQ.Context context;
    private final ZMQ.Socket server;
    private final String address;
    private final List<ZMQ.Socket> peers = new ArrayList<>();
    private final List<String> items = new ArrayList<>();

    private boolean sharedContext = true;

    public ZeroMQSocketNode(String address) {
        this(address, ZMQ.context(1));
        sharedContext = false;
    }

    public ZeroMQSocketNode(String address, ZMQ.Context context) {
        this.address = address;
        this.context = context;
        this.server = context.socket(ZMQ.REP);
    }

    @Override
    public void onStart() {
        server.bind(address);
    }

    @Override
    public void onStop() {
        peers.stream().forEach(ZMQ.Socket::close);
        server.close();
        if (!sharedContext)
            context.close();
    }

    @Override
    public boolean receive() {
        try {
            String msg = server.recvStr();
            server.send("ACK");
            onReceive(msg);
            return true;
        } catch (ClosedSelectorException ex) {
            // expected
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        return false;
    }

    private void onReceive(String msg) {
        items.add(msg);
    }

    public List<String> getItems() {
        return items;
    }

    public String getAddress() {
        return address;
    }

    public void addPeer(String address) {
        ZMQ.Socket peer = context.socket(ZMQ.REQ);
        peers.add(peer);
        peer.connect(address);
        peer.send("SYN1");
        System.out.println(Arrays.toString(peer.recv()));
        peer.send("SYN2");
        System.out.println(Arrays.toString(peer.recv()));
    }
}
