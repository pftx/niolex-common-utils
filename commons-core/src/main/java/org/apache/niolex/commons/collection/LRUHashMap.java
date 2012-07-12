/**
 * LRUHashMap.java
 *
 * Copyright 2012 Niolex, Inc.
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
package org.apache.niolex.commons.collection;

import java.util.LinkedHashMap;

/**
 * This HashMap will maintain the map size to <code>maxSize</code>
 * If there are more put than that, the eldest item will be removed.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-31
 */
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -1355706789097024625L;
	// The max map size.
	private int maxSize;

	public LRUHashMap(int maxSize) {
		super((int)(maxSize * 1.4));
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}

}
