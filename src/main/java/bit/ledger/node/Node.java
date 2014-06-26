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

    private final Network network;
    private final String address;
    private final ZContext context = new ZContext();
    private final Socket replySocket = context.createSocket(ZMQ.REP);
    private final List<String> items = new ArrayList<>();

    public Node(Network network) {
        this.network = network;
        this.address = network.discoverAddress();
    }

    @Override
    public void run(Object... args) {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> context.destroy());

        replySocket.bind(address);
        while (!Thread.currentThread().isInterrupted()) {
            ZMsg reply = handle(ZMsg.recvMsg(replySocket));
            reply.send(replySocket);
        }
        context.destroy();
    }

    private ZMsg handle(ZMsg request) {
        String text = request.popString();
        if (text.startsWith("item:")) {
            items.add(text);
            return ZMsg.newStringMsg("ACK");
        }
        else if (text.equals("count")) {
            return ZMsg.newStringMsg("" + items.size());
        }
        else if (text.startsWith("get:")) {
            String itemId = text.substring("get:".length());
            //J-
            return ZMsg.newStringMsg(items.stream()
                    .filter(item -> item.equals("item:" + itemId))
                    .findFirst()
                    .orElse("error: item " + itemId + " not found!"));
            //J+
        }
        return ZMsg.newStringMsg("unknown request: " + text);
    }

    /**
     * @return a new {@link ZMQ#REQ} socket connected to this node's address, typically
     *         used for testing.
     */
    public Socket createRequestSocket() {
        Socket requestSocket = context.createSocket(ZMQ.REQ);
        requestSocket.connect(address);
        return requestSocket;
    }
}
