package pms.client.handler.request.impl;

import org.apache.http.HttpResponse;

import pms.client.handler.request.BasicRequest;

public class CompanyRequestImpl extends BasicRequest {
	private static final String uri = "company";

	public HttpResponse company_info() {
		return client.get(root_url + String.format("%s?action=query", uri), null);
	}

	public HttpResponse company_image() {
		return client.get(root_url + String.format("%s?action=company_image", uri), null);
	}

	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
