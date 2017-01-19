/**
 * RESTClientDemo.java
 *
 * Copyright 2017 the original author or authors.
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
package org.apache.niolex.commons.net;

import java.io.IOException;
import java.util.List;

import org.apache.niolex.commons.bean.BeanUtil;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The demo to show how to use RESTClient.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Jan 19, 2017
 */
public class RESTClientDemo {

    public static class Contributor {
        private String login;
        private int id;
        private String type;
        private boolean site_admin;
        private int contributions;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isSite_admin() {
            return site_admin;
        }

        public void setSite_admin(boolean site_admin) {
            this.site_admin = site_admin;
        }

        public int getContributions() {
            return contributions;
        }

        public void setContributions(int contributions) {
            this.contributions = contributions;
        }

    }

    /**
     * @param args
     * @throws IOException
     * @throws NetException
     */
    public static void main(String[] args) throws NetException, IOException {
        RESTClient client = new RESTClient("https://api.github.com", "utf8");
        TypeReference<List<Contributor>> typeRef = new TypeReference<List<Contributor>>() {};

        RESTResult<List<Contributor>> res = client.get("/repos/pftx/niolex-common-utils/contributors", typeRef, null);
        List<Contributor> response = res.getResponse();

        System.out.println("The contributors of niolex-common-utils:");
        System.out.println(BeanUtil.toString(response));

        res = client.get("/repos/google/guava/contributors", typeRef, null);
        response = res.getResponse();

        System.out.println("The contributors of guava:");
        System.out.println(BeanUtil.toString(response));
    }

}
