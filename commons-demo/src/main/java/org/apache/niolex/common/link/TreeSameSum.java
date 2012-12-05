/**
 * TreeSameSum.java
 *
 * Copyright 2012 Niolex, Inc.
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
package org.apache.niolex.common.link;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-19
 */
public class TreeSameSum {

	/**
	 * 输入一个整数和一棵二元树。从树的根结点开始往下访问一直到叶结点所经过的所有结点形成一条路径。
	 * 打印出和与输入整数相等的所有路径。
	 *
	 * @param root
	 * @param k
	 */
	public static void treeSum(final DLink root, final int k) {
		if (root == null) {
			return;
		}
		Stack<Integer> s = new Stack<Integer>();
		treePath(root, s, k, 0);
	}

	public static void treePath(final DLink ptr, final Stack<Integer> s, final int k, int sum) {
		sum += ptr.value;
		s.push(ptr.value);
		boolean isLeaf = ptr.left == null && ptr.right == null;
		if (sum == k && isLeaf) {
			// We found a path just in this stack.
			printPath(s);
		} else {
			// We can still look into it's children.
			if (ptr.left != null) {
				treePath(ptr.left, s, k, sum);
			}
			if (ptr.right != null) {
				// We can still look into it's children.
				treePath(ptr.right, s, k, sum);
			}
		}
		s.pop();
	}

	/**
	 * @param s
	 */
	private static void printPath(Stack<Integer> s) {
		Iterator<Integer> it = s.iterator();
		while (it.hasNext()) {
			System.out.print(" " + it.next());
		}
		System.out.println();
	}
}
