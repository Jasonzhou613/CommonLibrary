package com.ttsea.library.common.customHttpClient;

import android.text.TextUtils;

import com.ttsea.commonlibrary.debug.JLog;
import com.ttsea.library.common.utils.Utils;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * http请求log拦截器 <br>
 * 1.默认情况下，request是会打印的 <br>
 * 2.可通过参数{@link #needLogResponse}来决定是否打印response <br>
 * 3.<b>特殊处理:</b> 当请求头中设置了“{@link #LOG_REQUEST_KEY}=false”的时候，该请求的request和response都不会打印<br>
 * <p>
 * <b>date:</b> 2017/12/25 9:47 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final class HttpLogInterceptor implements Interceptor {
    private static final String TAG = "OkHttpUtils";
    private final String LOG_REQUEST_KEY = "logRequest";

    private String tag;
    private boolean needLogResponse;

    /**
     * 构造函数<br>
     * 默认会将response打印出来
     *
     * @param tag TAG
     */
    public HttpLogInterceptor(String tag) {
        this(tag, true);
    }

    /**
     * 构造函数
     *
     * @param tag             TAG
     * @param needLogResponse 是否将response打印出来
     */
    public HttpLogInterceptor(String tag, boolean needLogResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.needLogResponse = needLogResponse;
        this.tag = tag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        long startTimeMillis = System.currentTimeMillis();
        Request request = chain.request();

        String logRequest = request.headers() == null ? "" : request.header(LOG_REQUEST_KEY);

        //某个请求特殊指定了不打印request和response，则直接执行请求，都不打印log
        if ("false".equalsIgnoreCase(logRequest)) {
            //能到这里，原始的request header肯定不为空
            //因为LOG_REQUEST_KEY是特殊的，只用来特殊标识该请求是否需要打印log，不做请求参数
            //所以从head中移除这个，然后重新组织request进行http请求，防止该LOG_REQUEST_KEY对请求有影响
            Headers headers = request.headers().newBuilder().removeAll(LOG_REQUEST_KEY).build();
            request = request.newBuilder().headers(headers).build();
            //执行请求
            return chain.proceed(request);
        }

        //打印request
        logRequest(request, startTimeMillis);
        //执行请求
        Response response = chain.proceed(request);
        //如果需要打印response则打印
        if (needLogResponse) {
            response = logResponse(response, startTimeMillis);
        } else {
            JLog.d(tag, "logResponse:" + needLogResponse + ", will not log the response");
        }
        return response;
    }

    /**
     * 打印请求
     *
     * @param request         要打印的请求
     * @param startTimeMillis 该请求的开始时间戳
     */
    private void logRequest(Request request, long startTimeMillis) {
        try {
            String requestStr = URLDecoder.decode(request.toString());
            String headers = headerToString(request.headers());
            String contentType = "unknown";
            String body = null;

            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    contentType = mediaType.toString();
                    if (isText(mediaType)) {
                        body = bodyToString(request);
                    } else {
                        body = "requestBody maybe [file part] , too large too print , ignored!";
                    }
                }
            }

            JLog.d(tag, "\n"
                    + "========request start======>>> " + startTimeMillis + "\n"
                    + "requestStr:" + requestStr + "\n"
                    + "contentType:" + contentType + "\n"
                    + "requestBody:" + body + "\n"
                    + "requestHeaders:" + headers + "\n"
                    + "========request end========="
            );


        } catch (Exception e) {
            JLog.e(tag, "Exception e:" + e.getMessage());
        }
    }

    /**
     * 打印response
     *
     * @param response        服务端的响应
     * @param startTimeMillis 该请求的开始时间戳
     * @return {@link Response}
     */
    private Response logResponse(Response response, long startTimeMillis) {
        Response.Builder builder = response.newBuilder();

        try {
            String responseStr = URLDecoder.decode(response.toString());
            String contentType = "unknown";
            String headers = headerToString(response.headers());
            String body = null;

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                MediaType mediaType = responseBody.contentType();
                if (mediaType != null) {
                    contentType = mediaType.toString();
                    if (isText(mediaType)) {
                        body = responseBody.string();
                        builder.body(ResponseBody.create(mediaType, body));
                    } else {
                        body = "requestBody maybe [file part] , too large too print , ignored!";
                    }
                }
            }

            long endTimeMillis = System.currentTimeMillis();

            JLog.d(tag, "\n"
                    + "<<<=====response start========= " + startTimeMillis + "\n"
                    + "responseStr:" + responseStr + "\n"
                    + "contentType:" + contentType + "\n"
                    + "responseHeaders:" + headers + "\n"
                    + "responseBody:" + body + "\n"
                    + "startTimeMillis:" + startTimeMillis + ", endTimeMillis:" + endTimeMillis
                    + ", take:" + (endTimeMillis - startTimeMillis) + "(ms)\n"
                    + "========response end========="
            );

        } catch (Exception e) {
            JLog.e(tag, "Exception e:" + e.getMessage());
        }
        return builder.build();
    }

    /**
     * 判断请求体类型是否为文本类型
     *
     * @param mediaType 请求体类型
     * @return true:文本类型, false:非文本类型
     */
    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml") ||
                    mediaType.subtype().equals("x-www-form-urlencoded")
            )
                return true;
        }
        return false;
    }

    /** 将request body拼接成String，如："platform=android&version=1.0.1&login_pass=123456&login_name=aaa108" */
    private String bodyToString(Request request) {
        try {
            Request copyRequest = request.newBuilder().build();
            if (copyRequest.body() == null) {
                return null;
            }
            Buffer buffer = new Buffer();
            copyRequest.body().writeTo(buffer);
            String body = buffer.readUtf8();

            String result = URLDecoder.decode(body);

            return Utils.isEmpty(result) ? null : result;

        } catch (final IOException e) {
            return "something error, e:" + e.getMessage();
        }
    }

    /** 将header拼接成String，如："User-Agent: mvp, Cache-Control: no-cache" */
    private String headerToString(Headers headers) {
        if (headers == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            result.append(headers.name(i)).append(":").append(headers.value(i)).append(", ");
        }
        return result.toString();
    }
}
