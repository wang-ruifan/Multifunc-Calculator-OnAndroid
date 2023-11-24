package com.ruifan_wang.calculator;

import java.io.Serializable; // 导入Serializable接口

public class LogHistory implements Serializable { // 实现Serializable接口
    private static final long serialVersionUID = 1L; // 添加一个serialVersionUID
    private String log_String; // 将类型改为String
    private double log_answer;

    public LogHistory(String s, double i) {
        log_String=s;
        log_answer=i;
    }

    public String getLog_String() {
        return log_String;
    }
    public void setLog_String(String log_String) {
        this.log_String = log_String;
    }

    public double getLog_answer() {
        return log_answer;
    }
    public void setLog_answer(double log_answer) {
        this.log_answer = log_answer;
    }
}

