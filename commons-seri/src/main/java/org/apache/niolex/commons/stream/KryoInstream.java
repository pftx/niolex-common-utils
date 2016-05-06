/**
 * KryoInstream.java
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
package org.apache.niolex.commons.stream;

import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

/**
 * The input stream proxy for Kryo serialization algorithm.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class KryoInstream {

	private final Kryo kryo;
	private final Input inp;

	/**
	 * Create a simple kryo to process this input stream.
	 *
	 * @param in the input stream
	 */
	public KryoInstream(InputStream in) {
		this(new Kryo(), in);
	}

	/**
	 * Create a kryo in stream with the specified Kryo directly.
	 *
	 * @param kryo the kryo instance
	 * @param in the input stream
	 */
	public KryoInstream(Kryo kryo, InputStream in) {
		super();
		this.kryo = kryo;
		this.inp = new Input(in);
	}

	/**
	 * Read object from the internal stream, this method will not close the stream.
	 * You can call it repeatedly.
	 *
	 * @param <T> the object type
	 * @param clazz the object class
	 * @return the object
	 */
	public <T> T readObject(Class<T> clazz) {
		return kryo.readObject(inp, clazz);
	}

	/**
	 * Close the internal managed stream.
	 */
	public void close() {
		inp.close();
	}

}
