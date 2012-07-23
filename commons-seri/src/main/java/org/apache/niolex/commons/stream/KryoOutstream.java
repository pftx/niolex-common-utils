/**
 * KryoOutstream.java
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

import java.io.OutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
 */
public class KryoOutstream {

	private final Kryo kryo;
	private final Output outp;

	/**
	 * Create a simple kryo by this output stream.
	 * @param in
	 */
	public KryoOutstream(OutputStream out) {
		this(new Kryo(), out);
	}

	/**
	 * Create a kryo outstream directly.
	 * @param kryo
	 * @param outp
	 */
	public KryoOutstream(Kryo kryo, OutputStream out) {
		super();
		this.kryo = kryo;
		this.outp = new Output(out);
	}


	/**
	 * Write this object into the internal output stream, this method will not
	 * close that stream. You can call it repeatedly.
	 *
	 * @param o
	 */
	public void writeObject(Object o) {
		kryo.writeObject(outp, o);
	}

	/**
	 * Close the internal managed stream.
	 */
	public void close() {
		outp.close();
	}
}
