package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.Random;

import org.apache.niolex.commons.net.HTTPClient;
import org.apache.niolex.commons.net.HTTPMethod;
import org.apache.niolex.commons.net.HTTPResult;
import org.junit.Test;

public class SQLGeneratorTest extends SQLGenerator {

    @Test
    public void testGenerateColumnName() throws Exception {
        assertEquals("TEST_GENERATE_COLUMN_NAME", generateColumnName("testGenerateColumnName"));
        assertEquals("IS_HTTP_REQUEST", generateColumnName("isHttpRequest"));
        assertEquals("PASS_PARAMETERS_IN_URL", generateColumnName("passParametersInURL"));
        assertEquals("GOOD_LIKE", generateColumnName("GoodLike"));
    }

    @Test
    public void testGenerateColumnMap() throws Exception {
        System.out.println(generateColumnMap(StopWatch.class));
    }

    @Test
    public void testGenerateInsert() throws Exception {
        assertEquals("INSERT INTO HTTP (COOKIE, READ_TIMEOUT, CONNECT_TIMEOUT, END_POINT, CHARSET, AUTHORIZATION, REFERER, END_POINT_DIR_DEPTH, REQ_HEADERS) VALUES (#cookie, #readTimeout, #connectTimeout, #endPoint, #charset, #authorization, #referer, #endPointDirDepth, #reqHeaders);", generateInsert(HTTPClient.class, "HTTP"));
        assertEquals("INSERT INTO HTTP_METH (NAME, ORDINAL, PASS_PARAMETERS_IN_URL) VALUES (#name, #ordinal, #passParametersInURL);", generateInsert(HTTPMethod.class, "HTTP_METH"));
        assertEquals("INSERT INTO HTTP_RESULT (RESP_BODY_STR, RESP_CODE, CLIENT, RESP_BODY, RESP_HEADERS) VALUES (#respBodyStr, #respCode, #client, #respBody, #respHeaders);", generateInsert(HTTPResult.class, "HTTP_RESULT"));
    }

    @Test
    public void testGenerateUpdate() throws Exception {
        assertEquals("UPDATE TMP_S SET COUNT = #count, HASH = #hash, VALUE = #value WHERE OFFSET = #offset;", generateUpdate(String.class, "TMP_S", "offset"));
        System.out.println(generateUpdate(Random.class, "TMP_S", "seed"));
    }

    @Test
    public void testGenerateSelect() throws Exception {
        assertEquals("SELECT HAVE_NEXT_NEXT_GAUSSIAN, NEXT_NEXT_GAUSSIAN FROM TMP_S WHERE SEED = #seed;", generateSelect(Random.class, "TMP_S", "seed"));
        System.out.println(generateSelect(DateFormat.class, "TMP_DATE", "numberFormat"));
    }

}
