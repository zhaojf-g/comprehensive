package com.zhaojf.pingan.task;

import com.alibaba.fastjson.JSONObject;
import com.zhaojf.pingan.vo.BookingRule;
import com.zhaojf.pingan.vo.InsuranceInfo;
import com.zhaojf.pingan.vo.Result;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Task {

    private final RestTemplate restTemplate;
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static String sessionId = "a85e62e496084dc9bc6d506d05f28adb8T9M";
    public static String signature = "699c4cfaf3839daa1a2ff23fc5bfb1ce";
    public boolean falg = true;
    public AtomicBoolean search = new AtomicBoolean(true);

    public static Long time = 0L;

    public Task(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 59 16 * * ?")
    public void task() throws InterruptedException {

        for (int i = 0; i < 50; i++) {
            new Thread(this::select).start();
            Thread.sleep(10);
        }
    }


    public void select() {
        if (this.search.get()) {
            final long timeMillis = System.currentTimeMillis();
            long diff = timeMillis - time;
            time = timeMillis;
            String url = "https://newretail.pingan.com.cn/ydt/reserve/store/bookingTime?storefrontseq=39807&businessType=14&time=" + System.currentTimeMillis();
            ResponseEntity<Result> responseEntity = restTemplate.getForEntity(url, Result.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Result result = responseEntity.getBody();

                final long currentTimeMillis = System.currentTimeMillis();


                long t = currentTimeMillis - timeMillis;
                if (result != null && result.getData().size() > 0) {
                    InsuranceInfo insuranceInfo = result.getData().get(0);
                    System.out.println(format.format(new Date(timeMillis)) + "\t间隔:" + diff + "\t请求时长：" + t + "\t" + insuranceInfo.getBookingDate() + "\t可预约总数：" + insuranceInfo.getTotalBookableNum());
                    if (insuranceInfo.getTotalBookableNum() > 0) {
                    this.search.set(false);
                    List<BookingRule> bookingRules = insuranceInfo.getBookingRules();
                    for (int i = bookingRules.size() - 1; i >= 0; i--) {
                        final BookingRule bookingRule = bookingRules.get(i);
                        new Thread(() -> booking(insuranceInfo.getBookingDate(), bookingRule.getStartTime(), bookingRule.getEndTime(), bookingRule.getIdBookingSurvey(), 1)).start();
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("sessionId", sessionId);
        headers.add("signature", signature);

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

        HttpEntity<JSONObject> request = new HttpEntity<>(json, headers);

        Result result = null;
        try {
            ResponseEntity<Result> responseEntity = restTemplate.postForEntity(url, request, Result.class);
            result = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println("请求超时！");
        }


        final long currentTimeMillis = System.currentTimeMillis();

        if (result != null) {
            long t = currentTimeMillis - timeMillis;
            System.out.println(format.format(new Date(timeMillis)) + "\t间隔:" + diff + "\t请求时长：" + t + "\t:返回信息:" + result.getCode() + "\t" + result.getMsg());
        }

        if (falg && (result == null || result.getCode() != 200)) {
            this.booking(date, start_time, end_time, id_booking_survey, ++count);
        } else {
            this.falg = false;
            System.out.println("预约成功！！！！！！");
        }
    }
}
