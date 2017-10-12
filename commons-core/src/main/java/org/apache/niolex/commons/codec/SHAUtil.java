/**
 * SHAUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.codec;

import java.security.MessageDigest;

/**
 * SHAUtil is a utility for generating SHA signatures and verifying SHA signatures.
 *
 * The summary of current functionality:
 * 1. Generates a SHA signature on the input string list.
 * public static final String sha1(String... plainTexts)
 * 
 * 2. Validate the input string list against the provided SHA signature.
 * public static final boolean sha1Check(String sha1, String... plainTexts)
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class SHAUtil extends CipherUtil {

    /**
     * Generates a SHA signature on the input string list.
     *
     * @param plainTexts the list of strings used to generate SHA signature
     * @return the generated SHA signature
     * @throws IllegalStateException if the user's JDK does not support the SHA hash algorithm
     */
    public static final String sha1(String... plainTexts) {
        MessageDigest md = getInstance("SHA");
        digest(md, plainTexts);
        byte bytes[] = md.digest();

        return Base16Util.byteToBase16(bytes);
    }

    /**
     * Validate the input string list against the provided SHA signature.
     *
     * @param sha1 the SHA signature used to validate the strings
     * @param plainTexts the list of strings to be validated
     * @return true if the plain texts are valid, false otherwise
     * @throws IllegalStateException if the user's JDK does not support the SHA hash algorithm
     */
    public static final boolean sha1Check(String sha1, String... plainTexts) {
        return sha1.equalsIgnoreCase(sha1(plainTexts));
    }

}
