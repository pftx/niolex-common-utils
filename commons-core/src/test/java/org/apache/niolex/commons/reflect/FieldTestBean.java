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

class FastBean {
    static String strName;
    int intId;
    int intLevel;
    Integer age;
    long empno;
    boolean gender;
    byte resvered;
    short veridk;
    char chdier;
    double earned;
    float tax;
}

class Super {
    protected boolean isSuper = true;
    private int mark = 3;

    /**
     * @return the isSuper
     */
    public boolean isSuper() {
        return isSuper;
    }

    /**
     * @param isSuper the isSuper to set
     */
    public void setSuper(boolean isSuper) {
        this.isSuper = isSuper;
    }

    public int inc() {
        return mark++;
    }

}

class Sub extends Super {
    private int age;
    private int mark = 8;

    /**
     * Constructor
     */
    public Sub() {
        super();
        isSuper =false;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    public int inc() {
        return mark++;
    }

}