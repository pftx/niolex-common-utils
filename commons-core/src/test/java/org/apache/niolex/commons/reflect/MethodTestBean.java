package org.apache.niolex.commons.reflect;

interface Echo {
    public String echoName();
}

interface EchoName extends Echo {
    public String echoName(String strName);
}

public class MethodTestBean implements EchoName {
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
        return strName;
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

