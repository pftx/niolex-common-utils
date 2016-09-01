package org.apache.niolex.commons.net;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;

public class RESTResultTest {

    public static class A {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Err {
        private int err;
        private String msg;

        public int getErr() {
            return err;
        }

        public void setErr(int err) {
            this.err = err;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

    @Test
    public void testRESTResult() throws Exception {
        HTTPResult http = new HTTPResult(200, null, "{\"id\":202023,\"name\": \"Lex\"}".getBytes(), null);
        RESTResult<A> rest = new RESTResult<A>(http, A.class);
        A a = rest.getResponse();

        assertEquals(202023, a.id);
        assertEquals("Lex", a.name);
        assertEquals(http, rest.getResult());
        assertEquals(200, rest.getRespCode());
    }

    @Test
    public void testGetResult() throws Exception {
        HTTPResult http = new HTTPResult(200, null, "{\"err\":202023,\"info\": \"Failed to connect to server.\"}".getBytes(),
                null);
        RESTResult<A> rest = new RESTResult<A>(http, A.class);
        A a = rest.getResponse();

        assertEquals(0, a.id);
        assertEquals(null, a.name);
        assertEquals(http, rest.getResult());
        assertEquals(200, rest.getRespCode());
    }

    @Test
    public void testGetRespCode() throws Exception {
        HTTPResult http = new HTTPResult(200, null, "{\"err\":-6550,\"msg\": \"Failed to connect to server.\"}".getBytes(),
                null);
        RESTResult<Err> rest = new RESTResult<Err>(http, Err.class);
        Err err = rest.getResponse();

        assertEquals(-6550, err.err);
        assertEquals("Failed to connect to server.", err.msg);
        assertEquals(http, rest.getResult());
        assertEquals(200, rest.getRespCode());
    }

    @Test(expected = JsonParseException.class)
    public void testGetResponse() throws Exception {
        HTTPResult http = new HTTPResult(200, null, "Go to hell.".getBytes(), null);
        RESTResult<A> rest = new RESTResult<A>(http, A.class);
        A a = rest.getResponse();

        assertEquals(0, a.id);
        assertEquals(null, a.name);
        assertEquals(http, rest.getResult());
        assertEquals(200, rest.getRespCode());
    }

}
