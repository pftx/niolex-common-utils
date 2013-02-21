package org.apache.niolex.common.demo;

import java.util.Arrays;
import java.util.UUID;

import org.apache.niolex.commons.util.SystemUtil;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        System.out.println("\n**** Test system property");
        String fileName = SystemUtil.getSystemProperty("ConfigClient.configurationFile", "config-client-properties",
                "config.client.property.file");
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

        System.out.println("\n**** Test String functions");
        System.out.println("+null = " + god);
        System.out.println("//split = " + Arrays.toString("remote://1.2.3.4:808///abc/de".split("//")));
        System.out.println("last index of a = " + "last index of ** = ".lastIndexOf('a'));
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID = " + uuid);

        System.out.println("\n**** Test Java SecurityManager");
        SecurityManager mgr = System.getSecurityManager();
        System.out.println("Is Null ? " + (mgr == null));
    }
}
