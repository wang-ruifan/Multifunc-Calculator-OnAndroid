package com.ruifan_wang.calculator;

/**
 * 货币数据模型类
 */
public class Currency {
    private String code;        // 货币代码，如USD, CNY
    private String flagResName; // 国旗资源名称
    private double amount;      // 金额
    private double rate;        // 相对于基准货币的汇率

    public Currency(String code, String flagResName, double rate) {
        this.code = code;
        this.flagResName = flagResName;
        this.rate = rate;
        this.amount = 0.0;
    }

    public String getCode() {
        return code;
    }

    public String getFlagResName() {
        return flagResName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
