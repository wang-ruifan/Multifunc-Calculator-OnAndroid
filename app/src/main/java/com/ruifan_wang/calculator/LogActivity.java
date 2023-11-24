package com.ruifan_wang.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {
    private TextView log1;
    private TextView log2;
    private TextView log3;
    private TextView log4;
    private TextView log5;
    private TextView log6;
    private TextView log7;
    private TextView log8;
    private int text;
    private String s;

    private String init = "0";
    private LogHistory mLog_History_history[] = new LogHistory[]{
            new LogHistory(init, 0)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        log1 = findViewById(R.id.log_1);
        log2 = findViewById(R.id.log_2);
        log3 = findViewById(R.id.log_3);
        log4 = findViewById(R.id.log_4);
        log5 = findViewById(R.id.log_5);
        log6 = findViewById(R.id.log_6);
        log7 = findViewById(R.id.log_7);
        log8 = findViewById(R.id.log_8);
        int count = getIntent().getIntExtra("count", 0);
        Intent intent = getIntent();
        mLog_History_history = (LogHistory[]) intent.getSerializableExtra("log");
        display(count);
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

    public void display(int count) {
        TextView[] logs = {log1, log2, log3, log4, log5, log6,log7,log8};
        for (int i = 0; i < count; i++) {
            s = mLog_History_history[i].getLog_String();
            s=transfer(s);
            if(i<8)text=i;
            else text=i%8;
            if ((int) mLog_History_history[i].getLog_answer() == mLog_History_history[i].getLog_answer()) {
                logs[text].setText(s + "=" + (int) mLog_History_history[i].getLog_answer());
            } else {
                logs[text].setText(s + "=" + mLog_History_history[i].getLog_answer());
            }

        }
    }
}