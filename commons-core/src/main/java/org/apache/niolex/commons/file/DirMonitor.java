/**
 * DirMonitor.java
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
package org.apache.niolex.commons.file;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.util.CollectionUtil;

/**
 * Monitoring the directory changes from the file system and notify users.<br>
 * If you want to be notified for children related events, please implement the {@link ChildrenListener}
 * instead of {@link FileMonitor.EventListener}
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-23
 */
public class DirMonitor extends FileMonitor implements FileMonitor.EventListener {

    /**
     * The interface used to dispatch the children change event.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-23
     */
    public static interface ChildrenListener extends EventListener {

        /**
         * Notify the event of children changes.
         *
         * @param type the event type.
         * @param list the changed children. For add, it's the new children; for delete, it's the removed children.
         */
        public void childrenChange(EventType type, List<String> list);

    }

    /**
     * The previous file list in this directory.
     */
    protected List<String> fileList;

    /**
     * The previous status of this directory.
     */
    protected Boolean isDir;

    /**
     * Overload super Constructor, and add this as event listener.
     *
     * @param monitorInterval
     * @param filePathName
     */
    public DirMonitor(int monitorInterval, String filePathName) {
        super(monitorInterval, filePathName);
        addListener(this);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.file.FileMonitor.EventListener#notify(org.apache.niolex.commons.file.FileMonitor.EventType, long)
     */
    @Override
    public void notify(EventType type, long happenTime) {
        if (type == EventType.CREATE || type == EventType.UPDATE) {
            if (isDir == null) checkCreate();
            else if (isDir == Boolean.TRUE) checkUpdate();
        } else if (type == EventType.DELETE) {
            checkDelete();
        }
    }

    /**
     * Check the file after it is created.
     */
    private void checkCreate() {
        isDir = Boolean.valueOf(file.isDirectory());
        // If not a dir, we will not process.
        if (isDir == Boolean.FALSE) {
            notifyChildrenListeners(EventType.NOT_DIR, null);
        } else {
            // This is the first time we check the children list.
            fileList = Arrays.asList(file.list());
            notifyChildrenListeners(EventType.ADD_CHILDREN, Collections.unmodifiableList(fileList));
        }
    }

    /**
     * Check the update of the current directory.
     */
    private void checkUpdate() {
        List<String> current = Arrays.asList(file.list());
        Pair<List<String>,List<String>> intersection = CollectionUtil.intersection(fileList, current);
        fileList = current;
        if (!CollectionUtil.isEmpty(intersection.a)) {
            notifyChildrenListeners(EventType.REMOVE_CHILDREN, intersection.a);
        }
        if (!CollectionUtil.isEmpty(intersection.b)) {
            notifyChildrenListeners(EventType.ADD_CHILDREN, intersection.b);
        }
    }

    /**
     * @param type
     * @param a
     */
    private void notifyChildrenListeners(EventType type, List<String> a) {
        for (EventListener li : list) {
            if (li instanceof ChildrenListener) {
                ((ChildrenListener) li).childrenChange(type, a);
            }
        }
    }

    /**
     * When directory is deleted, we have nothing to do, but mark the isDir to null.
     */
    private void checkDelete() {
        isDir = null;
    }

    /**
     * @return the current children list.
     */
    public List<String> currentChildren() {
        return fileList;
    }

    /**
     * @return the current monitor file path is directory or not.
     */
    public Boolean isDir() {
        return isDir;
    }

}
