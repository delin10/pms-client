package pms.client.funcs;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import pms.client.bean.User;
import util.comm.file.FileUtil;
import util.comm.lambda.exception.Handler;
import util.comm.lambda.exception.SimpleExec;
import util.system.hook.HookUtil;
import util.web.conn_httpclient.DHttpClient;

public class Func {
	private static User user;
	private static ExecutorService exec = Executors.newSingleThreadExecutor();
	private static DHttpClient client = DHttpClient.create().init();
	private static final String root_url = "http://localhost:12001/pms/";
	static {
		HookUtil.addHook(() -> {
			client.close();
			exec.shutdownNow();
		});
	}

	public static void exec(Runnable longtime) {
		// submit发生zuse
		exec.execute(longtime);
	}

	public static HttpResponse post(String uri, String json) {
		return client.postJSON(root_url + uri, null, json);
	}

	public static Object cookies() {
		return client.handle_cookie_store(store -> {
			return store.getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue())
					.collect(Collectors.joining(";"));
		});
	}

	public static HttpResponse logout() {
		return client.get(root_url + "logout", null);
	}

	public static HttpResponse login_cookie(String id) {
		String cookie = Sys.cookieOf(id);
		// System.out.println(cookie);
		if (cookie != null && !cookie.trim().isEmpty()) {
			System.out.println("cookie缓存=" + cookie);
			return client.get_carry_cookie(root_url + "login?action=check", null, cookie);
		}
		return null;
	}

	public static HttpResponse login_id_pwd(String id, String pwd) {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("pwd", pwd);

		return client.postJSON(root_url + "login", null, json.toJSONString());
	}

	public static HttpResponse post(String json) {
		return client.postJSON(root_url, null, json);
	}

	public static HttpResponse get(String uri) {
		return client.get(root_url + uri, null);
	}

	public static HttpResponse menus() {
		return client.get(root_url + "init?comp=tree_menu", null);
	}

	public static HttpResponse resources() {
		return client.get(root_url + "setting?action=resources&resource_id=resources&role_id=0", null);
	}

	public static HttpResponse roles() {
		return client.get(root_url + "setting?action=roles&resource_id=roles", null);
	}

	public static HttpResponse auth(String json, String role_id) {
		return client.postJSON(root_url + String.format("setting?action=auth&role_id=%s", role_id), null, json);
	}

	public static HttpResponse backups() {
		return client.get(root_url + "setting?action=backups&resource_id=backups", null);
	}

	public static HttpResponse company_image() {
		return client.get(root_url + "company?action=company_image&resource_id=backups", null);
	}

	public static HttpResponse company_info() {
		return client.get(root_url + "company?action=query", null);
	}

	public static HttpResponse users() {
		return client.get(root_url + "setting?action=showUser&resource_id=showUser", null);
	}

	// 获取全部building
	public static HttpResponse buildings() {
		return client.get(root_url + "building?action=query", null);
	}
	// 获取全部车子

	public static HttpResponse cars() {
		return client.get(root_url + String.format("car?action=query&community_name=%s", user.getCommunity()), null);
	}
	// 获取全部收款记录

	public static HttpResponse charge_record() {
		return client.get(root_url + "comm?action=query&type=charge_form", null);
	}
	// 获取全部收款项

	public static HttpResponse charge_items() {
		return client.get(root_url + "comm?action=query&type=charge_item", null);
	}
	// 获取全部社区

	public static HttpResponse communities() {
		return client.get(root_url + "community?action=query&type=community", null);
	}
	// 获取全部合同

	public static HttpResponse contracts() {
		return client.get(root_url + "comm?action=query&type=contract", null);
	}
	// 获取全部部门

	public static HttpResponse departments() {
		return client.get(root_url + "comm?action=query&type=department", null);
	}
	// 获取全部设备检查记录

	public static HttpResponse device_check_record() {
		return client.get(root_url + String.format("car?action=query&community_name=%s", user.getCommunity()), null);
	}
	// 获取全部设备信息

	public static HttpResponse devices() {
		return client.get(root_url + "comm?type=device&action=query", null);
	}
	// 获取全部职员信息

	public static HttpResponse employees() {
		return client.get(root_url + "comm?type=employee&action=query", null);
	}
	// 获取全部住户家属信息

	public static HttpResponse owner_familes(String owner_id) {
		return client.get(root_url + String.format("ownerFamily?action=query&owner_name=%s", owner_id), null);
	}
	// 获取全部住户

	public static HttpResponse owners() {
		return client.get(root_url + String.format("comm?action=query&type=owner", user.getCommunity()), null);
	}
	// 获取全部宠物列表

	public static HttpResponse pets() {
		return client.get(root_url + String.format("car?action=query&community_name=%s", user.getCommunity()), null);
	}
	// 获取全部保修记录

	public static HttpResponse repair_reports() {
		return client.get(root_url + String.format("car?action=query&community_name=%s", user.getCommunity()), null);
	}

	// 获取房间
	public static HttpResponse rooms() {
		return client.get(root_url + "room?action=query", null);
	}

	public static HttpResponse community_names() {
		return client.get(root_url + "community?action=names", null);
	}

	public static HttpResponse set_role(String user_id, String role_id) {
		return client.get(root_url + String.format("setting?action=setRole&user_id=%s&role_id=%s", user_id, role_id),
				null);
	}

	public static HttpResponse backup() {
		return client.get(root_url + "setting?action=backup", null);
	}

	public static HttpResponse delete_backup(String[] paths) {
		return client.postJSON(root_url + "setting?action=delBackup", null, JSON.toJSONString(paths));
	}

	public static HttpResponse restore(String path) {
		JSONObject jo = new JSONObject();
		jo.put("path", path);
		return client.postJSON(root_url + "setting?action=restore", null, jo.toJSONString());
	}

	public static HttpResponse delete_building(Map<String, Object>[] ids) {
		System.out.println(JSON.toJSONString(ids));
		return client.postJSON(root_url + "building?action=delete", null, JSON.toJSONString(ids));
	}

	public static String[] toArray(HttpResponse response, String field) {
		String json = toJSON(response);
		System.out.println(json);
		if (json == null) {
			return new String[0];
		} else {
			if (field == null) {
				return JSON.parseObject(json, String[].class);
			} else {
				return JSONObject.parseObject(json).getJSONArray(field).toArray(new String[0]);
			}
		}
	}

	public static String toJSON(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		} else {
			return SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				return json;
			}, Handler.PRINTTRACE).toString();

		}
	}

	public static String get(String json, String field) {
		JSONObject jo = JSONObject.parseObject(json);
		return jo.getString(field);
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Func.user = user;
	}
}
