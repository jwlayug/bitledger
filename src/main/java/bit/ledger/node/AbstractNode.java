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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNode implements Node {

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    private Status status = Status.NEW;
    private boolean canReceive = true;

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
            while (!Thread.currentThread().isInterrupted() && canReceive) {
                canReceive = receive();
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
