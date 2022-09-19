package ru.lifanova.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class HttpEngine {
    // TODO: вынести все урлы отдельно
    public static final String REMOTE_SERVICE_URL = "https://jsonplaceholder.typicode.com/posts";

    private CloseableHttpClient httpClient;

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create()
                    .setUserAgent("Test")
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(Timeout.ofDays(5000))
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
        }

        return httpClient;
    }

    public void process() {
        try (CloseableHttpClient client = getHttpClient()) {
            URI uri = new URI(REMOTE_SERVICE_URL);
            executeRequestAndProcess(uri);
        } catch (URISyntaxException | IOException e) {
            //log.error(e.getLocalizedMessage());
        }
    }

    public abstract void executeRequestAndProcess(URI uri);
}
