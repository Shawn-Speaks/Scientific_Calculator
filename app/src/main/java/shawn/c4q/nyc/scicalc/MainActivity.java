package shawn.c4q.nyc.scicalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG TOOL";
    StringBuilder myBuiltStr = new StringBuilder();
    String tempStr = "";
    TextView inputTextView;
    private static final String myPlaceHolder = "random_string";
    int idxOfLastOpp = 0;
    int idxOfDecimal = 0;

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

        //// FIXME: 10/14/16 I DONT DISPLAY 'tempStr' WHEN SWITCHED FROM LANDSCAPE TO PORTRAIT
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(myPlaceHolder, tempStr);
    }








    public void buttonOnClick(View view){
        int buttonID = view.getId();
        Button button = (Button) view;
        boolean decimalAllowed = true; //SET TRUE WHEN DECIMAL IS PRESSED, RESET WHEN OPERATION OR TRIG IS CALLED
        String buttonPressedStrForm = button.getText().toString();


        if(tempStr.length() > 0) {
            for (int i = 0; i < tempStr.length(); i++) {
                if (isLastCharOperand(tempStr.charAt(i))){
                    idxOfLastOpp = i;
                }else if (tempStr.charAt(i) =='.'){
                    idxOfDecimal = i;
                }
            }
            if(idxOfDecimal > idxOfLastOpp){
                decimalAllowed = false;
            }else
                decimalAllowed = true;
        }  // // FIXME: 10/14/16 I DO WEIRD STUFF AND I DON'T WORK


        if (buttonID == (R.id.portrait_delete)){
            if (tempStr.length() > 0) {
                tempStr = tempStr.substring(0, tempStr.length()-1);
            }
            inputTextView.setText(tempStr);
        } else if(buttonID == (R.id.portrait_clear)){
            tempStr = "";
            inputTextView.setText(tempStr);
        } else if(buttonID == (R.id.portrait_period)){
            if(decimalAllowed){
                tempStr = tempStr.concat(buttonPressedStrForm);
                inputTextView.setText(tempStr);
            }
        }else if(buttonID == (R.id.sin) || buttonID == (R.id.cos) || buttonID == (R.id.tan)){
                tempStr = tempStr.concat(buttonPressedStrForm + "(");
                inputTextView.setText(tempStr);
        }
        else if(buttonID == (R.id.portrait_equals)){

            tempStr = fillInLast(tempStr);

            tempStr = matchParens(tempStr);

            tempStr = makeNice(tempStr);

            Calculator calc = new Calculator();
            tempStr = calc.calculate(tempStr);

            inputTextView.setText(tempStr);


            //// FIXME: 10/13/16  CALCULATOR WILL NOT SAVE ANSWER AS NUMBER BUT RATHER EXPRESSION THAT WAS PASSED IN.
        }
        else {

            if (tempStr.length() == 0 && (buttonID == R.id.portrait_division || buttonID == R.id.portrait_plus || buttonID == R.id.portrait_multiplication)) { //IF NO BUTTON WAS PRESSED DISABLE OPERATIONS BESIDES MINUS.
                myBuiltStr.append("");
            }else {
                        if (!tempStr.isEmpty() && (isLastCharOperand(tempStr.charAt(tempStr.length() - 1)) && isLastCharOperand(buttonPressedStrForm.charAt(0)))) {
                            if(buttonID == R.id.portrait_division || buttonID == R.id.portrait_plus ||buttonID == R.id.portrait_multiplication){
//                                tempStr.replace(tempStr.length() - 1, tempStr.length(), buttonPressedStrForm);
                                    tempStr = tempStr.substring(0, tempStr.length()-1);
                                    tempStr = tempStr.concat(buttonPressedStrForm);
                                    inputTextView.setText(tempStr);

                            }else if(buttonID == R.id.portrait_minus){
                                if(tempStr.charAt(tempStr.length()-1) == '-'){
                                    myBuiltStr.append("");
                                }else{
                                    tempStr = tempStr.concat(buttonPressedStrForm);
                                    inputTextView.setText(tempStr);
                                }
                            } else {
                                tempStr = tempStr.concat(buttonPressedStrForm);
                                inputTextView.setText(tempStr);
                            }
                        }else{
                            tempStr = tempStr.concat(buttonPressedStrForm);
                            inputTextView.setText(tempStr);
                        }
                    }
        }
    }

    static String matchParens(String inputString){
        int openCount = 0;
        int closeCount = 0;
        StringBuilder mySB = new StringBuilder();
        for(int i = 0; i < inputString.length(); i++){
            if(inputString.charAt(i) == '('){
                openCount++;
            }else if(inputString.charAt(i) == ')'){
                closeCount++;
            }
        }
                if(openCount > closeCount){
                    while(openCount!=closeCount){
                        mySB.append(")");
                        closeCount++;
                    }
                    inputString = inputString + mySB.toString();
                }else if(openCount < closeCount){
                    while(openCount != closeCount){
                        mySB.append("(");
                        openCount++;
                    }
                inputString = mySB.toString() + inputString;
                }
        return inputString;
    }


