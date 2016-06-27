/**
 * SQLGenerator.java
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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.reflect.FieldFilter;

/**
 * Generate SQL by the corresponding Java entity class.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jun 24, 2016
 */
public class SQLGenerator {
    
    /**
     * Generate the 'all-upper-case word separated by one underscore' form of database column name
     * from the specified CamelCase field name. <br>
     * i.e. fieldName =&gt; FIELD_NAME
     * 
     * @param fieldName the java field name
     * @return the generated database column name
     */
    public static final String generateColumnName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        boolean upperFlag = true;
        for (int i = 0; i < fieldName.length(); ++i) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                if (upperFlag) {
                    // Several upper case char together.
                    sb.append(c);
                } else {
                    // The first upper case char.
                    upperFlag = true;
                    sb.append('_');
                    sb.append(c);
                }
            } else {
                upperFlag = false;
                sb.append(Character.toUpperCase(c));
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Generate the map which contains mapping for all the instance fields to DB column name.
     * 
     * @param clazz the Java entity class
     * @return the generated map as {field name =&gt; DB Column name}.
     */
    public static final Map<String, String> generateColumnMap(Class<?> clazz) {
        Map<String, String> map = new HashMap<String, String>();
        
        List<Field> list = FieldFilter.c().noStatic().noSynthetic().clazz(clazz).find().results();
        
        for (Field f : list) {
            String fieldName = f.getName();
            map.put(fieldName, generateColumnName(fieldName));
        }
        
        return map;
    }

    /**
     * Generate the insert SQL.
     * 
     * @param clazz the Java entity class
     * @param tableName the DB table name
     * @return the generated insert SQL
     */
    public static final String generateInsert(Class<?> clazz, String tableName) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbK = new StringBuilder();
        StringBuilder sbV = new StringBuilder();
        Map<String, String> map = generateColumnMap(clazz);
        
        sb.append("INSERT INTO ").append(tableName).append(" (");
        
        for (Map.Entry<String, String> en : map.entrySet()) {
            sbV.append('#').append(en.getKey()).append(", ");
            sbK.append(en.getValue()).append(", ");
        }
        sbK.setLength(sbK.length() - 2);
        sbV.setLength(sbV.length() - 2);
        
        sb.append(sbK.toString()).append(") VALUES (");
        sb.append(sbV.toString()).append(");");
        
        return sb.toString();
    }
    
    /**
     * Generate the update SQL.
     * 
     * @param clazz the Java entity class
     * @param tableName the DB table name
     * @param whereConditions the query where conditions
     * @return the generated update SQL
     */
    public static final String generateUpdate(Class<?> clazz, String tableName, String ...whereConditions) {
        StringBuilder sb = new StringBuilder();
        StringBuilder where = new StringBuilder();
        Map<String, String> map = generateColumnMap(clazz);
        
        sb.append("UPDATE ").append(tableName).append(" SET ");
        
        for (Map.Entry<String, String> en : map.entrySet()) {
            if (StringUtil.isIn(en.getKey(), whereConditions)) {
                where.append(en.getValue()).append(" = #").append(en.getKey()).append(" AND ");
            } else {
                sb.append(en.getValue()).append(" = #").append(en.getKey()).append(", ");
            }
        }
        sb.setLength(sb.length() - 2);
        where.setLength(where.length() - 5);
        
        sb.append(" WHERE ").append(where.toString()).append(';');
        
        return sb.toString();
    }
    
    /**
     * Generate the select SQL.
     * 
     * @param clazz the Java entity class
     * @param tableName the DB table name
     * @param whereConditions the query where conditions
     * @return the generated select SQL
     */
    public static final String generateSelect(Class<?> clazz, String tableName, String ...whereConditions) {
        StringBuilder sb = new StringBuilder();
        StringBuilder where = new StringBuilder();
        Map<String, String> map = generateColumnMap(clazz);
        
        sb.append("SELECT ");
        
        for (Map.Entry<String, String> en : map.entrySet()) {
            if (StringUtil.isIn(en.getKey(), whereConditions)) {
                where.append(en.getValue()).append(" = #").append(en.getKey()).append(" AND ");
            } else {
                sb.append(en.getValue()).append(", ");
            }
        }
        sb.setLength(sb.length() - 2);
        where.setLength(where.length() - 5);
        
        sb.append(" FROM ").append(tableName).append(" WHERE ").append(where.toString()).append(';');
        
        return sb.toString();
    }
    
}
