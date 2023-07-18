package com.zhaojf.pingantest.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InsuranceInfo {

    private int storefrontSeq;

    private String bookingDate;

    private int totalBookableNum;

    private List<BookingRule> bookingRules = new ArrayList<>();

}
