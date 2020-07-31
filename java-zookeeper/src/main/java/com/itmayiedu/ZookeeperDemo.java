
package com.itmayiedu;

import java.io.IOException;
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
 * @classDesc: 功能描述:(zookDemo)
 * @author: 余胜军
 * @createTime: 2017年10月11日 下午5:16:06
 * @version: v1.0
 * @copyright:上海每特教育科技有限公司
 * @QQ:644064779
 */
public class ZookeeperDemo implements Watcher {

	/**
	 * 集群地址
	 */
	// private static final String CONNECT_ADDRES =
	// "192.168.110.155:2181,192.168.110.156:2181,192.168.110.157:2181";
	private static final String CONNECT_ADDRES = "192.168.110.159:2181,192.168.110.160:2181,192.168.110.162:2181";
	/**
	 * 超时时间
	 */
	private static final int SESSIONTIME = 2000;
	
	/**
	 * 信号量,阻塞程序执行,用户等待zookeeper连接成功,发送成功信号，
	 */
	private static final CountDownLatch countDownLatch = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		ZooKeeper zk = new ZooKeeper(CONNECT_ADDRES, SESSIONTIME, new Watcher() {

			public void process(WatchedEvent watchedEvent) {
				// 获取事件状态
				KeeperState keeperState = watchedEvent.getState();
				// 获取事件类型
				EventType eventType = watchedEvent.getType();
				// 判断是否建立连接
				if (KeeperState.SyncConnected == keeperState) {
					if (EventType.None == eventType) {
						// 如果建立建立成功,让后程序往下走
						System.out.println("zk 建立连接成功!");
						// 放行-----zk链接成功后，唤醒其他线程
						countDownLatch.countDown();
					}
				}
			}
		});
		// 阻塞-----让其他线程等待
		countDownLatch.await();
		// 创建一个父节点
		System.out.println("开始创建一个父节点...");
		zk.create("/pa", "123456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// String result = zk.create("/pa2", "123456".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// System.out.println("resut:" + result);
		// Thread.sleep(5000);
		// String result = new String(zk.getData("/pa2", false, null));
		// System.out.println("result:" + result);
		
		
//		zk.close(); 链接zk，是通过创建子线程的方式链接，如果没有zk.close();，主线程走完了，子线程还没有连接到zk，
//		就会链接失败，用zk.close();就是让主线程等待子线程链接zk，操作完成后主线程结束
		zk.close();
	}

	public void process(WatchedEvent event) {
		System.out.println("进入 process ....... event =" + event);
		if (event == null) {
			return;
		}
		// 获取事件状态
		KeeperState keeperState = event.getState();
		// 获取事件类型
		EventType eventType = event.getType();
		// 判断是否建立连接
		if (KeeperState.SyncConnected == keeperState) {
			if (EventType.None == eventType) {
				// 如果建立建立成功,让后程序往下走
				System.out.println("zk 建立连接成功!");
				// 放行
				countDownLatch.countDown();
			    //创建节点事件
			} else if (EventType.NodeCreated == eventType) {
          
			}

		}

	}
}
