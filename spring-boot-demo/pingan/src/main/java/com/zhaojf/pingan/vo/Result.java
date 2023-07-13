package com.zhaojf.pingan.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Result {

    private int code;

    private String msg;

    private List<InsuranceInfo> data = new ArrayList<>();

}
