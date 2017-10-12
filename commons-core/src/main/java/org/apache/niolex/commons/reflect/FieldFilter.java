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

import org.apache.niolex.commons.reflect.FieldUtil.Filter;
import org.apache.niolex.commons.test.Check;

import com.google.common.collect.Lists;

/**
 * This class is used to help user to build filter to filter field when
 * try to get the correct field from a class.
 * <br>
 * And user can use the method {@link #find()} to find the correct fields
 * and then operate on them.
 *
 * @param <FT> the filtered field type, set to Object if not known
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class FieldFilter<FT> implements Filter {

    // ========================================================================
    // The static methods.
    // ========================================================================

    /**
     * A short cut for method {@link #create()}.
     *
     * @param <CT> the filtered field type
     * @return a newly created field filter
     */
    public static final <CT> FieldFilter<CT> c() {
        return create();
    }

    /**
     * Create a new field filter.
     *
     * @param <CT> the filtered field type
     * @return a newly created field filter
     */
    public static final <CT> FieldFilter<CT> create() {
        return new FieldFilter<CT>();
    }

    /**
     * A short cut for method {@link #exactType(Class)}.
     *
     * @param <ET> the exact field type
     * @param type the exact field type
     * @return a newly created field filter
     */
    public static final <ET> FieldFilter<ET> t(final Class<ET> type) {
        return exactType(type);
    }

    /**
     * Filter the fields exactly with this type, only consider auto boxing.
     *
     * @param <ET> the exact field type
     * @param type the exact field type
     * @return a newly created field filter
     */
    public static final <ET> FieldFilter<ET> exactType(final Class<ET> type) {
        // Step 1. Create a new field filter.
        FieldFilter<ET> e = c();
        // Step 2. Add filter.
        e.filterList.add(new Filter() {
            @Override
            public boolean isValid(Field f) {
                return TypeUtil.typeMatches(f.getType(), type);
            }});
        return e;
    }

    /**
     * A short cut for method {@link #retrieveAs(Class)}.
     *
     * @param <QT> the filtered field type, the type try to assign into
     * @param type the field type
     * @return a newly created field filter
     */
    public static final <QT> FieldFilter<QT> to(final Class<QT> type) {
        return retrieveAs(type);
    }

    /**
     * Create a new filter using the specified type as the filtered field type. Unlike {@link #exactType(Class)},
     * we do widening of primitive classes and match sub classes too.<br>
     * In short, retrieve the field value and assign it to a variable of the specified type should work without
     * any class cast exception.
     *
     * @param <QT> the filtered field type, the type try to assign into
     * @param type the field type
     * @return a newly created field filter
     */
    public static final <QT> FieldFilter<QT> retrieveAs(final Class<QT> type) {
        // Step 1. Create a new field filter.
        FieldFilter<QT> e = c();
        // Step 2. Add filter.
        e.add(new Filter() {
            @Override
            public boolean isValid(Field f) {
                return TypeUtil.isAssignable(f.getType(), type);
            }});
        return e;
    }

    /**
     * Create a new filter and want to assign value of the specified type into the field.
     * 
     * @param <ST> the value type, try to assign a value of this type into the field
     * @param type the value type
     * @return a newly created field filter
     */
    public static final <ST> FieldFilter<ST> setWith(final Class<ST> type) {
        // Step 1. Create a new field filter.
        FieldFilter<ST> e = c();
        // Step 2. Add filter.
        e.add(new Filter() {
            @Override
            public boolean isValid(Field f) {
                return TypeUtil.isAssignable(type, f.getType());
            }
        });
        return e;
    }

    // ========================================================================
    // The class fields.
    // ========================================================================

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

    // ========================================================================
    // The instance methods.
    // ========================================================================

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
    public FieldFilter<FT> add(Filter filter) {
        filterList.add(filter);
        return this;
    }

    /**
     * Find the fields match this filter.
     *
     * @return the field result
     * @throws IllegalStateException if neither host variable nor host class was set
     */
    public final FieldResult<FT> find() {
        if (clazz != null) {
            return new FieldResult<FT>(host, FieldUtil.getFields(clazz, this));
        } else {
            throw new IllegalStateException("Please set host variable or class first!");
        }
    }

    // ========================================================================
    // The chain methods for clients.
    // ========================================================================

    /**
     * Set the host variable.
     *
     * @param host the host variable
     * @return this
     */
    public final FieldFilter<FT> host(Object host) {
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
    public final FieldFilter<FT> clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * Filter the fields with this name.<br>
     * A short cut for method {@link #withName(String)}.
     *
     * @param fieldName the field name
     * @return this
     */
    public final FieldFilter<FT> name(final String fieldName) {
        return withName(fieldName);
    }

    /**
     * Filter the fields with this name.
     *
     * @param fieldName the field name
     * @return this
     */
    public final FieldFilter<FT> withName(final String fieldName) {
        Check.notNull(fieldName, "fieldName should not be null.");
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
    public final FieldFilter<FT> nameLike(final String nameRegex) {
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
    public final FieldFilter<FT> onlyStatic() {
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
    public final FieldFilter<FT> noStatic() {
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
    public final FieldFilter<FT> noSynthetic() {
        return this.add(new Filter(){
            @Override
            public boolean isValid(Field f) {
                return !f.isSynthetic();
            }});
    }

}
