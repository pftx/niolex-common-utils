/**
 * TypeUtil.java
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
package org.apache.niolex.commons.reflect;

import org.apache.commons.lang3.ClassUtils;
import org.apache.niolex.commons.test.Check;

/**
 * Doing type casting, type matching here.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.0
 * @since Oct 24, 2016
 */
public class TypeUtil extends ClassUtils {

    /**
     * Test whether the two types matches. We only consider auto boxing here.
     * For example, int.class matches int.class and Integer.class.
     * 
     * @param aType the fist type
     * @param bType the second type
     * @return true if the two types matches, false otherwise
     */
    public static boolean typeMatches(Class<?> aType, Class<?> bType) {
        // A is primitive.
        if (aType.isPrimitive()) {
            if (!bType.isPrimitive()) {
                bType = wrapperToPrimitive(bType);
            }
            return aType.equals(bType);
        }

        // A is not primitive.
        if (bType.isPrimitive()) {
            return bType.equals(wrapperToPrimitive(aType));
        }

        // Both types are not primitive.
        return aType.equals(bType);
    }

    /**
     * Safely cast the original object into the destination type, consider auto boxing and
     * widenings of primitive types and {@code null}s.
     * 
     * @param <T> the destination type
     * @param original the original object
     * @param destination the destination type to be casted to
     * @return the casted object as the destination type
     * @throws ClassCastException if we can not safely cast the original object into the destination type
     */
    @SuppressWarnings("unchecked")
    public static <T> T safeCast(Object original, Class<T> destination) {
        if (original == null) {
            return null;
        }

        if (destination.isAssignableFrom(original.getClass())) {
            return (T) original;
        }

        return widenPrimitive(original, destination);
    }

    /**
     * Widen the primitive to the destination type.
     * 
     * @param <T> the destination type
     * @param original the original object
     * @param destination the destination type to be widening to
     * @return the widening object as the destination type
     * @throws ClassCastException if we can not safely cast the original object into the destination type
     */
    @SuppressWarnings("unchecked")
    public static <T> T widenPrimitive(Object original, Class<T> destination) {
        // Consider widenings of primitive types.
        Class<?> toPrim = wrapperToPrimitive(destination);
        if (toPrim == null && destination.isPrimitive()) {
            toPrim = destination;
        }
        if (toPrim == null) {
            // The destination is not primitive or the corresponding wrapper.
            // Just throw ClassCastException here.
            return (T) original;
        }

        // Primitive widenings allow an int to be assigned to a long, float or double.
        if (isAssignable(original.getClass(), toPrim)) {
            Number n = null;
            if (original instanceof Number) {
                n = (Number) original;
            } else if (original instanceof Character) {
                // Cast char to int first.
                n = ((int) ((Character) original).charValue());
            } else {
                // Just throw ClassCastException here.
                return (T) original;
            }

            // Cast to destination here.
            if (toPrim.equals(byte.class)) {
                return (T) ((Byte) n.byteValue());
            }
            if (toPrim.equals(short.class)) {
                return (T) ((Short) n.shortValue());
            }
            if (toPrim.equals(int.class)) {
                return (T) ((Integer) n.intValue());
            }
            if (toPrim.equals(long.class)) {
                return (T) ((Long) n.longValue());
            }
            if (toPrim.equals(float.class)) {
                return (T) ((Float) n.floatValue());
            }
            if (toPrim.equals(double.class)) {
                return (T) ((Double) n.doubleValue());
            }
            Check.eq(toPrim, char.class, "A new primitive type found, please contact the author! - " + toPrim);
            return (T) (Character) ((char) n.intValue());
        }
        throw new ClassCastException("Can not cast from " + original.getClass() + " to " + destination);
    }

}
