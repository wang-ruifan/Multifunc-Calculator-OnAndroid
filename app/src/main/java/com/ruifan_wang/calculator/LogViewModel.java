package com.ruifan_wang.calculator;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class LogViewModel extends ViewModel {
    public List<LogHistory> log_history;

    public LogViewModel() {
        log_history=LogRepository.getInstance().mLogHistoryList;
    }
    public List getLogList(){
        return log_history;
    }
    public void clearLog_history() {
        LogRepository.getInstance().clearLog_history();
        log_history = LogRepository.getInstance().mLogHistoryList;
    }
}