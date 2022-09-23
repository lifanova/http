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
    public static final String REMOTE_SERVICE_URL = "https://jsonplaceholder.typicode.com/posts";
    private CloseableHttpClient httpClient;

    public void connect() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Test")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofDays(5000))
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        // Создаем запрос
        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);
        // выводим заголовки
        Arrays.stream(response.getHeaders()).forEach(System.out::println);

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);

        response.close();
        httpClient.close();
    }

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
                list.forEach(x -> System.out.println(x.toString()));
            //} catch (ParseException e) {
           //     System.out.println(e.getLocalizedMessage());
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void processGetAndParse() {
        ObjectMapper mapper = new ObjectMapper();

        try (CloseableHttpClient client = getHttpClient()) {
            URI uri = new URI(REMOTE_SERVICE_URL);
            HttpUriRequest httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                HttpEntity httpEntity = response.getEntity();
                //System.out.println(EntityUtils.toString(httpEntity));

                List<Post> posts = mapper.readValue(httpEntity.getContent(), new TypeReference<List<Post>>() {
                });
                posts.forEach(x -> System.out.println(x.toString()));
            }
        } catch (URISyntaxException | IOException e) {
            //log.error(e.getLocalizedMessage());
        }
    }

    public void processPost() {
        try (CloseableHttpClient client = getHttpClient()) {
            URI uri = new URI(REMOTE_SERVICE_URL);
            HttpPost httpPost = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("title", "foo"));
            params.add(new BasicNameValuePair("body", "bar"));
            params.add(new BasicNameValuePair("userId", "1"));

            httpPost.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                HttpEntity httpEntity = response.getEntity();
                //System.out.println(EntityUtils.toString(httpEntity));


            }
        } catch (URISyntaxException | IOException e) {
            //log.error(e.getLocalizedMessage());
        }
    }
}
