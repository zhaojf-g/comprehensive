package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.BookingRule;
import com.zhaojf.pinganclient.vo.InsuranceInfo;
import com.zhaojf.pinganclient.vo.Result;
import com.zhaojf.pinganclient.vo.TokenInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Task {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static String sessionId = "a85e62e496084dc9bc6d506d05f28adb8T9M";
    public static String signature = "699c4cfaf3839daa1a2ff23fc5bfb1ce";
    public final AtomicBoolean falg = new AtomicBoolean(true);
    public final AtomicBoolean search = new AtomicBoolean(true);

    public static Long time = 0L;


    //    @Scheduled(cron = "0 59 16 * * ?")
    @Scheduled(fixedDelay = 1000000)
    public void task() throws InterruptedException {

        String url = "http://82.157.162.192:80/pingan/token?time=" + System.currentTimeMillis();
        final HttpRequest httpRequest = HttpRequest.get(url);
        final int code = httpRequest.code();
        if (code == 200) {
            final String body = httpRequest.body();
            final TokenInfo tokenInfo = JSONObject.parseObject(body, TokenInfo.class);
            sessionId = tokenInfo.getSessionId();
            signature = tokenInfo.getSignature();
            System.out.println("获取身份成功：" + body);
            Thread.sleep(1000);
            for (int i = 0; i < 10; i++) {
                new Thread(this::select).start();
                Thread.sleep(2);
            }
        } else {
            System.out.println("获取身份失败!");
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
                    System.out.println(format.format(new Date(timeMillis)) + "\t间隔:" + diff + "\t请求时长：" + t + "\t" + insuranceInfo.getBookingDate() + "\t可预约总数：" + insuranceInfo.getTotalBookableNum());
                    if (insuranceInfo.getTotalBookableNum() > 0) {
                        List<BookingRule> bookingRules = insuranceInfo.getBookingRules();
                        synchronized (this.search) {
                            if (this.search.get()) {
                                this.search.set(false);

                                int[] i = new int[]{3, 4, 2, 1, 0};

                                for (int j : i) {
                                    final BookingRule bookingRule = bookingRules.get(j);
                                    new Thread(() -> booking(insuranceInfo.getBookingDate(), bookingRule.getStartTime(), bookingRule.getEndTime(), bookingRule.getIdBookingSurvey(), 1)).start();
                                    try {
                                        Thread.sleep(10);
                                    } catch (Exception ignored) {

                                    }
                                }
                            }
                        }

                    } else {
                        this.select();
                    }
                } else {
                    System.out.println(format.format(new Date(timeMillis)) + "\t间隔:" + diff + "\t请求时长：" + t + "\t无号！！！！！！！！");
                    this.select();
                }
            }

        }


    }

    public void booking(String date, String start_time, String end_time, String id_booking_survey, int count) {
        String url = "https://newretail.pingan.com.cn/ydt/reserve/reserveOffline?time=" + System.currentTimeMillis();
        Map<String, String> headers = new HashMap<>();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("sessionId", sessionId);
        headers.put("signature", signature);
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
        json.put("contactName", "赵健锋");
        json.put("contactTelephone", "18310458721");
        json.put("applicantName", "");
        json.put("applicantIdCard", "");
        json.put("bookingSource", "miniApps");
        json.put("businessKey", null);
        json.put("agentFlag", "0");
        json.put("newCarFlag", "0");
        json.put("noPolicyFlag", "0");
        json.put("vehicleNo", "京B-310A1");
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
            System.out.println("请求超时！");
        }


        final long currentTimeMillis = System.currentTimeMillis();

        if (result != null) {
            long t = currentTimeMillis - timeMillis;
            System.out.println(format.format(new Date(timeMillis)) + "\t间隔:" + diff + "\t请求时长：" + t + "\t:返回信息:" + result.getCode() + "\t" + result.getMsg());
        }

        synchronized (this.falg) {
            if (result == null) {
                if (falg.get()) {
                    this.booking(date, start_time, end_time, id_booking_survey, ++count);
                }
            } else {
                if (result.getCode() == 200) {
                    this.falg.set(false);
                    System.out.println(Thread.currentThread().getName() + "预约成功！！！！！！");
                } else if (falg.get()) {
                    this.booking(date, start_time, end_time, id_booking_survey, ++count);
                }
            }
        }

    }
}
