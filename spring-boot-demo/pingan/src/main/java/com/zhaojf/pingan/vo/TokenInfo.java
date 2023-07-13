package com.zhaojf.pingan.vo;

import lombok.Data;

@Data
public class TokenInfo {

    private String sessionId;

    private String signature;

}
