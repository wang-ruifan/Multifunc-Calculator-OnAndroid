package com.ruifan_wang.calculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LogRepository {
    private static LogRepository sLogRepository;
    public List<LogHistory> mLogHistoryList;
    private boolean empty=true;
    private int count=0;
    private LogRepository(){
        mLogHistoryList=new ArrayList<>();
        initializeList();
    }

    private void initializeList(){
        String init = "暂无历史记录，计算后的历史记录将会显示在此";
        mLogHistoryList.add(new LogHistory(init,0));
    }

    public static LogRepository getInstance(){
        if(sLogRepository==null)
            throw new IllegalStateException("LogRepository must be initialized!");
        else return sLogRepository;
    }

    public static void initialize(){
        if(sLogRepository==null) sLogRepository=new LogRepository();
    }

    public void setLog(String log_String, double result) {
        Log.d("LogRepository", "setLog called");
        empty=false;
        if (count == 0) {
            mLogHistoryList.get(0).setLog_String(log_String);
            mLogHistoryList.get(0).setLog_answer(result);
        } else {
            mLogHistoryList.add(new LogHistory(log_String,result));
        }
        count += 1;
    }

    public void clearLog_history() {
        mLogHistoryList.clear();
        count = 0;
        empty=true;
        initializeList();
    }
    public boolean isEmpty(){
        Log.d("LogRepository", String.format("Return isEmpty: %s", empty));
        return empty;
    }
}
