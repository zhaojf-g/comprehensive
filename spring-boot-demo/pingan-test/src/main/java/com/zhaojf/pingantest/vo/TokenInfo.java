package com.zhaojf.pingantest.vo;

import lombok.Data;

@Data
public class TokenInfo {

    private String sessionId;

    private String signature;

}
