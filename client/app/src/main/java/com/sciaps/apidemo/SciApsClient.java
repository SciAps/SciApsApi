package com.sciaps.apidemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sciaps.InstrumentId;
import com.sciaps.ZAcquisitionResult;
import com.sciaps.ZAcquisitionSettings;
import com.sciaps.ZCalibration;
import com.sciaps.ZFactoryAcquisitionSettings;
import com.sciaps.ZInstrumentConfig;
import com.sciaps.ZInstrumentStatus;
import com.sciaps.ZTestResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class SciApsClient {
    public static final int DEFAULT_CONNECTION_TIMEOUT = 10000; // 10 seconds
    public static final int DEFAULT_SOCKET_TIMEOUT = 0; // 0=infinite if this timeout is less than the test time it will cause an socket-timeout error
    public static final int ANALYZER_PORT = 8080;

    private final Gson mGson;
    private final CloseableHttpClient mHttpClient;
    private InetSocketAddress mInetSocketAddress;

    public SciApsClient(final String ipaddress) {
        mInetSocketAddress = new InetSocketAddress(ipaddress, ANALYZER_PORT);
        mGson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .create();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();

        mHttpClient = HttpClientBuilder.create()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .setDefaultRequestConfig(config)
                .build();
    }

    private String getUrlForEndpointWithArgs(final String endpoint, final String mode, final String spectra) {
        String url = String.format("http://%s/api/v2/%s", mInetSocketAddress.getHostString(), endpoint);
        if (spectra != null) {
            url += "/" + spectra;
        }
        if (mode != null) {
            url += "?mode=" + mode;
        }
        return url;
    }

    private String getUrlForEndpoint(final String endpoint) {
        return getUrlForEndpointWithArgs(endpoint, null, null);
    }

    private String getUrlForEndpointWithMode(final String endpoint, final String mode) {
        return getUrlForEndpointWithArgs(endpoint, mode, null);
    }

    private String getUrlForEndpointWithModeAndSpecta(final String endpoint, final String mode, boolean allSpectra) {
        return getUrlForEndpointWithArgs(endpoint, mode, allSpectra ? "all" : "final");
    }

    public InstrumentId getInstrumentId() {
        InstrumentId retval = null;
        HttpGet request = new HttpGet(getUrlForEndpoint("id"));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
            retval = mGson.fromJson(reader, InstrumentId.class);

            EntityUtils.consume(entity);
            Main.LOGGER.info("getInstrumentId completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getInstrumentId failed", e);
        }

        return retval;
    }

    public ZInstrumentConfig getZInstrumentConfig() {
        ZInstrumentConfig retval = null;
        HttpGet request = new HttpGet(getUrlForEndpoint("config"));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
            retval = mGson.fromJson(reader, ZInstrumentConfig.class);

            EntityUtils.consume(entity);
            Main.LOGGER.info("getZInstrumentConfig completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getZInstrumentConfig failed", e);
        }

        return retval;
    }

    public ZInstrumentStatus getZInstrumentStatus() {
        ZInstrumentStatus retval = null;
        HttpGet request = new HttpGet(getUrlForEndpoint("status"));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
            retval = mGson.fromJson(reader, ZInstrumentStatus.class);

            EntityUtils.consume(entity);
            Main.LOGGER.info("getZInstrumentStatus completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getZInstrumentStatus failed", e);
        }

        return retval;
    }

    public ZCalibration getZCalibration() {
        ZCalibration retval = null;
        HttpGet request = new HttpGet(getUrlForEndpoint("wlcalibration"));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
            retval = mGson.fromJson(reader, ZCalibration.class);

            EntityUtils.consume(entity);
            Main.LOGGER.info("getZCalibration completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getZCalibration failed", e);
        }

        return retval;
    }

    public void runWlCalibration() {
        HttpPost request = new HttpPost(getUrlForEndpoint("wlcalibration"));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            Main.LOGGER.info("runWlCalibration completed with status: " + status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.info("runWlCalibration failed " + e);
        }
    }

    public ZAcquisitionSettings getZAcquisitionSettings(final String mode) {
        ZAcquisitionSettings retval = null;
        HttpGet request = new HttpGet(getUrlForEndpointWithMode("acquisitionParams/user", mode));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
                retval = mGson.fromJson(reader, ZAcquisitionSettings.class);

                EntityUtils.consume(entity);
            }
            Main.LOGGER.info("getZAcquisitionSettings completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getZAcquisitionSettings failed", e);
        }

        return retval;
    }

    public void setZAcquisitionSettings(final String mode, ZAcquisitionSettings settings) {
        HttpPut request = new HttpPut(getUrlForEndpointWithMode("acquisitionParams/user", mode));

        String jsonStr = mGson.toJson(settings);
        StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            Main.LOGGER.info("setZAcquisitionSettings completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("setZAcquisitionSettings failed", e);
        }
    }

    public ZTestResult runTest(final String mode, ZAcquisitionSettings settings, boolean allSpectra) {
        ZTestResult retval = null;
        HttpPost request = new HttpPost(getUrlForEndpointWithModeAndSpecta("test", mode, allSpectra));
        String jsonStr = mGson.toJson(settings);
        StringEntity reqEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        request.setEntity(reqEntity);

        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity rspEntity = response.getEntity();

                JsonReader reader = new JsonReader(new InputStreamReader(rspEntity.getContent()));
                retval = mGson.fromJson(reader, ZTestResult.class);

                EntityUtils.consume(rspEntity);
            }
            Main.LOGGER.info("runTest completed with status: " + status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.info("runTest failed", e);
        }
        return retval;
    }

    public ZFactoryAcquisitionSettings getZFactoryAcquisitionSettings(final String mode) {
        ZFactoryAcquisitionSettings retval = null;
        HttpGet request = new HttpGet(getUrlForEndpointWithMode("acquisitionParams/factory", mode));
        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                JsonReader reader = new JsonReader(new InputStreamReader(entity.getContent()));
                retval = mGson.fromJson(reader, ZFactoryAcquisitionSettings.class);

                EntityUtils.consume(entity);
            }
            Main.LOGGER.info("getZAcquisitionSettings completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("getZAcquisitionSettings failed", e);
        }

        return retval;
    }

    public void setZFactoryAcquisitionSettings(final String mode, ZFactoryAcquisitionSettings settings) {
        HttpPut request = new HttpPut(getUrlForEndpointWithMode("acquisitionParams/factory", mode));

        String jsonStr = mGson.toJson(settings);
        StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            Main.LOGGER.info("setZAcquisitionSettings completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("setZAcquisitionSettings failed", e);
        }
    }

    public ZAcquisitionResult runAcquisition(final String mode, ZFactoryAcquisitionSettings settings, boolean allSpectra) {
        ZAcquisitionResult retval = null;
        HttpPost request = new HttpPost(getUrlForEndpointWithModeAndSpecta("acquire", mode, allSpectra));
        String jsonStr = mGson.toJson(settings);
        StringEntity reqEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        request.setEntity(reqEntity);

        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity rspEntity = response.getEntity();

                JsonReader reader = new JsonReader(new InputStreamReader(rspEntity.getContent()));
                retval = mGson.fromJson(reader, ZAcquisitionResult.class);

                EntityUtils.consume(rspEntity);
            }
            Main.LOGGER.info("runAcquisition completed with status: " + status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.info("runAcquisition failed", e);
        }
        return retval;
    }

    public void abort() {
        HttpPost request = new HttpPost(getUrlForEndpoint("abort"));

        try (CloseableHttpResponse response = mHttpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            Main.LOGGER.info("abort completed with status: {}", status);
        } catch (Exception e) {
            e.printStackTrace();
            Main.LOGGER.error("abort failed", e);
        }
    }
}
