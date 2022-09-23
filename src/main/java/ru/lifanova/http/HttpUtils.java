package ru.lifanova.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import ru.lifanova.dto.Cat;
import ru.lifanova.dto.Post;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HttpUtils {

    private CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .setUserAgent("Test")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofDays(5000))
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

    public List<Cat> processGet(String url) {
        ObjectMapper mapper = new ObjectMapper();
        List<Cat> list = null;

        try (CloseableHttpClient client = getHttpClient()) {
            URI uri = new URI(url);
            HttpUriRequest httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                HttpEntity httpEntity = response.getEntity();
                //System.out.println(EntityUtils.toString(httpEntity));
                InputStream content = httpEntity.getContent();

                //list = mapper.readValue(content, new TypeReference<List<Cat>>() {});

                CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Cat.class);

                list = mapper.readValue(content, collectionType);

            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return list;
    }


}
