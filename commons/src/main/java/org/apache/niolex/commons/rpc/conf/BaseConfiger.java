package org.apache.niolex.commons.rpc.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseConfiger<B extends BaseConfigBean> {
    private static final Logger log = LoggerFactory.getLogger(BaseConfiger.class);

    public static final String SUPER = "superList";
    public static final String GROUP = "groupList";
    public static final String DEFAULT = "default-group";

    private Properties props;

    protected Map<String, B> superMap = new HashMap<String, B>();
    protected Map<String, B> groupMap = new HashMap<String, B>();

    public BaseConfiger(String fileName) {
        props = new Properties();
        try {
            props.load(BaseConfiger.class.getResourceAsStream(fileName));
            readSuperList();
            readConfigList();
            log.info("Instantiate properties for " + fileName + " successful.");
        } catch (Exception ioe) {
            log.error("Cannot access the config file [" + fileName + "].", ioe);
        }
    }

    // ----------------------------------------------------------------------
    // read SuperList
    // ----------------------------------------------------------------------

    private void readSuperList() {
        buildSuper();
        configSuper();
    }

    private void buildSuper() {
        String str = props.getProperty(SUPER);
        if (!StringUtils.isBlank(str)) {
            String[] superArr = str.split(" *, *");
            for (String superName : superArr) {
                superMap.put(superName, newConfigBean(superName));
            }
            log.info("[superList] loaded, super list: " + superMap.keySet());
        }
    }

    private void configSuper() {
        if (superMap.isEmpty())
            return;
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            for (String superName : superMap.keySet()) {
                if (key.startsWith(superName + ".")) {
                    superMap.get(superName).setConfig(key.substring(superName.length() + 1),
                            entry.getValue().toString());
                    break;
                }
            }
        }
    }

    // ----------------------------------------------------------------------
    // read ConfigList
    // ----------------------------------------------------------------------

    private void readConfigList() {
        buildGroup();
        loadSuperConfig();
        configGroup();
    }

    private void buildGroup() {
        String str = props.getProperty(GROUP);
        if (!StringUtils.isBlank(str)) {
            String[] groupArr = str.split(" *, *");
            for (String groupName : groupArr) {
                groupMap.put(groupName, newConfigBean(groupName));
            }
            log.info("[groupList] loaded, groups: " + groupMap.keySet());
        } else {
            guessGroup();
            log.info("[groupList] property not found, try to guess groups: " + groupMap.keySet());
        }
    }

    private void guessGroup() {
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            if (key.matches("[\\w-_]+.serviceUrl")) {
                String groupName = key.substring(0, key.indexOf(".serviceUrl"));
                groupMap.put(groupName, newConfigBean(groupName));
            }
        }
        if (groupMap.isEmpty()) {
            groupMap.put(DEFAULT, newConfigBean(DEFAULT));
        }
    }

    private void loadSuperConfig() {
        if (superMap.isEmpty()) {
            return;
        }
        for (String groupName : groupMap.keySet()) {
            String superName = props.getProperty(groupName + ".superName");
            if (!StringUtils.isBlank(superName)) {
                BaseConfigBean superConf = superMap.get(superName);
                if (superConf != null)
                    groupMap.get(groupName).setSuper(superConf);
            }
        }
    }

    private void configGroup() {
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            if (groupMap.containsKey(DEFAULT)) {
                groupMap.get(DEFAULT).setConfig(key, entry.getValue().toString());
                continue;
            }
            for (String groupName : groupMap.keySet()) {
                if (key.startsWith(groupName + ".")) {
                    groupMap.get(groupName).setConfig(key.substring(groupName.length() + 1),
                            entry.getValue().toString());
                    break;
                }
            }
        }
    }

    abstract protected B newConfigBean(String groupName);

    // ----------------------------------------------------------------------
    // get ConfigList
    // ----------------------------------------------------------------------

    public B getConfig(String groupName) {
        return groupMap.get(groupName);
    }

    public Map<String, B> getConfigs() {
        return groupMap;
    }
}
