package com.ld.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 *  连接zookeeper,并监听
 * @author administrator-pc
 *
 */
public class ZKDemo implements Watcher{

	//连接信息
	private static String connectString = "192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183";
	//session超时时间
	private static int sessionTimeout = 2000;
	//计数器
	private final static CountDownLatch dl = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new ZKDemo());
		System.out.println(zooKeeper.getState());
		dl.await();
		System.out.println("监听器已经执行完成");
	}
	
	/**
	 * 执行监听的方法
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("执行监听器");
		if (KeeperState.SyncConnected == event.getState()) {
			dl.countDown();
		}
	}

	
}
