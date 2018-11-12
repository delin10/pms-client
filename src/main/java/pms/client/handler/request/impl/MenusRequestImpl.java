package pms.client.handler.request.impl;

import org.apache.http.HttpResponse;

import pms.client.handler.request.BasicRequest;

public class MenusRequestImpl extends BasicRequest{
	public HttpResponse menus() {
		return client.get(getRootURL()+"init?comp=tree_menu",null);
	}
	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return root_url;
	}

}
