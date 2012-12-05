/**
 * Palindrome.java
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
package org.apache.niolex.common.text;

import java.util.Arrays;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-22
 */
public class Palindrome {

	public static int palindrome(String s) {
		if (s.length() ==0) {
			return 0;
		}
		final int LEN = s.length() * 2 + 2;
		final char[] str = new char[LEN + 1];
		final int[] P = new int[LEN + 1];
		str[0] = '^';
		str[1] = '#';
		P[0] = 0;
		P[1] = 0;
		str[LEN] = '$';
		for (int i = 2; i < LEN; i += 2) {
			str[i] = s.charAt(i / 2 - 1);
			str[i + 1] = '#';
		}
		// Do calc
		int max = 0, lastCenter = 1, rightEdge = 1, pp;
		for (int i = 2; i < LEN; ++i) {
			pp = P[2 * lastCenter - i];
			if (pp < rightEdge - i) {
				P[i] = pp;
			} else {
				int k = rightEdge > i ? rightEdge : i;
				while (str[k] == str[2 * i - k]) {
					++k;
				}
				if (rightEdge < k - 1) {
					rightEdge = k - 1;
					lastCenter = i;
				}
				P[i] = rightEdge - i;
				if (P[i] > max) {
					max = P[i];
				}
			}
		}
		System.out.println(str);
		System.out.println(Arrays.toString(P));
		return max;
	}

}
