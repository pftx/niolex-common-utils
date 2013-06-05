/**
 * AccessProcess.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.common.esper;

import java.util.List;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.ObjToStringUtil;
import org.apache.niolex.commons.util.SystemUtil;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-5
 */
public class AccessProcess {

    public static class AccessRec {
        private String ip;
        private String url;
        private String serviceName;
        private String referer;
        private String ua;

        /**
         * Constructor
         * @param ip
         * @param url
         * @param serviceName
         * @param referer
         * @param ua
         */
        public AccessRec(String ip, String url, String serviceName, String referer, String ua) {
            super();
            this.ip = ip;
            this.url = url;
            this.serviceName = serviceName;
            this.referer = referer;
            this.ua = ua;
        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @return the serviceName
         */
        public String getServiceName() {
            return serviceName;
        }

        /**
         * @return the referer
         */
        public String getReferer() {
            return referer;
        }

        /**
         * @return the ua
         */
        public String getUa() {
            return ua;
        }

    }

    private static List<String> IP_LIST = Lists.newArrayList();

    static {
        // Prepare 100 random IP
        for (int i = 0; i < 100; ++i) {
            String ip = MockUtil.randInt(10, 20) + "." + MockUtil.randInt(5, 100) + "." + MockUtil.randInt(256) + "." + MockUtil.randInt(1, 256);
            IP_LIST.add(ip);
        }
    }

    public static void generateAccess(EPRuntime cepRT) {
        final int SIZE = MockUtil.randInt(1, 20);
        AccessRec rec = new AccessRec(IP_LIST.get(MockUtil.randInt(100)), "index.html", "lex", "baidu", "Chrome");
        for (int i = 0; i < SIZE; ++i) {
            cepRT.sendEvent(rec);
        }
    }

    public static class CEPListener implements UpdateListener {
        public void update(EventBean[] newData, EventBean[] oldData) {
            SystemUtil.println("Event received, old: %s, new: %s.", ObjToStringUtil.objToString(oldData), ObjToStringUtil.objToString(newData));
        }
    }

    public static void main(String[] args) {
        // The Configuration is meant only as an initialization-time object.
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("Access", AccessRec.class);
        EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
        EPRuntime cepRT = cep.getEPRuntime();

        EPAdministrator cepAdm = cep.getEPAdministrator();
        EPStatement cepStatement = cepAdm.createEPL("select ip, count(*) from " + "Access.win:time(1 sec) group by ip"
                + " having count(*) > 120");

        cepStatement.addListener(new CEPListener());

        // We generate a few ticks...
        for (int i = 0; i < 3000; i++) {
            generateAccess(cepRT);
            SystemUtil.sleep(1);
        }
    }

}
