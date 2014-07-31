/**
 * WordReverse.java
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
 * @since 2012-10-19
 */
public class WordReverse {

	public static String wordReverse(String s) {
		char[] str = s.toCharArray();
		char[] b = new char[str.length];
		int prei = 0, i = 0, j = str.length;
		while (i < str.length) {
			if (str[i] == ' ') {
				j = copyTo(str, prei, i, b, j);
				prei = i + 1;
			}
			++i;
		}
		if (prei != i)
			copyTo(str, prei, i, b, j);
		return new String(b);
	}

	/**
	 * @param str
	 * @param prei
	 * @param i
	 * @param b
	 * @param j
	 * @return
	 */
	private static int copyTo(char[] str, int prei, int nexti, char[] b, int j) {
		j -= nexti - prei;
		int k = j;
		for (int i = prei; i < nexti; ++i) {
			b[k++] = str[i];
		}
		if (j != 0)
			b[j - 1] = ' ';
		return j - 1;
	}

	/**
     * @param args
     */
    public static void main(String[] args) {
        String a;
        a = "Hello, this is a good start.";
        System.out.println(a + "\nreverse => " + wordReverse(a));

        a = "We are delighted to welcome you to SIGMOD 2014, the 2014 edition of the ACM SIGMOD International Conference on Management of Data, at Snowbird, Utah, in the Rocky Mountains of the United States.";
        System.out.println(a + "\nreverse => " + wordReverse(a));
    }

}
