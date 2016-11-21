/**
 * PrintTree.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.common.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.0
 * @since Nov 14, 2016
 */
public class PrintTree {

    public static class Node {
        Node left;
        Node right;

        int value;

        public Node(int value) {
            super();
            this.value = value;
        }

        public Node(Node left, Node right, int value) {
            super();
            this.left = left;
            this.right = right;
            this.value = value;
        }

    }

    public static void printTree(Node root) {
        final Node NIL = new Node(-1);
        List<List<Node>> tree = new ArrayList<List<Node>>();
        List<Node> level = new ArrayList<Node>();
        level.add(root);
        tree.add(level);

        while (true) {
            List<Node> subLevel = new ArrayList<Node>();
            for (Node n : level) {
                if (n == NIL) {
                    subLevel.add(NIL);
                    subLevel.add(NIL);
                } else {
                    subLevel.add(n.left == null ? NIL : n.left);
                    subLevel.add(n.right == null ? NIL : n.right);
                }
            }

            boolean isEnd = true;
            for (Node n : subLevel) {
                if (n != NIL) {
                    isEnd = false;
                    break;
                }
            }

            if (isEnd) {
                break;
            } else {
                tree.add(subLevel);
                level = subLevel;
            }
        }

        int depth = tree.size();
        int start = 0, middle = 1;
        for (int i = 1; i < depth; ++i) {
            start = middle;
            middle = middle * 2 + 1;
        }

        for (List<Node> l : tree) {
            printSpace(start);
            for (int i = 0; i < l.size() - 1; ++i) {
                Node n = l.get(i);
                if (n == NIL) {
                    // printSpace(1);
                    System.out.print('.');
                } else {
                    System.out.print(n.value);
                }
                printSpace(middle);
            }

            Node n = l.get(l.size() - 1);
            if (n == NIL) {
                // printSpace(1);
                System.out.print('.');
            } else {
                System.out.print(n.value);
            }
            System.out.println();

            middle = start;
            start = (start - 1) / 2;
        }

    }

    public static void printSpace(int size) {
        for (int i = 0; i < size; ++i) {
            System.out.print(" ");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Node n11 = new Node(0);
        Node n10 = new Node(null, n11, 0);
        Node n1 = new Node(null, n10, 9);
        Node n2 = new Node(8);
        Node n3 = new Node(n1, null, 7);
        Node n4 = new Node(6);
        Node n5 = new Node(n2, n4, 5);
        Node n6 = new Node(n3, n5, 4);
        Node n71 = new Node(1);
        Node n72 = new Node(null, n71, 2);
        Node n7 = new Node(n72, null, 3);
        Node n8 = new Node(n7, null, 2);
        Node n9 = new Node(null, n8, 1);
        Node n0 = new Node(n6, n9, 0);

        printTree(n0);
    }

}
