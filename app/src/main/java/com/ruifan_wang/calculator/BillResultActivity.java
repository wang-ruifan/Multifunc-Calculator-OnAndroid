package com.ruifan_wang.calculator;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BillResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        double[] pay = getIntent().getDoubleArrayExtra("pay");
        String[] names = getIntent().getStringArrayExtra("names");
        for (int i = 0; i < pay.length; i++) {
            TextView tv = new TextView(this);
            String name = (names != null && names[i] != null && !names[i].isEmpty()) ? names[i] : "第" + (i + 1) + "个人";
            tv.setText(name + " 需要支付：" + String.format("%.2f", pay[i]));
            tv.setTextSize(24);
            layout.addView(tv);
        }
        setContentView(layout);
    }
}