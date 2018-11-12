package util.web.conn_httpclient.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.BasicClientCookie;

import util.comm.array.CollectionUtil;

public class Tools {
	public static List<BasicClientCookie> cookies(HttpResponse response) {
		return Arrays.stream(response.getAllHeaders()).filter(header -> "Set-Cookie".equalsIgnoreCase(header.getName()))
				.map(header -> new BasicClientCookie(header.getName(), header.getValue())).collect(Collectors.toList());
	}
	

	public static byte[] content_wait(int content_length, InputStream in) {
		try {
			byte[] buffer = new byte[1024];
			ArrayList<Object> list = new ArrayList<>();
			int len;
			int size = 0;
			while (size < content_length) {
				len = in.read(buffer);
				if (len < 0) {
					continue;
				}
				//System.out.println(len);
				// if (len < 0) {
				// Thread.sleep(1);
				// len = in.read(buffer);
				// if (len < 0) {
				// break;
				// }
				// }

				if (len < 1024) {
					list.add(CollectionUtil.trimArray(buffer, len));
					// break;
				} else {
					list.add(buffer);
				}
				size += len;
				buffer = new byte[1024];
			}
			byte[] bytes = (byte[]) CollectionUtil.mergeArrays(list, byte.class);
			System.out.println("读取的字节数量:"+bytes.length);
			return bytes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
