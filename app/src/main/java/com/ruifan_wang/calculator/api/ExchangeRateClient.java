package com.ruifan_wang.calculator.api;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 汇率 API 客户端
 */
public class ExchangeRateClient {
    private static final String TAG = "ExchangeRateClient";
    // 更新API基础URL，使用Open Exchange Rates API
    private static final String BASE_URL = "https://open.er-api.com/v6/";
    private static ExchangeRateClient instance;
    private final ExchangeRateApi api;

    private ExchangeRateClient() {
        // 创建日志拦截器，方便调试
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 创建OkHttp客户端，增加超时设置
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建API接口实例
        api = retrofit.create(ExchangeRateApi.class);
    }

    // 单例模式获取客户端实例
    public static synchronized ExchangeRateClient getInstance() {
        if (instance == null) {
            instance = new ExchangeRateClient();
        }
        return instance;
    }

    /**
     * 获取最新汇率
     * @param baseCurrency 基础货币
     * @param currencyCodes 需要获取汇率的货币代码列表
     * @param listener 回调监听器
     */
    public void getLatestRates(String baseCurrency, String[] currencyCodes, OnExchangeRateListener listener) {
        // 将货币代码数组转为逗号分隔的字符串
        StringBuilder symbolsBuilder = new StringBuilder();
        for (int i = 0; i < currencyCodes.length; i++) {
            symbolsBuilder.append(currencyCodes[i]);
            if (i < currencyCodes.length - 1) {
                symbolsBuilder.append(",");
            }
        }
        String symbols = symbolsBuilder.toString();

        // 发起API请求
        Call<ExchangeRateResponse> call = api.getLatestRates(baseCurrency, symbols);
        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExchangeRateResponse rateResponse = response.body();
                    if (rateResponse.isSuccess() && rateResponse.getRates() != null) {
                        // 请求成功，返回汇率数据
                        listener.onSuccess(rateResponse.getRates());
                    } else {
                        // API请求成功但返回错误
                        listener.onFailure("API返回错误数据");
                    }
                } else {
                    // 请求不成功
                    listener.onFailure("获取汇率失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                // 网络请求失败
                Log.e(TAG, "获取汇率失败", t);
                listener.onFailure("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 获取汇率数据的回调接口
     */
    public interface OnExchangeRateListener {
        void onSuccess(Map<String, Double> rates);
        void onFailure(String errorMessage);
    }
}
