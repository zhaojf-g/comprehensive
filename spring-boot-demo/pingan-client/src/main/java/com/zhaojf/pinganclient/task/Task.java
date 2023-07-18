package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONArray;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class Task {


    private final TaskRequest taskRequest;

    private final AtomicBoolean search = new AtomicBoolean(true);

    private List<User> users = new ArrayList<>();

    public Task(TaskRequest taskRequest) {
        this.taskRequest = taskRequest;
    }

    //        @Scheduled(cron = "0 54 21 * * ?")

    //    @PostConstruct
//    @Scheduled(cron = "0 55 16 * * ?")
    @Scheduled(fixedDelay = 1000000000)
    public void task() throws InterruptedException {

        String url = "http://82.157.162.192:80/pingan/token?time=" + System.currentTimeMillis();
//        String url = "http://127.0.0.1:51071/pingan/token?time=" + System.currentTimeMillis();
        final HttpRequest httpRequest = HttpRequest.get(url);
        final int code = httpRequest.code();
        if (code == 200) {
            final String body = httpRequest.body();
            this.users = JSONArray.parseArray(body, User.class);
            log.info("获取用户【" + this.users.size() + "】：");
            for (User user : this.users) {
                log.info("用户：" + user.toString());
            }
            Thread.sleep(1000);
            while (this.search.get()) {
                new Thread(() -> taskRequest.select(users, search)).start();
                Thread.sleep(2);
            }
        } else {
            log.error("获取身份失败!");
            Thread.sleep(1000);
            this.task();
        }

    }


//    @Scheduled(cron = "0 5 17 * * ?")
//    public void stopTask() {
//        log.info("本次预约成功用户：");
//        for (User user : this.users) {
//            if (!user.isReservation()) {
//                log.info(user.toString());
//            }
//            user.setReservation(false);
//        }
//    }

}
