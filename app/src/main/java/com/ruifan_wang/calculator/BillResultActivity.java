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
            cardParams.setMargins(16, 24, 16, 24);
            card.setLayoutParams(cardParams);

            TextView tv = new TextView(this);
            String line = name + " 需付：" + String.format("%.2f", pay[i]);
            tv.setText(line);
            tv.setTextSize(23);
            tv.setPadding(32, 32, 32, 32);
            tv.setTextColor(getResources().getColor(R.color.bill_result_text_color, getTheme()));

            card.addView(tv);
            layout.addView(card);
            sb.append(line).append("\n");

            card.setOnClickListener(v -> {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("支付信息", line.trim());
                clipboard.setPrimaryClip(clip);
                android.widget.Toast.makeText(this, "已复制: " + line.trim(), android.widget.Toast.LENGTH_SHORT).show();
            });
        }
        setContentView(layout);
        setTitle("账单结果");
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }
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

