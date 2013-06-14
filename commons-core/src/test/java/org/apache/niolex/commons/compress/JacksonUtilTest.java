package org.apache.niolex.commons.compress;

import static org.junit.Assert.*;
import static org.apache.niolex.commons.compress.JacksonUtil.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.codec.StringUtil;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

public class JacksonUtilTest {

	@Test
	public final void testObj2Str() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		String s = obj2Str(t);
		System.out.println("str => " + s);
		assertEquals(st, s);
	}

	@Test
	public final void testStr2ObjStringClassOfT() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		CTBean s = str2Obj(st, CTBean.class);
		assertEquals(t, s);
	}

	@Test
	public final void testStr2ObjStringJavaType() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		CTBean m = str2Obj(st, TypeFactory.fromCanonical("org.apache.niolex.commons.compress.CTBean"));
		assertEquals(t, m);
	}

	@Test
	public final void testStr2ObjStringRef() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		List<CTBean> m = str2Obj(st, new TypeReference<List<CTBean>>(){});
		assertEquals(t, m.get(0));
		assertEquals(1, m.size());
	}

	@Test
	public final void testWriteObj() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		writeObj(out, t);
		out.close();
		assertArrayEquals(st.getBytes("UTF-8"), out.toByteArray());
	}

	@Test
	public final void testReadObjInputStreamClassOfT() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		ByteArrayInputStream in = new ByteArrayInputStream(st.getBytes("UTF-8"));
		CTBean[] m = readObj(in, CTBean[].class);
		assertEquals(1, m.length);
		assertEquals(t, m[0]);
	}

	@Test
	public final void testReadObjInputStreamJavaType() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}";
		ByteArrayInputStream in = new ByteArrayInputStream(st.getBytes("UTF-8"));
		CTBean m = readObj(in, TypeFactory.fromCanonical("org.apache.niolex.commons.compress.CTBean"));
		assertEquals(t, m);
	}

	@Test
	public final void testReadObjInputStreamRef() throws Exception {
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		String st = "[{\"likely\":3,\"name\":\"Qute\",\"id\":12212,\"birth\":1338008328709}]";
		ByteArrayInputStream in = new ByteArrayInputStream(st.getBytes("UTF-8"));
		List<CTBean> m = readObj(in, new TypeReference<List<CTBean>>(){});
		assertEquals(t, m.get(0));
		assertEquals(1, m.size());
	}

	@Test
	public final void testJacksonUtil() throws Exception {
		JsonFactory fac = JacksonUtil.getJsonFactory();
		System.out.println(new JacksonUtil(){} + ", " + fac.getCodec().toString());
	}

	/**
	 * We can not read two objects from one input stream, due to prefetch.
	 * @throws Exception
	 */
	@Test(expected=EOFException.class)
	public void testReadObjOfTwo() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CTBean t = new CTBean(3, "Qute", 12212, new Date(1338008328709L));
		CTBean q = new CTBean(3, "Another", 523212, new Date(1338008328334L));
		writeObj(out, t);
		writeObj(out, q);
		out.close();
		System.out.println("ObjOfTwo => " + StringUtil.utf8ByteToStr(out.toByteArray()));
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		CTBean r = readObj(in, CTBean.class);
		CTBean s = readObj(in, CTBean.class);
		assertEquals(t, r);
		assertEquals(q, s);
	}

    @Test
    public void testObj2bin() throws Exception {
        CTBean t = new CTBean(3, "Lex", 2341, new Date(1338008328709L));
        String st = "{\"likely\":3,\"name\":\"Lex\",\"id\":2341,\"birth\":1338008328709}";
        byte[] out = obj2bin(t);
        String ot = StringUtil.utf8ByteToStr(out);
        assertEquals(st, ot);
    }

    @Test
    public void testBin2Obj() throws Exception {
        CTBean t = new CTBean(3, "Lex", 2341, new Date(1338008328709L));
        String st = "{\"likely\":3,\"name\":\"Lex\",\"id\":2341,\"birth\":1338008328709}";
        byte[] in = StringUtil.strToAsciiByte(st);
        CTBean r = bin2Obj(in, CTBean.class);
        assertEquals(t, r);
    }

    @Test
    public void testBin2ObjJavaType() throws Exception {
        CTBean t = new CTBean(3, "Lex", 2341, new Date(1338008328709L));
        String st = "[{\"likely\":3,\"name\":\"Lex\",\"id\":2341,\"birth\":1338008328709}]";
        byte[] in = StringUtil.strToAsciiByte(st);
        CTBean[] r = bin2Obj(in, getTypeFactory().constructArrayType(CTBean.class));
        assertEquals(1, r.length);
        assertEquals(t, r[0]);
    }

    @Test
    public void testBin2ObjTypeRef() throws Exception {
        CTBean t = new CTBean(3, "Lex", 2341, new Date(1338008328709L));
        String st = "[{\"likely\":3,\"name\":\"Lex\",\"id\":2341,\"birth\":1338008328709}]";
        byte[] in = StringUtil.strToAsciiByte(st);
        List<CTBean> m = bin2Obj(in, new TypeReference<List<CTBean>>(){});
        assertEquals(t, m.get(0));
        assertEquals(1, m.size());
    }

}
