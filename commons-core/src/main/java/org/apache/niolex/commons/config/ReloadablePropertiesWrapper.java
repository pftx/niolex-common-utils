/**
 * ReloadablePropertiesWrapper.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.config;

import java.io.IOException;

import org.apache.niolex.commons.file.FileMonitor;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * We use the {@link org.apache.niolex.commons.file.FileMonitor} to monitor the changes of
 * the properties file, and reload it immediately.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-12
 */
public class ReloadablePropertiesWrapper extends PropertiesWrapper implements FileMonitor.EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReloadablePropertiesWrapper.class);

    /**
     * Generated ID
     */
    private static final long serialVersionUID = -2353322837021805417L;
    private final FileMonitor monitor;
    private final String fileName;

    /**
     * Read properties from file system, and reload it when the file changes.
     *
     * @param fileName the properties file name.
     * @param monitorInterval the file change monitor interval in milliseconds.
     * @throws IOException If error reading this file.
     */
    public ReloadablePropertiesWrapper(String fileName, int monitorInterval) throws IOException {
        super(fileName);
        this.fileName = fileName;
        monitor = new FileMonitor(monitorInterval, fileName);
        monitor.addListener(this);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.file.FileMonitor.EventListener#notify(org.apache.niolex.commons.file.FileMonitor.EventType, long)
     */
    @Override
    public void notify(EventType type, long happenTime) {
        if (type == FileMonitor.EventType.UPDATE) {
            try {
                this.load(fileName);
                LOG.debug("Properties file refreshed: {}", fileName);
            } catch (IOException e) {
                LOG.warn("Failed to read config file: {}", fileName, e);
            }
        }
    }

    /**
     * Stop refresh the properties file.
     */
    public void stopRefresh() {
        monitor.removeListener(this);
        monitor.stop();
    }

}
