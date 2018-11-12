package pms.client.handler.request.impl;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;

import pms.client.handler.request.BasicRequest;

public class LoginRequest extends BasicRequest{

	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return "login";
	}
	

	public static HttpResponse logout() {
		return client.get(root_url + "logout", null);
	}

	public static HttpResponse login_cookie(String id,String cookie) {
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

}
