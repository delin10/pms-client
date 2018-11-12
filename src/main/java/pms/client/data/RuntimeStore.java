package pms.client.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pms.client.handler.ui.BasicUIHandler;

public class RuntimeStore {
	private static ExecutorService exec=Executors.newSingleThreadExecutor();
	private static ConcurrentHashMap<String, Object> attrs=new ConcurrentHashMap<>();
	private static BasicUIHandler ui=new BasicUIHandler();
	public static void put(String key,Object value) {
		attrs.put(key, value);
	}
	
	public static Object get(String key) {
		return attrs.get(key);
	}
	
	public static BasicUIHandler runUI() {
		return ui;
	}
	
	public static void exec(Runnable run) {
		exec.execute(run);
	}
}
