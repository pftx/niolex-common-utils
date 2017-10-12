package org.apache.niolex.commons.file;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.file.FileMonitor.EventListener;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.apache.niolex.commons.util.SystemUtil;

public class FileMonitorTestMain {
	
	static final String TMP = System.getProperty("user.home") + "/tmpfm";
    static FileMonitor monitor;
    
    static final EventListener listn = new EventListener() {

        @Override
        public void notify(EventType type, long happenTime) {
            System.out.println(type + " " + happenTime);
        }
        
    };

	public static void main(String[] args) throws IOException {
		DirUtil.mkdirsIfAbsent(TMP);
		String fileName = TMP + "/file-monitor";
		
        DirUtil.delete(fileName, true);
        monitor = new FileMonitor(10, fileName);
        monitor.addListener(listn);
        
        FileOutputStream fos = new FileOutputStream(fileName);
        System.out.println(SystemUtil.getSystemProperty("os.name"));
        System.out.println(SystemUtil.getSystemProperty("os.arch"));
        System.out.println(SystemUtil.getSystemProperty("os.version"));
        
        // 1. Create.
        fos.write("Hello, ".getBytes());
        fos.flush();
        fos.close();
        ThreadUtil.sleepAtLeast(1000);
        System.out.println("Create Done.");

        // 2. Update.
        fos = new FileOutputStream(fileName, true);
        fos.write("World!".getBytes());
        fos.flush();
        fos.close();
        ThreadUtil.sleepAtLeast(1000);
        System.out.println("Update Done.");
        
        // 3. Delete.
        DirUtil.delete(fileName, false);
        ThreadUtil.sleepAtLeast(100);
        System.out.println("Delete Done.");
        
        monitor.stop();
        DirUtil.delete(TMP, true);
	}

}