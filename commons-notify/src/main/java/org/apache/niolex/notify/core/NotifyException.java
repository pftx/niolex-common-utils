/**
 * NotifyException.java, 2012-6-21.
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
package org.apache.niolex.notify.core;

import org.apache.zookeeper.KeeperException;

/**
 * The RuntimeException thrown in Find core framework.
 * 
 * @author Xie, Jiyun
 */
public class NotifyException extends RuntimeException {

    public static enum Code {
        INTERRUPT, NOAUTH, DISCONNECTED, OTHER, SYSTEMERROR, NONODE, NODEEXISTS
    }

    /**
     * Version UID
     */
    private static final long serialVersionUID = 6217415731238015041L;

    private final Code code;

    public NotifyException(String message, Throwable cause, Code code) {
        super(message, cause);
        this.code = code;
    }

    public NotifyException(String message, Code code) {
        super(message);
        this.code = code;
    }

    /**
     * Make an instance of FindException
     * 
     * @param message The message you want to say.
     * @param e The nested exception.
     * @return an instance of FindException
     */
    public static final NotifyException makeInstance(String message, Throwable e) {
        Code code = Code.OTHER;
        if (e instanceof KeeperException) {
            KeeperException ke = (KeeperException) e;
            switch (ke.code()) {
                case NOAUTH:
                case AUTHFAILED:
                    code = Code.NOAUTH;
                    break;
                case SYSTEMERROR:
                    code = Code.SYSTEMERROR;
                    break;
                case CONNECTIONLOSS:
                    code = Code.DISCONNECTED;
                    break;
                case NONODE:
                    code = Code.NONODE;
                    break;
                case NODEEXISTS:
                    code = Code.NODEEXISTS;
                    break;
            }
        } else if (e instanceof InterruptedException) {
            code = Code.INTERRUPT;
        }
        return new NotifyException(message, e, code);
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
