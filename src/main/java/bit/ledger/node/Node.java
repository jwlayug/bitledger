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

import org.zeromq.ZMQ;

import zmq.ZError;

import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedSelectorException;

import java.util.ArrayList;
import java.util.List;

public class Node implements Runnable {

    private final ZMQ.Context context;
    private final ZMQ.Socket server;
    //private final List<ZMQ.Socket> peers = new ArrayList<>();

    public final String address;
    public final List<String> items = new ArrayList<>();

    public Node(String address) {
        this.address = address;
        this.context = ZMQ.context(1);
        this.server = context.socket(ZMQ.REP);
    }

    @Override
    public void run() {
        System.out.println("Node.start");

        System.out.println("Node1: binding to " + address);
        server.bind(address);

        System.out.println("Node1: registering shutdown hook");
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Node1: about to receive");
            receive();
            System.out.println("Node1: called receive");
        }
        System.out.println("thread interrupted or otherwise cannot receive.");
    }

    private void receive() {
        try {
            System.out.println("Node1: about to recvStr");
            String msg = server.recvStr();
            System.out.println("Node1: about to send ACK");
            server.send("ACK");
            System.out.println("Node1: sent ACK");
            items.add(msg);
            System.out.println("Node1: added msg to items");
        } catch (ZError.IOException ex) {
            if (ex.getCause() instanceof ClosedByInterruptException) {
                // expected.

                System.out.println("DEBUG: receive interrupted. Exiting.");
                return;
            }
            // something unexpected happened.
            throw new RuntimeException(ex);
        }
    }

    /*
    public void addPeer(String address) {
        ZMQ.Socket peer = context.socket(ZMQ.REQ);
        peers.add(peer);
        System.out.println("before");
        peer.connect(address);
        System.out.println("after");
        //peer.send("SYN1");
        //System.out.println(Arrays.toString(peer.recv()));
        //peer.send("SYN2");
        //System.out.println(Arrays.toString(peer.recv()));
    }
    */

    private void shutdown() {
        System.out.println("Node.shutdown");

        //peers.stream().forEach(ZMQ.Socket::close);
        server.close();
        //context.close();
    }
}
