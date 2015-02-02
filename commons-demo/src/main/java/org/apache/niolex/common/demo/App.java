package org.apache.niolex.common.demo;

import static org.apache.niolex.commons.util.DateTimeUtil.*;

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

    public static void main(String[] args) throws Exception {
        long init = parseDateFromDateStr("2014-07-10").getTime();
        long dual = (new Date().getTime() - init) / DAY;

        System.out.println("DAYS - " + dual);
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
        System.out.println("Just Name! " + App.class.getName());

        System.out.println("\n**** Test class loader");
        ClassLoader loader = App.class.getClassLoader();
        while (loader != null) {
            System.out.println("V " + loader.toString());
            loader = loader.getParent();
        }

        System.out.println("\n**** Test instanceof");
        String god = null;
        System.out.println("NULL instanceof! " + (god instanceof String));

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

        System.out.println("\n**** Test extends Static " + HTTP_OK);
        System.out.println(Logger.class.getResource("Logger.class").toExternalForm());

        System.out.println("\n**** Test array cast");
        try {
            String[] arr = (String[]) new Object[] {"Lex"};
            System.out.println("cast obj array to string array ? " + arr[0]);
        } catch (ClassCastException e) {
            System.out.println("EX! " + e.getMessage());
        }
    }
}
