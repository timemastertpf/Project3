package com.s3.https.d0730;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServletRequest extends HttpServlet {
	Map<String, String> headerMap = new HashMap<String, String>();
	Map<String, String> params = new HashMap<String, String>();
	String method;
	String requestUri;
	String protocol;

	public HttpServletRequest() {

	}

	public HttpServletRequest(String requestText) {
		// 完成对请求报文的解析

		String[] lines = requestText.split("\\n");
		String[] items = lines[0].split("\\s");
		method = items[0];

		String[] uri = items[1].split("\\u003F");
		requestUri = uri[0];
		if (uri.length > 1) {
			String[] parameters = uri[1].split("&");
			for (int i = 0; i < parameters.length; i++) {
				String[] parameter = parameters[i].split("=");
				if (parameter.length == 1) {
					params.put(parameter[0], "");
				} else if (parameter.length > 1) {
					params.put(parameter[0], parameter[1]);
				}
			}
		}

		protocol = items[2];

		for (int i = 1; i < lines.length; i++) {
			lines[i] = lines[i].trim();
			if (lines[i].isEmpty()) {
				break;
			}
			items = lines[i].split(":");

			headerMap.put(items[0], items[1].trim());

		}
	}

	// 获取请求方法
	public String getMethod() {
		return method;
	}

	// 获取请求地址
	public String getRequestURI() {
		return requestUri;
	}

	// 获取请求协议版本
	public String getProtocol() {
		return protocol;
	}

	// 获取请求头域值
	public String getHeader(String name) {
		return null;
	}

	// 获取请求参数
	public String getParameter(String name) {
		String result=getGetParameter(name);
		if(result!=null) {
			return result;
		}
		return null;
		
		
	}
	private String getGetParameter(String name) {
		if(params.get(name)!=null) {
			if (params.get(name).indexOf("%") > -1) {
				try {
					new URLDecoder();
					return URLDecoder.decode(params.get(name), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return params.get(name);
		}
		return null;
	}
	// 获取cookie
	public Cookie[] getCookies() {
		String cookieStr = headerMap.get("Cookie");
		if (cookieStr == null) {
			return null;
		} else {
			List<Cookie> cookieList = new ArrayList<Cookie>();
			String[] strCookie = cookieStr.split(";\\s*");
			for (int i = 0; i < strCookie.length; i++) {
				String[] nv = strCookie[i].split("=");
				cookieList.add(new Cookie(nv[0], nv[1]));
			}
			return cookieList.toArray(new Cookie[0]);
		}
	}

}
