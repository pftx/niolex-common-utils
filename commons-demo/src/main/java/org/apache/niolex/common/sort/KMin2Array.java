/**
 * KMin2Array.java
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
package org.apache.niolex.common.sort;

/**
 * Not finished yet.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-25
 */
public class KMin2Array {

	public static int findTheKMin(int[] A, int[] B, int k) {
		int i = k / 2;
		int j = k - i - 1;
		if (A[i] <= B[j]) {
			if (A[i] >= B[j - 1]) {
				// found.
				return A[i];
			}
			;
		} else {
			if (B[j] > A[i - 1]) {
				return B[j];
			}
		}
		return 0;
	}

}
