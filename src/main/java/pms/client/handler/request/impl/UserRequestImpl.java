package pms.client.handler.request.impl;

import org.apache.http.HttpResponse;

import pms.client.handler.request.BasicRequest;
/**
 * 发送user面板相关的请求
 * @author delin
 */
public class UserRequestImpl extends BasicRequest {
	public HttpResponse authRole(String roleId,String userId) {
		return client.get(root_url+"user?action=update&roleId="+roleId+"&userId="+userId, null);
	}
	
	public HttpResponse getUsers() {
		return client.get(root_url+"user?action=query", null);
	}

	@Override
	public String getRootURL() {return null;}
}
