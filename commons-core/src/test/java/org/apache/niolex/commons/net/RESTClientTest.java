package org.apache.niolex.commons.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.commons.compress.JacksonUtil;
import org.apache.niolex.commons.test.ObjToStringUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

public class RESTClientTest {

    private RESTClient client = new RESTClient("http://httpbin.org", "utf8");

    public static class R {
        private Map<String, String> args;
        private String data;
        private Map<String, String> headers;
        private Map<String, String> json;

        public Map<String, String> getArgs() {
            return args;
        }

        public void setArgs(Map<String, String> args) {
            this.args = args;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public Map<String, String> getJson() {
            return json;
        }

        public void setJson(Map<String, String> json) {
            this.json = json;
        }

        @Override
        public String toString() {
            return "R [args=" + args + ", data=" + data + ", headers=" + headers + ", json=" + json + "]";
        }

    }

    public static class Ex extends Exception {
        private static final long serialVersionUID = -7536716504851905041L;
        private final E e;

        public Ex(E e) {
            super(e.message);
            this.e = e;
        }

        public E getE() {
            return e;
        }

    }

    public static class E implements ErrorDecoder {
        private String message;
        private String[] accept;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String[] getAccept() {
            return accept;
        }

        public void setAccept(String[] accept) {
            this.accept = accept;
        }

        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.commons.net.ErrorDecoder#decode(int, byte[])
         */
        @Override
        public Exception decode(int respCode, byte[] respBody) throws IOException {
            E e = JacksonUtil.bin2Obj(respBody, E.class);
            return new Ex(e);
        }

    }

    @Test
    public void testGetError() throws Exception {
        RESTClient client = new RESTClient("http://httpbin.org", "utf8", 6000, 6000);
        client.setErrorDecoder(new E());
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        RESTResult<R> result = client.get("status/406", R.class, params);

        assertNull(result.getResponse());
        assertEquals(406, result.getRespCode());
        Exception e = result.getError();
        System.out.println(ObjToStringUtil.objToString(e));
        assertEquals("Client did not request a supported media type.", e.getMessage());
    }

    @Test
    public void testGet() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        RESTResult<R> result = client.get("get", R.class, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getArgs().get("who"));
    }

    @Test
    public void testDelete() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        RESTResult<R> result = client.delete("delete", R.class, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getArgs().get("who"));
    }

    @Test
    public void testPost() throws Exception {
        RESTResult<R> result = client.post("post", R.class, "Who is Lily?");

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Who is Lily?", result.getResponse().getData());
    }

    @Test
    public void testGetType() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        RESTResult<R> result = client.get("get", new TypeReference<R>() {
        }, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getArgs().get("who"));
    }

    @Test
    public void testDeleteType() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        RESTResult<R> result = client.delete("delete", new TypeReference<R>() {
        }, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getArgs().get("who"));
    }

    @Test
    public void testPostType() throws Exception {
        RESTResult<R> result = client.post("post", new TypeReference<R>() {
        }, "Who is Lily?");

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Who is Lily?", result.getResponse().getData());
    }

    @Test
    public void testPostJson() throws Exception {
        RESTClient client = new RESTClient("http://httpbin.org", "utf8", 3000, 3000);
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        params.put("when", "Yesterday");
        RESTResult<R> result = client.post("post", R.class, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getJson().get("who"));
        assertEquals("Yesterday", result.getResponse().getJson().get("when"));
    }

    @Test
    public void testPut() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        params.put("when", "Yesterday");
        RESTResult<R> result = client.put("put", R.class, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getJson().get("who"));
        assertEquals("Yesterday", result.getResponse().getJson().get("when"));
    }

    @Test
    public void testPutType() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("who", "Lily");
        params.put("when", "Yesterday");
        RESTResult<R> result = client.put("put", new TypeReference<R>() {
        }, params);

        System.out.println(result.getResponse());
        assertEquals(200, result.getRespCode());
        assertEquals("Lily", result.getResponse().getJson().get("who"));
        assertEquals("Yesterday", result.getResponse().getJson().get("when"));
    }

    @Test
    public void testObj2json() throws Exception {
        assertNull(client.obj2json(null));
        assertEquals("Not JSON", client.obj2json("Not JSON"));
        assertEquals("33445", client.obj2json(33445));
    }

}
