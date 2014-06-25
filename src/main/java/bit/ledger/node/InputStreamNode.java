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

/**
 * A {@link Node} implementation that accepts its messages via an {@link java.io.InputStream}, such as {@code System.in}
 */
public class InputStreamNode extends AbstractNode {

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
