/**
 * RpcConfigerTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.rpc;

import static org.junit.Assert.assertEquals;

import org.apache.niolex.commons.rpc.conf.RpcConfigBean;
import org.apache.niolex.commons.rpc.conf.RpcConfiger;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-15$
 * 
 */
public class RpcConfigerTest {
    static RpcConfiger configer = new RpcConfiger("/com/baidu/api/core/rpc/api-modules.properties");

    @Test
    public void doConfig_Old() {
        RpcConfigBean conf = configer.getConfig("fch-stat");
        assertEquals(conf.serverList[0], "http://10.23.247.6:8090");
        assertEquals(conf.serviceUrl, "/fc-apiV2/api/AccountStatusAPI");
        assertEquals(conf.readTimeout, 1200000);
        assertEquals(conf.retryTimes, 3);
    }
    
    @Test
    public void doConfig_New() {
        RpcConfigBean conf = configer.getConfig("sfdrm");
        assertEquals(conf.serverList[0], "http://10.23.247.6:8003");
        assertEquals(conf.serviceUrl, "/sf-drm2/services/account.php");
        assertEquals(conf.readTimeout, 5000);
    }
}
