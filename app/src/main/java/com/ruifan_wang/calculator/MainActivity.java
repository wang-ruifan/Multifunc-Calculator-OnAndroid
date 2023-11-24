package com.ruifan_wang.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText editText_display;
    private TextView textView_error;
    private boolean error_state;
    private MainMenu menu;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        editText_display = (EditText) findViewById(R.id.editView_display);
        textView_error = (TextView) findViewById(R.id.textView_error);
        menu=new MainMenu();
        addMenuProvider(menu);
    }

    private class MainMenu implements MenuProvider{
        @Override
        public void onCreateMenu(Menu menu, MenuInflater menuInflater){
            menuInflater.inflate(R.menu.activity_main,menu);
        }
        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId()==R.id.btn_log) {
                showLog();
                return true;
            } else if(menuItem.getItemId()==R.id.btn_clear_log) {
                clearLog();
                return true;
            } else{
                    return false;
            }
        }
    }
    private void showLog(){
        Intent intent=new Intent(this,LogActivity.class);
        intent.putExtra("count",viewModel.getCount());
        intent.putExtra("log",viewModel.log_history);
        startActivity(intent);
    }
    private void clearLog(){
        viewModel.clearlog_history();
        Toast.makeText(this, String.format("历史记录已清除"), Toast.LENGTH_LONG).show();
    }
    public void num(View view) {
        Button button = (Button) view;
        editText_display.append(button.getText());
        viewModel.input_str.append(button.getText());
    }

    public void clear(View view) {
        editText_display.setText(null);
        textView_error.setText(null);
        viewModel.input_str.delete(0, viewModel.input_str.length());
    }

    public void delete(View view) {
        if (editText_display.length() != 0 && viewModel.input_str.length() != 0) {
            String now_display = editText_display.getText().toString();
            editText_display.setText(now_display.substring(0, now_display.length() - 1));
            viewModel.input_str.deleteCharAt(viewModel.input_str.length() - 1);
        } else if (editText_display.length() == 0&&viewModel.input_str.length()!=0) {
            viewModel.input_str.delete(0, viewModel.input_str.length());
        }
        textView_error.setText(null);
    }

    public void sqrt(View view) {
        editText_display.append("√");
        viewModel.input_str.append("k");
    }

    public void ln(View view) {
        editText_display.append("ln");
        viewModel.input_str.append("n");
    }

    public void lg(View view) {
        editText_display.append("lg");
        viewModel.input_str.append("g");
    }

    public void fact(View view) {
        editText_display.append("!");
        viewModel.input_str.append("!");
    }

    public void pi(View view) {
        editText_display.append("π");
        viewModel.input_str.append("p");
    }

    public void tan(View view) {
        editText_display.append("tan");
        viewModel.input_str.append("t");
    }

    public void cos(View view) {
        editText_display.append("cos");
        viewModel.input_str.append("c");
    }

    public void sin(View view) {
        editText_display.append("sin");
        viewModel.input_str.append("s");
    }

    public void e(View view) {
        editText_display.append("e");
        viewModel.input_str.append("e");
    }

    public void ex(View view) {
        editText_display.append("e^");
        viewModel.input_str.append("e^");
    }

    public void x2(View view) {
        editText_display.append("^2");
        viewModel.input_str.append("^2");
    }

    public void xy(View view) {
        editText_display.append("^");
        viewModel.input_str.append("^");
    }

    public void left_bracket(View view) {
        editText_display.append("(");
        viewModel.input_str.append("(");
    }

    public void right_bracket(View view) {
        editText_display.append(")");
        viewModel.input_str.append(")");
    }

    public void divide(View view) {
        editText_display.append("÷");
        viewModel.input_str.append("/");
    }

    public void multiply(View view) {
        editText_display.append("×");
        viewModel.input_str.append("*");
    }

    public void subtract(View view) {
        editText_display.append("-");
        if(viewModel.input_str.length()==0){
            viewModel.input_str.append("0-");
        }else {
            viewModel.input_str.append("-");
        }
    }

    public void add(View view) {
        editText_display.append("+");
        if(viewModel.input_str.length()==0){
            viewModel.input_str.append("0+");
        }else {
            viewModel.input_str.append("+");
        }
    }

    public void percentage(View view) {
        editText_display.append("%");
        viewModel.input_str.append("*0.01");
    }

    public void dot(View view) {
        editText_display.append(".");
        viewModel.input_str.append(".");
    }

    public void equal(View view) {
        error_state = false;
        judge();
        if (!error_state) {
            List<String> infix = CharTransToInfix();
            List<String> postfix = InfixTransToPostfix(infix);
            /*textView_error.setText(infix.toString());
            editText_display.append("\n"+postfix);*/
            double result =calculate(postfix);
            if(result%1<=0.0000000000001)result=(int)result;
            if(!error_state){
                viewModel.setLog(viewModel.input_str.toString(),result);
                if((int)result-result==0){
                    editText_display.append("\n" + (int)result);
                    viewModel.input_str.delete(0,viewModel.input_str.length());
                    if(result<0){
                        viewModel.input_str.append("0"+(int)result);
                    }else{
                        viewModel.input_str.append((int)result);
                    }
                }else{
                    editText_display.append("\n" + result);
                    viewModel.input_str.delete(0,viewModel.input_str.length());
                    if(result<0){
                        viewModel.input_str.append("0"+result);
                    }else{
                        viewModel.input_str.append(result);
                    }
                }
            }
        }
    }

    public void judge() {
        if (viewModel.input_str.length() == 0) {
            textView_error.setText("输入不能为空！");
            error_state = true;
            return;
        }
        if (viewModel.input_str.length() == 1 && ("0123456789ep".indexOf(viewModel.input_str.charAt(0)) == -1)) {
            textView_error.setText("没有输入数字或常数！");
            error_state = true;
            return;
        }
        if (viewModel.input_str.length() == 1 && viewModel.input_str.charAt(0) == '0') {
            textView_error.setText("不能只输入0！");
            error_state = true;
            return;
        }
        if ("0123456789ep!)".indexOf(viewModel.input_str.charAt(viewModel.input_str.length() - 1)) == -1) {
            textView_error.setText("最后一位字符无效!");
            error_state = true;
            return;
        }
        if (viewModel.input_str.length() > 1) {
            if ("kngltcs(0123456789ep-".indexOf(viewModel.input_str.charAt(0)) == -1) {
                textView_error.setText("首个字符无效！");
                error_state = true;
            }
            for (int i = 0; i < viewModel.input_str.length() - 1; i++) {
                if ("+-*/".indexOf(viewModel.input_str.charAt(i)) >= 0 && "kngltcs(0123456789ep".indexOf(viewModel.input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("运算符号不能重复出现！");
                    error_state = true;
                }
                if (".".indexOf(viewModel.input_str.charAt(i)) >= 0 && "0123456789".indexOf(viewModel.input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("小数点后只能为数字！");
                    error_state = true;
                }
            }
        }
    }

    public List<String> CharTransToInfix() {
        int infix_length = 0;
        List<String> infix_list = new ArrayList<>();
        do {
            char current = viewModel.input_str.charAt(infix_length);
            if ("+-*/^kng!tcs()ep".indexOf(viewModel.input_str.charAt(infix_length)) >= 0) {
                infix_length++;
                infix_list.add(current + "");
            } else if ("0123456789".indexOf(viewModel.input_str.charAt(infix_length)) >= 0) {
                StringBuilder str_temp = new StringBuilder();
                while (infix_length < viewModel.input_str.length() && "0123456789.".indexOf(viewModel.input_str.charAt(infix_length)) >= 0) {
                    str_temp.append(viewModel.input_str.charAt(infix_length));
                    infix_length++;
                }
                infix_list.add(str_temp.toString());
            }
        } while (infix_length < viewModel.input_str.length());
        return infix_list;
    }

    private boolean IsOp(@NonNull String string_temp) {
        String operators = "+-*/^!kngtcs";
        return operators.contains(string_temp);
    }

    public static boolean IsNum(@NonNull String string_temp) {
        return string_temp.matches("[0-9.]+") || string_temp.equals("p") || string_temp.equals("e");
    }

    public List<String> InfixTransToPostfix(@NonNull List<String> list_infix) {
        Stack<String> stack_temp = new Stack<>();
        List<String> list_postfix = new ArrayList<>();
        for (int i = 0; i < list_infix.size(); i++) {
            String token = list_infix.get(i);
            if (IsNum(token)) {
                list_postfix.add(token);
            } else if (token.equals("(")) {
                stack_temp.push(token);
            } else if (IsOp(token)) {
                while (!stack_temp.isEmpty() && !stack_temp.peek().equals("(") && priority(stack_temp.peek()) >= priority(token)) {
                    list_postfix.add(stack_temp.pop());
                }
                stack_temp.push(token);
            } else if (token.equals(")")) {
                while (!stack_temp.isEmpty() && !stack_temp.peek().equals("(")) {
                    list_postfix.add(stack_temp.pop());
                }
                if (!stack_temp.isEmpty() && stack_temp.peek().equals("(")) {
                    stack_temp.pop();
                }
            }
        }
        while (!stack_temp.isEmpty()) {
            list_postfix.add(stack_temp.pop());
        }
        return list_postfix;
    }

    public static int priority(@NonNull String string_temp) {
        int result = 0;
        switch (string_temp) {
            case "+":
            case "-":
                result = 1;
                break;
            case "*":
            case "/":
                result = 2;
                break;
            case "^":
                result = 3;
                break;
            case "k":
            case "n":
            case "g":
            case "!":
            case "t":
            case "c":
            case "s":
                result = 4;
                break;
        }
        return result;
    }

    public double calculate(@NonNull List<String> list_postfix) {
        Stack<String> stack_temp = new Stack<>();
        for (int i = 0; i < list_postfix.size(); i++) {
            if (IsNum(list_postfix.get(i))) {
                if (list_postfix.get(i).charAt(0) == 'e') {
                    stack_temp.push(String.valueOf(Math.E));
                } else if (list_postfix.get(i).charAt(0) == 'p') {
                    stack_temp.push(String.valueOf(Math.PI));
                } else {
                    stack_temp.push(list_postfix.get(i));
                }
            } else if (IsOp(list_postfix.get(i))) {
                double temp = 0;
                switch (list_postfix.get(i)) {
                    case "+": {
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 + num_2;
                        break;
                    }
                    case "-": {
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 - num_2;
                        break;
                    }
                    case "*": {
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 * num_2;
                        break;
                    }
                    case "/": {
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        if (num_2 != 0) {
                            temp = num_1 / num_2;
                        } else {
                            textView_error.setText("被除数不能为0！");
                            error_state = true;
                        }
                        break;
                    }
                    case "^": {
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        temp = Math.pow(num_1, num_2);
                        break;
                    }
                    case "k": {
                        double num = Double.parseDouble(stack_temp.pop());
                        temp = Math.sqrt(num);
                        break;
                    }
                    case "n": {
                        double num = Double.parseDouble(stack_temp.pop());
                        if (num > 0) {
                            temp = Math.log(num);
                        } else {
                            textView_error.setText("对数的真数不能为非正数！");
                            error_state = true;
                        }
                        break;
                    }
                    case "g": {
                        double num = Double.parseDouble(stack_temp.pop());
                        if (num > 0) {
                            temp = Math.log(num) / Math.log(10);
                        } else {
                            textView_error.setText("对数的真数不能为非正数！");
                            error_state = true;
                        }
                        break;
                    }
                    case "!": {
                        double num = Double.parseDouble(stack_temp.pop());
                        if (num ==0||num==1) {
                            temp=1;
                        } else if (num==(int)num&&num>1) {
                            temp=1;
                            for(int i1=(int)num;i1>1;i1--){
                                temp*=i1;
                            }
                        } else {
                            textView_error.setText("阶乘必须为自然数！");
                            error_state = true;
                        }
                        break;
                    }
                    case "t": {
                        double num = Double.parseDouble(stack_temp.pop());
                        double pi=(double) Math.PI;
                        if (Math.abs(num)%pi==pi/2) {
                            textView_error.setText("正切函数的输入值不能为(k+1/2)π!");
                            error_state = true;
                        } else {
                            temp = Math.sin(num)/Math.cos(num);
                        }
                        break;
                    }
                    case "c": {
                        double num = Double.parseDouble(stack_temp.pop());
                        temp = Math.cos(num);
                        break;
                    }
                    case "s": {
                        double num = Double.parseDouble(stack_temp.pop());
                        temp = Math.sin(num);
                        break;
                    }
                }
                stack_temp.push("" + temp);
            }
        }
        if (!error_state) {
            if (!stack_temp.isEmpty()) {
                return Double.parseDouble(stack_temp.pop());
            } else return 0;
        }
        return 0;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeMenuProvider(menu);
    }
}
