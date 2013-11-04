package org.apache.niolex.common.demo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.niolex.commons.util.SystemUtil;
import org.slf4j.Logger;


/**
 * Hello world!
 *
 */
public abstract class App extends HttpURLConnection
{
    /**
     * Constructor
     * @param u
     */
    protected App(URL u) {
        super(u);
    }

    public static void main(String[] args) {
        System.out.println((new Date()).getTime());
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
        System.out.println("rnsplit[rnrnanbcrrndefrnrnnrn] = " + Arrays.toString("\r\n\r\na\nbc\r\r\ndef\r\n\r\n\n\r\n".split("\r*\n")));
        System.out.println("rnsplit[abcde] = " + Arrays.toString("abcde".split(" ")));
        System.out.println("last index of a = " + "last index of ** = ".lastIndexOf('a'));
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID = " + uuid);

        System.out.println("\n**** Test Java SecurityManager");
        SecurityManager mgr = System.getSecurityManager();
        System.out.println("Is Null ? " + (mgr == null));

        System.out.println("\n**** Bits Op");
        System.out.println("-128 >> 3 ? " + (-128 >> 3));
        System.out.println("-128 >>> 3 ? " + (-128 >>> 3));
        System.out.println("~-128 ? " + (~-128));
        System.out.println("1L << 200 ? " + (1L << 200));

        System.out.println("\n**** isAssignableFrom");
        System.out.println("Long isAssignableFrom Number ? " + (Long.class.isAssignableFrom(Number.class)));
        System.out.println("Number isAssignableFrom Long ? " + (Number.class.isAssignableFrom(Long.class)));
        Long ll = 129012l;
        System.out.println("Number.class isInstance Long Object ? " + (Number.class.isInstance(ll)));
        System.out.println("long.class isInstance Long Object ? " + (long.class.isInstance(ll)));

        System.out.println("\n**** Test extends Static " + HTTP_OK);
        System.out.println(Logger.class.getResource("Logger.class").toExternalForm());

        System.out.println("\n**** Test array cast");
        try {
            String[] arr = (String[]) new Object[] {"Lex"};
            System.out.println("cast obj array to string array ? " + arr[0]);
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }
}
