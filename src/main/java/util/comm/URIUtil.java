package util.comm;

import java.util.Map;
import java.util.stream.Collectors;

public class URIUtil {
	public static String parse(String baseuri, Map<String, String> params) {
		return baseuri + params.entrySet().stream().map(param -> param.getKey() + "=" + param.getValue())
				.collect(Collectors.joining("&", "?", ""));
	}
}
