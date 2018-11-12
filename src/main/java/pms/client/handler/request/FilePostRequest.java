package pms.client.handler.request;

import org.apache.http.HttpResponse;

public class FilePostRequest extends BasicRequest{
	public HttpResponse upload(byte[] bytes) {
		System.out.println("本地上传图片大小:"+bytes.length);
		return client.postFile(root_url+"file", null, bytes);
	}

	@Override
	public String getRootURL() {
		// TODO Auto-generated method stub
		return root_url;
	}

}
