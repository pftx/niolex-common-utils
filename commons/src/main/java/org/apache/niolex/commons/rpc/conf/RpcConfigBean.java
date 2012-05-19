package org.apache.niolex.commons.rpc.conf;

import java.util.Arrays;

import org.apache.niolex.commons.config.Constants;


public class RpcConfigBean extends BaseConfigBean {

    public int errorBlockTime = Constants.SERVER_ERROR_BLOCK_TIME;
    public int connectTimeout = Constants.SERVER_CONNECT_TIMEOUT;
    public int readTimeout = Constants.SERVER_READ_TIMEOUT;
    public int retryTimes = Constants.SERVER_RETRY_TIMES;
    public int intervalBetweenRetry = Constants.SERVER_INTERVAL_BT_RETRY;
    public String[] serverList;
    public String serviceUrl = "";

    public RpcConfigBean(String groupName) {
        super(groupName);
        hasHeader = true;
    }

    @Override
    public void setConfig(String key, String value) {
        if ("serverList".equals(key)) {
            serverList = value.split(" *, *");
        } else {
            super.setConfig(key, value);
        }
    }

    @Override
    public void setSuper(final BaseConfigBean superConf) {
        if (superConf instanceof RpcConfigBean) {
            final RpcConfigBean co = (RpcConfigBean) superConf;
            copyFrom(co);
        }
        super.setSuper(superConf);
    }

    public void copyFrom(final RpcConfigBean co) {
        this.errorBlockTime = co.errorBlockTime;
        this.connectTimeout = co.connectTimeout;
        this.readTimeout = co.readTimeout;
        this.retryTimes = co.retryTimes;
        this.intervalBetweenRetry = co.intervalBetweenRetry;
        this.serverList = co.serverList;
        this.serviceUrl = co.serviceUrl;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(groupName).append(") [connectTimeout=").append(connectTimeout).append(
                ", errorBlockTime=").append(errorBlockTime).append(", intervalBetweenRetry=").append(
                intervalBetweenRetry).append(", readTimeout=").append(readTimeout).append(", retryTimes=").append(
                retryTimes).append(", serverList=").append(Arrays.toString(serverList)).append(", serviceUrl=").append(
                serviceUrl).append(", header=").append(header).append(", prop=").append(prop).append("]");
        return builder.toString();
    }

}
