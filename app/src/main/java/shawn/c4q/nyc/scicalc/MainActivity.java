package shawn.c4q.nyc.scicalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG TOOL";
    StringBuilder myBuiltStr = new StringBuilder();
    String tempStr;
    TextView inputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputTextView = (TextView) findViewById(R.id.edit_text_id);
        inputTextView.setKeyListener(null);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);

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
        }else{
            String buttonPressedStrForm = button.getText().toString();
            myBuiltStr.append(buttonPressedStrForm);
            tempStr = myBuiltStr.toString();
            Log.d(TAG, tempStr);
            inputTextView.setText(tempStr);
        }
    }
}
