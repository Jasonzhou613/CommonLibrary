package com.ttsea.library.common.customHttpClient;

import android.content.Context;

import com.ttsea.commonlibrary.debug.JLog;
import com.ttsea.library.common.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttpClient <br>
 * <p>
 * <b>date:</b> 2017/7/12 10:25 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class HttpClient {
    private final static String TAG = "HttpClient";
    private static OkHttpClient okHttpClient;
    private static OkHttpClient okHttpsClient;

    /**
     * 获取OkHttpClient实例，这个实例使用的是http请求
     *
     * @param context 上下文
     * @return OkHttpClient实例
     */
    public static OkHttpClient getHttpClient(Context context) {
        if (okHttpClient == null) {
            synchronized (HttpClient.class) {
                okHttpClient = getBuilder(context, false).build();
                logClient(okHttpClient);
            }
        }

        return okHttpClient;
    }

    /**
     * 获取OkHttpClient实例，这个实例使用的是https请求
     *
     * @param context 上下文
     * @return OkHttpClient实例
     */
    public static OkHttpClient getHttpsClient(Context context) {
        if (okHttpsClient == null) {
            synchronized (HttpClient.class) {
                okHttpsClient = getBuilder(context, true).build();
                logClient(okHttpsClient);
            }
        }

        return okHttpClient;
    }

    /**
     * 获取OkHttpClient.Builder实例
     *
     * @param context  上下文
     * @param useHttps 是否使用https协议
     * @return OkHttpClient.Builder实例
     */
    private static OkHttpClient.Builder getBuilder(Context context, boolean useHttps) {
        File file = new File(context.getApplicationContext().getCacheDir() + File.separator + "okHttpCache");
        //cache设置为30M
        Cache cache = new Cache(file, 30 * 1024 * 1024);
        if (!file.exists()) {
            file.mkdirs();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLogInterceptor("OkHttp3Log"))
                .cache(cache)
                .connectTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(6, 6, TimeUnit.MINUTES))
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);

        if (useHttps) {
            try {
                System.setProperty("https.protocols", "TLSv1.2");
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

                X509TrustManager[] list = {new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};

                sslContext.init(null, list, new SecureRandom());

                builder.hostnameVerifier((hostname, session) -> true)
                        .followSslRedirects(true)
                        .sslSocketFactory(new Tls12SocketFactory(sslContext.getSocketFactory()), list[0]);

            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                JLog.e("" + e.getMessage());
                e.printStackTrace();
            }
        }

        return builder;
    }

    /**
     * 打印OkHttpClient相关信息
     *
     * @param client 待打印的OkHttpClient
     */
    private static void logClient(OkHttpClient client) {
        StringBuilder builder = new StringBuilder();
        if (client == null) {
            JLog.d("client is null.");
            return;
        }

        Cache cache = client.cache();
        if (cache != null) {
            builder.append("\n");
            builder.append("cache Dir:").append(cache.directory().getAbsolutePath()).append("\n");
            builder.append("cacheHitCount:").append(cache.hitCount()).append("\n");
            builder.append("cacheMaxCount:").append(cache.maxSize()).append("\n");
            builder.append("cache network count:").append(cache.networkCount()).append("\n");
            builder.append("cache request count:").append(cache.requestCount()).append("\n");

            try {
                builder.append("cacheSize:").append(cache.size()).append("\n");
            } catch (Exception e) {
                JLog.e(TAG, "Exception e:" + e.toString());
            }
        }

        builder.append("connectTimeoutMillis:").append(client.connectTimeoutMillis()).append("\n");
        builder.append("writeTimeoutMillis:").append(client.writeTimeoutMillis()).append("\n");
        builder.append("readTimeoutMillis:").append(client.readTimeoutMillis()).append("\n");
        builder.append("connectionPoolCount:").append(client.connectionPool().connectionCount()).append("\n");
        builder.append("connectionPoolIdleCount:").append(client.connectionPool().idleConnectionCount()).append("\n");

        JLog.d(builder.toString());
    }

    /**
     * response Interceptor,这里将从request中获取Cache-Control信息，然后设置给response，便于缓存
     */
    private static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Response response = chain.proceed(request);

            //获取请求头部信息
            String cacheControl = "";
            if (request.cacheControl() != null) {
                cacheControl = request.cacheControl().toString();
            }
            Response.Builder builder = response.newBuilder();
            if (!Utils.isEmpty(cacheControl)) {
                builder.removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", "public, " + cacheControl);
            }

            return builder.build();
        }
    };
}
