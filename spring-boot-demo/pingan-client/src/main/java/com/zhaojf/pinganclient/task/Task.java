package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONArray;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class Task {


    private final TaskRequest taskRequest;

    private final AtomicBoolean search = new AtomicBoolean(true);

    private final ThreadPoolExecutor threadPoolExecutor;

    private List<User> users = new ArrayList<>();

    public Task(TaskRequest taskRequest, ThreadPoolExecutor threadPoolExecutor) {
        this.taskRequest = taskRequest;
        this.threadPoolExecutor = threadPoolExecutor;
    }


//        @Scheduled(cron = "0 54 21 * * ?")
    @Scheduled(fixedDelay = 1000000000)
//    @PostConstruct
    public void task() throws InterruptedException {

        String url = "http://82.157.162.192:80/pingan/token?time=" + System.currentTimeMillis();
//        String url = "http://127.0.0.1:51071/pingan/token?time=" + System.currentTimeMillis();
        final HttpRequest httpRequest = HttpRequest.get(url);
        final int code = httpRequest.code();
        if (code == 200) {
            final String body = httpRequest.body();
            List<User> users = JSONArray.parseArray(body, User.class);

            this.users.add(users.get(0));
            log.info("获取用户【" + this.users.size() + "】：");
            for (User user : this.users) {
                log.info("用户：" + user.toString());
            }
            Thread.sleep(1000);
            while (this.search.get()) {
                Thread thread = new Thread(() -> taskRequest.select(this.users, search));
                thread.setPriority(1);
                threadPoolExecutor.execute(thread);
                Thread.sleep(10);
            }
        } else {
            log.error("获取身份失败!");
            Thread.sleep(1000);
            this.task();
        }

    }


}
