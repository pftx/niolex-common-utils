package org.apache.niolex.commons.reflect;

import java.util.Date;

@SuppressWarnings("unused")
public class FieldTestBean {
    public String strName;
    Date time;
    private int intId = 5;
    protected int intLevel = 7;
    private Integer age;
    private long empno = 15;
    private Long logno;
    private short veridk;
    private Short dfijd;
    private byte resvered;
    private Byte unused;
    private boolean gender;
    private Boolean flag;
    private char chdier = 'c';
    private Character grade = 'A';
    private double earned;
    private Double sal;
    float tax;
    Float remain;
    Object obj;

    public String echoName() {
        System.out.println("My Name IS " + strName + ", Welcome to use FieldTestBean!");
        return strName;
    }

    public int echoLevel() {
        System.out.println("My English level IS " + intLevel + ", Welcome to use FieldTestBean!");
        return intLevel;
    }
}
