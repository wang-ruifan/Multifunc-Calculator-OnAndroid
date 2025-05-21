package com.ruifan_wang.calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BillResultActivity extends AppCompatActivity {
    private String shareString = "";
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        double[] pay = getIntent().getDoubleArrayExtra("pay");
        String[] names = getIntent().getStringArrayExtra("names");
        StringBuilder sb = new StringBuilder();
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
            String line = name + " 需要支付：" + String.format("%.2f", pay[i]) + "\n";
            tv.setText(line);
            tv.setTextSize(22);
            tv.setPadding(32, 32, 32, 32);

            card.addView(tv);
            layout.addView(card);
            sb.append(line).append("\n");
        }
        setContentView(layout);
        shareString = sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_bill_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_share) {
            shareBill();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareBill() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        startActivity(Intent.createChooser(shareIntent, "分享账单"));
    }
}