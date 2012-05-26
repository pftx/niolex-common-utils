/**
 * Constants.java
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
package org.apache.niolex.commons.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-7-11$
 * 
 */
public abstract class Constants {
    private static final Logger log = LoggerFactory.getLogger(Constants.class);
    
    static {
        try {
            PropUtil.loadConfigFromClassPath("api-core.properties");
        } catch (Throwable t) {
            log.info("api-core.properties not found, use default configurations instead.");
        }
    }
    
    public static final int SERVER_ERROR_BLOCK_TIME = PropUtil.getInteger("rpcServerErrorBlockTime", 60000);
    public static final int SERVER_CONNECT_TIMEOUT = PropUtil.getInteger("rpcServerConnectTimeout", 3000);
    public static final int SERVER_READ_TIMEOUT = PropUtil.getInteger("rpcServerReadTimeout", 3000);
    public static final int SERVER_RETRY_TIMES = PropUtil.getInteger("rpcServerRetryTimes", 2);
    public static final int SERVER_INTERVAL_BT_RETRY = PropUtil.getInteger("rpcServerIntervalBetweenRetry", 50);

    public static final String SERVER_ENCODING = PropUtil.getProperty("rpcServerEncoding", "utf-8");
    
    public static final int DOWNLOAD_CONNECT_TIMEOUT = PropUtil.getInteger("downloadConnectTimeout", 6000);
    public static final int DOWNLOAD_READ_TIMEOUT = PropUtil.getInteger("downloadReadTimeout", 6000);
    public static final int DOWNLOAD_MAX_FILE_SIZE = PropUtil.getInteger("downloadMaxFileSize", 102400);
    public static final int DOWNLOAD_MIN_FILE_SIZE = PropUtil.getInteger("downloadMinFileSize", 10);
}
