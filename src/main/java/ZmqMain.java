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

import org.zeromq.ZMQ;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZmqMain {

    public static void main(String... args) throws InterruptedException {

        for (String address : new String[]{
                "inproc://endpoint",
                "tcp://127.0.0.1:2121",
                "ipc://endpoint"
        }) {
            sendAndReceive(address);
        }
    }

    private static void sendAndReceive(String address) throws InterruptedException {

        System.out.println("sending and receiving message at: " + address);

        byte[] msg = new byte[]{1, 2, 3};

        ZMQ.Context context = ZMQ.context(1);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.execute(() -> {
            ZMQ.Socket server = context.socket(ZMQ.REP);
            System.out.println("binding server");
            server.bind(address);
            byte[] recMsg = server.recv(0);
            System.out.println(Arrays.equals(msg, recMsg) ? "RECD" : "FAIL");
            server.close();
        });

        executor.schedule(() -> {
            ZMQ.Socket client = context.socket(ZMQ.REQ);
            System.out.println("connecting client");
            client.connect(address);
            client.send(msg, 0);
            client.close();
        }, address.startsWith("inproc") ? 1 : 0, TimeUnit.SECONDS);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        context.term();
    }
}
