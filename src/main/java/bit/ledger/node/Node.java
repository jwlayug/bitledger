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

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.List;

import static org.zeromq.ZMQ.Socket;

import static org.zeromq.ZThread.IDetachedRunnable;

public class Node implements IDetachedRunnable {

    private final ZContext context = new ZContext();
    private final Socket socket = context.createSocket(ZMQ.REP);
    public final List<String> items = new ArrayList<>();
    public final String address;

    public Node(String address) {
        this.address = address;
    }

    @Override
    public void run(Object... args) {
        System.out.println("Node.start");

        System.out.println("Node1: binding to " + address);
        socket.bind(address);

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Node1: about to receive");
            receive();
            System.out.println("Node1: called receive");
        }
        System.out.println("thread interrupted or otherwise cannot receive.");
        context.destroy();
    }

    private void receive() {
        System.out.println("about to recvMsg");
        ZMsg msg = ZMsg.recvMsg(socket);
        System.out.println("after recvMsg: " + msg);
        items.add(msg.popString());
        ZMsg ack = ZMsg.newStringMsg("ACK");
        ack.send(socket);
    }
}
