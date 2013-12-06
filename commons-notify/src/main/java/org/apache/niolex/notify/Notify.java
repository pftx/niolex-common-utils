/**
 * Notify.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.codec.KVBase64Util;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKListener;

/**
 * The core class of this notify framework. Watch the changes of node data and node properties.
 * User can also change node data and node properties.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public class Notify implements ZKListener {

    private final Map<ByteArray, byte[]> properties = new HashMap<ByteArray, byte[]>();
    private final List<NotifyListener> list = new ArrayList<NotifyListener>();
    private final ZKConnector zkConn;
    private final String basePath;

    private volatile byte[] data;
    private volatile List<String> children;

    /**
     * Create a new notify under this bash path.
     *
     * @param basePath the ZK base path of this notify. It's fixed for one notify.
     */
    public Notify(ZKConnector zkConn, String basePath) {
        super();
        this.zkConn = zkConn;
        this.basePath = basePath;
        this.data = zkConn.watchData(basePath, this);
        this.children = Collections.emptyList();
        List<String> list = zkConn.watchChildren(basePath, this);
        this.onChildrenChange(list);
    }

    /**
     * Update the data of this notify.
     *
     * @param data the new data.
     */
    public void updateData(String data) {
        updateData(StringUtil.strToUtf8Byte(data));
    }

    /**
     * Update the data of this notify.
     *
     * @param data the new data.
     */
    public synchronized void updateData(byte[] data) {
        this.zkConn.updateNodeData(basePath, data);
        // We will not set the new data to local directly, it will be updated by events.
    }

    /**
     * Delete the property specified by this key.
     *
     * @param key the property key
     * @return true if deleted, false if not found.
     */
    public boolean deleteProperty(String key) {
        return deleteProperty(StringUtil.strToUtf8Byte(key));
    }

    /**
     * Delete the property specified by this key.
     *
     * @param key the property key
     * @return true if deleted, false if not found.
     */
    public synchronized boolean deleteProperty(byte[] key) {
        ByteArray k = new ByteArray(key);
        byte[] v = this.properties.get(k);
        if (v != null) {
            // Old property exist, we delete it.
            String p = KVBase64Util.kvToBase64(key, v);
            this.zkConn.deleteNode(basePath + "/" + p);
            this.properties.remove(k);
            return true;
        }
        return false;
    }

    /**
     * Replace the property specified by this key if found, add a new
     * property for this key otherwise.
     *
     * @param key the key to replace
     * @param value the new value
     */
    public void replaceProperty(String key, String value) {
        replaceProperty(StringUtil.strToUtf8Byte(key), StringUtil.strToUtf8Byte(value));
    }

    /**
     * Replace the property specified by this key if found, add a new
     * property for this key otherwise.
     *
     * @param key the key to replace
     * @param value the new value
     */
    public synchronized void replaceProperty(byte[] key, byte[] value) {
        ByteArray k = new ByteArray(key);
        byte[] v = this.properties.get(k);
        if (v != null) {
            if (Arrays.equals(v, value)) {
                return;
            }
            // Old property exist, we delete it.
            String p = KVBase64Util.kvToBase64(key, v);
            this.zkConn.deleteNode(basePath + "/" + p);
        }
        this.properties.put(k, value);
        // The new property path.
        String p = KVBase64Util.kvToBase64(key, value);
        this.zkConn.createNode(basePath + "/" + p);
    }

    /**
     * Add this specified listener.
     *
     * @param listener element to be appended to this notify
     */
    public synchronized void addListener(NotifyListener listener) {
        list.add(listener);
    }

    /**
     * Remote this specified listener.
     *
     * @param listener element to be removed from this notify, if present
     * @return true if success, false if not found.
     */
    public synchronized boolean removeListener(NotifyListener listener) {
        return list.remove(listener);
    }

    /**
     * Fired when the data of this notify changed.
     * This method is invoked from {@link org.apache.niolex.notify.core.NotifyDataWatcher}
     *
     * @param data the new data
     */
    public synchronized void onDataChange(byte[] data) {
        this.data = data;
        for (NotifyListener li : list) {
            li.onDataChange(data);
        }
    }

    /**
     * Fired when the children of this notify changed.
     * This method is invoked from {@link org.apache.niolex.notify.core.NotifyChildrenWatcher}
     *
     * @param list the new children list
     */
    public void onChildrenChange(List<String> list) {
        Pair<List<String>,List<String>> pair = CollectionUtil.intersection(this.children, list);
        for (String s : pair.a) {
            // All the delete item.
            if (Base64.isBase64(s)) {
                // We try to decode it.
                try {
                    Pair<byte[],byte[]> kv = KVBase64Util.base64toKV(s);
                    properties.remove(new ByteArray(kv.a));
                } catch (Exception e) {}
            }
        }
        this.children = list;
        for (String s : pair.b) {
            // All the added item.
            if (Base64.isBase64(s)) {
                // We try to decode it.
                try {
                    Pair<byte[],byte[]> kv = KVBase64Util.base64toKV(s);
                    properties.put(new ByteArray(kv.a), kv.b);
                    firePropertyChange(kv.a, kv.b);
                } catch (Exception e) {}
            }
        }
    }

    /**
     * We will not fire property change on property delete.
     *
     * @param a the new property key
     * @param b the new property value
     */
    private synchronized void firePropertyChange(byte[] a, byte[] b) {
        for (NotifyListener li : list) {
            li.onPropertyChange(a, b);
        }
    }

    /**
     * Get the property in this notify.
     *
     * @param key the property key
     * @return the property value, null if not found
     */
    public String getProperty(String key) {
        byte[] v = getProperty(StringUtil.strToUtf8Byte(key));
        return v == null ? null : StringUtil.utf8ByteToStr(v);
    }

    /**
     * Get the property in this notify.
     *
     * @param key the property key
     * @return the property value, null if not found
     */
    public byte[] getProperty(byte[] key) {
        return properties.get(new ByteArray(key));
    }

    /**
     * Get the property in this notify.
     *
     * @param key the property key
     * @return the property value, null if not found
     */
    public byte[] getProperty(ByteArray key) {
        return properties.get(key);
    }

    /**
     * @return the current notify data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return the properties map, it's read-only.
     */
    public Map<ByteArray, byte[]> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

}
