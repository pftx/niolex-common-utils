/**
 * BinaryTree.java
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

import java.util.LinkedList;

import org.apache.niolex.commons.util.MathUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-9
 */
public class BinaryTree {

    static final int MAX_NUM = 1000;

    public static DLink generateBinaryTree(int nodeNum) {
        DLink root = new DLink(MathUtil.randInt(MAX_NUM));
        for (int i = 1; i < nodeNum; ++i) {
            insert(root, MathUtil.randInt(MAX_NUM));
        }
        return root;
    }

    /**
     * Insert this value into the binary tree.
     *
     * @param root
     * @param value
     */
    public static void insert(DLink root, int value) {
        DLink tmp = null;

        while (root != null) {
            if (root.value > value) {
                tmp = root;
                root = root.left;
            } else if (root.value < value) {
                tmp = root;
                root = root.right;
            } else {
                return;
            }
        }

        if (tmp.value > value) {
            tmp.left = new DLink(value);
        } else {
            tmp.right = new DLink(value);
        }
    }

    public static void printTree(DLink root) {
        LinkedList<DLink> list1 = new LinkedList<DLink>();
        LinkedList<DLink> list2 = new LinkedList<DLink>();
        StringBuilder sb = new StringBuilder();
        DLink l, r, n;

        list1.add(root);

        while (!list1.isEmpty()) {
            while (!list1.isEmpty()) {
                n = list1.poll();
                if ((l = n.left) != null) list2.add(l);
                if ((r = n.right) != null) list2.add(r);
                sb.append(n.value).append(", ");
            }
            sb.append("\n");
            list1 = list2;
            list2 = new LinkedList<DLink>();
        }

        System.out.print(sb.toString());
    }

}
