package com.zhaojf.pingantest.vo;

import lombok.Data;

@Data
public class User {

    private String sessionId;

    private String signature;

    private String contactName; // 姓名

    private String contactTelephone; // 电话

    private String vehicleNo; // 车牌号

    private boolean reservation = true;

    private String number;

    public User(String sessionId, String signature, String contactName, String contactTelephone, String vehicleNo, String number) {
        this.sessionId = sessionId;
        this.signature = signature;
        this.contactName = contactName;
        this.contactTelephone = contactTelephone;
        this.vehicleNo = vehicleNo;
        this.number = number;
    }

    @Override
    public String toString() {
        return "User{" +
                ", contactName='" + contactName + '\'' +
                ", contactTelephone='" + contactTelephone + '\'' +
                ", vehicleNo='" + vehicleNo + '\'' +
                "sessionId='" + sessionId + '\'' +
                ", signature='" + signature + '\'' +
                ", reservation=" + reservation +
                ", number='" + number + '\'' +
                '}';
    }
}
