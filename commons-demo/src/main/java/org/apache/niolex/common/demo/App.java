package org.apache.niolex.common.demo;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
    	String fileName = System.getProperty("config.client.property.file");
		if (fileName != null) {
			System.out.println(fileName);
		} else {
			System.out.println("N");
		}
        System.out.println( "Hello World! "  + App.class.getCanonicalName());
    }
}
