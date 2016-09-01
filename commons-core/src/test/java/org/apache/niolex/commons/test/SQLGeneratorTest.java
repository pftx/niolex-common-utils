package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;

import org.apache.niolex.commons.coder.AESCoder;
import org.apache.niolex.commons.collection.CircularList;
import org.apache.niolex.commons.control.FrequencyCheck;
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
    public void testGenerateColumnMappingList() throws Exception {
        assertEquals("[{a=distributionInterval, b=DISTRIBUTION_INTERVAL}, {a=linkList, b=LINK_LIST}, {a=rpsList, b=RPS_LIST}, {a=counter, b=COUNTER}, {a=rumme, b=RUMME}, {a=startTime, b=START_TIME}, {a=distributions, b=DISTRIBUTIONS}, {a=avg, b=AVG}, {a=max, b=MAX}, {a=min, b=MIN}, {a=rps, b=RPS}]", generateColumnMappingList(StopWatch.class).toString());
    }

    @Test
    public void testGenerateInsert() throws Exception {
        assertEquals("INSERT INTO HTTP (REQ_HEADERS, COOKIES_MAP, END_POINT, CHARSET, END_POINT_DIR_DEPTH,"
                + " CONNECT_TIMEOUT, READ_TIMEOUT, REFERER, AUTHORIZATION) VALUES (#{reqHeaders},"
                + " #{cookiesMap}, #{endPoint}, #{charset}, #{endPointDirDepth}, #{connectTimeout},"
                + " #{readTimeout}, #{referer}, #{authorization});",
                generateInsert(HTTPClient.class, "HTTP"));
        assertEquals("INSERT INTO HTTP_METH (PASS_PARAMETERS_IN_URL, NAME, ORDINAL) VALUES (#{passParametersInURL}, #{name}, #{ordinal});", generateInsert(HTTPMethod.class, "HTTP_METH"));
        assertEquals("INSERT INTO HTTP_RESULT (RESP_CODE, RESP_HEADERS, RESP_BODY, RESP_BODY_STR, CLIENT) VALUES (#{respCode}, #{respHeaders}, #{respBody}, #{respBodyStr}, #{client});", generateInsert(HTTPResult.class, "HTTP_RESULT"));
    }

    @Test
    public void testGenerateUpdate() throws Exception {
        assertEquals("UPDATE TMP_S SET CYCLIC = #{cyclic}, TOTAL_NUM = #{totalNum} WHERE CURRENT_NUM = #{currentNum};", generateUpdate(FrequencyCheck.class, "TMP_S", "currentNum"));
        System.out.println(generateUpdate(AESCoder.class, "TMP_S", "secretKey"));
    }

    @Test
    public void testGenerateSelect() throws Exception {
        assertEquals("SELECT CAPACITY, ELEMENT_DATA, SIZE, MOD_COUNT FROM TMP_S WHERE HEAD = #{head};", generateSelect(CircularList.class, "TMP_S", "head"));
        System.out.println(generateSelect(DateFormat.class, "TMP_DATE", "numberFormat"));
    }

}
