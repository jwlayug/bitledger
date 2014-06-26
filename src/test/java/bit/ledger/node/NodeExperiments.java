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
import org.zeromq.ZMQException;

import zmq.ZError;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeExperiments {

    public static void main(String... args) throws Exception {
        withReceivingZmqSocketAndShutdownHook();
    }

    private static void withReceivingZmqSocketAndShutdownHook() throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        ExecutorService executor = Executors.newFixedThreadPool(1);

        socket.bind("inproc://foo");
        executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String msg = socket.recvStr();
                        System.out.println("msg = " + msg);
                    } catch (ZMQException e) {
                        if (e.getErrorCode() == ZMQ.Error.ETERM.getCode()) {
                            System.out.println("context shutdown. as expected.");
                            //e.printStackTrace(System.err);
                            break;
                        } else {
                            e.printStackTrace(System.err);
                        }
                    }
                }
                System.out.println(Thread.currentThread() + " was interrupted. Exit.");
            });

        Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("calling socket.close()");
                    socket.close();
                    System.out.println("calling context.close()");
                    context.close();

                    System.out.println("calling shutdownNow");
                    List<Runnable> incomplete = executor.shutdownNow();
                    System.out.println(incomplete.size() + " runnables are incomplete");
                    System.out.println("calling awaitTermination");
                    boolean result = false;
                    try {
                        result = executor.awaitTermination(2, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("result = " + result);
                }
            });

        System.out.println("Everything is running. Press ctrl-c at any time stop.");
    }

    private static void withReceivingZmqSocket() throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        ExecutorService executor = Executors.newFixedThreadPool(1);

        socket.bind("inproc://foo");
        executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String msg = socket.recvStr();
                        System.out.println("msg = " + msg);
                    } catch (ZMQException e) {
                        if (e.getErrorCode() == ZError.ETERM) {
                            System.out.println("context shutdown. as expected.");
                            //e.printStackTrace(System.err);
                            break;
                        } else {
                            e.printStackTrace(System.err);
                        }
                    }
                }
                System.out.println(Thread.currentThread() + " was interrupted. Exit.");
            });

        // give the task above a moment to establish the blocking recv call
        TimeUnit.SECONDS.sleep(1);

        System.out.println("calling socket.close()");
        socket.close();
        System.out.println("calling context.close()");
        context.close();

        System.out.println("calling shutdownNow");
        List<Runnable> incomplete = executor.shutdownNow();
        System.out.println(incomplete.size() + " runnables are incomplete");
        System.out.println("calling awaitTermination");
        boolean result = executor.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("result = " + result);
    }

    public static void withBlockingCall() throws InterruptedException, IOException {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        System.out.println("Enter a number: ");
                        int n = System.in.read();
                        System.out.println("you entered: " + n);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(Thread.currentThread() + " was interrupted. Exit.");
            });

        // give the task above a moment to prompt us at least once.
        TimeUnit.SECONDS.sleep(1);

        System.out.println("calling shutdownNow");
        List<Runnable> incomplete = executor.shutdownNow();
        System.out.println(incomplete.size() + " runnables are incomplete");
        System.out.println("calling awaitTermination");
        boolean result = executor.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("result = " + result);

        // Note: I expected that the process would exit at this point, but it did not.
        // the read() call continues to keep the JVM running. It would only exit upon my
        // entering a number and pressing return. Solution: close the input stream.

        System.in.close();
    }

    public static void withSleep() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread());
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                System.out.println(Thread.currentThread() + " was interrupted. Exit.");
            });

        TimeUnit.MILLISECONDS.sleep(1000);

        executor.shutdownNow();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
    }

    public static void main2(String... args) {
        ZNode bobNode = new ZNode("ipc://bob");
        bobNode.start();

        ZNode aliceNode = new ZNode("ipc://alice");
        aliceNode.start();

        System.out.println(bobNode);
        System.out.println(aliceNode);

        aliceNode.addPeer(bobNode.getAddress());

        bobNode.stop();
        aliceNode.stop();
    }
}

@ToString(of = "address")
class ZNode {

    private final ZMQ.Context context = ZMQ.context(1);
    private final ZMQ.Socket server = context.socket(ZMQ.REP);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final String address;
    private final List<ZMQ.Socket> peers = new ArrayList<>();

    public ZNode(String address) {
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
