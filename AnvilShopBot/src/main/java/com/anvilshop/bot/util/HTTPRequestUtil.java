package com.anvilshop.bot.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTPRequestUtil {
	private final static CloseableHttpClient httpClient = HttpClients.createDefault();
	public static String GETRequest(String url) throws Exception {
		HttpGet http = new HttpGet(url);
		try (CloseableHttpResponse response = httpClient.execute(http)) {
			HttpEntity entity = response.getEntity();
			if(entity != null) return EntityUtils.toString(entity);
			return "";
		}
	}
}