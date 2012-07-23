package org.apache.niolex.commons.seri;

import static org.apache.niolex.commons.seri.SmileUtil.bin2Obj;
import static org.apache.niolex.commons.seri.SmileUtil.obj2bin;
import static org.apache.niolex.commons.seri.SmileUtil.readObj;
import static org.apache.niolex.commons.seri.SmileUtil.writeObj;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.test.Benchmark.Bean;
import org.codehaus.jackson.map.type.TypeFactory;
import org.junit.Test;

public class SmileUtilTest {

	@Test
	public final void testObj2Str() throws Exception {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		byte[] bin = obj2bin(t);
		Bean st = bin2Obj(bin, Bean.class);
		assertEquals(st, t);
	}

	@Test
	public final void testStr2binStringClassOfT() throws Exception {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		byte[] bin = obj2bin(t);
		@SuppressWarnings("deprecation")
		Bean st = bin2Obj(bin, TypeFactory.fastSimpleType(Bean.class));
		assertEquals(st, t);
	}

	@Test
	public final void testWriteObj() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		List<Bean> m = new ArrayList<Bean>();
		m.add(t);
		m.add(t);
		writeObj(out, m);
		out.close();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		@SuppressWarnings("deprecation")
		List<Bean> m2 = readObj(in, TypeFactory.collectionType(ArrayList.class, Bean.class));
		assertEquals(2, m2.size());
		assertEquals(t, m2.get(0));
	}

	@Test
	public final void testReadObjInputStreamClassOfT() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		List<Bean> m = new ArrayList<Bean>();
		m.add(t);
		m.add(t);
		writeObj(out, m);
		out.close();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Bean[] m2 = readObj(in, Bean[].class);
		assertEquals(2, m2.length);
		assertEquals(t, m2[0]);
	}

}
