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

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Node} implementation that accepts its messages via an {@link BlockingQueue}.
 */
public class BlockingQueueNode extends AbstractNode {

    private final BlockingQueue<String> queue;
    private final List<String> items = new ArrayList<>();

    public BlockingQueueNode(BlockingQueue<String> queue) {
        super();
        this.queue = queue;
    }

    public BlockingQueueNode(BlockingQueue<String> queue, ExecutorService executor) {
        super(executor);
        this.queue = queue;
    }

    @Override
    public boolean receive() throws InterruptedException {
        System.out.println("BlockingQueueNode.receive");
        try {
            System.out.println("calling blocking take()");
            String item = queue.take();
            items.add(item);
            System.out.println("take() returned with: " + item);
            return true;
        } catch (InterruptedException e) {
            throw e;
        }
    }
}
