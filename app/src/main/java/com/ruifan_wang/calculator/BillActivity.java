package com.ruifan_wang.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private EditText editTotalAmount;
    private LinearLayout personInputContainer;
    private List<EditText> personNameInputs = new ArrayList<>();
    private List<EditText> personAmountInputs = new ArrayList<>();
    private int peopleCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        setTitle("算账器");

        numberPicker = findViewById(R.id.number_picker);
        editTotalAmount = findViewById(R.id.edit_total_amount);
        personInputContainer = findViewById(R.id.person_input_container);
        FloatingActionButton fab = findViewById(R.id.fab);

        // 自动填充共同金额
        String calcResult = getIntent().getStringExtra("calc_result");
        if (calcResult != null && !calcResult.isEmpty()) {
            editTotalAmount.setText(calcResult);
        }

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(2);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updatePersonInputs(newVal));

        updatePersonInputs(2);

        fab.setOnClickListener(v -> calculateAndShowResult());
    }

    private void updatePersonInputs(int n) {
        int oldCount = personInputContainer.getChildCount();

        personInputContainer.setLayoutTransition(new android.animation.LayoutTransition());
        // 增加新的人数输入
        for (int i = oldCount; i < n; i++) {
            CardView card = new CardView(this);
            card.setRadius(12);
            card.setCardElevation(4);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 8, 0, 8);
            card.setLayoutParams(cardParams);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(16, 16, 16, 16);

            EditText nameEt = new EditText(this);
            nameEt.setHint("人名");
            nameEt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            row.addView(nameEt);

            EditText amountEt = new EditText(this);
            amountEt.setHint("金额(支持加法)");
            amountEt.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            amountEt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            row.addView(amountEt);

            card.addView(row);
            personInputContainer.addView(card);
            personNameInputs.add(nameEt);
            personAmountInputs.add(amountEt);
        }

        // 删除多余的人数输入
        for (int i = oldCount - 1; i >= n; i--) {
            personInputContainer.removeViewAt(i);
            personNameInputs.remove(i);
            personAmountInputs.remove(i);
        }

        peopleCount = n;
    }

    // 解析加法表达式
    private double parseAmount(String expr) {
        double sum = 0;
        for (String part : expr.split("\\+")) {
            part = part.trim();
            if (!part.isEmpty()) sum += Double.parseDouble(part);
        }
        return sum;
    }

    private void calculateAndShowResult() {
        try {
            double total = parseAmount(editTotalAmount.getText().toString());
            double[] personAmounts = new double[peopleCount];
            String[] personNames = new String[peopleCount];
            for (int i = 0; i < peopleCount; i++) {
                personNames[i] = personNameInputs.get(i).getText().toString().trim();
                if (personNames[i].isEmpty()) personNames[i] = "第" + (i + 1) + "个人";
                String input = personAmountInputs.get(i).getText().toString();
                personAmounts[i] = parseAmount(input);
            }
            double[] pay = new double[peopleCount];
            for (int i = 0; i < peopleCount; i++) {
                pay[i] = total / peopleCount + personAmounts[i];
            }
            Intent intent = new Intent(this, BillResultActivity.class);
            intent.putExtra("pay", pay);
            intent.putExtra("names", personNames);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "输入有误", Toast.LENGTH_SHORT).show();
        }
    }
}

