package org.apache.niolex.notify;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.niolex.notify.core.Notify;
import org.apache.niolex.notify.core.NotifyException;
import org.apache.niolex.notify.core.ZKConnector;

/**
 * The entrance of notify-core. User can create an instance of this class and use it,
 * or use the static style, init the global instance, and use it by static method
 * {@link #instance()}
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-4
 */
public class App extends ZKConnector {
    
    /**
     * The static field to store the global instance.
     */
    private static App APP;
    
    /**
     * Init the global instance only once.
     * 
     * @param clusterAddress
     * @param sessionTimeout
     * @throws IOException
     */
    public synchronized static void init(String clusterAddress, int sessionTimeout) throws IOException {
        if (APP == null) {
            APP = new App(clusterAddress, sessionTimeout);
        }
    }
    
    /**
     * Get the global instance.
     * 
     * @return the global instance, null if not initialized.
     */
    public static App instance() {
        return APP;
    }
    
    protected ConcurrentHashMap<String, Notify> notifyMap = new ConcurrentHashMap<String, Notify>();

    /**
     * Construct a new App and connect to ZK server.
     * 
     * @param clusterAddress
     * @param sessionTimeout
     * @throws IOException
     */
    public App(String clusterAddress, int sessionTimeout) throws IOException {
        super(clusterAddress, sessionTimeout);
    }
    
    /**
     * Get a Notify to represent this path.
     * 
     * @param path the path of notify
     * @return null if not found, a Notify instance otherwise
     * @throws NotifyException if error occurred.
     */
    public Notify getNotify(String path) {
        try {
            if (this.zk.exists(path, false) != null) {
                path = path.intern();
                synchronized (path) {
                    Notify no = notifyMap.get(path);
                    if (no == null) {
                        no = new Notify(this, path);
                        notifyMap.put(path, no);
                    }
                    return no;
                }
            }
        } catch (Exception e) {
            NotifyException.makeInstance(path, e);
        }
        return null;
    }
    
    /**
     * Close the connection to ZK server.
     * 
     * Override super method
     * @see org.apache.niolex.notify.core.ZKConnector#close()
     */
    public void close() {
        super.close();
    }
    
}
