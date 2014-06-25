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

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class NodeTests {

    public static void main(String[] args) {
        //new NodeTests().statusLifecycle();
    }

    @Test
    public void lifecycle() {
        //InputStream input = new ByteArrayInputStream("OHAI".getBytes());
        InputStream input = System.in;

        Node node = new InputStreamNode(input);
        assertThat(node.getStatus(), is(Node.Status.NEW));

        node.start();
        assertThat(node.getStatus(), is(Node.Status.STARTED));

        node.stop();
        assertThat(node.getStatus(), is(Node.Status.STOPPED));
    }
}


interface Node {

    public enum Status { NEW, STARTED, STOPPED, CRASHED }

    void start();

    void stop();

    boolean receive();

    Status getStatus();
}


abstract class AbstractNode implements Node {


    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    private Status status = Status.NEW;
    private boolean canRecieve = true;

    protected void onStart() {
        System.out.println("AbstractNode.onStart");
    }

    protected void onStop() {
        System.out.println("AbstractNode.onStop");
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public void start() {
        System.out.println("AbstractNode.start");

        status = Status.STARTED;

        onStart();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        //Runtime.getRuntime().addShutdownHook(new Thread(() -> { System.out.println("shut. it. down."); }));

        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted() && canRecieve) {
                canRecieve = receive();
            }
        });
    }

    @Override
    public void stop() {
        switch (status) {
            case STOPPED:
                return; // nothing to do here.
            default:
                break; // otherwise, let's proceed.
        }

        System.out.println("AbstractNode.stop");

        onStop();

        // interrupt any threads started by this executor
        executor.shutdownNow();

        try {
            boolean result = executor.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println("result = " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        status = Status.STOPPED;
    }
}


/**
 * A {@link Node} implementation that accepts its messages via an {@link InputStream}, such as {@code System.in}
 */
class InputStreamNode extends AbstractNode {

    private final InputStream input;

    public InputStreamNode(InputStream input) {
        this.input = input;
    }

    @Override
    public boolean receive() {
        System.out.println("InputStreamNode.receive");
        try {
            System.out.println("enter a message:");
            input.read();
            return true;
        } catch (IOException e) {
            System.out.println("read interrupted due to " + e.getMessage());
            assert "Stream closed".equals(e.getMessage()) : "Unexpected IOException message: " + e.getMessage();
            return false;
        }
        /*
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Override
    protected void onStop() {
        System.out.println("InputStreamNode.onStop");
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


/**
 * A {@link Node} implementation that accepts its messages via a ZeroMQ
 * {@link ZMQ.Socket Socket}.
 */
/*
@ToString(of = "address")
class ZeroMQSocketNode implements Node {

    private final ZMQ.Context context = ZMQ.context(1);
    private final ZMQ.Socket server = context.socket(ZMQ.REP);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final String address;
    private final List<ZMQ.Socket> peers = new ArrayList<>();

    public ZeroMQSocketNode(String address) {
        this.address = address;
    }

    public void start() {
        server.bind(address);
        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String msg = server.recvStr();
                System.out.println("msg = " + msg);
                server.send("ACK");
            }
        });
    }

    public void stop() {
        peers.parallelStream().forEach(ZMQ.Socket::close);
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        server.close();
        context.close();
    }

    @Override
    public void receive() {

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
*/
