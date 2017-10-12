/**
 * Create.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.common.neo4j;

import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.bean.Triple;
import org.apache.niolex.commons.net.HTTPMethod;
import org.apache.niolex.commons.net.HTTPUtil;
import org.apache.niolex.commons.net.NetException;

import com.google.common.collect.ImmutableMap;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-1-26
 */
public class Create {

    private static final String URL = "http://10.34.130.53:9474";
    private static final String ENDP_CREATE = "/db/data/transaction/commit";

    private static final Map<String, String> REQ_HEADER = ImmutableMap.of("Authorization", "Basic bmVvNGo6ZGJtcw==",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/24.0",
            "Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");

    /**
     * @param args
     */
    public static void main(String[] args) throws NetException {
        String data = "{\"statements\":[{\"statement\":\"CREATE (p:Person {name:{name},born:{born}}) RETURN p\",\"parameters\":{\"name\":\"Keanu Reeves\",\"born\":1964}}]}";

        System.out.println(doHTTP(ENDP_CREATE, data));
    }


    /**
     * @param reqBody
     * @return
     * @throws NetException
     */
    private static String doHTTP(String endpoint, String reqBody) throws NetException {
        Triple<Integer, Map<String, List<String>>, byte[]> res = HTTPUtil.doHTTP(URL + endpoint, null, reqBody, "utf8", REQ_HEADER, 3000, 3000, HTTPMethod.POST);

        return new String(res.z, HTTPUtil.inferCharset(res.y, res.z));
    }

}
