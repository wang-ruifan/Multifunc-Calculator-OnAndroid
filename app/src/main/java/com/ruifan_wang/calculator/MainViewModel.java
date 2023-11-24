package com.ruifan_wang.calculator;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String init = "0";
    public StringBuilder input_str = new StringBuilder();
    public LogHistory Log_history[] = new LogHistory[]{
            new LogHistory(init, 0)
    };
    private int count = 0;
    public MainViewModel() {    }
    public int getCount() {
        return count;
    }

    public String getLog_String(int i) {
        return Log_history[i].getLog_String();
    }
    public void setLog(String log_String,double result) {
        if(count==0){
            Log_history[count].setLog_String(log_String);
            Log_history[count].setLog_answer(result);
        }else {
            LogHistory newLog = new LogHistory(log_String,result);
            LogHistory[] newLog_history = new LogHistory[Log_history.length + 1];
            System.arraycopy(Log_history, 0, newLog_history, 0, Log_history.length);
            newLog_history[newLog_history.length - 1] = newLog;
            Log_history=newLog_history;
        }
        count+=1;
    }

    public double getLog_answer(int i) {
        return Log_history[i].getLog_answer();
    }
    public void clearLog_history(){
        for(int i=0;i<count;i++){
            Log_history[i].setLog_answer(0);
            Log_history[i].setLog_String(init);
        }
        count=0;
    }
}
