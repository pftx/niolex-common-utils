package org.apache.niolex.commons.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServiceHandler implements InvocationHandler {
	private static final Logger log = LoggerFactory.getLogger(RpcServiceHandler.class);
	
	private final String serviceUrl;
	private final InvocationHandler handler;
	private final int errorBlockTime;
	private long nextWorkTime = -1;
	
	public RpcServiceHandler(String serviceUrl, InvocationHandler handler, int errorBlockTime, boolean isReady) {
		super();
		this.serviceUrl = serviceUrl;
		this.handler = handler;
		this.errorBlockTime = errorBlockTime;
		if (!isReady)
			notReady(new IOException("Failed to connect when server initialize."));
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return handler.invoke(proxy, method, args);
	}

	public boolean isReady() {
		boolean isReady = System.currentTimeMillis() > nextWorkTime;
		if (!isReady && log.isDebugEnabled())
			log.debug("Server [{}] is not ready for work.", serviceUrl);
		return isReady;
	}
	
	public void notReady(IOException ioe) {
		nextWorkTime = System.currentTimeMillis() + errorBlockTime;
		log.warn("Server [" + serviceUrl + "] has been set to not ready status: " + ioe.getMessage());
	}
	
	@Override
	public String toString() {
		return serviceUrl;
	}
}
