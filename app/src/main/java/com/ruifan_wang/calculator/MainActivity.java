package com.ruifan_wang.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.nio.file.LinkPermission;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText editText_display;
    private TextView textView_error;
    private boolean error_state;
    private final StringBuilder input_str = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_display = (EditText) findViewById(R.id.editView_display);
        textView_error = (TextView) findViewById(R.id.textView_error);
    }

    public void num(View view) {
        Button button = (Button) view;
        editText_display.append(button.getText());
        input_str.append(button.getText());
    }

    public void clear(View view) {
        editText_display.setText(null);
        textView_error.setText(null);
        input_str.delete(0, input_str.length());
    }

    public void delete(View view) {
        if (editText_display.length() != 0 && input_str.length() != 0) {
            String now_display = editText_display.getText().toString();
            editText_display.setText(now_display.substring(0, now_display.length() - 1));
            input_str.deleteCharAt(input_str.length() - 1);
        }
        textView_error.setText(null);
    }

    public void sqrt(View view) {
        editText_display.append("√(");
        input_str.append("k(");
    }

    public void ln(View view) {
        editText_display.append("ln(");
        input_str.append("n(");
    }

    public void lg(View view) {
        editText_display.append("lg(");
        input_str.append("g(");
    }

    public void log2(View view) {
        editText_display.append("log2(");
        input_str.append("l(");
    }

    public void pi(View view) {
        editText_display.append("π");
        input_str.append("p");
    }

    public void tan(View view) {
        editText_display.append("tan");
        input_str.append("t");
    }

    public void cos(View view) {
        editText_display.append("cos");
        input_str.append("c");
    }

    public void sin(View view) {
        editText_display.append("sin");
        input_str.append("s");
    }

    public void e(View view) {
        editText_display.append("e");
        input_str.append("e");
    }

    public void ex(View view) {
        editText_display.append("e^");
        input_str.append("e^");
    }

    public void x2(View view) {
        editText_display.append("^2");
        input_str.append("^2");
    }

    public void xy(View view) {
        editText_display.append("^");
        input_str.append("^");
    }

    public void left_bracket(View view) {
        editText_display.append("(");
        input_str.append("(");
    }

    public void right_bracket(View view) {
        editText_display.append(")");
        input_str.append(")");
    }

    public void divide(View view) {
        editText_display.append("÷");
        input_str.append("/");
    }

    public void multiply(View view) {
        editText_display.append("×");
        input_str.append("*");
    }

    public void subtract(View view) {
        editText_display.append("-");
        input_str.append("-");
    }

    public void add(View view) {
        editText_display.append("+");
        input_str.append("+");
    }

    public void percentage(View view) {
        editText_display.append("%");
        input_str.append("*0.01");
    }

    public void dot(View view) {
        editText_display.append(".");
        input_str.append(".");
    }

    public void equal(View view) {
        error_state = false;
        judge();
        if (!error_state) {
            List<String> infix = CharTransToInfix();
            List<String> postfix = InfixTransToPostfix(infix);
            double result =calculate(postfix);
            editText_display.append("\n" + result);
            input_str.delete(0,input_str.length());
            input_str.append(result);
        }
    }

    public void judge() {
        if (input_str.length() == 0) {
            textView_error.setText("输入不能为空！");
            error_state = true;
        }
        if (input_str.length() == 1 && ("0123456789ep".indexOf(input_str.charAt(0)) == -1)) {
            textView_error.setText("没有输入数字或常数！");
            error_state = true;
        }
        if (input_str.charAt(0) == '0' && input_str.charAt(1) == '0') {
            textView_error.setText("不能只输入0！");
            error_state = true;
        }
        if ("0123456789ep!)".indexOf(input_str.charAt(input_str.length() - 1)) == -1) {
            textView_error.setText("最后一位字符无效!");
            error_state = true;
        }
        if (input_str.length() > 1) {
            if ("kngltcs(123456789ep".indexOf(input_str.charAt(0)) == -1) {
                textView_error.setText("首个字符无效！");
                error_state = true;
            }
            for (int i = 0; i < input_str.length() - 1; i++) {
                if ("+-*/".indexOf(input_str.charAt(i)) >= 0 && "kngltcs(0123456789ep".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("运算符号不能重复出现！");
                    error_state = true;
                }
                if (".".indexOf(input_str.charAt(i)) >= 0 && "0123456789".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("小数点后只能为数字！");
                    error_state = true;
                }
                if ("kngltcs".indexOf(input_str.charAt(i)) >= 0 && "0123456789ep".indexOf(input_str.charAt(i + 2)) == -1) {
                    textView_error.setText("运算符号不能连续出现！");
                    error_state = true;
                }
                if ("123456789".indexOf(input_str.charAt(i)) >= 0 && "0123456789ep+-*/.)^".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("数字后不能直接与运算符号相连接！");
                    error_state = true;
                }
                if ("(".indexOf(input_str.charAt(i)) >= 0 && "kngltcs()0123456789ep".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("括号内的符号无效！");
                    error_state = true;
                }
                if (")".indexOf(input_str.charAt(i)) >= 0 && "+-*/^)".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("括号后的符号无效！");
                    error_state = true;
                }
                if ("ep".indexOf(input_str.charAt(i)) >= 0 && "+-*/^)".indexOf(input_str.charAt(i + 1)) == -1) {
                    textView_error.setText("小数点后的符号无效！");
                    error_state = true;
                }
                if (i >= 1 && input_str.charAt(i) == '0') {
                    int zero_position = i;
                    int j = i - 1;
                    boolean state_dot = false;
                    if ("0123456789.".indexOf(input_str.charAt(zero_position - 1)) == -1 && "+-*/.^".indexOf(input_str.charAt(zero_position + 1)) == -1) {
                        textView_error.setText("0无效");
                        error_state = true;
                    }
                    if (input_str.charAt(zero_position - 1) == '.' && "0123456789+-*/^".indexOf(input_str.charAt(zero_position + 1)) == -1) {
                        textView_error.setText("0无效");
                        error_state = true;
                    }
                    while (j > 0) {
                        if ("(+-*/^kngltsc".indexOf(input_str.charAt(j)) >= 0) {
                            break;
                        }
                        if (input_str.charAt(j) == '.') {
                            state_dot = true;
                        }
                        if (input_str.charAt(j) == '.' && state_dot) {
                            textView_error.setText("输入多个小数点！");
                            error_state = true;
                        }
                        j--;
                    }
                    if ((!state_dot && input_str.charAt(j) == '0') || "0123456789+-*/.!^)".indexOf(input_str.charAt(zero_position + 1)) == -1) {
                        textView_error.setText("0无效！");
                        error_state = true;
                    }
                    if (state_dot && "0123456789+-*/.^)".indexOf(input_str.charAt(zero_position + 1)) == -1) {
                        textView_error.setText("0无效！");
                        error_state = true;
                    }
                }
                if (i >= 2 && input_str.charAt(i) == '.') {
                    int j = i - 1;
                    boolean state_dot = false;
                    while (j > 0) {
                        if ("(+-*/^kngltsc".indexOf(input_str.charAt(j)) >= 0) {
                            break;
                        }
                        if (input_str.charAt(j) == '.') {
                            state_dot = true;
                        }
                        j--;
                    }
                    if (state_dot) {
                        textView_error.setText("输入多个小数点！");
                        error_state = true;
                    }
                }
            }
        }
    }

    public List<String> CharTransToInfix() {
        int infix_length = 0;
        List<String> infix_list = new ArrayList<>();
        do {
            char current = input_str.charAt(infix_length);
            if ("+-*/^kngltcs()ep".indexOf(input_str.charAt(infix_length)) >= 0) {
                infix_length++;
                infix_list.add(current + "");
            } else if ("0123456789".indexOf(input_str.charAt(infix_length)) >= 0) {
                StringBuilder str_temp = new StringBuilder();
                while (infix_length < input_str.length() && "0123456789.".indexOf(input_str.charAt(infix_length)) >= 0) {
                    str_temp.append(input_str.charAt(infix_length));
                    infix_length++;
                }
                infix_list.add(str_temp.toString());
            }
        } while (infix_length < input_str.length());
        return infix_list;
    }

    public List<String> InfixTransToPostfix(@NonNull List<String> list_infix) {
        Stack<String> stack_temp = new Stack<>();
        List<String> list_postfix = new ArrayList<>();
        if (!list_infix.isEmpty()) {
            for (int i = 0; i < list_infix.size(); i++) {
                if (IsNum(list_infix.get(i))) {
                    list_postfix.add(list_infix.get(i));
                } else if (list_infix.get(i).charAt(0) == '(') {
                    stack_temp.push(list_infix.get(i));
                } else if (IsOp(list_infix.get(i))) {
                    if (stack_temp.isEmpty()) {
                        stack_temp.push(list_infix.get(i));
                    }
                } else {
                    if (list_infix.get(i).charAt(0) == ')') {
                        while (stack_temp.peek().charAt(0) != '(') {
                            list_postfix.add(stack_temp.pop());
                        }
                        stack_temp.pop();
                    } else {
                        if (priority(stack_temp.peek()) <= priority(list_infix.get(i))) {
                            stack_temp.push(list_infix.get(i));
                        } else {
                            while (!stack_temp.isEmpty() && !"(".equals(stack_temp.peek())) {
                                if (priority(list_infix.get(i)) <= priority(stack_temp.peek())) {
                                    list_postfix.add(stack_temp.pop());
                                }
                            }
                            if (stack_temp.isEmpty() || stack_temp.peek().charAt(0) == ')') {
                                stack_temp.push(list_infix.get(i));
                            }
                        }
                    }
                }
            }
            while (!stack_temp.isEmpty()) {
                list_postfix.add(stack_temp.pop());
            }
        } else {
            editText_display.setText("");
        }
        return list_postfix;
    }

    public static boolean IsNum(@NonNull String string_temp) {
        return "0123456789ep".indexOf(string_temp.charAt(0)) >= 0;
    }

    public static boolean IsOp(@NonNull String string_temp) {
        return "0123456789ep".indexOf(string_temp.charAt(0)) == -1;
    }

    @Contract(pure = true)
    public static int priority(@NonNull String string_temp) {
        int result = 0;
        switch (string_temp) {
            case "+":
                result = 1;
            case "-":
                result = 1;
            case "*":
                result = 2;
            case "/":
                result = 2;
            case "^":
                result = 3;
            case "k":
                result = 4;
            case "n":
                result = 4;
            case "g":
                result = 4;
            case "l":
                result = 4;
            case "t":
                result = 4;
            case "c":
                result = 4;
            case "s":
                result = 4;
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
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 + num_2;
                        break;
                    }
                    case "-": {
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 - num_2;
                        break;
                    }
                    case "*": {
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        temp = num_1 * num_2;
                        break;
                    }
                    case "/": {
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        if (num_1 != 0) {
                            temp = num_2 / num_1;
                        } else {
                            textView_error.setText("被除数不能为0！");
                            error_state = true;
                        }
                        break;
                    }
                    case "^": {
                        double num_1 = Double.parseDouble(stack_temp.pop());
                        double num_2 = Double.parseDouble(stack_temp.pop());
                        temp = Math.pow(num_2, num_1);
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
                    case "l": {
                        double num = Double.parseDouble(stack_temp.pop());
                        if (num > 0) {
                            temp = Math.log(num) / Math.log(2);
                        } else {
                            textView_error.setText("对数的真数不能为非正数！");
                            error_state = true;
                        }
                        break;
                    }
                    case "t": {
                        double num = Double.parseDouble(stack_temp.pop());
                        if (Math.cos(num) != 0) {
                            temp = Math.tan(num);
                        } else {
                            textView_error.setText("正切函数的输入值不能为+-π");
                            error_state = true;
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
}
