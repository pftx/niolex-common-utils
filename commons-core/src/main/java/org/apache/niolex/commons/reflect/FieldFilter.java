/**
 * FieldFilter.java
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
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.ClassUtils;
import org.apache.niolex.commons.reflect.FieldUtil.Filter;

import com.google.common.collect.Lists;

/**
 * This class is used to help user to build filter to filter field when
 * try to get the correct field from a class.
 * <br>
 * And user can use the method {@link #find()} to find the correct fields
 * and then operate on them.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class FieldFilter<T> implements Filter {

    /**
     * A short cut for method {@link #create()}.
     *
     * @return a new field filter
     */
    public static final FieldFilter<Object> c() {
        return create();
    }

    /**
     * Create a new field filter.
     *
     * @return a new field filter
     */
    public static final FieldFilter<Object> create() {
        return new FieldFilter<Object>();
    }

    /**
     * The list to save all the filters
     */
    private final List<Filter> filterList = Lists.newArrayList();

    /**
     * The host variable
     */
    private Object host;

    /**
     * The host class
     */
    private Class<?> clazz;

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.reflect.FieldUtil.Filter#isValid(java.lang.reflect.Field)
     */
    @Override
    public boolean isValid(Field f) {
        for (Filter filter : filterList) {
            if (!filter.isValid(f)) return false;
        }
        return true;
    }

    /**
     * Add a new filter to this composite filter.
     *
     * @param filter the filter to be added
     * @return this
     */
    public FieldFilter<T> add(Filter filter) {
        filterList.add(filter);
        return this;
    }

    /**
     * Set the host variable.
     *
     * @param host the host variable
     * @return this
     */
    public final FieldFilter<T> host(Object host) {
        this.host = host;
        this.clazz = host.getClass();
        return this;
    }

    /**
     * Set the host class.
     *
     * @param clazz the host class
     * @return this
     */
    public final FieldFilter<T> clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * Find the fields matches this filter.
     *
     * @return the field result
     * @throws IllegalStateException if neither host variable nor host class was set
     */
    public final FieldResult<T> find() {
        if (clazz != null) {
            return new FieldResult<T>(host, FieldUtil.getFields(clazz, this));
        } else {
            throw new IllegalStateException("Please set host variable or class first!");
        }
    }

    /**
     * Filter the fields with this type as the field type. Unlike {@link #exactType(Class)},
     * we do widening of primitive classes and match sub classes too.
     *
     * @param type the field type
     * @return a newly created field filter
     */
    public final <E> FieldFilter<? extends E> forType(final Class<E> type) {
        // Step 1. Add filter.
        this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return ClassUtils.isAssignable(f.getType(), type);
            }});
        // Step 2. Create a new field filter.
        FieldFilter<? extends E> e = new FieldFilter<E>();
        e.host = this.host;
        e.clazz = this.clazz;
        e.filterList.addAll(this.filterList);
        return e;
    }

    /**
     * Filter the fields exactly with this type.
     *
     * @param type the field type
     * @return a newly created field filter
     */
    public final <E> FieldFilter<E> exactType(final Class<E> type) {
        // Step 1. Add filter.
        this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return type.equals(f.getType());
            }});
        // Step 2. Create a new field filter.
        FieldFilter<E> e = new FieldFilter<E>();
        e.host = this.host;
        e.clazz = this.clazz;
        e.filterList.addAll(this.filterList);
        return e;
    }

    /**
     * Filter the fields with this name.
     *
     * @param fieldName the field name
     * @return this
     */
    public final FieldFilter<T> name(final String fieldName) {
        return withName(fieldName);
    }

    /**
     * Filter the fields with this name.
     *
     * @param fieldName the field name
     * @return this
     */
    public final FieldFilter<T> withName(final String fieldName) {
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return fieldName.equals(f.getName());
            }});
    }

    /**
     * Filter the fields who's field name matches this regex expression.
     *
     * @param nameRegex the field name regex expression
     * @return this
     */
    public final FieldFilter<T> nameLike(final String nameRegex) {
        final Pattern p = Pattern.compile(nameRegex);
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return p.matcher(f.getName()).matches();
            }});
    }

    /**
     * Filter the fields with only static fields.
     *
     * @return this
     */
    public final FieldFilter<T> onlyStatic() {
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return Modifier.isStatic(f.getModifiers());
            }});
    }

    /**
     * Filter the fields without static fields.
     *
     * @return this
     */
    public final FieldFilter<T> noStatic() {
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return !Modifier.isStatic(f.getModifiers());
            }});
    }

    /**
     * Filter the fields without synthetic fields.
     *
     * @return this
     */
    public final FieldFilter<T> noSynthetic() {
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return !f.isSynthetic();
            }});
    }

}
