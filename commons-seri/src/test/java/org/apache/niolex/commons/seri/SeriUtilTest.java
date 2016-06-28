package org.apache.niolex.commons.seri;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.seri.Proto.Person;
import org.apache.niolex.commons.seri.Proto.PhoneNumber;
import org.apache.niolex.commons.seri.Proto.PhoneType;
import org.junit.Test;

public class SeriUtilTest {

    @Test
    public void testPackJavaType() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testPackJavaTypes() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testCastJavaType() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testCastJavaTypes() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    public void method4(Pair<?, Map<String, Integer>> set) {}

    /**
     * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test(expected=SeriException.class)
    public void testSeriOneErr() {
        String s = "Not yet implemented";
        byte[] tar = seriOne(s);
        Method m = MethodUtil.getFirstMethod(this, "method4");
        parseOne(tar, (Class<Object>) ((ParameterizedType)(m.getGenericParameterTypes()[0])).getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    @Test(expected=SeriException.class)
    public void thisIsGood() {
        int i = 2345;
        Person p = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
                .setName("Niolex [" + i + "]")
                .addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
                .build();
        byte[] ret = p.toByteArray();
        Set<String> set = new HashSet<String>();
        parseMulti(ret, new Class[] {(Class<Object>) set.getClass().getGenericSuperclass(), Person.class});
    }
}
