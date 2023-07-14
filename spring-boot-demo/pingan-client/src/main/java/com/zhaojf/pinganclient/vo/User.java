package com.zhaojf.pinganclient.vo;

import lombok.Data;

@Data
public class User {

    private String sessionId;

    private String signature;

    private String contactName; // 姓名

    private String contactTelephone; // 电话

    private String vehicleNo; // 车牌号

    public User(String sessionId, String signature, String contactName, String contactTelephone, String vehicleNo) {
        this.sessionId = sessionId;
        this.signature = signature;
        this.contactName = contactName;
        this.contactTelephone = contactTelephone;
        this.vehicleNo = vehicleNo;
    }
}
