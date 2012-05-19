/**
 * McpackTest.java
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

import org.apache.niolex.commons.rpc.RpcServiceFactory;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-8-10$
 * 
 */
public class DrmcServiceTest {
	private static DrmcService service = RpcServiceFactory.getInstance("/com/baidu/api/core/mconf/drmc.properties").getService(DrmcService.class);
	
	@Test()
	public void doInsert() {
		DrmcResultBean res = service.tmpinsert(32, this);
		System.out.println(res);
		assertEquals(2, res.getStatus());
	}
}

interface DrmcService {
    public DrmcResultBean insert(int type, Object content);
    public DrmcResultBean tmpinsert(int type, Object content);
}

class DrmcResultBean {
    private int status;
    private Map<String, String> info;
    private Map<String, String> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DrmcResultBean [data=").append(data).append(", info=").append(info).append(", status=").append(
                status).append("]");
        return builder.toString();
    }
    
}
