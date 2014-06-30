/**
 * FieldResult.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.commons.reflect;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The field result used to save all the information about how to operate on host.
 *
 * @param <T> the field type
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class FieldResult<T> {

    private final List<Field> fields;
    private Object host;
    private Field field;
    private boolean onlyOne;

    /**
     * Construct a field result with this host variable and fields list.
     *
     * @param host the host variable
     * @param fields the fields list
     */
    public FieldResult(Object host, List<Field> fields) {
        this.host = host;
        this.fields = fields;
        if (fields.size() == 1) {
            this.onlyOne = true;
            this.field = fields.get(0);
        }
    }

    /**
     * Set the host variable.
     *
     * @param host the host variable
     * @return this
     */
    public FieldResult<T> host(Object host) {
        this.host = host;
        return this;
    }

    /**
     * @return the results fields list
     */
    public List<Field> results() {
        return fields;
    }

    /**
     * Use the first method.
     *
     * @return this
     */
    public FieldResult<T> first() {
        if (fields.size() >= 1) {
            this.onlyOne = true;
            this.field = fields.get(0);
        } else {
            throw new IllegalStateException("No field found.");
        }
        return this;
    }

    /**
     * Use the last method.
     *
     * @return this
     */
    public FieldResult<T> last() {
        if (fields.size() >= 1) {
            this.onlyOne = true;
            this.field = fields.get(fields.size() - 1);
        } else {
            throw new IllegalStateException("No field found.");
        }
        return this;
    }

    /**
     * @return the result field
     * @throws IllegalStateException if multiple fields found
     */
    public Field result() {
        if (onlyOne) {
            return field;
        } else {
            throw new IllegalStateException("Multiple fields found, please filter out one first!");
        }
    }

    /**
     * @return the field value of this field
     */
    public T get() {
        if (onlyOne) {
            return FieldUtil.getFieldValue(host, field);
        } else {
            throw new IllegalStateException("Multiple fields found, please filter out one first!");
        }
    }

    /**
     * Set the field value.
     *
     * @param t the new field value
     */
    public void set(T t) {
        if (onlyOne) {
            FieldUtil.setFieldValue(host, field, t);
        } else {
            throw new IllegalStateException("Multiple fields found, please filter out one first!");
        }
    }

}
