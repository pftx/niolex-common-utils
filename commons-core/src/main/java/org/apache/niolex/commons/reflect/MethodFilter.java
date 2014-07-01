/**
 * MethodFilter.java
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.ClassUtils;
import org.apache.niolex.commons.reflect.MethodUtil.Filter;

/**
 * This class is for {@link MethodUtil} to filter methods.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class MethodFilter implements Filter {

    // ========================================================================
    // The static methods.
    // ========================================================================

    /**
     * Create a new method filter. This method is a short cut of {@link #create()}.
     *
     * @return a new method filter
     */
    public static final MethodFilter c() {
        return create();
    }

    /**
     * Create a new method filter.
     *
     * @return a new method filter
     */
    public static final MethodFilter create() {
        return new MethodFilter();
    }

    // ========================================================================
    // The instance fields & methods.
    // ========================================================================

    private boolean includeInterfaces;
    private boolean includeSuper;
    private boolean includeStatic;
    private boolean includeSynthetic;
    private boolean includeAbstract;
    private String methodName;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.reflect.MethodUtil.Filter#isIncludeInterfaces()
     */
    @Override
    public boolean isIncludeInterfaces() {
        return includeInterfaces;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.reflect.MethodUtil.Filter#isIncludeSuper()
     */
    @Override
    public boolean isIncludeSuper() {
        return includeSuper;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.reflect.MethodUtil.Filter#isValid(java.lang.reflect.Method)
     */
    @Override
    public boolean isValid(Method m) {
        if (methodName != null && !methodName.equals(m.getName())) {
            return false;
        }
        if (returnType != null && !ClassUtils.isAssignable(m.getReturnType(), returnType, true)) {
            return false;
        }
        if (parameterTypes != null && !ClassUtils.isAssignable(parameterTypes, m.getParameterTypes(), true)) {
            return false;
        }
        if (!includeStatic && Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        if (!includeAbstract && Modifier.isAbstract(m.getModifiers())) {
            return false;
        }
        if (!includeSynthetic && m.isSynthetic()) {
            return false;
        }
        return true;
    }

    /**
     * Include all things.
     *
     * @return this
     */
    public MethodFilter includeAll() {
        this.includeSuper = true;
        this.includeInterfaces = true;
        this.includeStatic = true;
        this.includeSynthetic = true;
        this.includeAbstract = true;
        return this;
    }

    /**
     * If include interfaces, we automatically includes super and include
     * abstract methods too.
     */
    public MethodFilter includeInterfaces() {
        this.includeSuper = true;
        this.includeInterfaces = true;
        this.includeAbstract = true;
        return this;
    }

    /**
     * Include all the super methods.
     */
    public MethodFilter includeSuper() {
        this.includeSuper = true;
        return this;
    }

    /**
     * Include all the static methods.
     */
    public MethodFilter includeStatic() {
        this.includeStatic = true;
        return this;
    }

    /**
     * Include all the synthetic methods.
     */
    public MethodFilter includeSynthetic() {
        this.includeSynthetic = true;
        return this;
    }

    /**
     * Include all the abstract methods.
     */
    public MethodFilter includeAbstract() {
        this.includeAbstract = true;
        return this;
    }

    /**
     * Set the method name. This method is a short cut of {@link #methodName(String)}.
     *
     * @param methodName the methodName to set
     */
    public MethodFilter n(String methodName) {
        return methodName(methodName);
    }

    /**
     * Set the method name.
     *
     * @param methodName the methodName to set
     */
    public MethodFilter methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * Set the method return type. This method is a short cut of {@link #returnType(Class)}.
     *
     * @param returnType the returnType to set
     */
    public MethodFilter r(Class<?> returnType) {
        return returnType(returnType);
    }

    /**
     * Set the method return type.
     *
     * @param returnType the returnType to set
     */
    public MethodFilter returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    /**
     * Set the method parameter types. This method is a short cut of {@link #parameterTypes(Class[])}.
     *
     * @param parameterTypes the parameterTypes to set
     */
    public MethodFilter p(Class<?>... parameterTypes) {
        return parameterTypes(parameterTypes);
    }

    /**
     * Set the method parameter types.
     *
     * @param parameterTypes the parameterTypes to set
     */
    public MethodFilter parameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

}
