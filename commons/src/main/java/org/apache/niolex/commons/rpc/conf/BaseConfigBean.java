package org.apache.niolex.commons.rpc.conf;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseConfigBean {
    protected Map<String, String> prop = new HashMap<String, String>();
    protected Map<String, String> header = new HashMap<String, String>();
	protected String groupName;
	protected boolean hasHeader = false;
	
	/**
     * @param groupName The group name
     */
    public BaseConfigBean(String groupName) {
        super();
        this.groupName = groupName;
    }

    public void setConfig(String key, String value) {
		if (hasHeader && key.startsWith("header.")) {
			header.put(key.substring(7), value);
			return;
		}
		try {
			setField(this.getClass().getField(key), value);
		} catch (Exception e) {
			prop.put(key, value);
		}
	}

    protected void setSuper(final BaseConfigBean superConf) {
	    prop.putAll(superConf.prop);
	    header.putAll(superConf.header);
	}
	
	private void setField(Field field, String value) throws Exception {
		Class<?> cls = field.getType();
		if (cls.equals(int.class)) {
			field.setInt(this, Integer.parseInt(value));
		} else if (cls.equals(long.class)) {
			field.setLong(this, Long.parseLong(value));
		} else if (cls.equals(boolean.class)) {
			field.setBoolean(this, Boolean.parseBoolean(value));
		} else {
			field.set(this, value);
		}
	}
	
    // /////////////////////////////////////////////////////////////////////////////////////
    // GETTERS & SETTERS
    // /////////////////////////////////////////////////////////////////////////////////////

    public String getProp(String key) {
        return prop.get(key);
    }
    
    public String getHeader(String key) {
        return header.get(key);
    }
    
    public Map<String, String> getProp() {
        return prop;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

}
