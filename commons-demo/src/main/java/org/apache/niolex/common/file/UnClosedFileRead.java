/**
 * UnClosedFileRead.java
 *
 * Copyright 2013 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.common.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2013-4-10$
 */
public class UnClosedFileRead {

    public static class Benchmark implements Serializable {
        /**
         * Ser Id
         */
        private static final long serialVersionUID = -7919680617007658100L;
        private String name = "It's good.";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException, Throwable {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("D:\\home\\tmp\\out.obj"));
        out.writeObject(new Benchmark());
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("D:\\home\\tmp\\out.obj"));
        Benchmark ben = (Benchmark)in.readObject();
        System.out.print(ben.getName());
    }
}
