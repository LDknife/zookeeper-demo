package com.ld.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class GetDataDemo {

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient("192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183", 2000);
		String path = "/zk-client";
		//创建节点
		zkClient.createPersistent(path,"123");
		
		//进行监控
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("数据被删除了");
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("数据变：" + data);
			}
		});
		
		System.out.println(zkClient.readData(path).toString());
		//改变数据
		zkClient.writeData(path, "456");
		Thread.sleep(1000);
		//删除节点
		zkClient.delete(path);
		Thread.sleep(Long.MAX_VALUE);
	}
}
