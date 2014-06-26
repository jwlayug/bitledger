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

import org.zeromq.ZMQ;

import java.time.Instant;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Experiments with ZeroMQ based on reading through the guide at http://zguide.zeromq.org.
 */
public class ZmqExperiments {

    public static void main(String... args) throws InterruptedException {

        for (String address : new String[] {
                "inproc://endpoint",
                "ipc://endpoint",
                "tcp://127.0.0.1:2121",
            }) {
            Instant start = Instant.now();
            sendAndReceive(address);
            Instant finish = Instant.now();
            System.out.printf("%d: %s\n", finish.toEpochMilli() - start.toEpochMilli(), address);
        }
    }

    private static void sendAndReceive(String address) throws InterruptedException {

        byte[] msg = new byte[] { 1, 2, 3 };
        byte[] end = new byte[] { 'e', 'n', 'd' };

        ZMQ.Context context = ZMQ.context(1);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.execute(() -> {
                ZMQ.Socket server = context.socket(ZMQ.REP);
                server.bind(address);
                byte[] recMsg;
                do {
                    recMsg = server.recv(0);
                    server.send("");
                } while (!Arrays.equals(end, recMsg));
                server.close();
            });

        executor.schedule(() -> {
                ZMQ.Socket client = context.socket(ZMQ.REQ);
                client.connect(address);
                for (int i = 1; i <= 100_000; i++) {
                    client.send(msg, 0);
                    client.recv();
                }
                client.send(end, 0);
                client.recv();
                client.close();
            },
            address.startsWith("inproc") ? 100 : 0, TimeUnit.MILLISECONDS);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        context.term();
    }
}
