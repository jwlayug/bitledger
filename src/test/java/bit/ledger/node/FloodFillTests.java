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

import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

public class FloodFillTests {

    /*

    first node comes online.

    several Items are added to its (ordered) Collection (intentionally being generic here)

    second node comes online.

    second node "discovers" first (is directly supplied with its address), and adds it as a peer.

    second negotiates with first to receive all Items.

    --> wait in loop, asserting for both nodes to have identical information. <--

    add an Item to second.

    second broadcasts new Item to first.

    --> wait in loop, asserting for both nodes to have identical information. <--

     */

    public static void main(String... args) throws InterruptedException {

        //ExecutorService executor = Executors.newFixedThreadPool(2);

        Node node1 = new Node("ipc://node1");
        node1.run();
        //executor.execute(node1);
        /*

        assertThat(node1.items.size(), equalTo(0));

        { // add a couple items
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket socket = context.socket(ZMQ.REQ);
            socket.connect(node1.address);
            socket.send("item:1");
            String response = socket.recvStr();
            System.out.println("response = " + response);
            socket.send("item:2");
            socket.recvStr();
            socket.close();
        }

        assertThat(node1.items.size(), equalTo(2));


        //Node node2 = new Node("ipc://node2");
        //executor.execute(node1);

        //node2.addPeer(node1.getAddress());
        */

        /*
        System.out.println("sleeping for 1 sec after executing node1");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("calling shutdownNow");
        executor.shutdownNow();
        System.out.println("calling awaitTermination");
        executor.awaitTermination(2, TimeUnit.SECONDS);
        */
    }

}
