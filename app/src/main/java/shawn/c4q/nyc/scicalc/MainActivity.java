package shawn.c4q.nyc.scicalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG TOOL";
    StringBuilder myBuiltStr = new StringBuilder();
    String tempStr;
    TextView inputTextView;
    public static ArrayList<Double> myNumList = new ArrayList<>();
    public static ArrayList<String>  myOppList = new ArrayList<>();
    private static final String myPlaceHolder = "random_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputTextView = (TextView) findViewById(R.id.edit_text_id);
        inputTextView.setKeyListener(null);
        inputTextView.setText(myBuiltStr.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
        if(savedInstanceState != null){
            myBuiltStr = (StringBuilder) savedInstanceState.getCharSequence(myPlaceHolder);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(myPlaceHolder, myBuiltStr);
    }


    public void buttonOnClick(View view){
        int buttonID = view.getId();
        Button button = (Button) view;
        if (buttonID == (R.id.portrait_delete)){
            if (myBuiltStr.length() > 0) {
                myBuiltStr.deleteCharAt(myBuiltStr.length() - 1);
            }
            tempStr = myBuiltStr.toString();
            Log.d(TAG, tempStr);
            inputTextView.setText(tempStr);
        }else if(buttonID == (R.id.portrait_equals)){
            parseString(tempStr);
            inputTextView.setText(String.valueOf(evalAndPemdas()));
            tempStr = String.valueOf(evalAndPemdas());
            StringBuilder tempSB = new StringBuilder();
            myBuiltStr = tempSB.append(tempStr);
            myNumList.clear();
            myOppList.clear();
        }
        else{
            String buttonPressedStrForm = button.getText().toString();
            myBuiltStr.append(buttonPressedStrForm);
            tempStr = myBuiltStr.toString();
            Log.d(TAG, tempStr);
            inputTextView.setText(tempStr);
        }
    }

    public static double evalAndPemdas() {
        while (!myOppList.isEmpty()) {

            while(listContainsMultOrDiv(myOppList)) {
                for(int i = 0; i < myOppList.size(); i++) {
                    getOperation2(i);
                }
            }

            while(listContainsAddOrSub(myOppList)) {
                for (int i = 0; i < myOppList.size(); i++) {
                    getOperation3(i);
                }
            }
        }
        return myNumList.get(0);
    }

    public static boolean isOperand (char aChar){
        return (aChar == 42 ||aChar == 43 ||aChar == 45 ||aChar == 47);
    }

    public static boolean isDigitOrDecimal (char aChar){
        return ((aChar > 47 && aChar < 58) || aChar == 46);
    }

    public static void parseString (String bString) {
        String tempString = "";
        for (int z = 0; z < bString.length(); z++) {

            if (isDigitOrDecimal(bString.charAt(z))){

                tempString = tempString.concat("" + bString.charAt(z));
                if(z == bString.length()-1){
                    myNumList.add(Double.valueOf(tempString));
                }

            }else if (isOperand(bString.charAt(z))) {
                myNumList.add(Double.valueOf(tempString));
                myOppList.add("" + bString.charAt(z));
                tempString = "";
            }
        }
    }
    public static void getOperation2(int i){

        switch(myOppList.get(i)){

            case "/": Double tempDiv = divide(myNumList.get(i), myNumList.get(i+1));
                myNumList.add(i, tempDiv);
                myNumList.remove(i+1);
                myNumList.remove(i+1);
                myOppList.remove(i);
                break;

            case "*":  Double tempMult = multiply(myNumList.get(i), myNumList.get(i+1));
                myNumList.add(i, tempMult);
                myNumList.remove(i+1);
                myNumList.remove(i+1);
                myOppList.remove(i);
                break;
            default:
                break;
        }
    }

    public static void getOperation3(int i){

        switch(myOppList.get(i)){

            case "-": Double tempSub = subtract(myNumList.get(i), myNumList.get(i+1));
                myNumList.add(i, tempSub);
                myNumList.remove(i+1);
                myNumList.remove(i+1);
                myOppList.remove(i);
                break;

            case "+":  Double tempAdd = add(myNumList.get(i), myNumList.get(i+1));
                myNumList.add(i, tempAdd);
                myNumList.remove(i+1);
                myNumList.remove(i+1);
                myOppList.remove(i);
                break;
            default:
                break;
        }
    }

    public static boolean findMultOrDiv(String xString){
        return (xString.equals("*") || xString.equals("/"));
    }
    public static boolean findAddorSub(String yString){
        return (yString.equals("+") || yString.equals("-"));
    }

    public static boolean listContainsMultOrDiv(ArrayList<String> arraylist){
        return arraylist.contains("*") || arraylist.contains("/");
    }
    public static boolean listContainsAddOrSub(ArrayList<String> arrayList) {
        return arrayList.contains("+") || arrayList.contains("-");
    }

    public static double getOperation(ArrayList<String> arrayListA, int i, ArrayList<Double> arrayListB){

        switch(arrayListA.get(i)){
            case "+":  return add(arrayListB.get(i), arrayListB.get(i+1));
            case "-": return subtract(arrayListB.get(i), arrayListB.get(i+1));
            case "/": return divide(arrayListB.get(i), arrayListB.get(i+1));
            case "*": return multiply(arrayListB.get(i), arrayListB.get(i+1));
        }
        return 0;
    }

    public static double add (double a, double b){
        return a + b;
    }

    public static double subtract(double a, double b){
        return a - b;
    }

    public static double divide (double a, double b){
        return a/b;
    }

    public static double multiply (double a, double b){
        return a * b;
    }
}
