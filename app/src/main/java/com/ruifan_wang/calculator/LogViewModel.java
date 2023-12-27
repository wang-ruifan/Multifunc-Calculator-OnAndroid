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
}