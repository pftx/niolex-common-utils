/**
 * NotifyListener.java
 * 
 * Copyright 2012 Niolex, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.notify;

/**
 * Listen to the change from Notify.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public interface NotifyListener {
    
    /**
     * The data of notify changed.
     * 
     * @param data the new data
     */
    public void onDataChange(byte[] data);
    
    /**
     * One property of notify changed. We will not notify property deletion.
     * 
     * @param key the property key
     * @param value the property value
     */
    public void onPropertyChange(byte[] key, byte[] value);

}
