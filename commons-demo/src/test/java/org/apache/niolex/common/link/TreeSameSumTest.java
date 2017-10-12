/**
 * TreeSameSumTest.java
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

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-19
 */
public class TreeSameSumTest {

	@Test
	public void testSimple() {
		DLink root = new DLink(10);
		root.left = new DLink(5);
		root.right = new DLink(12);
		DLink t = root.left;
		t.left = new DLink(4);
		t.right = new DLink(7);
		TreeSameSum.treeSum(root, 22);
	}

	@Test
	public void testNormal() {
		DLink root = new DLink(3);
		root.left = new DLink(5);
		root.right = new DLink(8);
		DLink t = root.left;
		t.left = new DLink(1);
		t.right = new DLink(2);
		t = t.left;
		t.left = new DLink(8);
		t.right = new DLink(7);
		t = root.right;
		t.left = new DLink(6);
		t.right = new DLink(7);
		TreeSameSum.treeSum(root, 17);
		System.out.println(12 << 3);
	}

}
