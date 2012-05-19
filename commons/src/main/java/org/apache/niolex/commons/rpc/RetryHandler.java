package org.apache.niolex.commons.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.niolex.commons.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class RetryHandler implements InvocationHandler {
	private static final Logger log = LoggerFactory.getLogger(RetryHandler.class);

	
	private List<RpcServiceHandler> handlers;
	private int retryTimes;
	private int intervalBetweenRetry;
	private int handlerNum;
	
	public RetryHandler(List<RpcServiceHandler> handlers, int retryTimes, int intervalBetweenRetry) {
		super();
		this.handlers = handlers;
		this.handlerNum = handlers.size();
		this.retryTimes = retryTimes;
		this.intervalBetweenRetry = intervalBetweenRetry;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    int[] idx = RandomUtil.randIntArray(handlerNum);
		RpcServiceHandler handler;
		Throwable cause = null;
		long handleStart = 0, handleEnd = intervalBetweenRetry;
		int anyTried = 0;
		int curTry = 0;
		while (curTry < retryTimes && anyTried < handlerNum) {
			// Try this.
			handler = handlers.get(idx[anyTried]);
			// Count the tried server number.
			++anyTried;
			if (!handler.isReady()) {
				continue;
			}
			if (handleEnd - handleStart < intervalBetweenRetry) {
			    // We need this to prevent RetryHandler from retry too fast and lower level has not recovered.
			    try {
                    Thread.sleep(intervalBetweenRetry + handleStart - handleEnd);
                } catch (Throwable t) {}
			}
			// Ready to try.
			++curTry;
			try {
				handleStart = System.currentTimeMillis();
				LogContext.serviceUrl(handler.getServiceUrl());
				Object obj = handler.invoke(proxy, method, args);
				handleEnd = System.currentTimeMillis();
				if (log.isDebugEnabled()) {
				    StringBuilder sb = new StringBuilder();
				    sb.append(LogContext.prefix()).append(" Succeed to invoke handler on [").append(handler.getServiceUrl());
				    sb.append("] time {").append(handleEnd - handleStart).append("}");
					log.debug(sb.toString());
				}
				return obj;
			} catch (Throwable e) {
			    handleEnd = System.currentTimeMillis();
			    StringBuilder sb = new StringBuilder();
                sb.append(LogContext.prefix()).append(" Failed to invoke handler on [").append(handler.getServiceUrl());
                sb.append("] time {").append(handleEnd - handleStart).append("} RETRY? ").append(e.getCause() != null);
                sb.append(" MSG: ").append(e.getMessage());
				log.info(sb.toString());
				if (e.getCause() == null)
					throw e;
				else if (e.getCause() instanceof UnknownHostException || e.getCause() instanceof SocketException)
					handler.notReady((IOException)(e.getCause()));
				cause = e;
			}
		}
		if (anyTried == handlerNum)
			throw new RpcInvokeException("Failed to service " + method.getName() + ": No rpc server is ready to work!");
		throw new RpcInvokeException("Failed to service " + method.getName() + ": exceeds retry time [" + retryTimes + "].", cause);
	}

	@Override
	public String toString() {
		return handlers.toString();
	}
}
