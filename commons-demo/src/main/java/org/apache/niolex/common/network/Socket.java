/**
 * Socket.java
 *
 * Copyright 2013 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.common.network;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * For nio client connection immediate connected
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-2-27
 */
public class Socket {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SocketChannel ch = SocketChannel.open();
        ch.configureBlocking(false);
        ch.socket().setTcpNoDelay(true);
        ch.socket().setSoLinger(false, 0);
        boolean b = ch.connect(new InetSocketAddress("localhost", 8088));
        System.out.println("\n**** Test NIO Socket");
        System.out.println("Connected ? ..." + b);
        Selector selector = Selector.open();
        SelectionKey key = ch.register(selector, SelectionKey.OP_CONNECT);
        System.out.println("Key ? ..." + key);
        for (int i = 0; i < 5; ++i) {
            selector.select();
            System.out.print("Keys ? ..." + selector.selectedKeys().size());
            System.out.println("..." + selector.selectedKeys().iterator().next().interestOps());
        }
        ch.close();
    }

}