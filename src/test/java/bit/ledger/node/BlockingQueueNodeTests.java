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

import com.google.common.util.concurrent.MoreExecutors;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BlockingQueueNode}.
 */
public class BlockingQueueNodeTests {

    @Test
    public void lifecycle() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

        Node node = new BlockingQueueNode(queue);
        assertThat(node.getStatus(), is(Node.Status.NEW));

        node.start();
        assertThat(node.getStatus(), is(Node.Status.STARTED));

        node.stop();
        assertThat(node.getStatus(), is(Node.Status.STOPPED));
    }

    @Ignore
    @Test
    public void putAndTake() throws IOException, InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        ExecutorService executor = MoreExecutors.sameThreadExecutor();

        Node node = new BlockingQueueNode(queue, executor);
        assertThat(node.getStatus(), is(Node.Status.NEW));

        System.out.println("calling put()");
        queue.put("SYN");
        System.out.println("put call returned");

        node.start();
        assertThat(node.getStatus(), is(Node.Status.STARTED));

        node.stop();
        assertThat(node.getStatus(), is(Node.Status.STOPPED));
    }
}
