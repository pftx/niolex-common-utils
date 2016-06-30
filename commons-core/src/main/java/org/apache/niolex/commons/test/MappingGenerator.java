/**
 * MappingGenerator.java
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
package org.apache.niolex.commons.test;

/**
 * Generate the mapping between DB column name and java field name.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jun 29, 2016
 */
public class MappingGenerator {
    
    /**
     * Generate the java field name from the specified DB column name.
     * 
     * @param columnName the DB column name
     * @return the generated java field name
     */
    public static final String generateFieldName(String columnName) {
        StringBuilder sb = new StringBuilder();
        columnName = columnName.toLowerCase();
        boolean upperFlag = false;
        
        for (int i = 0; i < columnName.length(); ++i) {
            char c = columnName.charAt(i);
            if (c == '_') {
                upperFlag = true;
            } else {
                if (upperFlag) {
                    // Change the first char after underscore to upper case.
                    upperFlag = false;
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Tidy the DB column name to pure alias name or column name.
     * 
     * @param originalColumnName the original DB column name
     * @return the tidied column name
     */
    public static final String tidyDBColumnName(String originalColumnName) {
        // 0. trim space char from both ends.
        int start = 0;
        while (Character.isWhitespace(originalColumnName.charAt(start))) {
            ++start;
        }
        int end = originalColumnName.length() - 1;
        while (Character.isWhitespace(originalColumnName.charAt(end))) {
            --end;
        }
        String s = originalColumnName.substring(start, end + 1);
        
        // 1. remove function or sub-query.
        if (s.indexOf('(') != -1) {
            int r = s.lastIndexOf(')');
            if (r == -1) {
                throw new IllegalArgumentException("Not a valid column name: " + originalColumnName);
            }
            s = s.substring(r + 1);
        }
        
        // 2. remove dot prefix.
        int dot = s.lastIndexOf('.');
        if (dot != -1) {
            s = s.substring(dot + 1);
        }
        
        // 3. remove alias prefix.
        int space = s.lastIndexOf(' ');
        if (space != -1) {
            s = s.substring(space + 1);
        }
        
        return s;
    }

    /**
     * Generate the mapping string according to the select statement.
     * 
     * @param template the mapping template
     * @param select the select statement
     * @return the generated mapping string
     */
    public static final String generateMapping(String template, String select) {
        StringBuilder sb = new StringBuilder();
        select = select.toUpperCase();
        int start = select.indexOf("SELECT") + 7;
        int end = select.indexOf("FROM") - 1;
        
        if (start == 6) {
            start = 0;
        }
        if (end == -2) {
            end = select.length();
        }
        
        int right = 0;
        // Split all the tokens into DB column name.
        for (int left = start; left < end; ++left) {
            Inner:
            for (int j = left; j < end; ++j) {
                switch (select.charAt(j)) {
                    case ',':
                        right = j;
                        break Inner;
                    case '(':
                        j = TidyUtil.matchParenthesis(select, j) + 1;
                        break;
                }
            }
        
            if (right == 0) {
                right = end;
            }
            
            String s = select.substring(left, right);
            left = right;
            right = 0;
            
            s = tidyDBColumnName(s);
            sb.append(String.format(template, generateFieldName(s), s)).append('\n');
        }
        
        return sb.toString();
    }
}
