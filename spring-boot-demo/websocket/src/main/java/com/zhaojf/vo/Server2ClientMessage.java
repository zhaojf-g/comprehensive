package com.zhaojf.vo;

public class Server2ClientMessage {

    private String responseMessage;

    public Server2ClientMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
