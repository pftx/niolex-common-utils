/**
 * DLinkedList.java
 *
 * Copyright 2014 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.common.link;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-9
 */
public class DLinkedList {

    public static void printList(DLink root) {
        StringBuilder sb = new StringBuilder();
        sb.append(root.value);
        while (root.right != null) {
            root = root.right;
            sb.append(" -> ");
            sb.append(root.value);
        }
        System.out.println(sb.toString());
    }

}
