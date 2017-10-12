package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MappingGeneratorTest extends MappingGenerator {

    @Test
    public void testGenerateFieldName() throws Exception {
        assertEquals("ABcD", generateFieldName("_a_bc_d"));
        assertEquals("aBcD", generateFieldName("a_bc_d"));
        assertEquals("aBcD", generateFieldName("A_BC_D"));
        assertEquals("aBcD", generateFieldName("a_bc__d"));
        assertEquals("abcd", generateFieldName("ABCD"));
        assertEquals("displayTargetIntRate", generateFieldName("DISPLAY_TARGET_INT_RATE"));
    }

    @Test
    public void testGenerateMapping() throws Exception {
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", "good_id, nice, name"));
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", "select good_id, nice, name"));
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", "good_id, nice, name from csv"));
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", "select id.good_id, f.nice, ok as name from csv i;"));
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", "SELECT a as good_id, b as nice, c as name from cc"));
        assertEquals("@Result(property = 'goodId', column = 'GOOD_ID'\n@Result(property = 'nice', column = 'NICE'\n@Result(property = 'name', column = 'NAME'\n", generateMapping("@Result(property = '%1$s', column = '%2$s\'", " Select good_id, nice, decode(u_name, '', 0) as name FROM tmp"));
    }

    @Test
    public void testTidyDBColumnName() throws Exception {
        assertEquals("implemented", tidyDBColumnName("not yet implemented"));
        assertEquals("implemented", tidyDBColumnName("  \t not.implemented \t "));
        assertEquals("implemented", tidyDBColumnName("nvl(not, 0, 1) implemented"));
        assertEquals("implemented", tidyDBColumnName(" not.yet implemented"));
        assertEquals("implemented", tidyDBColumnName("not.yet as implemented"));
        assertEquals("implemented", tidyDBColumnName("not yet implemented\r\n\t\n"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTidyDBColumnNameEx() throws Exception {
        tidyDBColumnName("nvl(not, 0, 1 implemented");
    }
}
