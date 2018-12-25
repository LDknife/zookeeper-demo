package com.ld.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author LD
 * @date 2018年12月25日 下午2:31:22 
 * @Description: 改变子节点，进行监听
 */
public class ZKChildrenDemo implements Watcher{
	
	//连接信息
	private static String connectString = "192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183";
	//session超时时间
	private static int sessionTimeout = 2000;
	//计数器
	private final static CountDownLatch dl = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new ZKChildrenDemo());
		dl.await();
		
		zk.create("/zk-test", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.create("/zk-test/test1", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		List<String> children = zk.getChildren("/zk-test", true);
		for (String string : children) {
			System.out.println(string);
		}
		
		zk.create("/zk-test/test2", "789".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		zk.create("/zk-test/test3", "789".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Thread.sleep(Long.MAX_VALUE);
	}
	
	@Override
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()) {
			if(EventType.None ==  event.getType() && null == event.getPath()) {
				dl.countDown();
			}else {
				try {
					System.out.println("Children:" + zk.getChildren("/zk-test", true));
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
