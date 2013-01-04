package org.apache.niolex.common.demo;

import java.util.Arrays;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.file.FileUtil;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args)
    {
        String fileName = System.getProperty("config.client.property.file");
        if (fileName != null) {
            System.out.println(fileName);
        } else {
            System.out.println("N");
        }
        System.out.println("Hello World! " + App.class.getCanonicalName());
        byte[] arr = FileUtil.getBinaryFileContentFromFileSystem("D:\\data\\exchange\\1354175418000");
        System.out.println("Len " + arr.length);
        System.out.println(Base16Util.byteToBase16(Arrays.copyOfRange(arr, arr.length - 10, arr.length)));
        ClassLoader loader = App.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }
    }
}
