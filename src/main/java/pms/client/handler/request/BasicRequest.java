package pms.client.handler.request;

import java.util.stream.Collectors;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;

import util.system.hook.HookUtil;
import util.web.conn_httpclient.DHttpClient;

public abstract class BasicRequest {
	protected static DHttpClient client = DHttpClient.create().init();
	protected static final String root_url = "http://localhost:12001/pms/";
	static {
		HookUtil.addHook(() -> {
			client.close();
		});
	}
	
	public static Object cookies() {
		return client.handle_cookie_store(store -> {
			return store.getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue())
					.collect(Collectors.joining(";"));
		});
	}
	
	public static HttpResponse postData(String uri,Object data) {
		String json=JSON.toJSONString(data);
		return client.postJSON(uri, null, json);
	}
	
	public abstract String getRootURL();
}
