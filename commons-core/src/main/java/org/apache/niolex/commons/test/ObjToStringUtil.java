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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * ObjToStringUtil translate general objects into string.
 * We only nest into 5 levels.
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
 *
 * This class is for test-using only, not all cases are covered.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0
 * @since 2011-1-24
 */
public abstract class ObjToStringUtil {

    /**
     * translate general objects into string.
     *
     * @param o
     * @return The string representation of this Object.
     */
    public static final String objToString(Object o) {
        StringBuilder sb = new StringBuilder();
        printFields(o, sb, 2);
        return sb.toString();
    }

    private static final void printFields(Object o, StringBuilder sb, int indentation) {
        if (o == null) {
            sb.append("null");
            return;
        }
        if (indentation > 12) {
            sb.append(o);
            return;
        }
        if (o instanceof Object[]) {
            Object[] os = (Object[]) o;
            sb.append("(").append(os.length).append(")[\n");
            int i = 0;
            for (Object o2 : os) {
                generateIndentation(sb, indentation);
                sb.append(i++);
                sb.append(" => ");
                printFields(o2, sb, indentation + 2);
                sb.append("\n");
            }
            i = sb.length() - 1;
            if (os.length == 0)
                sb.setCharAt(i, ']');
            else {
                generateIndentation(sb, indentation - 2);
                sb.append(']');
            }
            return;
        }
        if (o instanceof Collection<?>) {
            Collection<? extends Object> os = (Collection<?>) o;
            sb.append("(").append(os.size()).append(")[\n");
            int i = 0;
            for (Object o2 : os) {
                generateIndentation(sb, indentation);
                sb.append(i++);
                sb.append(" => ");
                printFields(o2, sb, indentation + 2);
                sb.append("\n");
            }
            i = sb.length() - 1;
            if (os.size() == 0)
                sb.setCharAt(i, ']');
            else {
                generateIndentation(sb, indentation - 2);
                sb.append(']');
            }
            return;
        }
        if (o.getClass().getName().startsWith("java") && !o.getClass().getName().equals("java.lang.Object")) {
            sb.append(o);
            return;
        }
        sb.append("{\n");
        for (Field f : o.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isSynthetic() || (f.getModifiers() & Modifier.STATIC) > 0)
                continue;
            try {
                generateIndentation(sb, indentation);
                sb.append(f.getName()).append("=");
                printFields(f.get(o), sb, indentation + 2);
                sb.append("\n");
            } catch (Exception e) {
            }
        }
        int i = sb.length() - 2;
        if (sb.charAt(i) == '{')
            sb.setCharAt(i + 1, '}');
        else {
            generateIndentation(sb, indentation - 2);
            sb.append("}");
        }
    }

    private static final void generateIndentation(StringBuilder sb, int indentation) {
        while ((indentation = indentation - 2) >= 0) {
            sb.append("  ");
        }
    }
}
