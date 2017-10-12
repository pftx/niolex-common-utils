package org.apache.niolex.commons.codec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;

public class CommonBase64Test extends CommonBase64 {

    public static final Base64Variant DEFAULT = Base64Variants.MIME_NO_LINEFEEDS;
    public static final Base64Variant URL_SAFE = Base64Variants.MODIFIED_FOR_URL;
    public static final String ASC = FileUtil.getCharacterFileContentFromClassPath("Base64.txt", CommonBase64Test.class,
            StringUtil.UTF_8);
    public static final String JPG = FileUtil.getCharacterFileContentFromClassPath("Jpg.base64", CommonBase64Test.class,
            StringUtil.UTF_8);

    @Test
    public void testEncodeByteArray() throws Exception {
        for (int i = 0; i < 100000; ++i) {
            byte[] b = MockUtil.randByteArray(100 + i % 7);
            String s = encode(b);
            byte[] o = DEFAULT.decode(s);
            assertArrayEquals(b, o);
            byte[] o2 = decode(s);
            assertArrayEquals(b, o2);

            String q = encodeURL(b);
            byte[] w = URL_SAFE.decode(q);
            assertArrayEquals(b, w);
            byte[] w2 = decode(q);
            assertArrayEquals(b, w2);
        }
    }

    @Test
    public void testEncodeAsc() {
        byte[] a = DEFAULT.decode(ASC);
        byte[] b = decode(ASC);
        assertArrayEquals(a, b);
        String s = encode(b, true, 64, false);
        assertEquals(ASC, s);
    }

    @Test
    public void testDecodeJpg() {
        byte[] a = DEFAULT.decode(JPG);
        byte[] b = decode(JPG);
        assertArrayEquals(a, b);
    }

    @Test
    public void testEncodeCoverAll() throws Exception {
        byte[] a = new byte[1];
        byte[] b = new byte[2];
        byte[] c = new byte[3];
        byte[] d;
        boolean fast = true;

        for (int i = -128; i < 128; ++i) {
            a[0] = (byte) i;
            String s1 = encode(a);
            String s2 = DEFAULT.encode(a);
            assertEquals(s1, s2);
            d = decode(s1);
            assertArrayEquals(a, d);

            String s3 = encodeURL(a);
            String s4 = URL_SAFE.encode(a);
            assertEquals(s3, s4);
            d = decode(s3);
            assertArrayEquals(a, d);

            for (int j = -128; j < 128; ++j) {
                b[0] = (byte) i;
                b[1] = (byte) j;

                String s5 = encode(b);
                String s6 = DEFAULT.encode(b);
                assertEquals(s5, s6);
                d = decode(s5);
                assertArrayEquals(b, d);

                String s7 = encodeURL(b);
                String s8 = URL_SAFE.encode(b);
                assertEquals(s7, s8);
                d = decode(s7);
                assertArrayEquals(b, d);

                for (int k = -128; k < 128; ++k) {
                    c[0] = (byte) i;
                    c[1] = (byte) j;
                    c[2] = (byte) k;

                    String sa = encode(c);
                    String sb = DEFAULT.encode(c);
                    assertEquals(sa, sb);
                    d = decode(sa);
                    assertArrayEquals(c, d);

                    String sc = encodeURL(c);
                    String sd = URL_SAFE.encode(c);
                    assertEquals(sc, sd);
                    d = decode(sc);
                    assertArrayEquals(c, d);

                    if (fast && k > -100) {
                        break;
                    }
                }
            }
        }
    }

    @Test
    public void testEncTail() throws Exception {
        String alpha = "~!@#$%^&*()_+`1234567890-=[]\\{}|;:'<>?,./qwertyuiopasdfghjklzxcvbnmMNBVCXZASDGFHJKL";
        StringBuilder out = new StringBuilder();
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, true, 1);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, true, 2);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, true, 3);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, true, 4);
        assertEquals("3q==3q;=3q;3q;", out.toString());
        out.setLength(0);

        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, false, 1);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, false, 2);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, false, 3);
        encTail(alpha.toCharArray(), out, (byte) 66, (byte) -104, false, 4);
        assertEquals("3q3q;3q;3q;", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeByteArrayBooleanIntBoolean1() throws Exception {
        encode("not yet implemented".getBytes(), true, 100, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeByteArrayBooleanIntBoolean2() throws Exception {
        encode("not yet implemented".getBytes(), false, 33, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeByteArrayBooleanIntBoolean3() throws Exception {
        encode("not yet implemented".getBytes(), true, -2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeByteArrayBooleanIntBoolean4() throws Exception {
        encode("not yet implemented".getBytes(), false, 75, false);
    }

    @Test
    public void testIsBase64() throws Exception {
        assertFalse(isBase64("1\r\n"));
        assertFalse(isBase64("2$"));
        assertFalse(isBase64("3^"));
        assertFalse(isBase64("4="));
        assertFalse(isBase64("€="));
        assertTrue(isBase64("ab=="));
    }

}
