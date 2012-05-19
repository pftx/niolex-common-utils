/**
 * LogContext.java
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

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-12$
 * 
 */
public abstract class LogContext {

    private static LogContext INSTANCE = new LogContext() {
        
        public String getLogPrefix() {
            return "LOGID";
        }

        public void setServerUrl(String serverUrl) {
        }
        
    };

    public static void setInstance(LogContext instance) {
        LogContext.INSTANCE = instance;
    }

    public abstract String getLogPrefix();
    public abstract void setServerUrl(String serverUrl);

    static String prefix() {
        return INSTANCE.getLogPrefix();
    }
    
    static void serviceUrl(String serverUrl) {
        INSTANCE.setServerUrl(serverUrl);
    }
}
