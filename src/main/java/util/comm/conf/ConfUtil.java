package util.comm.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import util.comm.file.FileUtil;
import util.comm.regex.PatternUtil;
import util.system.hook.HookUtil;

public class ConfUtil {
	private static Map<String, Conf> conf;
	static {
		conf = new HashMap<>();
		HookUtil.addHook(()->{
			conf.values().forEach(conf_->conf_.sync_properties());
		});
	}

	public static void relateTo(String path, String key, boolean is_ab) {
		String ab = is_ab ? path : FileUtil.getProjectAb(path);
		try (InputStream in = Files.newInputStream(Paths.get(ab), StandardOpenOption.CREATE_NEW)) {
			Properties properties = new Properties();
			properties.load(in);
			conf.put(key, Conf.create().setConf(properties).setPath(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String get(String key, String name) {
		return get(key).getProperty(name);
	}

	public static Properties get(String key) {
		return (Properties) conf.get(key).getConf();
	}

	public static void set(String key, String name, String value) {
		Conf conf_ = conf.get(key);
		if (conf_ != null) {
			conf_.setProperty(name, value);
		}
	}
	
	public static Conf getConf(String key) {
		return conf.get(key);
	}

	public static void putAndSave(String key, String name, String value) {
		Conf conf_ = conf.get(key);
		if (conf_ == null) {
			return;
		}
		Properties properties = (Properties) conf_.getConf();
		properties.put(name, value);
		sync(key);
	}

	public static void sync(String key) {
		Conf conf_ = conf.get(key);
		if (conf_ == null) {
			return;
		}
		Properties properties = (Properties) conf_.getConf();
		try (OutputStream out = Files.newOutputStream(Paths.get(conf_.getPath()))) {
			properties.store(out, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sync_soft(String key) {
		Conf conf_ = conf.get(key);
		if (conf_ == null || !conf_.isUpdate()) {
			return;
		}
		Properties properties = (Properties) conf_.getConf();
		try (OutputStream out = Files.newOutputStream(Paths.get(conf_.getPath()))) {
			properties.store(out, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class Conf {
		private Object conf;
		private String path;
		private boolean update = false;

		public static Conf create() {
			return new Conf();
		}

		public Object getConf() {
			return conf;
		}

		public Conf setConf(Object conf) {
			this.conf = conf;
			return this;
		}

		public String getPath() {
			return path;
		}

		public Conf setPath(String path) {
			this.path = path;
			return this;
		}

		public Conf setProperty(String key, String value) {
			((Properties) conf).setProperty(key, value);
			setUpdate(true);
			return this;
		}

		public String getProperty(String key) {
			return ((Properties) conf).getProperty(key);
		}
		
		public Conf removeProperty(Object key) {
			 ((Properties) conf).remove(key);
			 return this;
		}

		public String[] getPropertyByKeyPattern(String key_regex) {
			Properties properties = ((Properties) conf);
			return properties.entrySet().stream().filter(kv -> {
				System.out.println(PatternUtil.match(key_regex, kv.getValue().toString()));
				return !PatternUtil.match(key_regex, kv.getKey().toString()).isEmpty();
			}).map(kv -> kv.getValue().toString()).toArray(String[]::new);
		}

		public boolean isUpdate() {
			return update;
		}

		public void setUpdate(boolean update) {
			this.update = update;
		}
		//考虑到配置文件存在xml、properties等格式，必须建立一个基类，提供各种格式的标准实现
		public Conf sync_properties() {
			if (update) {
				Properties properties = (Properties) conf;
				try (OutputStream out = Files.newOutputStream(Paths.get(path))) {
					properties.store(out, "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return this;
		}
	}
}
