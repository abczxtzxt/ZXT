package com.liebe.base_lib.network;

import android.content.Context;
import android.text.TextUtils;

import com.liebe.base_lib.util.Flog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 本地模拟数据拦截器，请求Header中添加[fake: true]即可
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public class FakeInterceptor implements Interceptor {
    private static final String TAG = FakeInterceptor.class.getSimpleName();
    private static final String FILE_EXTENSION = ".json";
    private Context mContext;

    private String mContentType = "application/json";

    public FakeInterceptor(Context context) {
        mContext = context;
    }

    /**
     * Set content type for header
     *
     * @param contentType Content type
     * @return FakeInterceptor
     */
    public FakeInterceptor setContentType(String contentType) {
        mContentType = contentType;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String fake = chain.request().header("fake");
        if (!"true".equals(fake)) {
            return chain.proceed(chain.request());
        }

        List<String> listSuggestionFileName = new ArrayList<>();
        String method = chain.request().method().toLowerCase();

        Response response = null;
        // Get Request URI.
        final URI uri = chain.request().url().uri();

        String defaultFileName = getFileName(chain);

        //create file name with http method
        //eg: getLogin.json
        listSuggestionFileName.add(method + upCaseFirstLetter(defaultFileName));

        //eg: login.json
        listSuggestionFileName.add(defaultFileName);

        String responseFileName = getFirstFileNameExist(listSuggestionFileName, uri);
        if (responseFileName != null) {
            String fileName = getFilePath(uri, responseFileName);
            Flog.d(TAG, "Read data from file: " + fileName);
            try {
                InputStream is = mContext.getAssets().open(fileName);
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder responseStringBuilder = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    responseStringBuilder.append(line).append('\n');
                }
                Flog.d(TAG, "Response: " + responseStringBuilder.toString());
                response = new Response.Builder()
                        .code(200)
                        .message(responseStringBuilder.toString())
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse(mContentType), responseStringBuilder.toString().getBytes()))
                        .addHeader("content-type", mContentType)
                        .build();
            } catch (IOException e) {
                Flog.e(TAG, e.getMessage(), e);
            }
        } else {
            for (String file : listSuggestionFileName) {
                Flog.e(TAG, "File not exist: " + getFilePath(uri, file));
            }
            response = chain.proceed(chain.request());
        }

        Flog.d(TAG, "<-- END [" + method.toUpperCase() + "]" + uri.toString());
        return response;
    }

    private String upCaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getFirstFileNameExist(List<String> inputFileNames, URI uri) throws IOException {
        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) return null;
        if (path.startsWith("/")) path = path.substring(1, path.length());

        path = path.substring(0, path.lastIndexOf('/'));
        Flog.d(TAG, "Scan files in: " + path);
        //List all files in folder
        String[] files = mContext.getAssets().list(path);
        if (files == null) return null;
        for (String fileName : inputFileNames) {
            if (fileName != null) {
                for (String file : files) {
                    if (fileName.equals(file)) {
                        return fileName;
                    }
                }
            }
        }
        return null;
    }

    private String getFileName(Chain chain) {
        String fileName = chain.request().url().pathSegments().get(chain.request().url().pathSegments().size() - 1);
        return fileName.isEmpty() ? "index" + FILE_EXTENSION : fileName + FILE_EXTENSION;
    }

    private String getFilePath(URI uri, String fileName) {
        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) return "";
        if (path.startsWith("/")) path = path.substring(1, path.length());

        if (path.lastIndexOf('/') != path.length() - 1) {
            path = path.substring(0, path.lastIndexOf('/') + 1);
        }
        return path + fileName;
    }
}