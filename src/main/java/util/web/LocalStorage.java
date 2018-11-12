package util.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import util.comm.conf.ConfUtil;
import util.system.hook.HookUtil;
import util.thread.schedule.Scheduler;

public class LocalStorage {
	private static Scheduler scheduler;
	// 管理的配置文件夹
	private String responsibility;
	// 需要管理的配置文件
	private HashMap<String, String> files = new HashMap<>();
	// 定时任务列表
	private HashSet<String> schedule_keys = new HashSet<>();
	static {
		HookUtil.addHook(() -> {
			scheduler.shutdownNow();
		});
	}

	/**
	 * @param res
	 *            需要管理的缓存仓库
	 */
	public LocalStorage(String res) {
		// TODO Auto-generated constructor stub
		this.responsibility = res;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public LocalStorage setResponsibility(String responsibility) {
		this.responsibility = responsibility;
		return this;
	}

	/**
	 * 获取对应key的配置文件中变量的值
	 * 
	 * @param key
	 *            文件对应的key
	 * @param name
	 *            文件中的变量名称
	 * @return
	 */
	public String get(String key, String name) {
		return ConfUtil.get(key, name);
	}
	

	/**
	 * 设置对应key的配置文件中变量的值
	 * 
	 * @param key
	 *            文件对应的key
	 * @param name
	 *            文件中的变量名称
	 * @return
	 */
	public LocalStorage setProperty(String key, String name,String value) {
		ConfUtil.set(key, name, value);
		return this;
	}

	/**
	 * 加载配置文件并设置映射参数
	 * 
	 * @param key
	 * @param file_rel_path
	 * @return
	 */
	public LocalStorage set(String key, String file_rel_path) {
		ConfUtil.relateTo(responsibility + "/" + file_rel_path, key,true);
		this.files.put(key, file_rel_path);
		return this;
	}

	/**
	 * 把对应key的配置持久化到文件中去
	 * 
	 * @param key
	 * @return
	 */
	public LocalStorage persist(String key) {
		if (files.containsKey(key)) {
			ConfUtil.sync(key);
		}
		return this;
	}

	/**
	 * 将所有的配置文件持久到硬盘中
	 * 
	 * @return
	 */
	public LocalStorage persistAll() {
		files.keySet().forEach(ConfUtil::sync);
		return this;
	}

	/**
	 * 把对应的key添加至定期缓存的列表中去
	 * 
	 * @param key
	 * @return
	 */
	public LocalStorage addToSchedule(String key) {
		if (files.containsKey(key)) {
			schedule_keys.add(key);
		}
		return this;
	}

	/**
	 * 缓存shedule_keys中的值
	 * @return
	 */
	public LocalStorage schedulePersist() {
		schedule_keys.forEach(ConfUtil::sync);
		return this;
	}
	/**
	 * 开始定期任务
	 * @param time
	 * @return
	 */
	public LocalStorage start(int time) {
		if (scheduler == null) {
			scheduler = new Scheduler();
			scheduler.init(10, true);
		}
		scheduler.scheduleControllable(()->{
			schedulePersist();
		}, time, TimeUnit.SECONDS, ""+this.hashCode());
		return this;
	}
	/**
	 * 接受定期缓存任务
	 * @return
	 */
	public LocalStorage stop() {
		scheduler.shutdown(this.hashCode()+"");
		return this;
	}
}
