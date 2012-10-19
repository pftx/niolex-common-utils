/**
 * Tree2LinkListTest.java
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-10-18
 */
public class Tree2LinkListTest {

	@Test
	public void testNormal() {
		DLink root = new DLink(3);
		root.left = new DLink(5);
		root.right = new DLink(8);
		DLink t = root.left;
		t.left = new DLink(1);
		t.right = new DLink(2);
		t = root.right;
		t.left = new DLink(6);
		t.right = new DLink(7);
		root = Tree2LinkList.transform(root);
		while (root != null) {
			System.out.print(" - " + root.value);
			t = root;
			root = root.right;
		}
		assertTrue(t.value == 7);
		while (t.left != null) {
			t = t.left;
		}
		assertTrue(t.value == 1);
		System.out.println();
	}

	@Test
	public void testDict() {
		DLink root = new DLink(3);
		root.left = new DLink(5);
		DLink t = root.left;
		root.right = new DLink(8);
		t.left = new DLink(1);
		t.right = new DLink(2);
		t = t.right;
		t.left = new DLink(6);
		t.right = new DLink(7);
		t = t.left;
		t.right = new DLink(9);
		root = Tree2LinkList.transform(root);
		while (root != null) {
			System.out.print(" - " + root.value);
			t = root;
			root = root.right;
		}
		assertTrue(t.value == 8);
		while (t.left != null) {
			t = t.left;
		}
		assertTrue(t.value == 1);
		System.out.println();
	}
}
