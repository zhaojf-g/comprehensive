package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONArray;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class Task {

    public static long sleepMillis;

    private final TaskRequest taskRequest;

    private final AtomicBoolean search = new AtomicBoolean(true);

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 10000,
            20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), r -> {
        final Thread thread = new Thread(r);
        thread.setPriority(1);
        return thread;
    });

    private final List<User> users = new ArrayList<>();

    public Task(TaskRequest taskRequest) {
        this.taskRequest = taskRequest;
    }


    //    @Scheduled(cron = "0 57 16 * * ?")
    @PostConstruct
    public void start() {
        if (sleepMillis <= 1) {
            sleepMillis = 2;
        }
        threadPoolExecutor.execute(() -> {
            try {
                this.task();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    public void task() throws InterruptedException {

        String url = "http://82.157.162.192:80/pingan/token?time=" + System.currentTimeMillis();
//        String url = "http://127.0.0.1:51071/pingan/token?time=" + System.currentTimeMillis();
        final HttpRequest httpRequest = HttpRequest.get(url);
        final int code = httpRequest.code();
        if (code == 200) {
            this.users.clear();
            final String body = httpRequest.body();
            final List<User> users = JSONArray.parseArray(body, User.class);
            log.info("获取用户【" + users.size() + "】，其中本次可预约用户为：");
            for (User user : users) {
                if (user.getSessionId() != null && !"".equals(user.getSessionId())) {
                    final Thread thread = new Thread(new UserTask(user, threadPoolExecutor));
                    thread.setPriority(10);
                    user.setThread(thread);
                    log.info("用户：" + user);
                    this.users.add(user);
                }
            }
            AtomicInteger i = new AtomicInteger(1);
            while (this.search.get()) {
                threadPoolExecutor.execute(() -> taskRequest.select(this.users, search, i.getAndIncrement()));
                Thread.sleep(sleepMillis);
            }
        } else {
            log.error("获取身份失败!");
            Thread.sleep(1000);
            if (this.search.get()) {
                this.task();
            }

        }

    }

    //    @Scheduled(cron = "30 3 17 * * ?")
    public void stopTask() {
        this.search.set(false);
        StringBuilder str = new StringBuilder("本次预约成功用户：");
        int i = 0;
        for (User user : this.users) {
            if (!user.isReservation()) {
                i++;
                str.append(user.getContactName()).append(",");
            }
            user.setReservation(false);
        }
        str.append("共【").append(i).append("】人");
        log.info(str.toString());
        this.users.clear();
    }


}
