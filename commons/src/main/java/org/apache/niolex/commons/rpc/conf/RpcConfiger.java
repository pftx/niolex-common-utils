package org.apache.niolex.commons.rpc.conf;


public class RpcConfiger extends BaseConfiger<RpcConfigBean> {
	
	public RpcConfiger(String fileName) {
		super(fileName);
	}

	@Override
	protected RpcConfigBean newConfigBean(String groupName) {
		return new RpcConfigBean(groupName);
	}
	
	public RpcConfigBean getConfig() {
		return (RpcConfigBean)super.getConfig(BaseConfiger.DEFAULT);
	}
	
}
