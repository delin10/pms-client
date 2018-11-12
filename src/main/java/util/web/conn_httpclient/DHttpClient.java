package util.web.conn_httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.alibaba.fastjson.JSON;

import util.comm.URIUtil;
import util.comm.array.CollectionUtil;
import util.comm.file.FileUtil;
import util.comm.lambda.exception.SimpleExec;
import util.serialize.SerializeUtil;
import util.thread.schedule.Scheduler;
import util.web.conn_httpclient.func.CookieStoreHandler;
import util.web.conn_httpclient.impl.StandardCookieStoreImpl;

public class DHttpClient {
	private static Scheduler scheduler;
	private static String responsibility;
	private static RequestConfig conf;
	private static final String COOKIE_FILE_FORMAT = "DHTTPCLIENT_COOKIE_PERSISTENCE_%s.cookie";
	private CookieStore store = null;
	private HttpClientContext context = null;
	private CloseableHttpClient client;
	private ReentrantLock lock = new ReentrantLock();

	public static DHttpClient create(boolean cache) {
		if (cache) {
			scheduler = new Scheduler();
		}
		return new DHttpClient();
	}

	public static DHttpClient create() {
		return new DHttpClient();
	}

	public DHttpClient init() {
		conf = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000)
				.build();
		client = HttpClients.custom().setDefaultRequestConfig(conf).build();
		context = new HttpClientContext();
		store = new StandardCookieStoreImpl();
		context.setCookieStore(store);
		// Cookie没有持久化到磁盘
		// store.getCookies().stream().forEach(System.out::println);
		return this;
	}

	public DHttpClient change() {
		client = HttpClients.custom().setDefaultRequestConfig(conf).build();
		return this;
	}

	public DHttpClient setResponsibility(String responsibility) {
		DHttpClient.responsibility = responsibility;
		return this;
	}

	public Object handle_cookie_store(CookieStoreHandler handler) {
		return handler.handle(store);
	}

	public DHttpClient persistCookies() {
		// 文件名称使用实例的hash值
		String cookie_file_name = String.format(COOKIE_FILE_FORMAT, this.hashCode() + "");
		ArrayList<Object> cookies = new ArrayList<>();
		store.getCookies().stream().map(SerializeUtil::serialize).forEach(cookies::add);
		byte[] all_cookies = (byte[]) CollectionUtil.mergeArrays(cookies, byte.class);
		FileUtil.writeBytes(all_cookies, responsibility + "/" + cookie_file_name);
		return this;
	}

	public DHttpClient addCookies(List<BasicClientCookie> cookies) {
		// cookies.stream().forEach(System.out::println);
		cookies.stream().forEach(store::addCookie);
		return this;
	}

	public HttpResponse postJSON(String uri, Map<String, String> params, String json) {
		HttpPost post = new HttpPost();
		// post.setConfig(conf);
		return (HttpResponse) SimpleExec.exec(wrapper -> {
			post.setURI(new URI(params == null ? uri : URIUtil.parse(uri, params)));
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(URLEncoder.encode(json, "utf-8")));
			// lock.lock();
			System.out.println("执行" + uri);
			store.getCookies().stream().forEach(System.out::println);
			HttpResponse response = client.execute(post, context);
			// addCookies(Tools.cookies(response));
			return response;
		}, e -> {
			System.out.println("发生异常，修改连接");
			change();
		}, (data) -> {
			// ((ReentrantLock) data.getValue()).unlock();
		});
	}

	public HttpResponse postFile(String uri, Map<String, String> params, byte[] bytes) {
		HttpPost post = new HttpPost();
		// post.setConfig(conf);
		return (HttpResponse) SimpleExec.exec(wrapper -> {
			post.setURI(new URI(params == null ? uri : URIUtil.parse(uri, params)));
			post.setHeader("Content-Type", "multipart/form-data");
			post.setEntity(new ByteArrayEntity(bytes));
			// lock.lock();
			System.out.println("执行" + uri);
			store.getCookies().stream().forEach(System.out::println);
			HttpResponse response = client.execute(post, context);
			// addCookies(Tools.cookies(response));
			return response;
		}, data -> {
			System.out.println("发生异常，修改连接");
			change();
		}, (data) -> {
			// ((ReentrantLock) data.getValue()).unlock();
		});
	}

	// 抛出异常client会关闭，需要重新初始化
	public HttpResponse get(String uri, Map<String, String> params) {
		HttpGet get = new HttpGet();
		// get.setConfig(conf);

		return (HttpResponse) SimpleExec.exec(wrapper -> {
			get.setURI(new URI(params == null ? uri : URIUtil.parse(uri, params)));
			// lock.lock();
			System.out.println("执行" + uri);
			store.getCookies().stream().forEach(System.out::println);
			HttpResponse response = client.execute(get, context);
			// cookie自动缓存
			System.out.println(uri);
			// store.getCookies().stream().forEach(System.out::println);
			// addCookies(Tools.cookies(response));
			return response;
		}, data -> {
			System.out.println("发生异常，修改连接");
			change();
		}, (data) -> {
			// ((ReentrantLock) data.getValue()).unlock();
		});
	}

	public HttpResponse get_carry_cookie(String uri, Map<String, String> params, String cookies) {
		HttpGet get = new HttpGet();
		// get.setConfig(conf);
		return (HttpResponse) SimpleExec.exec(wrapper -> {
			get.setURI(new URI(params == null ? uri : URIUtil.parse(uri, params)));
			get.addHeader("cookie", cookies);
			// 必须加context以便存储cookie
			HttpResponse response = client.execute(get, context);
			// cookie自动缓存
			// store.getCookies().stream().forEach(System.out::println);
			// addCookies(Tools.cookies(response));
			return response;
		}, data -> {
			change();
		});
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String... args) {
		BasicClientCookie cookie = new BasicClientCookie("a", "12");
		cookie.setExpiryDate(Date.from(Instant.now()));
		cookie.setDomain("localhost");
		// JSON无法得到过期时间等信息
		// 可序列化保存完整cookie信息
		System.out.println(JSON.parseObject(JSON.toJSONString(cookie), BasicClientCookie.class));
	}
}
