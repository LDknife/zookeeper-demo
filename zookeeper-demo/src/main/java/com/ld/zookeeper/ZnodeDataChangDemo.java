package com.ld.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 
 * @author LD
 * @date 2018年12月25日 下午2:10:46 
 * @Description: znode节点数据改变
 */
public class ZnodeDataChangDemo implements Watcher{

	//连接信息
	private static String connectString = "192.168.2.68:2181,192.168.2.68:2182,192.168.2.68:2183";
	//session超时时间
	private static int sessionTimeout = 2000;
	//计数器
	private final static CountDownLatch dl = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	private static Stat stat = new Stat();
	
	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new ZnodeDataChangDemo());
		dl.await();
	
		zk.create("/zk-test", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(new String(zk.getData("/zk-test", true, stat)));
		
		zk.getData("/zk-test", true, stat);
		System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
		zk.setData("/zk-test", "234".getBytes(), -1);
		Thread.sleep(Long.MAX_VALUE);
	}
	
	/**
	 * 监听
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("监听器启动成功");
		if(KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
                dl.countDown();
            } else if (event.getType() == EventType.NodeDataChanged) {
                try {
                    System.out.println(new String(zk.getData(event.getPath(), true, stat)));
                    System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
                } catch (Exception e) {
                }
            }
		}
	}

}
