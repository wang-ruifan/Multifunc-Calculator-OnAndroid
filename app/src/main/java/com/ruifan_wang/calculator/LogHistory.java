package com.ruifan_wang.calculator;

import java.io.Serializable;

public class LogHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String log_String;
    private double log_answer;
    private String log_display;

    public LogHistory(String s, double i) {
        log_String = s;
        log_answer = i;
        setLog_display();
    }

    public String getLog_String() {
        return log_String;
    }

    public void setLog_String(String log_String) {
        this.log_String = log_String;
        setLog_display();
    }

    public double getLog_answer() {
        return log_answer;
    }

    public void setLog_answer(double log_answer) {
        this.log_answer = log_answer;
        setLog_display();
    }

    public String getLog_display() {
        return log_display;
    }

    public void setLog_display() {
        String temp = transfer(log_String);
        if ((int) log_answer == log_answer) {
            log_display = temp + "=" + (int) log_answer;
        } else {
            log_display = temp + "=" + log_answer;
        }
    }

    private String transfer(String s) {
        s = s.replace("k", "√");
        s = s.replace("n", "ln");
        s = s.replace("g", "lg");
        s = s.replace("p", "π");
        s = s.replace("t", "tan");
        s = s.replace("s", "sin");
        s = s.replace("c", "cos");
        s = s.replace("/", "÷");
        s = s.replace("*", "×");
        s = s.replace("×0.01", "%");
        return s;
    }
}

