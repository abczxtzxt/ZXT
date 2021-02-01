package com.liebe.base_lib.network;

import android.util.Log;

import com.liebe.base_lib.util.AesUtil;
import com.liebe.base_lib.util.Flog;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 日志拦截器
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */

public class LogInterceptor implements Interceptor {
    private static final String TAG = "Request";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,

        /**
         * .Logger request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting
         *
         * <-- 200 OK (22ms)
         * }</pre>
         */
        BASIC,

        /**
         * .Logger request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        ALL
    }

    public LogInterceptor() {
    }

    private volatile Level level = Level.ALL;

    public LogInterceptor setLevel(Level level) {
        this.level = level;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        String startMessage = "--> " + request.method() + ' ' + URLDecoder.decode(request.url().toString());
        Flog.e(TAG, startMessage);

        boolean logAll = level == Level.ALL;
        if (logAll) {
            // 打印请求所有参数
            logAllRequest(request);
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Flog.e(TAG, "<-- FAILED: " + URLDecoder.decode(request.url().toString()) + "\n" + Log.getStackTraceString(e));
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        String msg = "<-- " + response.code() + ' ' + response.message() + ' ' + URLDecoder.decode(response.request().url().toString()) + " (" + tookMs + "ms)";
        if (response.code() == 200) {
            Flog.e(TAG, msg);
        } else {
            Flog.e(TAG, msg);
        }

        if (logAll) {
            // 打印请求响应参数
            logAllResponse(response);
        }
        return response;
    }

    private void logAllRequest(Request request) throws IOException {
//        Flog.e("tagRequest","获取通过Mobile连接收到的字节总数:"+ TrafficStats.getMobileRxBytes()+"   \n"+"Mobile发送的总字节数:"+TrafficStats.getMobileTxBytes());
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor.
            // Force them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                Flog.e(TAG, "Content-Type: " + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                Flog.e(TAG, "Content-Length: " + requestBody.contentLength());
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                //Flog.e(TAG, name + ": " + headers.value(i));
            }
        }

        if (!hasRequestBody) {
            Flog.e(TAG, "--> END " + request.method());
        } else if (isEncoded(request.headers())) {
            Flog.e(TAG, "--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                Flog.e(TAG, buffer.readString(charset));
                Flog.e(TAG, "--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                Flog.e(TAG, "--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
            }
        }
    }

    private void logAllResponse(Response response) throws IOException {
        Headers headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            //Flog.e(TAG, headers.name(i) + ": " + headers.value(i));
        }

        if (!HttpHeaders.hasBody(response)) {
            Flog.e(TAG, "<-- END HTTP");
        } else if (isEncoded(response.headers())) {
            Flog.e(TAG, "<-- END HTTP (encoded body omitted)");
        } else {
            ResponseBody responseBody = response.body();
            if (null == responseBody) return;

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    Flog.e(TAG, "Couldn't decode the response body; charset is likely malformed.");
                    Flog.e(TAG, "<-- END HTTP");
                    return;
                }
            }

            if (!isPlaintext(buffer)) {
                Flog.e(TAG, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return;
            }

            if (responseBody.contentLength() != 0) {
                //Flog.e(TAG, buffer.clone().readString(charset));
                try {
                    Flog.e(TAG, "解密 content:" + new AesUtil().decrypt(buffer.clone().readString(charset)));
                } catch (GeneralSecurityException e) {
                    Flog.e(TAG, e.getMessage());
                }
            }

            Flog.e(TAG, "<-- END HTTP (" + buffer.size() + "-byte body)");
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small
     * sample of code points to detect unicode control characters commonly used in binary file
     * signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean isEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
