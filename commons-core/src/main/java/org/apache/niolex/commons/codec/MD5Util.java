/**
 * MD5Util.java
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
 * MD5Util is a utility for generating MD5 signatures and verifying MD5 signatures.
 *
 * The summary of current functionality:
 * 1. Generates an MD5 signature on the input string list.
 * public static final String md5(String... plainTexts)
 * 
 * 2. Validate the input string list against the provided MD5 signature.
 * public static final boolean md5Check(String md5, String... plainTexts)
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class MD5Util extends CipherUtil {

    /**
     * Generates an MD5 signature on the input string list.
     *
     * @param plainTexts the list of strings used to generate MD5 signature
     * @return the generated MD5 signature
     */
    public static final String md5(String... plainTexts) {
        MessageDigest md = getInstance("MD5");
        digest(md, plainTexts);
        byte bytes[] = md.digest();

        return Base16Util.byteToBase16(bytes);
    }

    /**
     * Validate the input string list against the provided MD5 signature.
     *
     * @param md5 the MD5 signature used to validate the strings
     * @param plainTexts the list of strings to be validated
     * @return true if the plain texts are valid, false otherwise
     * @throws IllegalStateException if the user's JDK does not support the MD5 hash algorithm
     */
    public static final boolean md5Check(String md5, String... plainTexts) {
        String digest = md5(plainTexts);
        return digest.equalsIgnoreCase(md5);
    }

}
