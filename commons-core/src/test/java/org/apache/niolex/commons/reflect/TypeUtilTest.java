package org.apache.niolex.commons.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeUtilTest extends TypeUtil {

    @Test
    public void testTypeMatches() throws Exception {
        assertTrue(typeMatches(int.class, int.class));
        assertTrue(typeMatches(int.class, Integer.class));
        assertFalse(typeMatches(int.class, long.class));
        assertFalse(typeMatches(short.class, int.class));
        assertFalse(typeMatches(long.class, int.class));
        assertFalse(typeMatches(int.class, short.class));
        assertTrue(typeMatches(float.class, float.class));
        assertTrue(typeMatches(float.class, Float.class));
        assertFalse(typeMatches(float.class, double.class));
    }

    @Test
    public void testTypeMatches2() throws Exception {
        assertFalse(typeMatches(short.class, Exception.class));
        assertFalse(typeMatches(int.class, String.class));
        assertFalse(typeMatches(Integer.class, short.class));
        assertFalse(typeMatches(Integer.class, long.class));
        assertFalse(typeMatches(Integer.class, float.class));
        assertTrue(typeMatches(Integer.class, int.class));
        assertTrue(typeMatches(Integer.class, Integer.class));
        assertTrue(typeMatches(Float.class, float.class));
        assertTrue(typeMatches(Float.class, Float.class));
        assertTrue(typeMatches(Exception.class, Exception.class));
        assertFalse(typeMatches(Exception.class, RuntimeException.class));
        assertFalse(typeMatches(String.class, Exception.class));
    }

    @Test
    public void testSafeCast() throws Exception {
        char a = safeCast('\n', char.class);
        assertEquals(a, '\n');
        Character c = '\r';
        char d = safeCast(c, char.class);
        char e = safeCast(c, Character.class);
        assertEquals(c.charValue(), e);
        assertEquals(d, e);

        byte b = -123;
        char f = (char) b;
        short s = safeCast(b, Short.class);
        int k = safeCast(b, int.class);
        long l = safeCast(b, Long.class);
        float t = safeCast(b, Float.class);
        double g = safeCast(b, double.class);

        assertEquals(b, s);
        assertEquals(b, k);
        assertEquals(b, l);
        assertEquals(b, t, 0.000001);
        assertEquals(b, g, 0.00000000001);
        assertEquals(k, s);
        System.out.println("cr = " + f + ", fl = " + t);
    }

    @Test
    public void testSafeCastBool() throws Exception {
        boolean b = true;
        boolean c = widenPrimitive(b, boolean.class);
        assertEquals(c, b);
    }

    @Test
    public void testSafeCastChar() throws Exception {
        char b = (char) -66;
        char s = safeCast(b, char.class);
        int k = safeCast(b, int.class);
        long l = safeCast(b, Long.class);
        float t = safeCast(b, Float.class);
        double g = safeCast(b, double.class);

        assertEquals(l, k);
        assertEquals(l, s);
        assertEquals(b, k);
        assertEquals(b, l);
        assertEquals(b, t, 0.000001);
        assertEquals(b, g, 0.00000000001);
        System.out.println("cs = " + s + ", fl = " + t);
    }

    @Test
    public void testSafeCastNullAll() throws Exception {
        safeCast(null, char.class);
        safeCast(null, Exception.class);
        safeCast(null, String.class);
    }

    @Test(expected = NullPointerException.class)
    public void testSafeCastNullAllEx() throws Exception {
        char c = safeCast(null, char.class);
        assertEquals(c, ' ');
    }

    @Test(expected = ClassCastException.class)
    public void testSafeCastByte2Char() throws Exception {
        byte b = 10;
        char c = safeCast(b, char.class);
        assertEquals(c, '\n');
    }

    @Test(expected = ClassCastException.class)
    public void testSafeCastEx() throws Exception {
        Exception c = safeCast("Like it.", Exception.class);
        assertNotNull(c);
    }

    @Test
    public void testWidenPrimitive() throws Exception {
        byte b = 10;
        byte c = safeCast(b, byte.class);
        assertEquals(c, '\n');
    }

    @Test
    public void testWidenPrimitiveChar() throws Exception {
        char b = 10;
        char c = safeCast(b, char.class);
        assertEquals(c, '\n');
    }

    @Test(expected = ClassCastException.class)
    public void testWidenPrimitiveErr() throws Exception {
        boolean b = true;
        byte c = safeCast(b, byte.class);
        assertEquals(c, '\n');
    }

    @Test
    public void testWidenPrimitiveBool() throws Exception {
        boolean b = true;
        boolean c = widenPrimitive(b, Boolean.class);
        assertEquals(c, b);
    }

    @Test(expected = ClassCastException.class)
    public void testWidenPrimitiveErrTotal() throws Exception {
        boolean b = true;
        Void c = widenPrimitive(b, Void.TYPE);
        assertEquals(c, b);
    }

}
