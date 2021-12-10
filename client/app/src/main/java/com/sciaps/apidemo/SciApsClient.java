package com.sciaps.apidemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.sciaps.InstrumentConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SciApsClient {
    public static final int DEFAULT_CONNECTION_TIMEOUT = 10000; // 10 seconds
    public static final int DEFAULT_SOCKET_TIMEOUT = 0; // 0=infinite if this timeout is less than the test time it will cause an socket-timeout error
    public static final int ANALYZER_PORT = 8080;

    private final Gson mGson;
    private static HttpPost mAcquirePostRequest;
    private final HttpClient mHttpClient;
    private String mIPaddress;
    private InetSocketAddress mInetSocketAddress;

    public SciApsClient(final String ipaddress) {
        mGson = new GsonBuilder().create();

        mHttpClient = HttpClientBuilder.create()
                .setMaxConnTotal(5)
                .build();
        mIPaddress = ipaddress;
        mInetSocketAddress = new InetSocketAddress(ipaddress, ANALYZER_PORT);
    }

    private String getUrlForEndpoint(final String endpoint) {
        return String.format("http://%s:%d/api/v2/%s", mInetSocketAddress.getHostString(), mInetSocketAddress.getPort(), endpoint);
    }

    public InstrumentConfig getInstrumentConfig() {

        String url = getUrlForEndpoint("/config");
        HttpGet request = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();

        request.setConfig(requestConfig);

        InstrumentConfig retval = null;
        try {
            HttpResponse response = mHttpClient.execute(request);
            HttpEntity entity = response.getEntity();

            JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));

            retval = mGson.fromJson(reader, InstrumentConfig.class);

            EntityUtils.consume(entity);
            Main.LOGGER.info(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()) + " get config Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.info(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()) + " get config Failed " + e);
        }

        return retval;
    }
}
