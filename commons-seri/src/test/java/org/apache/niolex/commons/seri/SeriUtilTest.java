package org.apache.niolex.commons.seri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.seri.Proto.Person;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

public class SeriUtilTest extends SeriUtil {


    public List<String> method4(Pair<?, Map<String, Integer>> set, Integer i) {return null;}
    
    public Method m = MethodUtil.getFirstMethod(this, "method4");
    
    @Test
    public void testPackJavaType() throws Exception {
        TypeReference<Object> clz = SeriUtil.packJavaType(m.getGenericReturnType());
        assertTrue(clz.getType() instanceof ParameterizedType);
    }

    @Test
    public void testPackJavaTypes() throws Exception {
        List<TypeReference<Object>> list = packJavaTypes(m.getGenericParameterTypes());
        assertEquals(Integer.class, list.get(1).getType());
    }

    @Test
    public void testCastJavaType1() throws Exception {
        Class<?> cls =  castJavaType(m.getGenericReturnType());
        assertEquals(List.class, cls);
    }

    @Test
    public void testCastJavaType2() throws Exception {
        Class<?> cls =  castJavaType(Exception.class);
        assertEquals(Exception.class, cls);
    }
    
    @Test
    public void testCastJavaType3() throws Exception {
        Class<?> cls =  castJavaType(Exception.class);
        assertEquals(Exception.class, cls);
    }

    @Test(expected=ClassCastException.class)
    public void testCastJavaTypesEx() throws Exception {
        castJavaType(((ParameterizedType)(m.getGenericParameterTypes()[0])).getActualTypeArguments()[0]);
    }

    @Test
    public void testCastJavaTypes() throws Exception {
        Class<?>[] arr = castJavaTypes(m.getGenericParameterTypes());
        assertEquals(2, arr.length);
        assertEquals(Pair.class, arr[0]);
        assertEquals(Integer.class, arr[1]);
    }

    @Test
    public void thisIsGood() {
        Set<String> set = new HashSet<String>();
        Class<?>[] arr = castJavaTypes(new Type[] {set.getClass().getGenericSuperclass(), Person.class});
        assertEquals(2, arr.length);
        assertEquals(AbstractSet.class, arr[0]);
        assertEquals(Person.class, arr[1]);
    }
    
}