static String fillInLast(String inputString){
    if(inputString.charAt(inputString.length()-1) == ('*') || inputString.charAt(inputString.length()-1) == ('/')){
        inputString = inputString.concat("1");
    }
    if(inputString.charAt(inputString.length()-1) == ('-') || inputString.charAt(inputString.length()-1) == ('+')){
        inputString = inputString.concat("0");
    }
    return inputString;
}

    static boolean isLastCharOperand(char c){
        return (c == '*'|| c == '/'|| c == '+' || c == '-');
    }


    static String makeNice(String inputString){
        String toReturn = inputString;
        for (int i = 1; i <toReturn.length() - 1 ; i++) {
            if(toReturn.charAt(i) == '('){
                if (Character.isDigit(toReturn.charAt(i -1))) {
                    toReturn = toReturn.substring(0,i) + "*" + toReturn.substring(i,toReturn.length());
                }
            }
            if(toReturn.charAt(i) == ')' && (i != toReturn.length()-1)){
                if (Character.isDigit(toReturn.charAt(i + 1))) {
                    toReturn = toReturn.substring(0,i + 1) + "*" + toReturn.substring(i + 1,toReturn.length());
                }
            }


        }
        return toReturn;
    }


}


/* EXCEPTIONS TO HANDLE~~~~~~~~~~~

 IF userinput(i) == ('*' || '/') DISABLE '*', '/', '+' SOLVED


 ************** HARD LOGIC ***********************
 *                                               *
 *  IF USER LAST INPUT NUMBER CONTAINS A DECIMAL *
 *  DISABLE '.' FROM BEING PRESSED IT WILL BREAK *
 *                  STUFF                        *
 *  **********************************************



 if (!inputString.contains(operand)){     ~~ if user has not input an operand/trig/factorial disable equal button.
        DISABLE EQUALS BUTTON
  }
  ^^^ SOLVED ^^^^


  if (userInput(userInput.length()-1) == '0'){
        DISABLE 0 button.
   }IRRELEVANT



   if (userInput(i) == '/' && (number following) ('/') == 0){
    answer == INFINITY.
    }



WHEN ORIENTATION == PORTRAIT




 if (!inputString.contains(operand)){     ~~ if user has not input an operand disable equal button.
        DISABLE EQUALS BUTTON
  }

 IF userinput(i) == '='{
        if inputString(inputString.charAt(inputString.length()-1) == ('+' || '-'){
            inputString.append('0')    ~~~ add or subtract zero to end of string.
            ~~ calculate ~~
        else if inputString(inputString.charAt(inputString.length()-1)) == ('*' || '/'){
        inputString.append('1')     ~~~~~ divide or multiply one resulting in same number.
       ~~~ calculate ~~~
}
~~~~~~~~~ POSSIBLY CHANGE DELETE BUTTON INTO CLEAR ~~~~~~~~~~~






    static String makeNice(String inputString){
        String toReturn = inputString;
        for (int i = 1; i <toReturn.length() - 1 ; i++) {
            if(toReturn.charAt(i) == '('){
                if (Character.isDigit(toReturn.charAt(i -1))) {
                    toReturn = toReturn.substring(0,i) + "*" + toReturn.substring(i,toReturn.length());
                }
            }
            if(toReturn.charAt(i) == ')' && (i != toReturn.length()-1)){
                if (Character.isDigit(toReturn.charAt(i + 1))) {
                    toReturn = toReturn.substring(0,i + 1) + "*" + toReturn.substring(i + 1,toReturn.length());
                }
            }


        }
        return toReturn;
    }

    static String exponent(String a, String b, String isANegative, String isBNegative){
        boolean isANeg = Boolean.getBoolean(isANegative);
        boolean isBNeg = Boolean.getBoolean(isBNegative);
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANeg){
            numa = numa * -1;
        }
        if(isBNeg){
            numb = numb * -1;
        }
        double result = Math.pow(numa,numb);
        return Double.toString(result);
    }

    static String multiply(String a, String b, String isANegative, String isBNegative){
        boolean isANeg = Boolean.getBoolean(isANegative);
        boolean isBNeg = Boolean.getBoolean(isBNegative);
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANeg){
            numa = numa * -1;
        }
        if(isBNeg){
            numb = numb * -1;
        }
        double result = numa * numb;
        return Double.toString(result);
    }

    static String divide(String a, String b, boolean isANegative, boolean isBNegative){
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANegative){
            numa = numa * -1;
        }
        if(isBNegative){
            numb = numb * -1;
        }
        double result = numa/numb;
        return Double.toString(result);
    }

    static String add(String a, String b, String isANegative, String isBNegative){
        boolean isANeg = Boolean.getBoolean(isANegative);
        boolean isBNeg = Boolean.getBoolean(isBNegative);
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANeg){
            numa = numa * -1;
        }
        if(isBNeg){
            numb = numb * -1;
        }
        double result = numa + numb;
        return Double.toString(result);
    }

    static String subtract(String a, String b, String isANegative, String isBNegative){
        if( a.equalsIgnoreCase("")){
            double numb = Double.parseDouble(b);
            return Double.toString(0 - numb);
        }
        boolean isANeg = Boolean.getBoolean(isANegative);
        boolean isBNeg = Boolean.getBoolean(isBNegative);
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANeg){
            numa = numa * -1;
        }
        if(isBNeg){
            numb = numb * -1;
        }
        double result = numa - numb;
        return Double.toString(result);
    }

    static String exp(String a,String b, String isANegative, String isBNegative){
        boolean isANeg = Boolean.getBoolean(isANegative);
        boolean isBNeg = Boolean.getBoolean(isBNegative);
        double numa = Double.parseDouble(a);
        double numb = Double.parseDouble(b);
        if(isANeg){
            numa = numa * -1;
        }
        if(isBNeg){
            numb = numb * -1;
        }
        double result = numa * Math.pow(10, numb);
        return Double.toString(result);
    }


    static String sine(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }

        double result = Math.sin(numa);
        return Double.toString(result);
    }

    static String cosine(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }
        double result = Math.cos(numa);
        return Double.toString(result);
    }

    static String tangent(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }
        double result = Math.tan(numa);
        return Double.toString(result);
    }

    static String arcSine(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }
        double result = Math.asin(numa);
        return Double.toString(result);
    }

    static String arcCosine(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }
        double result = Math.acos(numa);
        return Double.toString(result);
    }

    static String arcTangent(String a, String isANegative){
        double numa = Double.parseDouble(a);
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            numa = numa * -1;
        }
        double result = Math.atan(numa);
        return Double.toString(result);
    }

    static String pI(String isANegative){
        double result = Math.PI;
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            result = result * -1;
        }
        return Double.toString(result);
    }

    static String euler(String isANegative){
        double result = Math.E;
        boolean isANeg = Boolean.getBoolean(isANegative);
        if(isANeg){
            result = result * -1;
        }
        return Double.toString(result);
    }

    static String calculate(String inputString) {
        String exponent = "^";
        String multiplication = "*";
        String divide = "/";
        String add = "+";
        String subtract = "-";
        String openParentheses = "(";
        String closeParentheses = ")";
        String sin = "sin";
        String cos = "cos";
        String tan = "tan";
        String arcSin = "arcsin";
        String arcCos = "arccos";
        String arcTan = "arctan";
        String pi = "pi";
        String e = "e";
        String exp = "E";
        String fact = "!";

        if (inputString.contains(openParentheses)){

            String toReplace = "(" + flattenParenthese(inputString) + ")";
            String replacement = calculate(flattenParenthese(inputString));
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);
        }else if(inputString.contains(exponent)){
            String[] thisCalculation = getOperandsArray(inputString,exponent);
            int start = Integer.parseInt(thisCalculation[2]);
            int end = Integer.parseInt(thisCalculation[3]);
            String toReplace = inputString.substring(start,end);
            String replacement = exponent(thisCalculation[0],thisCalculation[1], "false", "false");
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);

        }else if(inputString.contains(multiplication)){
            String[] thisCalculation = getOperandsArray(inputString,multiplication);
            int start = Integer.parseInt(thisCalculation[2]);
            int end = Integer.parseInt(thisCalculation[3]);
            String toReplace = inputString.substring(start,end);
            String replacement = multiply(thisCalculation[0],thisCalculation[1], "false", "false");
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);

        }else if(inputString.contains(divide)){
            String[] thisCalculation = getOperandsArray(inputString,divide);
            int start = Integer.parseInt(thisCalculation[2]);
            int end = Integer.parseInt(thisCalculation[3]);
            String toReplace = inputString.substring(start,end);
            String replacement = divide(thisCalculation[0],thisCalculation[1], false , false);
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);

        }else if(inputString.contains(add)){
            String[] thisCalculation = getOperandsArray(inputString,add);
            int start = Integer.parseInt(thisCalculation[2]);
            int end = Integer.parseInt(thisCalculation[3]);
            String toReplace = inputString.substring(start,end);
            String replacement = add(thisCalculation[0],thisCalculation[1], "false", "false");
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);
        }else if(inputString.contains(subtract)){
            String[] thisCalculation = getOperandsArray(inputString,subtract);
            int start = Integer.parseInt(thisCalculation[2]);
            int end = Integer.parseInt(thisCalculation[3]);
            String toReplace = inputString.substring(start,end);
            String replacement = subtract(thisCalculation[0],thisCalculation[1], "false", "false");
            String operatedString = inputString.replace(toReplace,replacement);
            return calculate(operatedString);
        }
        return inputString;
    }

    private static String flattenParenthese(String inputString){
        int start = 0;
        int end = inputString.length();
        if(inputString.contains("(")){
            start = inputString.indexOf("(") + 1;
            for (int i = start; i < inputString.length() ; i++) {
                if(inputString.charAt(i) == '('){
                    return flattenParenthese(inputString.substring(i,inputString.length()));
                }else if (inputString.charAt(i) == ')'){
                    end = i;
                    break;
                }
            }
        }
        return inputString.substring(start, end);
    }

    static boolean hasOperand(String substring){
        if(substring.contains("^") || substring.contains("*") || substring.contains("/") || substring.contains("+")
                || substring.contains("-") ){
            return true;
        }
        return false;
    }

    static String[] getOperandsArray(String inputString, String operator){
        String[] operationArray = new String[4];
        operationArray[2] = operator;
        int operatorIndex = inputString.indexOf(operator);
        int start = 0;
        int endExclusive = 0;
        boolean isFirstNumberNegative = false;
        boolean isSecondNumberNegative = false;
        for (int i = operatorIndex - 1; i >= 0; i--) {
            if(i == 0) {
                start = 0;
                break;
            }else if (hasOperand(inputString.substring(i, operatorIndex))) {
                if(inputString.charAt(i) == '-'){
                    if(Character.isDigit(inputString.charAt( i - 1))){
                    }else{
                        start = i;
                        break;
                    }
                }
                else {
                    start = i + 1;
                    break;
                }
            }
        }

        for (int i = operatorIndex + 1; i < inputString.length() ; i++) {
            if(hasOperand(inputString.substring(operatorIndex + 1,i))){
                endExclusive = i - 1;
                break;
            } else if( i == inputString.length() - 1){
                endExclusive = inputString.length();
            }
        }
        operationArray[0] = inputString.substring(start, operatorIndex);
        operationArray[1] = inputString.substring(operatorIndex + 1,endExclusive);
        operationArray[2] = Integer.toString(start);
        operationArray[3] = Integer.toString(endExclusive);

        return operationArray;
    }



 */