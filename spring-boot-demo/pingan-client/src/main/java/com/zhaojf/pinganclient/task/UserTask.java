package com.zhaojf.pinganclient.task;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.zhaojf.pinganclient.vo.BookingRule;
import com.zhaojf.pinganclient.vo.InsuranceInfo;
import com.zhaojf.pinganclient.vo.Result;
import com.zhaojf.pinganclient.vo.User;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UserTask implements Runnable {

    private final User user;

    public static InsuranceInfo insuranceInfo;

    public static Long time = 0L;

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private final ThreadPoolExecutor threadPoolExecutor;

    public UserTask(User user, ThreadPoolExecutor threadPoolExecutor) {
        this.user = user;
        this.threadPoolExecutor = threadPoolExecutor;
    }


    @Override
    public void run() {

        String number = user.getNumber();
        if (number == null || "".equals(number)) {
            number = "2";
        }
        final String[] s = number.split(",");

        List<BookingRule> bookingRules = insuranceInfo.getBookingRules();
        String bookingDate = insuranceInfo.getBookingDate();
        AtomicInteger i = new AtomicInteger();
        while (this.user.isReservation()) {
            // 优先遍历指定的节点
//          if (i.get() == 0) {
            for (String j : s) {
                final BookingRule bookingRule = bookingRules.get(Integer.parseInt(j));
                threadPoolExecutor.execute(() -> this.booking(bookingDate, bookingRule.getStartTime(), bookingRule.getEndTime(), bookingRule.getIdBookingSurvey(), user, i.incrementAndGet()));
            }

            // 休眠5毫米
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {
            }

//         }

            // 开始随机预约
//            for (BookingRule bookingRule : bookingRules) {
//                threadPoolExecutor.execute(() -> this.booking(bookingDate, bookingRule.getStartTime(), bookingRule.getEndTime(), bookingRule.getIdBookingSurvey(), user, i.incrementAndGet()));
//            }
//
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            i.getAndIncrement();
        }


    }


    public void booking(String date, String start_time, String end_time, String id_booking_survey, User user, int i) {
        String url = "https://newretail.pingan.com.cn/ydt/reserve/reserveOffline?time=" + System.currentTimeMillis();
        Map<String, String> headers = new HashMap<>();
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

        Result result = null;
        try {
            HttpRequest httpRequest = HttpRequest.post(url).acceptJson();
            httpRequest.headers(headers);
            httpRequest.send(json.toJSONString());
            final int code = httpRequest.code();
            if (code == 200 && this.user.isReservation()) {
                final String body = httpRequest.body();
                result = JSONObject.parseObject(body, Result.class);
                final long currentTimeMillis = System.currentTimeMillis();

                if (result != null) {
                    long t = currentTimeMillis - timeMillis;
                    log.info("[" + user.getContactName() + "]间隔:" + diff + "\t请求时间：" + format.format(new Date(timeMillis)) + "\t当前时间：" + format.format(new Date(currentTimeMillis)) + "\t请求时长：" + t + "\t:返回信息:" + result.getCode() + "\t" + result.getMsg() + "请求次数：" + i);

                    if (result.getCode() == 0) {
                        user.setReservation(false);
                        log.info(Thread.currentThread().getName() + "【" + user.getContactName() + "】预约成功！！！！！！");
                        threadPoolExecutor.execute(() -> addRecord(user, date + " " + start_time + "-" + end_time));
                    }
                }
            }else{
                httpRequest.disconnect();
            }
        } catch (Exception e) {
            log.error("[" + user.getContactName() + "请求超时！");
        }


    }


    private void addRecord(User user, String date) {
        String url = "http://82.157.162.192:80/pingan/record";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("name", user.getContactName());
        json.put("telephone", user.getContactTelephone());
        json.put("vehicleNo", user.getVehicleNo());
        json.put("date", date);

        HttpRequest httpRequest = HttpRequest.post(url).acceptJson();
        httpRequest.headers(headers);
        httpRequest.send(json.toJSONString());
        httpRequest.body();
    }


}
