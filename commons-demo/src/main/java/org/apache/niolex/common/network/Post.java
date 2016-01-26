/**
 * Post.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.common.network;

import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.net.HTTPMethod;
import org.apache.niolex.commons.net.HTTPUtil;
import org.apache.niolex.commons.net.NetException;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-22
 */
public class Post {

    private static final String URL = "http://www.name321.net/xmdf.php";

    public static void main(String[] args) throws NetException {
        String inputs = FileUtil.getCharacterFileContentFromClassPath("name.txt", Post.class, StringUtil.UTF_8);
        Map<String, String> params = new HashMap<String, String>();
        params.put("dxfx", "1");
        params.put("input", "开始测算");
        String[] strings = StringUtil.split(inputs, "谢", false);
        for (String s : strings) {
            params.put("xm", "谢" + s);
            String string = doHTTP(params);
            int k = string.indexOf("<div class=\"xm_df\">") + 19;
            int e = string.indexOf(" ", k);
            int score = Integer.parseInt(string.substring(k, e));
            if (score >= 80) {
                System.out.println("Name: 谢" + s + ", score: " + score);
            }
            ThreadUtil.sleep(500);
        }
    }

    /**
     * @param params
     * @return
     * @throws NetException
     */
    private static String doHTTP(Map<String, String> params) throws NetException {
        return HTTPUtil.doHTTP(URL, params, null, "gbk", HTTPMethod.POST);
    }

}
