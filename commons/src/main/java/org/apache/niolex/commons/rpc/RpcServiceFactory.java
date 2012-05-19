package org.apache.niolex.commons.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.niolex.commons.rpc.conf.BaseConfiger;
import org.apache.niolex.commons.rpc.conf.RpcConfigBean;
import org.apache.niolex.commons.rpc.conf.RpcConfiger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcServiceFactory {
	private static final Logger log = LoggerFactory.getLogger(RpcServiceFactory.class);
	
	private Map<String, InvocationHandler> handlers = new HashMap<String, InvocationHandler>();
	private RpcConfiger configer;

	private RpcServiceFactory(String fileName) {
		configer = new RpcConfiger(fileName);
		Map<String, RpcConfigBean> confs = configer.getConfigs();
		for (Entry<String, RpcConfigBean> entry : confs.entrySet()) {
			RpcConfigBean conf = entry.getValue();
			InvocationHandler handler = RpcInitUtil.buildProxy(conf);
			handlers.put(entry.getKey(), handler);
			StringBuilder sb = new StringBuilder();
			sb.append("\n===>Api server list for [" + entry.getKey() + "]:\n");
				sb.append("    ").append(handler).append("\n");
			log.info(sb.toString());
		}
	}
	
	public static final RpcServiceFactory getInstance(String fileName) {
	    return new RpcServiceFactory(fileName);
	}

	@SuppressWarnings("unchecked")
    public <T> T getService(String groupName, Class<T> c) {
	    InvocationHandler handler = handlers.get(groupName);
	    if (handler == null)
	        throw new IllegalArgumentException("Rpc server config not found for your interface!");
		return (T) Proxy.newProxyInstance(RpcServiceFactory.class.getClassLoader(),
                new Class[] {c}, handler);
	}
	
	public <T> T getService(Class<T> c) {
	    if (c.isAnnotationPresent(RpcConfig.class)) {
	        String groupName = c.getAnnotation(RpcConfig.class).value();
	        log.info("Use config [" + groupName + "] for interface [" + c.getName() + "].");
	        return getService(groupName, c);
	    } else {
    	    log.info("Annotation not found for interface [" + c.getName() + "], default config is used instead.");
    		return getService(BaseConfiger.DEFAULT, c);
	    }
	}
	
	public RpcConfiger getconfiger() {
		return configer;
	}
}
