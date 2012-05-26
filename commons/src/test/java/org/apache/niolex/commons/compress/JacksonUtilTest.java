package org.apache.niolex.commons.compress;

import static org.junit.Assert.*;
import static org.apache.niolex.commons.compress.JacksonUtil.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.type.TypeFactory;
import org.junit.Test;

public class JacksonUtilTest {

	@Test
	public final void testObj2Str() throws Exception {
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		String s = obj2Str(t);
		System.out.println("s => " + s);
		assertEquals(st, s);
	}

	@Test
	public final void testStr2ObjStringClassOfT() throws Exception {
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		TestBean s = str2Obj(st, TestBean.class);
		assertEquals(t, s);
	}

	@Test
	public final void testStr2ObjStringJavaType() throws Exception {
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		@SuppressWarnings("deprecation")
		List<TestBean> m = str2Obj(st, TypeFactory.collectionType(ArrayList.class, TestBean.class));
		assertEquals(t, m.get(0));
		assertEquals(1, m.size());
	}

	@Test
	public final void testWriteObj() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		writeObj(out, t);
		out.close();
		assertArrayEquals(st.getBytes("UTF-8"), out.toByteArray());
	}

	@Test
	public final void testReadObjInputStreamClassOfT() throws Exception {
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		ByteArrayInputStream in = new ByteArrayInputStream(st.getBytes("UTF-8"));
		TestBean[] m = readObj(in, TestBean[].class);
		assertEquals(1, m.length);
		assertEquals(t, m[0]);
	}

	@Test
	public final void testReadObjInputStreamJavaType() throws Exception {
		TestBean t = new TestBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		ByteArrayInputStream in = new ByteArrayInputStream(st.getBytes("UTF-8"));
		@SuppressWarnings("deprecation")
		List<TestBean> m = readObj(in, TypeFactory.collectionType(ArrayList.class, TestBean.class));
		assertEquals(t, m.get(0));
		assertEquals(1, m.size());
	}

	@Test
	public void testReadObj()
	 throws Exception {

	}

}
