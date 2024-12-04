package com.github.rayinfinite.stock.utils;

import com.github.rayinfinite.stock.entity.exception.WebCrawlerException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.Map;

public class HttpUtils {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/131.0.0.0 Safari/537.36";

    private HttpUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String get(String urlString, Map<String, String> headers) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(urlString);
            request.setHeader("User-Agent", USER_AGENT);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }

            HttpClientResponseHandler<String> responseHandler = response -> {
                int statusCode = response.getCode();
                if (statusCode == 200) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new WebCrawlerException("GET request not worked, Response Code: " + statusCode);
                }
            };
            return httpClient.execute(request, responseHandler);
        } catch (Exception e) {
            throw new WebCrawlerException(e);
        }
    }

    public static String get(String url) {
        return get(url, Map.of());
    }
}
