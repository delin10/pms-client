package pms.client.handler.request.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;

import pms.client.handler.request.PatternRequestImpl;

public abstract class ADQUPatternRequestImpl extends PatternRequestImpl {
	{
		patterns.put("add", "%s?action=%s");
		patterns.put("delete", "%s?action=%s");
		patterns.put("query", "%s?action=%s");
		patterns.put("update", "%s?action=%s");
	}
	public HttpResponse add(Object data) {
		if (!enableAdd()) {
			return null;
		}
		System.out.println("在request中:"+data);
		Object[] params = getParams(getAddAction());
		return post("add", data, params);
	}

	public HttpResponse delete(Object data) {
		if (!enableDelete()) {
			return null;
		}
		Object[] params = getParams(getDeleteAction());
		if (data != null) {
			return post("delete", data, params);
		} else {
			return get("delete", params);
		}
	}

	public HttpResponse query() {
		if (!enableQuery()) {
			return null;
		}
		Object[] params = getParams(getQueryAction());
		return get("query", params);
	}

	public HttpResponse update(Object data) {
		if (!enableUpdate()) {
			return null;
		}
		Object[] params = getParams(getUpdateAction());
		if (data != null) {
			return post("update", data, params);
		} else {
			return get("update", params);
		}
	}

	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return "http://localhost:12001/pms/";
	}
	@Override
	public void initPattern() {
		
	}

	public String[] getAddAction() {
		// TODO Auto-generated method stub
		return new String[] { "add" };
	}

	public String[] getDeleteAction() {
		// TODO Auto-generated method stub
		return new String[] { "delete" };
	}

	public String[] getQueryAction() {
		// TODO Auto-generated method stub
		return new String[] { "query" };
	}

	public String[] getUpdateAction() {
		// TODO Auto-generated method stub
		return new String[] { "update" };
	}

	private Object[] getParams(String[] params) {
		List<String> list = Arrays.stream(params).collect(Collectors.toList());
		list.add(0, getIdetityURL());
		return list.toArray();
	}

	public boolean enableQuery() {
		return true;
	}

	public boolean enableUpdate() {
		return true;
	}

	public boolean enableDelete() {
		return true;
	}

	public boolean enableAdd() {
		return true;
	}

	public abstract String getIdetityURL();
}
