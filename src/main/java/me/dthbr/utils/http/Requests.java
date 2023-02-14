package me.dthbr.utils.http;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

public abstract class Requests<S> {

    public static PostRequest post(String string, Object... params) {
        return new PostRequest(string, params);
    }

    public static GetRequest get(String string, Object... params) {
        return new GetRequest(string, params);
    }

    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final String requestUrl;
    private int timeout = 30;

    private Requests(String requestUrl, Object... params) {
        this.requestUrl = String.format(requestUrl, params);
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/83.0.4103.116 Safari/537.36");
    }

    public S header(String name, String value) {
        headers.put(name, value);
        return (S) this;
    }

    public S param(String name, String value) {
        parameters.put(name, value);
        return (S) this;
    }

    public S timeout(int timeout) {
        this.timeout = timeout;
        return (S) this;
    }

    public <T> Response<T> get(Class<T> returnType) {
        try {
            Duration timeOutDuration = Duration.ofSeconds(timeout);

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(timeOutDuration)
                    .build();

            String finalUrl = requestUrl + buildParams();

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl))
                    .timeout(timeOutDuration)
                    .GET();
            bodyFunction().apply(builder);
            headers.forEach(builder::header);

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            T object = returnType == null || body.isEmpty() ? null : new Gson().fromJson(body, returnType);

            return new Response<>(response.statusCode(), body, object);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new Response<>(-1, "", null);
        }
    }

    @NotNull
    abstract Function<HttpRequest.Builder, HttpRequest.Builder> bodyFunction();

    private String buildParams() {
        if (parameters.isEmpty())
            return "";

        StringJoiner joiner = new StringJoiner("&", "?", "");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }

    public static class PostRequest extends Requests<PostRequest> {

        private HttpRequest.BodyPublisher postBody = HttpRequest.BodyPublishers.noBody();

        private PostRequest(String requestUrl, Object... params) {
            super(requestUrl, params);
        }

        public PostRequest stringBody(String data) {
            postBody = HttpRequest.BodyPublishers.ofString(data);
            return this;
        }

        public PostRequest jsonBody(Object object) {
            postBody = HttpRequest.BodyPublishers.ofString(new Gson().toJson(object));
            return this;
        }

        public PostRequest fileBody(File file) {
            try {
                postBody = HttpRequest.BodyPublishers.ofFile(file.toPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public PostRequest inputStreamBody(InputStream is) {
            postBody = HttpRequest.BodyPublishers.ofInputStream(() -> is);
            return this;
        }

        public PostRequest byteArrayBody(byte[] data) {
            postBody = HttpRequest.BodyPublishers.ofByteArray(data);
            return this;
        }

        @NotNull
        @Override
        Function<HttpRequest.Builder, HttpRequest.Builder> bodyFunction() {
            return builder -> builder.POST(postBody == null ? HttpRequest.BodyPublishers.noBody() : postBody);
        }

    }

    public static class GetRequest extends Requests<GetRequest> {

        private GetRequest(String requestUrl, Object... params) {
            super(requestUrl, params);
        }

        @NotNull
        @Override
        Function<HttpRequest.Builder, HttpRequest.Builder> bodyFunction() {
            return HttpRequest.Builder::GET;
        }

    }

}
