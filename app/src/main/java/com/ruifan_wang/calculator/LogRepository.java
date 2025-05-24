package com.ruifan_wang.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogRepository {
    private static LogRepository sLogRepository;
    public List<LogHistory> mLogHistoryList;
    private boolean empty = true;
    private int count = 0;
    private static final int MAX_LOG_ENTRIES = 10; // 最多保存10条记录
    private static final String PREFS_NAME = "calculator_logs";
    private static final String PREF_LOGS = "history_logs";
    private static Context applicationContext;

    private LogRepository() {
        mLogHistoryList = new ArrayList<>();
        loadLogsFromPrefs(); // 从SharedPreferences加载历史记录
    }

    private void initializeList() {
        String init = "暂无历史记录，计算后的历史记录将会显示在此";
        mLogHistoryList.add(new LogHistory(init, 0));
    }

    public static LogRepository getInstance() {
        if (sLogRepository == null)
            throw new IllegalStateException("LogRepository must be initialized!");
        else return sLogRepository;
    }

    public static void initialize(Context context) {
        applicationContext = context.getApplicationContext();
        if (sLogRepository == null) sLogRepository = new LogRepository();
    }

    public static void initialize() {
        if (sLogRepository == null) sLogRepository = new LogRepository();
    }

    public void setLog(String log_String, double result) {
        Log.d("LogRepository", "setLog called");
        empty = false;

        // 如果达到最大记录数，移除最旧的一条
        if (count >= MAX_LOG_ENTRIES && !mLogHistoryList.isEmpty()) {
            if (count == MAX_LOG_ENTRIES) {
                // 当刚好达到上限时，替换第一条记录
                mLogHistoryList.get(0).setLog_String(log_String);
                mLogHistoryList.get(0).setLog_answer(result);
            } else {
                // 已经超过上限，删除最早的记录并添加新记录
                mLogHistoryList.remove(0);
                mLogHistoryList.add(new LogHistory(log_String, result));
            }
        } else {
            // 未达到上限
            if (count == 0) {
                mLogHistoryList.get(0).setLog_String(log_String);
                mLogHistoryList.get(0).setLog_answer(result);
            } else {
                mLogHistoryList.add(new LogHistory(log_String, result));
            }
        }

        count = Math.min(count + 1, MAX_LOG_ENTRIES);

        // 保存到SharedPreferences
        saveLogsToPrefs();
    }

    public void clearLog_history() {
        mLogHistoryList.clear();
        count = 0;
        empty = true;
        initializeList();

        // 清除SharedPreferences中的记录
        if (applicationContext != null) {
            SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(PREF_LOGS);
            editor.apply();
        }
    }

    public boolean isEmpty() {
        Log.d("LogRepository", String.format("Return isEmpty: %s", empty));
        return empty;
    }

    // 将历史记录保存到SharedPreferences
    private void saveLogsToPrefs() {
        if (applicationContext == null || empty) return;

        try {
            JSONArray logsArray = new JSONArray();
            for (LogHistory log : mLogHistoryList) {
                if (log.getLog_String().equals("暂无历史记录，计算后的历史记录将会显示在此")) {
                    continue; // 跳过提示信息
                }
                JSONObject logObj = new JSONObject();
                logObj.put("expression", log.getLog_String());
                logObj.put("result", log.getLog_answer());
                logsArray.put(logObj);
            }

            if (logsArray.length() > 0) {
                SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREF_LOGS, logsArray.toString());
                editor.apply();
                Log.d("LogRepository", "历史记录已保存到SharedPreferences");
            }
        } catch (JSONException e) {
            Log.e("LogRepository", "保存历史记录失败", e);
        }
    }

    // 从SharedPreferences加载历史记录
    private void loadLogsFromPrefs() {
        if (applicationContext == null) {
            initializeList();
            return;
        }

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String logsJson = prefs.getString(PREF_LOGS, null);

        if (logsJson == null || logsJson.isEmpty()) {
            initializeList();
            return;
        }

        try {
            JSONArray logsArray = new JSONArray(logsJson);
            if (logsArray.length() > 0) {
                empty = false;
                count = Math.min(logsArray.length(), MAX_LOG_ENTRIES);

                for (int i = 0; i < count; i++) {
                    JSONObject logObj = logsArray.getJSONObject(i);
                    String expression = logObj.getString("expression");
                    double result = logObj.getDouble("result");
                    mLogHistoryList.add(new LogHistory(expression, result));
                }
            } else {
                initializeList();
            }
        } catch (JSONException e) {
            Log.e("LogRepository", "加载历史记录失败", e);
            initializeList();
        }
    }
}
