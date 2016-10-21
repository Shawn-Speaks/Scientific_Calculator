package shawn.c4q.nyc.scicalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG TOOL";
    String tempStr = "";
    TextView inputTextView;
    private static final String myPlaceHolder = "random_string";
    boolean decimalAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputTextView = (TextView) findViewById(R.id.edit_text_id);
        inputTextView.setKeyListener(null);
        inputTextView.setText(tempStr);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            tempStr = (String) savedInstanceState.getCharSequence(myPlaceHolder);
            inputTextView.setText(tempStr);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(myPlaceHolder, tempStr);
    }


    public void buttonOnClick(View view) {
        int buttonID = view.getId();
        Button button = (Button) view;
        String buttonPressedStrForm = button.getText().toString();

        if (buttonID == (R.id.portrait_delete)) {
            if (tempStr.length() > 0) {
                tempStr = tempStr.substring(0, tempStr.length() - 1);
            }
            inputTextView.setText(tempStr);
        } else if (buttonID == (R.id.portrait_clear)) {
            tempStr = "";
            inputTextView.setText(tempStr);
        } else if (buttonID == (R.id.portrait_period)) {
            if (decimalAllowed) {
                tempStr = tempStr.concat(buttonPressedStrForm);
                inputTextView.setText(tempStr);
            }else
                tempStr = tempStr.concat("");
        } else if (buttonID == (R.id.sin) || buttonID == (R.id.cos) || buttonID == (R.id.tan)) {
            tempStr = tempStr.concat(buttonPressedStrForm + "(");
            inputTextView.setText(tempStr);
        } else if (buttonID == (R.id.portrait_equals)) {
            tempStr = fillInLast(tempStr);
            tempStr = matchParens(tempStr);
            tempStr = makeNice(tempStr);
            Calculator calc = new Calculator();
            tempStr = calc.calculate(tempStr);
            if (!tempStr.isEmpty() && (Math.floor(Double.valueOf(tempStr)) == (Double.valueOf(tempStr)))) {
                tempStr = tempStr.substring(0,findLastDecimalIdx(tempStr));
            }
            inputTextView.setText(tempStr);
        } else {

            if (tempStr.length() == 0 && (buttonID == R.id.portrait_division || buttonID == R.id.portrait_plus || buttonID == R.id.portrait_multiplication)) { //IF NO BUTTON WAS PRESSED DISABLE OPERATIONS BESIDES MINUS.
                tempStr = tempStr.concat("");
            } else {
                if (!tempStr.isEmpty() && (isLastCharOperand(tempStr.charAt(tempStr.length() - 1)) && isLastCharOperand(buttonPressedStrForm.charAt(0)))) {
                    if (buttonID == R.id.portrait_division || buttonID == R.id.portrait_plus || buttonID == R.id.portrait_multiplication) {
                        tempStr = tempStr.substring(0, tempStr.length() - 1);
                        tempStr = tempStr.concat(buttonPressedStrForm);
                        inputTextView.setText(tempStr);

                    } else if (buttonID == R.id.portrait_minus) {
                        if (tempStr.charAt(tempStr.length() - 1) == '-') {
                            tempStr = tempStr.concat("");
                        } else {
                            tempStr = tempStr.concat(buttonPressedStrForm);
                            inputTextView.setText(tempStr);
                        }
                    } else {
                        tempStr = tempStr.concat(buttonPressedStrForm);
                        inputTextView.setText(tempStr);
                    }
                } else {
                    tempStr = tempStr.concat(buttonPressedStrForm);
                    inputTextView.setText(tempStr);
                }
            }
        }

        if (findLastDecimalIdx(tempStr) <= findLastOperatorIdx(tempStr)) {
            decimalAllowed = true;
        } else
            decimalAllowed = false;
    }

    static String matchParens(String inputString) {
        int openCount = 0;
        int closeCount = 0;
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            if (inputString.charAt(i) == '(') {
                openCount++;
            } else if (inputString.charAt(i) == ')') {
                closeCount++;
            }
        }
        if (openCount > closeCount) {
            while (openCount != closeCount) {
                mySB.append(")");
                closeCount++;
            }
            inputString = inputString + mySB.toString();
        } else if (openCount < closeCount) {
            while (openCount != closeCount) {
                mySB.append("(");
                openCount++;
            }
            inputString = mySB.toString() + inputString;
        }
        return inputString;
    }

    static String fillInLast(String inputString){
        if(!inputString.isEmpty()) {
            if (inputString.charAt(inputString.length() - 1) == ('*') || inputString.charAt(inputString.length() - 1) == ('/')) {
                inputString = inputString.concat("1");
            } else if (inputString.charAt(inputString.length() - 1) == ('-') || inputString.charAt(inputString.length() - 1) == ('+')) {
                inputString = inputString.concat("0");
            }
        }
        return inputString;
    }

    static boolean isLastCharOperand(char c){
        return (c == '*'|| c == '/'|| c == '+' || c == '-');
    }

    static String makeNice(String inputString) {
        String toReturn = inputString;
        for (int i = 1; i < toReturn.length() - 1; i++) {
            if (toReturn.charAt(i) == '(') {
                if (Character.isDigit(toReturn.charAt(i - 1))) {
                    toReturn = toReturn.substring(0, i) + "*" + toReturn.substring(i, toReturn.length());
                }
            }
            if (toReturn.charAt(i) == ')' && (i != toReturn.length() - 1)) {
                if (Character.isDigit(toReturn.charAt(i + 1))) {
                    toReturn = toReturn.substring(0, i + 1) + "*" + toReturn.substring(i + 1, toReturn.length());
                }
            }
        }
        return toReturn;
    }

    static int findLastDecimalIdx(String inputString){
        int decimalIdx = -1; //Initialized as -1 to make comparison vs inputString easier.
        for (int i = 0; i < inputString.length(); i++) {
            if(inputString.charAt(i) == '.'){
                decimalIdx = i;
            }
        }
        return decimalIdx;
    }

    static int findLastOperatorIdx(String inputString){
        int operatorIdx = -1; //Initialized as -1 to make comparison vs inputString easier.
        for (int i = 0; i < inputString.length(); i++) {
            if(isLastCharOperand(inputString.charAt(i))){
                operatorIdx = i;
            }
        }
        return operatorIdx;
    }
}
