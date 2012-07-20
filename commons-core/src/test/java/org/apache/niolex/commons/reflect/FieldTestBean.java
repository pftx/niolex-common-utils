package org.apache.niolex.commons.reflect;

@SuppressWarnings("unused")
public class FieldTestBean {
    public String strName;
    private int intId;
    protected int intLevel;
    private Integer age;
    private long empno;
    private boolean gender;
    private byte resvered;
    private short veridk;
    private char chdier;
    private double earned;
    float tax;

    public String echoName() {
        System.out.println("My Name IS " + strName + ", Welcome to use FieldTestBean!");
        return strName;
    }

    public int echoLevel() {
        System.out.println("My English level IS " + intLevel + ", Welcome to use FieldTestBean!");
        return intLevel;
    }
}
