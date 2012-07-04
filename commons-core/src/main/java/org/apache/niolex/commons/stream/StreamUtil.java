/**
 * StreamUtil.java
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
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some common function for input and output streams.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-4
 */
public class StreamUtil {
	private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);

	/**
	 * Close the specified input stream. It's OK to be null.
	 * @param s
	 */
	public static final void closeStream(InputStream s) {
		try {
            if (s != null)
                s.close();
        } catch (Exception ie) {
            LOG.info("Failed to close stream - " + ie.getMessage());
        }
	}

	/**
	 * Close the specified output stream. It's OK to be null.
	 * @param s
	 */
	public static final void closeStream(OutputStream s) {
		try {
			if (s != null)
				s.close();
		} catch (Exception ie) {
			LOG.info("Failed to close stream - " + ie.getMessage());
		}
	}

}
