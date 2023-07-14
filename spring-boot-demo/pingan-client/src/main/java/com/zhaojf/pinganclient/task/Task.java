package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class Task {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private List<User> users = new ArrayList<>();
    private final AtomicBoolean flag = new AtomicBoolean(true);
    private final AtomicBoolean search = new AtomicBoolean(true);

    public static Long time = 0L;


    //    @Scheduled(cron = "0 59 16 * * ?")
    @Scheduled(fixedDelay = 1000000)
//    @PostConstruct
    public void task() throws InterruptedException {

        String url = "http://82.157.162.192:80/pingan/token?time=" + System.currentTimeMillis();
        final HttpRequest httpRequest = HttpRequest.get(url);
        final int code = httpRequest.code();
        if (code == 200) {
            final String body = httpRequest.body();
            final TokenInfo tokenInfo = JSONObject.parseObject(body, TokenInfo.class);
            log.info("获取身份成功：" + body);
            User user1 = new User("", "", "赵健锋", "", "");
            User user2 = new User("", "", "李玲", "", "");
            users.add(user1);
            users.add(user2);
            Thread.sleep(1000);
            while (this.search.get()) {
                new Thread(this::select).start();
                Thread.sleep(2);
            }
        } else {
            log.error("获取身份失败!");
            Thread.sleep(1000);
            this.task();
        }

    }


    public void select() {
        if (this.search.get()) {
            final long timeMillis = System.currentTimeMillis();
            long diff = timeMillis - time;
            time = timeMillis;
            String url = "https://newretail.pingan.com.cn/ydt/reserve/store/bookingTime?storefrontseq=39807&businessType=14&time=" + System.currentTimeMillis();
            final HttpRequest httpRequest = HttpRequest.get(url);
            if (httpRequest.code() == 200) {
                final String body = httpRequest.body();
                Result result = JSONObject.parseObject(body, Result.class);

                final long currentTimeMillis = System.currentTimeMillis();


                long t = currentTimeMillis - timeMillis;
                if (result != null && result.getData().size() > 0) {
                    InsuranceInfo insuranceInfo = result.getData().get(0);
                    log.info("间隔:" + diff + "\t请求时长：" + t + "\t" + insuranceInfo.getBookingDate() + "\t可预约总数：" + insuranceInfo.getTotalBookableNum());
                    if (insuranceInfo.getTotalBookableNum() >= 0) {
                        List<BookingRule> bookingRules = insuranceInfo.getBookingRules();
                        synchronized (this.search) {
                            if (this.search.get()) {
                                this.search.set(false);

                                int[][] ss = new int[][]{{3, 4, 2, 1, 0}, {1, 2, 3, 4, 0}};
                                for (int i = 0; i < users.size(); i++) {
                                    int finalI = i;
                                    new Thread(() -> {
                                        int[] s = ss[finalI % 2];
                                        User user = users.get(finalI);
                                        for (int j : s) {
                                            final BookingRule bookingRule = bookingRules.get(j);
                                            new Thread(() -> booking(insuranceInfo.getBookingDate(), bookingRule.getStartTime(), bookingRule.getEndTime(), bookingRule.getIdBookingSurvey(), user)).start();
                                            try {
                                                Thread.sleep(10);
                                            } catch (Exception ignored) {

                                            }
                                        }

                                    }).start();
                                }
                            }
                        }

                    }
                } else {
                    log.info("\t间隔:" + diff + "\t请求时长：" + t + "\t无号！！！！！！！！");
                }
            }

        }


    }

    public void booking(String date, String start_time, String end_time, String id_booking_survey, User user) {
        String url = "https://newretail.pingan.com.cn/ydt/reserve/reserveOffline?time=" + System.currentTimeMillis();
        Map<String, String> headers = new HashMap<>();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("sessionId", user.getSessionId());
        headers.put("signature", user.getSignature());
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF XWEB/8259");

        JSONObject json = new JSONObject();
        json.put("businessName", "承保验车");
        json.put("storefrontName", "摩托车投保预约");
        json.put("bookingDate", date);
        json.put("bookingTime", start_time + '-' + end_time);
        json.put("bookingType", "1");
        json.put("storefrontseq", "39807");
        json.put("storefrontTelephone", "");
        json.put("businessType", "14");
        json.put("bookContent", "");
        json.put("idBookingSurvey", id_booking_survey);
        json.put("detailaddress", "北京市朝阳区世纪财富中心2号楼2层平安门店");
        json.put("deptCode", "201");
        json.put("contactName", user.getContactName());
        json.put("contactTelephone", user.getContactTelephone());
        json.put("applicantName", "");
        json.put("applicantIdCard", "");
        json.put("bookingSource", "miniApps");
        json.put("businessKey", null);
        json.put("agentFlag", "0");
        json.put("newCarFlag", "0");
        json.put("noPolicyFlag", "0");
        json.put("vehicleNo", user.getVehicleNo());
        json.put("inputPolicyNo", "");
        json.put("latitude", "");
        json.put("longitude", "");
        json.put("offlineItemList", new ArrayList<>());


        final long timeMillis = System.currentTimeMillis();
        long diff = timeMillis - time;
        time = timeMillis;

//        HttpEntity<JSONObject> request = new HttpEntity<>(json, headers);


        Result result = null;
        try {
            HttpRequest httpRequest = HttpRequest.post(url).acceptJson();
            httpRequest.headers(headers);
            httpRequest.send(json.toJSONString());
            final String body = httpRequest.body();
            result = JSONObject.parseObject(body, Result.class);
        } catch (Exception e) {
            log.info("请求超时！");
        }


        final long currentTimeMillis = System.currentTimeMillis();

        if (result != null) {
            long t = currentTimeMillis - timeMillis;
            log.info("[" + user.getContactName() + "]间隔:" + diff + "\t请求时长：" + t + "\t:返回信息:" + result.getCode() + "\t" + result.getMsg());
        }

        if (result == null) {
            if (flag.get()) {
                this.booking(date, start_time, end_time, id_booking_survey, user);
            }
        } else {
            if (result.getCode() == 200) {
                this.flag.set(false);
                log.info(Thread.currentThread().getName() + "预约成功！！！！！！");
            } else if (flag.get()) {
                this.booking(date, start_time, end_time, id_booking_survey, user);
            }
        }

    }
}
