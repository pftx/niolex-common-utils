/**
 * ZKException.java, 2012-6-21.
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
package org.apache.niolex.zookeeper.core;

import org.apache.zookeeper.KeeperException;

/**
 * The RuntimeException thrown in commons notify framework.
 *
 * @author Xie, Jiyun
 */
public class ZKException extends RuntimeException {

    /**
     * Version UID
     */
    private static final long serialVersionUID = 6217415731238015041L;

    /**
     * The exception Code.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-12-6
     */
    public static enum Code {
        INTERRUPT, NO_AUTH, DISCONNECTED, OTHER, SYSTEM_ERROR, NO_NODE, NODE_EXISTS;
    }

    /**
     * Make an instance of ZKException. If the exception is an instance of
     * RuntimeException, we throw it directly.
     *
     * @param message The message you want to say.
     * @param e The nested exception.
     * @return an instance of ZKException
     */
    public static final ZKException makeInstance(String message, Throwable e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        Code code = Code.OTHER;
        if (e instanceof KeeperException) {
            KeeperException ke = (KeeperException) e;
            switch (ke.code()) {
                case NOAUTH:
                case AUTHFAILED:
                    code = Code.NO_AUTH;
                    break;
                case SYSTEMERROR:
                    code = Code.SYSTEM_ERROR;
                    break;
                case CONNECTIONLOSS:
                    code = Code.DISCONNECTED;
                    break;
                case NONODE:
                    code = Code.NO_NODE;
                    break;
                case NODEEXISTS:
                    code = Code.NODE_EXISTS;
                    break;
            }
        } else if (e instanceof InterruptedException) {
            code = Code.INTERRUPT;
        }
        return new ZKException(message, e, code);
    }

    private final Code code;

    public ZKException(String message, Throwable cause, Code code) {
        super(message, cause);
        this.code = code;
    }

    public ZKException(String message, Code code) {
        super(message);
        this.code = code;
    }

    /**
     * @return the code
     */
    public Code getCode() {
        return code;
    }

	@Override
	public String getMessage() {
		return code + " " + super.getMessage();
	}

}
