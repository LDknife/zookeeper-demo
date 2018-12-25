package com.ld.zookeeper;

import org.I0Itec.zkclient.ZkClient;

public class CreateNodeDemo {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183", 2000);
		String path = "/zk-client/c1";
		zkClient.createPersistent(path,true);
	}
}
