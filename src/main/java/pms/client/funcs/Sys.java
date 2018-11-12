package pms.client.funcs;

import util.comm.conf.ConfUtil;
import util.comm.file.FileUtil;
import util.security.RSA;
import util.security.RSA.KeyType;
import util.web.LocalStorage;

public class Sys {
	private static LocalStorage localStorage = new LocalStorage(FileUtil.getProjectAb("conf"));
	private static RSA rsa = new RSA();
	private static boolean is_store_pwd = true;
	//private static final String IS_STORE_PWD_KEY = "IS_STORE_PWD_";
	// 后接秒级时间戳
	private static final String ID_KEY = "USER_ID_";
	// 密码后接id,采用rsa加密
	private static final String PWD_KEY = "PWD_";
	private static final String COOKIE_KEY = "COOKIE_ID_";
	//private static final String COOKIE_EXPIRE_TIME_KEY = "COOKIE_ID_EXPIRE_TIME_";
	private static final String CONF_KEY = "setting";
	static {
		localStorage.set("setting", "setting.conf").addToSchedule("setting").start(10);
		rsa.loadKey(FileUtil.getProjectAb("conf/private.pem"), KeyType.PRIVATE_KEY, true);
		rsa.loadKey(FileUtil.getProjectAb("conf/public.pem"), KeyType.PUBLIC_KEY, true);
	}

	/**
	 * 对缓存的cookie进行更新处理，并不立即写入缓存，所以必须添加运行钩子进行同步处理
	 * 
	 * @param cookie
	 * @param expire_time
	 */
	public static void updateCookie(String id) {
		localStorage.setProperty(CONF_KEY, COOKIE_KEY + id, Func.cookies().toString());
		// if (expire_time != null) {
		// localStorage.setProperty(CONF_KEY, COOKIE_EXPIRE_TIME_KEY + id, expire_time);
		// }
	}

	public static String getAutoLogin() {
		return ConfUtil.get(CONF_KEY, "auto_login");
	}

	public static void setAutoLogin(boolean auto_login) {
		ConfUtil.set(CONF_KEY, "auto_login", "" + auto_login);
	}

	public static String[] getIds() {
		String[] res = ConfUtil.getConf(CONF_KEY).getPropertyByKeyPattern(ID_KEY + ".*");
		return res;
	}

	public static void storeId(String id) {
		localStorage.setProperty(CONF_KEY, ID_KEY + id, id);
	}

	public static String getPwd(String id) {
		try {
			String pwd = ConfUtil.get(CONF_KEY, PWD_KEY + id);
			if (pwd == null || pwd.isEmpty()) {
				return "";
			}
			return rsa.decrypt(pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String cookieOf(String id) {
		return ConfUtil.get(CONF_KEY, COOKIE_KEY+id);
	}
	
	public static void removeCookie(String id) {
		ConfUtil.getConf(CONF_KEY).removeProperty(COOKIE_KEY+id);
	}

	public static void storeIdAndPwd(String id, String pwd) {
		// 考虑到storeid为必须过程
		// storeId(id);
		//
		//
		// 加密过程
		//
		storeId(id);
		if (is_store_pwd) {
			try {
				localStorage.setProperty(CONF_KEY, PWD_KEY + id, rsa.encrypt(pwd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
