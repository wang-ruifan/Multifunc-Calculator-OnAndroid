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
    private static LogRepository instance;
    private static final String PREFS_NAME = "log_history";
    private static final String KEY_LOGS = "logs";
    private static final String KEY_EXPRESSION = "expression";
    private static final String KEY_RESULT = "result";
    private static final int MAX_SAVED_LOGS = 10; // 退出时要保存的最大记录数

    private final Context applicationContext;
    public List<LogHistory> mLogHistoryList = new ArrayList<>();
    private boolean empty = true;

    private LogRepository(Context context) {
        this.applicationContext = context.getApplicationContext();
        loadLogsFromPrefs();
        if (mLogHistoryList.isEmpty()) {
            initializeList();
        }
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new LogRepository(context);
        }
    }

    public static LogRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LogRepository必须先初始化");
        }
        return instance;
    }

    public boolean isEmpty() {
        return empty;
    }

    private void initializeList() {
        mLogHistoryList.add(new LogHistory(applicationContext.getString(R.string.no_history_message), 0));
        empty = true;
    }

    // 加载之前保存的记录
    private void loadLogsFromPrefs() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String logsJson = prefs.getString(KEY_LOGS, "");
        mLogHistoryList.clear();

        if (!logsJson.isEmpty()) {
            try {
                JSONArray logsArray = new JSONArray(logsJson);
                int count = logsArray.length();

                for (int i = 0; i < count; i++) {
                    JSONObject logObj = logsArray.getJSONObject(i);
                    String expression = logObj.getString(KEY_EXPRESSION);
                    double result = logObj.getDouble(KEY_RESULT);
                    mLogHistoryList.add(new LogHistory(expression, result));
                }

                if (count > 0) {
                    empty = false;
                }
            } catch (JSONException e) {
                Log.e("LogRepository", "解析日志JSON时出错", e);
                initializeList();
            }
        }
    }

    // 添加新记录到列表（不受MAX_SAVED_LOGS限制）
    public void setLog(String log_String, double result) {
        if (mLogHistoryList.size() == 1 && mLogHistoryList.get(0).getLog_String().equals(applicationContext.getString(R.string.no_history_message))) {
            mLogHistoryList.clear();
        }

        mLogHistoryList.add(0, new LogHistory(log_String, result));
        empty = false;

        // 每次添加记录都保存，但不限制当前会话中的记录数量
        saveLogsToPrefs();
    }

    // 清除所有记录
    public void clearLog_history() {
        mLogHistoryList.clear();
        initializeList();

        // 同时清除SharedPreferences中的记录
        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    // 保存记录到SharedPreferences，但只保存最新的MAX_SAVED_LOGS条
    private void saveLogsToPrefs() {
        try {
            JSONArray logsArray = new JSONArray();

            // 计算要保存的记录的起始索引
            int startIndex = Math.max(0, mLogHistoryList.size() - MAX_SAVED_LOGS);

            // 只取最新的MAX_SAVED_LOGS条记录来保存
            for (int i = startIndex; i < mLogHistoryList.size(); i++) {
                LogHistory logHistory = mLogHistoryList.get(i);
                if (!logHistory.getLog_String().equals(applicationContext.getString(R.string.no_history_message))) {
                    JSONObject logObj = new JSONObject();
                    logObj.put(KEY_EXPRESSION, logHistory.getLog_String());
                    logObj.put(KEY_RESULT, logHistory.getLog_answer());
                    logsArray.put(logObj);
                }
            }

            if (logsArray.length() > 0) {
                SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_LOGS, logsArray.toString());
                editor.apply();
                Log.d("LogRepository", "历史记录已保存到SharedPreferences");
            }
        } catch (JSONException e) {
            Log.e("LogRepository", "创建日志JSON时出错", e);
        }
    }
}