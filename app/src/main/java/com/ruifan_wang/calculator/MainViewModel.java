package com.ruifan_wang.calculator;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {
    public StringBuilder input_str = new StringBuilder();
    public List<LogHistory> log_history;

    public MainViewModel() {
        log_history=LogRepository.getInstance().mLogHistoryList;
    }

    public void setLog(String log_String, double result) {
        LogRepository.getInstance().setLog(log_String,result);
        log_history=LogRepository.getInstance().mLogHistoryList;
    }

    public void clearLog_history() {
        LogRepository.getInstance().clearLog_history();
        log_history=LogRepository.getInstance().mLogHistoryList;
    }
}
