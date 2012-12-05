/**
 * Tree2LinkList.java
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

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-18
 */
public class Tree2LinkList {

	public static DLink transform(DLink root) {
		change(root);
		while (root.left != null) {
			root = root.left;
		}
		return root;
	}

	public static void change(DLink root) {
		DLink tmp, ptr;
		if (root.left != null) {
			tmp = ptr = root.left;
			while (ptr.right != null) {
				ptr = ptr.right;
			}
			root.left = ptr;
			change(tmp);
			ptr.right = root;
		}
		if (root.right != null) {
			tmp = ptr = root.right;
			while (ptr.left != null) {
				ptr = ptr.left;
			}
			root.right = ptr;
			change(tmp);
			ptr.left = root;
		}
	}

}
