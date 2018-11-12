package pms.client.handler.request.impl;

import org.apache.http.HttpResponse;

import pms.client.handler.request.BasicRequest;

public class CompanyInfoRequestImpl extends BasicRequest {

	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return root_url;
	}

	public HttpResponse company_info() {
		return client.get(root_url + "company?action=query", null);
	}
	
	public HttpResponse company_image() {
		return client.get(root_url + "company?action=company_image", null);
	}
}
