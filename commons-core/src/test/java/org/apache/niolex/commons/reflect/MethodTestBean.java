package org.apache.niolex.commons.reflect;

interface Neo {}

interface Echo {
    public String echoName();
}

interface EchoInt {
    public String echoName(int intAge);
}

interface EchoName extends Echo, EchoInt {
    public String echoName(String strName);
}

interface EchoClass extends Echo, Neo {
    public String echoClass();
}

public class MethodTestBean extends FieldTestBean implements EchoName, EchoClass {
    private String strName;

    public MethodTestBean(String strName) {
        super();
        this.strName = strName;
    }

    public String echoName() {
        System.out.println("My Name IS " + strName + ", Welcome to use MethodUtil!");
        return strName;
    }

    public String echoName(String strName) {
        System.out.println("My Name IS " + strName + ", Welcome to use MethodUtil!");
        return strName;
    }

    public String echoName(int intAge) {
        System.out.println("My Name IS " + strName + ", my age is " + intAge +", Welcome to use MethodUtil!");
        return strName + "-" + intAge;
    }

    public String echoName(String strName, int intAge) {
        System.out.println("My Name IS " + strName + ", my age is " + intAge +", Welcome to use MethodUtil!");
        return strName;
    }

    public String echoClass() {
        System.out.println("Welcome to use MethodTestBean!");
        return "MethodTestBean";
    }
}

class Lex {
    private int abc;

    public int getAndSet(int i) {
        int r = abc;
        abc = i;
        return r;
    }
}

class Jenny extends Lex {
    private static int abc;

    public int getAndSet(int i) {
        int r = abc;
        abc = i;
        return r;
    }

    public static int get() {
        return abc;
    }
}
