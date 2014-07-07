package org.apache.niolex.commons.seri;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.test.Benchmark.Bean;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

public class SmileUtilTest extends SmileUtil {

	@Test
	public final void testObj2Bin() throws Exception {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		byte[] bin = obj2bin(t);
		Bean st = bin2Obj(bin, Bean.class);
		assertEquals(st, t);
	}

	@Test
	public void testBin2ObjType() throws Exception {
		Bean t = new Bean(233, "Quty", 548166, new Date(1338008328709L));
		byte[] bin = obj2bin(t);
		Bean st = bin2Obj(bin, SmileUtil.getTypeFactory().constructType(Bean.class));
		assertEquals(st, t);
	}

	@Test
	public void testBin2ObjRef() throws Exception {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		byte[] bin = obj2bin(t);
		Bean st = bin2Obj(bin, new TypeReference<Bean>(){});
		assertEquals(st, t);
	}

	@Test
	public final void testWriteObj() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(67563, "Lex", 122435612, new Date(1338008328709L));
		List<Bean> m = new ArrayList<Bean>();
		m.add(t);
		m.add(t);
		writeObj(out, m);
		out.close();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		List<Bean> m2 = readObj(in, SmileUtil.getTypeFactory().constructParametricType(ArrayList.class, Bean.class));
		assertEquals(2, m2.size());
		assertEquals(t, m2.get(0));
	}

	@Test
	public final void testReadObjInputStreamClassOfT() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(6563, "Qute", 12546212, new Date(1338008328709L));
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

	@Test
	public void testReadObjRef() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(3565, "Qute", 123212139, new Date(1338008328709L));
		List<Bean> m = new ArrayList<Bean>();
		m.add(t);
		m.add(t);
		writeObj(out, m);
		out.close();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		List<Bean> m2 = readObj(in, new TypeReference<List<Bean>>(){});
		assertEquals(2, m2.size());
		assertEquals(t, m2.get(0));
	}

}
