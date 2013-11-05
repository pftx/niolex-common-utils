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

import static org.apache.niolex.commons.remote.ConnectionWorker.endl;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Execute commands from client.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-25
 */
public abstract class Executer {

	// can reuse, share globally
    private static final ObjectMapper mapper;

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
     * Write the string to the output stream and flush the output stream.
     *
     * @param out the output stream
     * @param s the string to be written
     * @throws IOException
     */
    protected static void writeAndFlush(OutputStream out, String s) throws IOException {
        out.write(StringUtil.strToAsciiByte(s));
        out.flush();
    }

	/**
	 * Execute the command on the object.
	 *
	 * @param o the target object
	 * @param out the output stream
	 * @param args The command line parsed from client input
	 * It's of the following format:<pre>
	 * Index	Explain
	 * 0		Command Name
	 * 1		Object Path
	 * 2		Extension Argument 1 (Optional)
	 * 3		Extension Argument 2 (Optional)</pre>
	 */
	public abstract void execute(Object o, OutputStream out, String[] args) throws IOException;

	/**
	 * Get bean details.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-7-25
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
			writeAndFlush(out, mapper.writeValueAsString(o) + endl());
		}

	}

	/**
	 * List all the fields in any bean.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-7-25
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
			sb.append("All Fields Of ").append(o.getClass().getSimpleName()).append(endl());
			for (Field f : fields) {
				sb.append("    ").append(f.getName()).append(endl());
			}
			sb.append("---").append(endl());
			writeAndFlush(out, sb.toString());
		}

	}

	/**
	 * Set bean properties.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-7-25
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
			if (args.length != 4) {
			    writeAndFlush(out, "Invalid Command." + endl());
				return;
			}
			try {
    			Field f = FieldUtil.getField(o.getClass(), args[2]);
    			FieldUtil.setFieldWithCorrectValue(f, o, args[3]);
    			writeAndFlush(out, "Set Field Success." + endl());
			} catch (NoSuchFieldException e) {
			    writeAndFlush(out, "Field Not Found." + endl());
			} catch (UnsupportedOperationException e) {
			    writeAndFlush(out, e.getMessage() + endl());
			} catch (Exception e) {
			    writeAndFlush(out, "Failed to Set Field:" + e.getMessage() + "." + endl());
			}
		}

	}

	/**
	 * Invoke method on target object.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-7-26
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
				sb.append(endl());
				writeAndFlush(out, sb.toString());
				return;
			}
			sb.append("---Invoke Success---").append(endl());
			writeAndFlush(out, sb.toString());
		}

	}

	/**
	 * Invoke the methods on instance of {@link org.apache.niolex.commons.remote.Monitor}
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-11-23$
	 */
	public static class InvoMonitor extends Executer {

		/**
		 * {@inheritDoc}
		 *
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
		 */
		@Override
		public void execute(Object o, OutputStream out, String[] args) throws IOException {
			if (o instanceof Monitor) {
				if (args.length < 3) {
					writeAndFlush(out, "Please specify the Key to Monitor." + endl());
					return;
				}
				String parameter = args.length > 3 ? args[3] : "default";
				((Monitor) o).doMonitor(out, args[2], parameter);
			} else {
				writeAndFlush(out, "Object is not a Monitor." + endl());
			}
		}

	}
}
