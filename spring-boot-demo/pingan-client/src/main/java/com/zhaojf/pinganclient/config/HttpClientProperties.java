package com.zhaojf.pinganclient.config;

import lombok.Data;

@Data
public class HttpClientProperties {

    /**
     * 是否使用httpclient连接池
     */
    private boolean useHttpClientPool = true;

    /**
     * 从连接池中获得一个connection的超时时间
     */
    private int connectionRequestTimeout = 3000;

    /**
     * 建立连接超时时间
     */
    private int connectTimeout = 5000;

    /**
     * 建立连接后读取返回数据的超时时间
     */
    private int readTimeout = 5000;

    /**
     * 连接池的最大连接数，0代表不限
     */
    private int maxTotalConnect = 128;

    /**
     * 每个路由的最大连接数
     */
    private int maxConnectPerRoute = 500;


}