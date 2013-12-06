package org.apache.niolex.notify;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.niolex.notify.core.Notify;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKException;

/**
 * The entrance of commons-notify. User can create an instance of this class and use it,
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
     * @param clusterAddress the zookeeper cluster address
     * @param sessionTimeout the zookeeper connection session timeout in milliseconds
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
     * @param clusterAddress the zookeeper cluster address
     * @param sessionTimeout the zookeeper connection session timeout in milliseconds
     * @throws IOException
     */
    public App(String clusterAddress, int sessionTimeout) throws IOException {
        super(clusterAddress, sessionTimeout);
    }

    /**
     * Get a Notify to represent this path. Will return null if the path not exist.
     *
     * @param path the path of notify
     * @return null if not found, a Notify instance otherwise
     * @throws ZKException if error occurred.
     */
    public Notify getNotify(String path) {
        if (exists(path)) {
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
        return null;
    }

    /**
     * Close the connection to ZK server.
     *
     * Override super method
     * @see org.apache.niolex.zookeeper.core.ZKConnector#close()
     */
    public void close() {
        super.close();
    }

}
