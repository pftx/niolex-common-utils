package org.apache.niolex.commons.rpc;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.niolex.commons.config.Constants;
import org.apache.niolex.commons.rpc.conf.RpcConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.rpc.exception.ExceptionHandler;

public class RpcInitUtil {
	private static final Logger log = LoggerFactory.getLogger(RpcInitUtil.class);
	
	private static final String SERVER_ENCODING = Constants.SERVER_ENCODING;

	public static RetryHandler buildProxy(RpcConfigBean conf) {
		log.info("Start to build rpc proxy: [serverList=" + Arrays.toString(conf.serverList) + ", serviceUrl=" + conf.serviceUrl + ", header="
				+ conf.getHeader() + "]");
		List<RpcServiceHandler> listHandlers = new ArrayList<RpcServiceHandler>();
		HeaderMcpackRpcProxy proxy = null;
		int serverNum = conf.serverList.length;
		String completeUrl = "";
		for (int i = 0; i < serverNum; ++i) {
			completeUrl = conf.serverList[i] + conf.serviceUrl;
			try {
				proxy = new HeaderMcpackRpcProxy(completeUrl, SERVER_ENCODING, new ExceptionHandler());
				proxy.setHeaderMap(conf.getHeader());
				proxy.setConnectTimeout(conf.connectTimeout);
				proxy.setReadTimeout(conf.readTimeout);
				listHandlers.add(new RpcServiceHandler(completeUrl, proxy, conf.errorBlockTime, checkServerStatus(completeUrl,
				        conf.connectTimeout, conf.readTimeout)));
			} catch (Exception e) {
				log.warn("Failed to build rpc proxy for " + completeUrl + " : " + e.getMessage());
			}
		}
		if (listHandlers.isEmpty()) {
			throw new IllegalStateException("No rpc server is ready for service: " + conf.serviceUrl);
		}
		
		return new RetryHandler(listHandlers, conf.retryTimes, conf.intervalBetweenRetry);
	}

	private static boolean checkServerStatus(String completeUrl, int connectTimeout, int readTimeout) {
		try {
			URL u = new URL(completeUrl);
			URLConnection proxy = u.openConnection();
			proxy.setConnectTimeout(connectTimeout);
			proxy.setReadTimeout(readTimeout);
			proxy.connect();
			if (proxy.getContentLength() <= 1) {
				log.warn("Failed to connect to " + completeUrl + " : Server response too short.");
				return false;
			}
			String serverStatus = proxy.getHeaderField(0);
			if (serverStatus != null && serverStatus.matches(".*[45][0-9][02-9].*")) {
				log.warn("Failed to connect to " + completeUrl + " : Invalid server response " + serverStatus);
				return false;
			}
			log.info("Server [" + completeUrl + "] status: " + serverStatus);
			return true;
		} catch (Exception e) {
			log.warn("Failed to connect to " + completeUrl + " : " + e.getMessage());
			return false;
		}
	}

}
