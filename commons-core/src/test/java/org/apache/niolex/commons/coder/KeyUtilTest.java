package org.apache.niolex.commons.coder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.apache.niolex.commons.coder.KeyUtil.*;

import javax.crypto.KeyGenerator;

import org.junit.Test;

public class KeyUtilTest {

    @Test
    public void testGetKeyGeneratorTripleDES() throws Exception {
        KeyGenerator keyGen = getKeyGenerator("TripleDES");
        assertNotNull(keyGen);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetKeyGeneratorRC3() throws Exception {
        KeyGenerator keyGen = getKeyGenerator("RC3");
        assertNull(keyGen);
    }

    @Test
    public void testGenKeyDES() throws Exception {
        String dKey = KeyUtil.genKey("DES");
        System.out.println("DES => " + dKey);
        assertEquals(32, dKey.length());
    }

    @Test
    public void testGenKeyAES() throws Exception {
        String dKey = KeyUtil.genKey("AES", KeyUtil.DEFAULT_IV, 0);
        System.out.println("AES => " + dKey);
        assertEquals(44, dKey.length());
    }

    @Test
    public void testGenKeyAES256() throws Exception {
        String dKey = KeyUtil.genKey("t7LrBCNVF+xKbc4Lsq73JHDQ0ZySEL3rD4LjQKSHUTcbfRbxfnwJfLqJqZMYYAkajTisgaxs84w=", "AES", KeyUtil.DEFAULT_IV, 256);
        System.out.println("AES => " + dKey);
        assertEquals(64, dKey.length());
    }

    @Test
    public void testGenKeyRC2() throws Exception {
        String dKey = KeyUtil.genKey("t7LrBCNVF+xKbc4Lsq73JHDQ0ZySEL3rD4LjQKSHUTcbfRbxfnwJfLqJqZMYYAkajTisgaxs84w=", "RC2", KeyUtil.DEFAULT_IV, 256);
        System.out.println("RC2 => " + dKey);
        assertEquals(64, dKey.length());
    }

    @Test
    public void testGenKeyTripleDES() throws Exception {
        String dKey = KeyUtil.genKey("TripleDES");
        System.out.println("TripleDES => " + dKey);
        assertEquals(56, dKey.length());
    }

    @Test
    public void testGenKeyTripleDES112() throws Exception {
        String dKey = KeyUtil.genKey("TripleDES", KeyUtil.DEFAULT_IV, 112);
        System.out.println("TripleDES => " + dKey);
        assertEquals(56, dKey.length());
    }

}
