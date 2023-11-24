package com.ruifan_wang.calculator;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String init = "0";
    public StringBuilder input_str = new StringBuilder();
    public LogHistory log_history[] = new LogHistory[]{
            new LogHistory(init, 0)
    };
    private int count = 0;
    private  int log_history_length=0;
    public MainViewModel() {    }
    public int getCount() {
        return count;
    }

    public String getLog_String(int i) {
        return log_history[i].getLog_String();
    }
    public void setLog(String log_String,double result) {
        if(count<=log_history_length){
            log_history[count].setLog_String(log_String);
            log_history[count].setLog_answer(result);
        }else {
            LogHistory newLog = new LogHistory(log_String,result);
            LogHistory[] temp = new LogHistory[log_history.length + 1];
            System.arraycopy(log_history, 0, temp, 0, log_history.length);
            temp[temp.length - 1] = newLog;
            log_history=temp;
            log_history_length+=1;
        }
        count+=1;
    }

    public double getLog_answer(int i) {
        return log_history[i].getLog_answer();
    }
    public void clearlog_history(){
        for(int i=0;i<count;i++){
            log_history[i].setLog_answer(0);
            log_history[i].setLog_String(init);
        }
        count=0;
    }
}
