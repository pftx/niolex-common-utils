/**
 * Executer.java
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
package org.apache.niolex.commons.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;

import org.apache.niolex.commons.codec.StringUtil;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Execute commands from client.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-25
 */
public abstract class Executer {

	// can reuse, share globally
    private static final ObjectMapper mapper;
    public static String END_LINE = "\n";

    static {
    	/**
    	 * Init the Object Mapper as follows.
    	 */
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

	/**
	 * Execute the command on the object.
	 * @param o
	 * @param out
	 * @param args
	 */
	public abstract void execute(Object o, OutputStream out, String[] args) throws IOException;

	/**
	 * Get bean details.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-25
	 */
	public static class Getter extends Executer {

		/**
		 * {@inheritDoc}
		 *
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
		 */
		@Override
		public void execute(Object o, OutputStream out, String[] args) throws IOException {
			String s = mapper.writeValueAsString(o) + END_LINE;
			out.write(StringUtil.strToUtf8Byte(s));
		}

	}

	/**
	 * List all the fields in any bean.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-25
	 */
	public static class Lister extends Executer {

		/**
		 * {@inheritDoc}
		 *
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
		 */
		@Override
		public void execute(Object o, OutputStream out, String[] args) throws IOException {
			Field[] fields = o.getClass().getDeclaredFields();
			StringBuilder sb = new StringBuilder();
			sb.append("All Fields Of ").append(o.getClass().getSimpleName()).append(END_LINE);
			for (Field f : fields) {
				sb.append("    ").append(f.getName()).append(END_LINE);
			}
			sb.append("---").append(END_LINE);
			out.write(StringUtil.strToUtf8Byte(sb.toString()));
		}

	}

	/**
	 * Set bean properties.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-25
	 */
	public static class Setter extends Executer {

		/**
		 * {@inheritDoc}
		 *
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
		 */
		@Override
		public void execute(Object o, OutputStream out, String[] args) throws IOException {
			Field[] fields = o.getClass().getDeclaredFields();
			if (args.length != 4) {
				out.write(StringUtil.strToAsciiByte("Invalid Command." + END_LINE));
				return;
			}
			String fieldName = args[2];
			String value = args[3];
			boolean found = false;
			for (Field f : fields) {
				if (f.getName().equals(fieldName)) {
					found = true;
					Class<?> type = f.getType();
					f.setAccessible(true);
					try {
						if (type == String.class) {
							f.set(o, value);
						} else if (type == Date.class) {
							Date d = new Date(Long.parseLong(value));
							f.set(o, d);
						} else if (type == Integer.class) {
							f.set(o, Integer.parseInt(value));
						} else if (type == Boolean.class) {
							f.set(o, Boolean.parseBoolean(value));
						} else if (type == Long.class) {
							f.set(o, Long.parseLong(value));
						} else if (type == int.class) {
							f.setInt(o, Integer.parseInt(value));
						} else if (type == long.class) {
							f.setLong(o, Long.parseLong(value));
						} else if (type == boolean.class) {
							f.setBoolean(o, Boolean.parseBoolean(value));
						} else if (type == short.class) {
							f.setShort(o, Short.parseShort(value));
						} else if (type == byte.class) {
							f.setByte(o, Byte.parseByte(value));
						} else if (type == char.class) {
							f.setChar(o, value.charAt(0));
						} else if (type == double.class) {
							f.setDouble(o, Double.parseDouble(value));
						} else if (type == float.class) {
							f.setFloat(o, Float.parseFloat(value));
						} else {
							out.write(StringUtil.strToAsciiByte("This Field Type " + type.getSimpleName()
									+ " Is Not Supported." + END_LINE));
							return;
						}
						out.write(StringUtil.strToAsciiByte("Set Field Success." + END_LINE));
					} catch (Exception e) {
						out.write(StringUtil.strToAsciiByte("Failed to Set Field:" + e.getMessage() + "." + END_LINE));
					}
					break;
				}
			}
			if (!found) {
				out.write(StringUtil.strToAsciiByte("Field Not Found." + END_LINE));
			}
		}

	}

	/**
	 * Invoke method on target object.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-26
	 */
	public static class Invoker extends Executer {

		/**
		 * {@inheritDoc}
		 *
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
		 */
		@Override
		public void execute(Object o, OutputStream out, String[] args) throws IOException {
			StringBuilder sb = new StringBuilder();
			if (o instanceof Invokable) {
				((Invokable)o).invoke(out, args);
			} else if (o instanceof Runnable) {
				((Runnable)o).run();
			} else {
				sb.append("Target ").append(o.getClass().getSimpleName());
				sb.append(" Is not Allowed to Invoke.");
				sb.append(END_LINE);
				out.write(StringUtil.strToUtf8Byte(sb.toString()));
				return;
			}
			sb.append("---Invoke Success---").append(END_LINE);
			out.write(StringUtil.strToUtf8Byte(sb.toString()));
		}

	}
}
