/**
 * SeriUtil.java
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
package org.apache.niolex.commons.seri;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The utility class to help do serialization.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.0.1
 * @since Jun 28, 2016
 */
public class SeriUtil {

    /**
     * This class is used to pack a Java Type into a Jackson TypeReference.
     *
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.0, Date: 2012-7-24
     * @param <T> the java class type
     */
    public static class TypeRe<T> extends TypeReference<T> {
        // The real type
        private Type type;

        public TypeRe(Type type) {
            super();
            this.type = type;
        }

        @Override
        public Type getType() {
            return type;
        }
    }

    /**
     * Pack the specified Java Type to Jackson TypeReference.
     * 
     * @param <T> the java class type
     * @param type the Java type to be packed
     * @return the packed Jackson TypeReference
     */
    public static final <T> TypeReference<T> packJavaType(Type type) {
        return new TypeRe<T>(type);
    }

    /**
     * Pack the Java Types array to Jackson TypeReference list.
     *
     * @param <T> the java class type
     * @param generic the generic Java types array
     * @return the packed Jackson TypeReference list
     */
    public static final <T> List<TypeReference<T>> packJavaTypes(Type[] generic) {
        List<TypeReference<T>> list = new ArrayList<TypeReference<T>>(generic.length);
        for (Type tp : generic) {
            list.add(new TypeRe<T>(tp));
        }
        return list;
    }

    /**
     * Cast the specified Java Type to Java Class.
     * 
     * @param <T> the java class type
     * @param type the Java type
     * @return the Java class casted from the specified type
     * @throws ClassCastException if the Java Type is not a Java Class
     */
    @SuppressWarnings("unchecked")
    public static final <T> Class<T> castJavaType(Type type) {
        // 1. Get raw type out of ParameterizedType.
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) (type)).getRawType();
        }

        // 2. Check whether it's Java Class.
        if (type instanceof Class) {
            return (Class<T>) type;
        } else {
            throw new ClassCastException(type + " can not be casted to Class.");
        }
    }

    /**
     * Cast the generic java types array to Java Class array.
     *
     * @param <T> the java class type
     * @param generic the generic type array
     * @return the casted java class array
     * @throws ClassCastException if any item in the generic array is not a class
     */
    public static final <T> Class<T>[] castJavaTypes(Type[] generic) {
        @SuppressWarnings("unchecked")
        Class<T>[] array = (Class<T>[]) new Class[generic.length];

        for (int i = 0; i < generic.length; ++i) {
            array[i] = castJavaType(generic[i]);
        }
        return array;
    }

}
