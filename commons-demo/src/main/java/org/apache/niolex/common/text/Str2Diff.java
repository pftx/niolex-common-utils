/**
 * Str2Diff.java
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

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-18
 */
public class Str2Diff {

	public static int diff(String str1, String str2) {
		final int M = str1.length(), N = str2.length();
		int arr[][] = new int[M + 1][N + 1];
		for (int i = 0; i <= M; ++i) {
			arr[i][0] = i;
		}
		for (int i = 0; i <= N; ++i) {
			arr[0][i] = i;
		}
		for (int i = 1; i <= M; ++i) {
			for (int j = 1; j <= N; ++j) {
				if (str1.charAt(i-1) == str2.charAt(j-1)) {
					arr[i][j] = arr[i-1][j-1];
				} else {
					arr[i][j] = Math.min(arr[i-1][j-1], Math.min(arr[i][j-1], arr[i-1][j])) + 1;
				}
			}
		}
		int i = M, j = N;
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		while (i != 0 && j != 0) {
			if (str1.charAt(i-1) == str2.charAt(j-1)) {
				sb1.append(str1.charAt(i-1));
				sb2.append(str2.charAt(j-1));
				sb3.append(' ');
				--i;
				--j;
			} else {
				int k = arr[i][j] - 1;
				if (k == arr[i][j-1]) {
					sb1.append('_');
					sb2.append(str2.charAt(j-1));
					--j;
				} else if (k == arr[i-1][j]) {
					sb1.append(str1.charAt(i-1));
					sb2.append('_');
					--i;
				} else {
					sb1.append(str1.charAt(i-1));
					sb2.append(str2.charAt(j-1));
					--i;
					--j;
				}
				sb3.append('*');
			}
		}
		while (i != 0) {
			sb1.append(str1.charAt(i-1));
			sb2.append('_');
			sb3.append('*');
			--i;
		}
		while (j != 0) {
			sb1.append('_');
			sb2.append(str2.charAt(j-1));
			sb3.append('*');
			--j;
		}
		System.out.println(sb1.reverse());
		System.out.println(sb2.reverse());
		System.out.println(sb3.reverse());
		return arr[M][N];
	}

}
