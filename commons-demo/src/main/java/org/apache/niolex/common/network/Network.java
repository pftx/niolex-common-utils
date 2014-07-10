/**
 * Network.java
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

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-2-21
 */
public class Network {

    static final int SLEEP_TIME = 10;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n**** Test DNS Lookup");
        String name = "www.baidu.com";
        listDNS(name);

        System.out.println("\n**** Test DNS Cache");
        name = "hi.com";
        listDNS(name);
        System.out.println("Sleep 10sec...");
        Thread.sleep(SLEEP_TIME);
        listDNS(name);

        System.out.println("\n**** Test InetSocketAddress");
        System.out.println("InetSocketAddress[1.2.3.4] = " + new InetSocketAddress("1.2.3.4", 808));
        System.out.println("InetSocketAddress[www.baidu.com] = " + new InetSocketAddress("www.baidu.com", 808));
    }

    public static void listDNS(String name) throws Exception {
        InetAddress[] addresses = InetAddress.getAllByName(name);
        for(int i = 0; i < addresses.length; ++i) {
            System.out.println(name+"["+i+"] : " + addresses[i].getHostAddress());
        }
    }

}
