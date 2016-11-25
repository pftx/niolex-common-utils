/**
 * RESTException.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.net;

import java.util.Date;

import org.apache.niolex.commons.test.ObjToStringUtil;

/**
 * The basic demo exception used to wrap the error information returned by RESTful API. Users
 * can throw away this class and create a totally different exception class and create the
 * corresponding error decoder for it.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Nov 24, 2016
 */
public class RESTException extends Exception {

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = 2707693729503541113L;

    /**
     * The Error information returned by the Spring Boot default error handler.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 3.0.1
     * @since Nov 24, 2016
     */
    public static class ErrorInfo {

        /**
         * The server time stamp when this error occurred.
         */
        private Date timestamp;

        /**
         * The HTTP status code, or user defined error code.
         */
        private int status;

        /**
         * The normal error message suitable to be displayed to user.
         */
        private String error;

        /**
         * The exception class type including package.
         */
        private String exception;

        /**
         * The detailed error message.
         */
        private String message;

        /**
         * The original requested URI path.
         */
        private String path;

        /**
         * Any extra information.
         */
        private String extra;

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public void setErrorCode(String error) {
            this.error = error;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setErrorMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        @Override
        public String toString() {
            return ObjToStringUtil.objToString(this);
        }

    }

    /**
     * The error information.
     */
    private final ErrorInfo info;

    /**
     * Create a new REST exception with the specified error information.
     * 
     * @param info the error information
     */
    public RESTException(ErrorInfo info) {
        super(info.message);
        this.info = info;
    }

    @Override
    public String toString() {
        return "RESTException " + info.toString();
    }

    /**
     * @return the error information.
     */
    public ErrorInfo getInfo() {
        return info;
    }

}
