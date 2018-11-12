package pms.client.handler.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;

public abstract class PatternRequestImpl extends BasicRequest {
	protected static Map<String, String> patterns=new HashMap<>();
	public HttpResponse get(String id,Object...params) {
		return client.get(String.format(getRootURL()+patterns.get(id), params), null);
	}
	
	public HttpResponse post(String id,Object data,Object...params) {
		return client.postJSON(String.format(getRootURL()+patterns.get(id), params), null,JSON.toJSONString(data));
	}
	
	public abstract void initPattern();
	
}
