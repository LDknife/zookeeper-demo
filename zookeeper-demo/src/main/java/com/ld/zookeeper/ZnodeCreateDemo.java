package com.ld.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * 
 * @author LD
 * @date 2018年12月25日 下午1:58:25 
 * @Description: 创建znode
 */
public class ZnodeCreateDemo implements Watcher{

	//连接信息
	private static String connectString = "192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183";
	//session超时时间
	private static int sessionTimeout = 2000;
	//计数器
	private final static CountDownLatch dl = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new ZnodeCreateDemo());
		dl.await();
		
		//创建znode 临时节点
		String path1 = zooKeeper.create("/zk-test-", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create path:" + path1);
		
		//创建znode 临时顺序节点
		String path2 = zooKeeper.create("/zk-test-", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create path:" + path2);
	}
	
	/**
	 * 监听
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("执行监听器");
		if (KeeperState.SyncConnected == event.getState()) {
			dl.countDown();
		}
	}

}
