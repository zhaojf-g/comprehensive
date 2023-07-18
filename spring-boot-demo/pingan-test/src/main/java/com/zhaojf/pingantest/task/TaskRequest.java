package com.zhaojf.pingantest.task;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pingantest.vo.Result;
import com.zhaojf.pingantest.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class TaskRequest {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static Long time = 0L;

    public void select(List<User> users, AtomicBoolean search) {
        if (search.get()) {
            final long timeMillis = System.currentTimeMillis();
            long diff = timeMillis - time;
            time = timeMillis;
            String url = "https://newretail.pingan.com.cn/ydt/reserve/store/bookingTime?storefrontseq=39807&businessType=14&time=" + System.currentTimeMillis();
            final HttpRequest httpRequest = HttpRequest.get(url);
            if (httpRequest.code() == 200) {
                final String body = httpRequest.body();
                final long currentTimeMillis = System.currentTimeMillis();
                long t = currentTimeMillis - timeMillis;
                log.info("\t间隔:" + diff + "\t请求时长：" + t + "\t无号！！！！！！！！");
            }
        }
    }

}

