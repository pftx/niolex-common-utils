/**
 * ObjToStringUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.util.Const;

/**
 * ObjToStringUtil translate general objects into string.
 * We only nest into 5 levels.<pre>
 * Object fields will be translate like this:
 * {
 *   field1=xxx
 *   field2=zzz
 *   ...
 * }
 * Arrays will be translate like this:
 * (length)[
 *   0 => aaa
 *   1 => bbb
 *   ...
 * ]
 * </pre>
 * This class is for test-using only, we are not sure that all cases are covered.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0
 * @since 2011-1-24
 */
public abstract class ObjToStringUtil implements Const {

    /**
     * The indent used in format object.
     */
    public static final int INDENT = 2;

    /**
     * The line separator length.
     */
    public static final int LSP_LEN = LINE_SP.length();

    /**
     * The string used to generate indent.
     */
    public static final String INDENT_STR = "                         ";

    /**
     * translate general objects into string.
     *
     * @param o the object to be translated
     * @return The string representation of this Object.
     */
    public static final String objToString(Object o) {
        StringBuilder sb = new StringBuilder();
        printFields(o, sb, INDENT);
        return sb.toString();
    }

    /**
     * Recursively print all the fields of this object into the string builder.
     *
     * @param o the current object
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printFields(Object o, StringBuilder sb, int indentation) {
        if (o == null) {
            sb.append("null");
        } else if (indentation > 6 * INDENT) {
            // Too many levels, so we just use native Object.toString()
            sb.append(o);
        } else if (o.getClass().isArray()) {
            printArray(o, sb, indentation);
        } else if (o instanceof Collection<?>) {
            printCollection((Collection<?>) o, sb, indentation);
        } else if(o instanceof Map<?, ?>) {
            printMap((Map<?, ?>) o, sb, indentation);
        } else if (o.getClass().getName().startsWith("java")) {
            sb.append(o);
        } else {
            printObject(o, sb, indentation);
        }
    }

    /**
     * Recursively print array inner items into the string builder.
     *
     * @param o the current array
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printArray(Object o, StringBuilder sb, int indentation) {
        if (o instanceof byte[]) {
            byte[] os = (byte[]) o;
            sb.append("(").append(os.length).append(")[");
            sb.append(Base16Util.byteToBase16(os)).append(']');
        } else {
            // Use Reflection to visit array.
            int length = Array.getLength(o);
            sb.append("(").append(length).append(")[");
            if (length != 0) {
                sb.append(LINE_SP);
                for (int i = 0; i < length; ++i) {
                    printArrayItem(i, Array.get(o, i), sb, indentation);
                }
                generateIndentation(sb, indentation - INDENT);
            }
            sb.append(']');
        }
    }

    /**
     * Recursively print collection inner items into the string builder.
     *
     * @param os the current collection
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printCollection(Collection<? extends Object> os, StringBuilder sb, int indentation) {
        sb.append("(").append(os.size()).append(")[");
        if (os.size() != 0) {
            sb.append(LINE_SP);
            int i = 0;
            for (Object o2 : os) {
                printArrayItem(i++, o2, sb, indentation);
            }
            generateIndentation(sb, indentation - INDENT);
        }
        sb.append(']');
    }

    /**
     * Print the array item into the string builder.
     *
     * @param index the item index
     * @param o2 the item object
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printArrayItem(int index, Object o2, StringBuilder sb, int indentation) {
        generateIndentation(sb, indentation);
        sb.append(index);
        sb.append(" => ");
        printFields(o2, sb, indentation + INDENT);
        sb.append(LINE_SP);
    }

    /**
     * Recursively print map inner items into the string builder.
     *
     * @param map the current map
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printMap(Map<? extends Object, ? extends Object> map, StringBuilder sb, int indentation) {
        sb.append("{").append(LINE_SP);
        for (Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
            printObjectItem(entry.getKey().toString(), entry.getValue(), sb, indentation);
        }
        printObjectEnd(sb, indentation);
    }

    /**
     * Recursively print object fields into the string builder by reflection.
     *
     * @param o the current object
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printObject(Object o, StringBuilder sb, int indentation) {
        sb.append("{").append(LINE_SP);
        for (Field f : o.getClass().getDeclaredFields()) {
            if (f.isSynthetic() || (f.getModifiers() & Modifier.STATIC) > 0)
                continue;
            printObjectItem(f.getName(), FieldUtil.safeGetFieldValue(f, o), sb, indentation);
        }
        printObjectEnd(sb, indentation);
    }

    /**
     * Print the array item into the string builder.
     *
     * @param key the object item key
     * @param value the object item value
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printObjectItem(String key, Object value, StringBuilder sb, int indentation) {
        generateIndentation(sb, indentation);
        sb.append(key).append("=");
        printFields(value, sb, indentation + INDENT);
        sb.append(LINE_SP);
    }

    /**
     * Generate object end into the string builder.
     *
     * @param sb the string builder
     * @param indentation the current indentation
     */
    private static final void printObjectEnd(StringBuilder sb, int indentation) {
        int i = sb.length() - LSP_LEN;
        // If string builder end with new line, we need to delete it.
        if (sb.charAt(i - 1) == '{') {
            sb.delete(i, sb.length());
        } else {
            generateIndentation(sb, indentation - INDENT);
        }
        sb.append("}");
    }

    /**
     * Generate indentation.
     *
     * @param sb the string builder used to append indentation
     * @param indentation the current indentation
     */
    private static final void generateIndentation(StringBuilder sb, int indentation) {
        sb.append(INDENT_STR.substring(0, indentation));
    }

}
