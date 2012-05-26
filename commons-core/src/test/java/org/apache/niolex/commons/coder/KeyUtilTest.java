package org.apache.niolex.commons.coder;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KeyUtilTest {

	@Test
	public void testGenKey() throws Exception {
		String dKey = KeyUtil.genKey("t7LrBCNVF+xKbc4Lsq73JHDQ0ZySEL3rD4LjQKSHUTcbfRbxfnwJfLqJqZMYYAkajTisgaxs84w=", "AES");
		System.out.println("中间结果：" + dKey);
		assertTrue(dKey.length() > 10);
	}

	@Test
	public void testGenKey_2() throws Exception {
		String dKey = KeyUtil.genKey("t7LrBCNVF+xKbc4Lsq73JHDQ0ZySEL3rD4LjQKSHUTcbfRbxfnwJfLqJqZMYYAkajTisgaxs84w=", "RC2");
		System.out.println("中间结果：" + dKey);
		assertTrue(dKey.length() > 10);
	}

	@Test
	public void testGenKeyNull() throws Exception {
		String dKey = KeyUtil.genKey(null, "AES");
		System.out.println("中间结果：" + dKey);
		assertTrue(dKey.length() > 10);
	}

	@Test
	public void testGenKeyNull_2() throws Exception {
		String dKey = KeyUtil.genKey("RC2");
		System.out.println("中间结果：" + dKey);
		assertTrue(dKey.length() > 10);
	}

}
