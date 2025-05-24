package com.ruifan_wang.calculator.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 汇率 API 响应数据模型类
 * 根据Open Exchange Rates API (https://open.er-api.com/v6/) 的返回格式修改
 */
public class ExchangeRateResponse {
    @SerializedName("result")
    private String result;  // "success" 表示成功

    @SerializedName("time_last_update_unix")
    private long timeLastUpdateUnix;

    @SerializedName("time_last_update_utc")
    private String timeLastUpdateUtc;

    @SerializedName("time_next_update_unix")
    private long timeNextUpdateUnix;

    @SerializedName("time_next_update_utc")
    private String timeNextUpdateUtc;

    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("rates")
    private Map<String, Double> rates;

    // 判断API调用是否成功
    public boolean isSuccess() {
        return "success".equals(result);
    }

    public String getResult() {
        return result;
    }

    public long getTimeLastUpdateUnix() {
        return timeLastUpdateUnix;
    }

    public String getTimeLastUpdateUtc() {
        return timeLastUpdateUtc;
    }

    public long getTimeNextUpdateUnix() {
        return timeNextUpdateUnix;
    }

    public String getTimeNextUpdateUtc() {
        return timeNextUpdateUtc;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}
