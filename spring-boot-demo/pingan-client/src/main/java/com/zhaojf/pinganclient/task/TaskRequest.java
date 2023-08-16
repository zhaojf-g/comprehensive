package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.InsuranceInfo;
import com.zhaojf.pinganclient.vo.Result;
import com.zhaojf.pinganclient.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class TaskRequest {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private final ThreadPoolExecutor threadPoolExecutor;

    private final RestTemplate restTemplate;

    public static Long time = 0L;

    public TaskRequest(ThreadPoolExecutor threadPoolExecutor, RestTemplate restTemplate) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.restTemplate = restTemplate;
    }

    public void select(List<User> users, AtomicBoolean search, int i) {
        if (search.get()) {
            final long timeMillis = System.currentTimeMillis();
            long diff = timeMillis - time;
            time = timeMillis;
            String url = "https://newretail.pingan.com.cn/ydt/reserve/store/bookingTime?storefrontseq=39807&businessType=14";
            int code = 0;

            final HttpRequest httpRequest = HttpRequest.get(url);
            httpRequest.readTimeout(5000);
            code = httpRequest.code();
//            ResponseEntity<Result> forEntity = restTemplate.getForEntity(url, Result.class);
//            code = forEntity.getStatusCode().value();

            try {
                if (code == 200) {
                    synchronized (search) {
                        if (search.get()) {

                            final String body = httpRequest.body();
                            Result result = JSONObject.parseObject(body, Result.class);
//                            Result result = forEntity.getBody();

                            final long currentTimeMillis = System.currentTimeMillis();
                            long t = currentTimeMillis - timeMillis;
                            if (result != null && result.getData().size() > 0) {
                                Task.timeCount += t;
                                StringBuilder str = new StringBuilder("间隔:" + diff + "\t请求时间：" + format.format(new Date(timeMillis)) + "\t当前时间：" + format.format(new Date(currentTimeMillis)) + "\t请求时长：" + t + "\t");
                                for (InsuranceInfo insuranceInfo : result.getData()) {
                                    str.append(insuranceInfo.getBookingDate()).append("\t可预约总数：").append(insuranceInfo.getTotalBookableNum()).append("\t");
                                }
                                log.info(str.toString());


                                for (InsuranceInfo insuranceInfo : result.getData()) {
                                    if (insuranceInfo.getTotalBookableNum() > 0) {
                                        search.set(false);
                                        UserTask.insuranceInfo = insuranceInfo;
                                        Thread.currentThread().setPriority(10);
                                        search.set(false);
                                        for (User user : users) {
                                            threadPoolExecutor.execute(user.getThread());
                                        }

                                    }

                                }
                            } else {
                                log.info("\t间隔:" + diff + "\t请求时长：" + t + "\t无号！！！！！！！！");
                            }

                        } else {
//                            httpRequest.disconnect();
                        }
                    }


                }
            } catch (Exception e) {
                log.error("请求超时");
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
            log.error("[" + user.getContactName() + "请求超时！");
        }


        final long currentTimeMillis = System.currentTimeMillis();

        if (result != null) {
            long t = currentTimeMillis - timeMillis;
            log.info("[" + user.getContactName() + "]间隔:" + diff + "\t请求时长：" + t + "\t:返回信息:" + result.getCode() + "\t" + result.getMsg());
        }

        if (result == null) {
            if (user.isReservation()) {
                this.booking(date, start_time, end_time, id_booking_survey, user);
            }
        } else {
            if (result.getCode() == 0) {
                user.setReservation(false);
                log.info(Thread.currentThread().getName() + "【" + user.getContactName() + "】预约成功！！！！！！");
            } else if (user.isReservation()) {
                this.booking(date, start_time, end_time, id_booking_survey, user);
            }
        }

    }

}

