package com.demo.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvExp, tvResult;
    String expression = "0", result = "0", temp = "0";
    int numberCount;
    boolean operatorIsClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    void findViews() {
        tvExp = (TextView) findViewById(R.id.tvExp);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvExp.setText("");
        tvResult.setText(result + "");
    }

    private String checkNumberPrefix(String num) {
        if (num.matches("0\\d+.*"))
            num = num.substring(1);

        return num;
    }

    private String checkPoint(String numOrExp) {
        if (numOrExp.matches(".+(\\.){2}$") || numOrExp.matches(".+(\\.\\d+\\.)$"))
            numOrExp = numOrExp.substring(0, numOrExp.length() - 1);

        if (numOrExp.matches(".+[\\+\\-x/]\\."))
            numOrExp = numOrExp.replace(".", "0.");

        return numOrExp;
    }

    private String checkInteger(String numOrExp) {
        try {
            double d = Double.parseDouble(numOrExp);
            if ((int) d == d)
                return (int) d + "";
            else
                return numOrExp;
        } catch (NumberFormatException e) {
            System.out.println("e = " + e);
            return numOrExp;
        }
    }

    public void onNumberClick(View view) {
        if (!expression.contains("Infinity") && !expression.contains("NaN")) {
            String num = ((Button) view).getText().toString();
            temp += num;
            temp = checkPoint(temp);
            temp = checkNumberPrefix(temp);
            tvResult.setText(temp);
        }
        operatorIsClicked = false;
    }

    public void onOperatorClick(View view) {
        if (!expression.contains("Infinity") && !expression.contains("NaN")) {
            temp = checkInteger(temp);
            if (!operatorIsClicked) {
                expression += temp;
            }
            expression = checkNumberPrefix(expression);
            concatenate(view);
            show();
        }
        operatorIsClicked = true;
    }

    public void onEqual(View view) {
        expression += temp;
        if (!expression.contains("Infinity") && !expression.contains("NaN")) {
            calc();
            expression = checkInteger(result + "");

            if (expression.matches("[\\-]?\\d+(\\.\\d+)?")) {
                temp = result = expression;
                tvResult.setText(result);
            }
            tvExp.setText("");
        }
    }

    void concatenate(View view) {
        String operator = ((Button) view).getText().toString();
        expression += operator;

        if (expression.matches(".+([\\.\\+\\-x/]{2,})$")) {
            expression = expression.substring(0, expression.length() - 2) +
                expression.substring(expression.length() - 1);
        }
        if (!operatorIsClicked)
            calc();
    }

    String getOperator() {
        if (expression.matches(".+[\\+\\-x/=]$")) {
            return expression.substring(0, expression.length() - 1).split(
                "\\d+(\\.\\d+)?")[numberCount - 1];
        } else {
            return expression.split("\\d+(\\.\\d+)?")[numberCount - 1];
        }
    }

    void calc() {
        String[] numbersArray = expression.split("[\\+\\-x/]");

        if (numbersArray[0].equals(""))
            numberCount = numbersArray.length - 1;
        else
            numberCount = numbersArray.length;

        if (numberCount == 0)
            return;

        if (numberCount == 1) {
            result = tvResult.getText().toString();
            result = checkInteger(result + "");
        } else if (numberCount >= 2) {
            String operator = getOperator();
            double lastNum = Double.parseDouble(temp);
            double result = Double.parseDouble(this.result);
            switch (operator) {
                case "+":
                    result += lastNum;
                    break;
                case "-":
                    result -= lastNum;
                    break;
                case "x":
                    result *= lastNum;
                    break;
                case "/":
                    result /= lastNum;
                    break;
            }
            this.result = checkInteger(result + "");
        }

        temp = "0";
        show();
    }

    void show() {
        expression = checkNumberPrefix(expression);
        tvExp.setText(expression);
        tvResult.setText(checkInteger(result + ""));
    }

    public void onClear(View view) {
        operatorIsClicked = false;
        result = "0";
        temp = "0";
        numberCount = 0;
        expression = "0";
        tvExp.setText("");
        tvResult.setText(result);
    }
}
