package com.ruifan_wang.calculator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 汇率 API 服务接口
 */
public interface ExchangeRateApi {

    /**
     * 获取最新汇率
     * @param base 基础货币代码
     * @param symbols 需要获取的货币代码列表（逗号分隔）
     * @return API响应对象
     */
    @GET("latest")
    Call<ExchangeRateResponse> getLatestRates(
            @Query("base") String base,
            @Query("symbols") String symbols);
}
