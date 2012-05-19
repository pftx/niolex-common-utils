/**
 * McpackConfTest.java
 *
 * Copyright 2010 @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.mconf;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.niolex.commons.rpc.conf.RpcConfigBean;
import org.apache.niolex.commons.rpc.conf.RpcConfiger;
import org.junit.Test;



/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-10-15$
 * 
 */
public class McpackConfigerTest {
    static RpcConfiger configer = new RpcConfiger("/com/baidu/api/core/mconf/new-dr-api.properties");

    @Test
    public void doConfig_Old() {
        Map<String, RpcConfigBean> map = configer.getConfigs();
        RpcConfigBean conf = map.get("druc");
        assertEquals(conf.serverList[0], "http://db-testing-ecom123-vm1.db01.baidu.com:8477");
        assertEquals(conf.serviceUrl, "/ucsvc/services/LoginService.php");
        assertEquals(conf.readTimeout, 3000);
        assertEquals(conf.retryTimes, 5);
    }
    
    @Test
    public void doConfig_New() {
        Map<String, RpcConfigBean> map = configer.getConfigs();
        RpcConfigBean conf = map.get("fc-acct");
        assertEquals(conf.serverList[0], "http://db-testing-ecom930-vm02.db01.baidu.com:8080");
        assertEquals(conf.serviceUrl, "/fc-apiV2/api/AccountAPI");
        assertEquals(conf.readTimeout, 1200000);
    }

}
