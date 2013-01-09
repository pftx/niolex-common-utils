package org.apache.niolex.common.demo;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        System.out.println("\n**** Test system property");
        String fileName = System.getProperty("config.client.property.file");
        if (fileName != null) {
            System.out.println(fileName);
        } else {
            System.out.println("No property, it's NULL");
        }

        System.out.println("\n**** Test class canonical name");
        System.out.println("CanonicalName! " + App.class.getCanonicalName());

        System.out.println("\n**** Test class loader");
        ClassLoader loader = App.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }

        System.out.println("\n**** Test instanceof");
        String god = null;
        System.out.println("NULL instanceof! " + (god instanceof String));

        System.out.println("\n**** Test operator priority");
        System.out.println("7 & ~1 = " + (7 & ~1));

        System.out.println("\n**** Test +null");
        System.out.println("+null = " + null);
    }
}
