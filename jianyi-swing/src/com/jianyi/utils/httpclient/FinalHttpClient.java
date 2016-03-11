package com.jianyi.utils.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by liwanzhong on 2016/2/25.
 */
public class FinalHttpClient {

    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static FinalHttpClient ourInstance= null;

    public static FinalHttpClient getInstance(CookieStore cookieStore) {
        ourInstance = new FinalHttpClient(cookieStore);
        return ourInstance;
    }


    private CloseableHttpClient httpClient =null;

    public CloseableHttpClient getHttpClient() {
        if(httpClient==null){
            new FinalHttpClient(null);
        }
        return httpClient;
    }


    private FinalHttpClient(CookieStore cookieStore) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        // Increase max connections for localhost:80 to 50
        HttpHost localhost = new HttpHost("test.ihavecar.com", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);


        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();

        // Create a local instance of cookie store
        if(cookieStore == null ){
            cookieStore = new BasicCookieStore();
        }

        //设置重定向策略
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();


        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore)
                .setRedirectStrategy(redirectStrategy)
                .build();

    }
}
