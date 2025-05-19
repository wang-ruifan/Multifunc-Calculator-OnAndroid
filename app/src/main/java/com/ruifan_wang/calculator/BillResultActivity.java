package com.ruifan_wang.calculator;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BillResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        double[] pay = getIntent().getDoubleArrayExtra("pay");
        String[] names = getIntent().getStringArrayExtra("names");
        for (int i = 0; i < pay.length; i++) {
            CardView card = new CardView(this);
            String name = (names != null && names[i] != null && !names[i].isEmpty()) ? names[i] : "第" + (i + 1) + "个人";
            card.setRadius(16);
            card.setCardElevation(8);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(16, 16, 16, 0);
            card.setLayoutParams(cardParams);

            TextView tv = new TextView(this);
            tv.setText(name + " 需要支付：" + String.format("%.2f", pay[i]));
            tv.setTextSize(22);
            tv.setPadding(32, 32, 32, 32);

            card.addView(tv);
            layout.addView(card);
        }
        setContentView(layout);
    }
}