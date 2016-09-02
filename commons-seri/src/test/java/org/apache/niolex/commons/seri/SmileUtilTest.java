package org.apache.niolex.commons.seri;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Test
    public void testSmile() throws Exception {
	    ObjectMapper objectMapper = SmileUtil.getmapper();
        Smile s = new Smile("Smile", 3);
        byte[] bytes = objectMapper.writeValueAsBytes(s);

        System.out.println(" [X] Smile -> " + Base16Util.byteToBase16(bytes));
        System.out.println(" [X] Smile -> " + new String(bytes));

        Smile s2 = objectMapper.readValue(bytes, Smile.class);
        assertEquals("Smile", s2.getFace());
        assertEquals(3, s2.getAge());
	}

    @Test
    public void testInt() throws Exception {
        ObjectMapper objectMapper = SmileUtil.getmapper();
        int s = 6;
        byte[] bytes = objectMapper.writeValueAsBytes(s);

        System.out.println(" [X] Int -> " + Base16Util.byteToBase16(bytes));

        int s2 = objectMapper.readValue(bytes, int.class);
        assertEquals(6, s2);
    }

}

class Smile {
    private String face;
    private int age;

    public Smile() {
        super();
    }

    public Smile(String face, int age) {
        super();
        this.face = face;
        this.age = age;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}