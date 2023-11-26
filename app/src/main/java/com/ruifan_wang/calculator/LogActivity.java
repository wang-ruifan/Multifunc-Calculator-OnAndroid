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
    private final String init = "0";
    private LogHistory[] log_history = new LogHistory[]{
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
        log_history = (LogHistory[]) intent.getSerializableExtra("log");
        display(count);
    }

    public void display(int count) {
        TextView[] logs = {log1, log2, log3, log4, log5, log6, log7, log8};
        for (int i = 0; i < count; i++) {
            int text;
            if (i < 8) text = i;
            else text = i % 8;
            logs[text].setText(log_history[i].getLog_display());
        }
    }
}