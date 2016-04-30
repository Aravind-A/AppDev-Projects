package com.example.aravind.cowbull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aravind on 8/4/16.
 */
public class GamePage extends AppCompatActivity {
    ArrayList<String> words;
    String curWord;
    int numAttempts;
    TextView attempts;
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepage);

        mPref = getSharedPreferences("Cowbull",MODE_PRIVATE);
        words = new ArrayList<String>();
        addWords();
        Random r = new Random();
        int pos = r.nextInt(10);
        curWord = words.get(pos);
        numAttempts = 0;
        TextView wordLength = (TextView) findViewById(R.id.wordLength);
        attempts = (TextView) findViewById(R.id.numAttempts);
        wordLength.setText("The word length is " + curWord.length());
        attempts.setText("Attempts made : " + numAttempts);

    }
    public void addWords(){
        words.add(0, "spider");
        words.add(1, "android");
        words.add(2,"google");
        words.add(3, "facebook");
        words.add(4, "java");
        words.add(5,"amazon");
        words.add(6,"marshmallow");
        words.add(7,"lollipop");
        words.add(8,"kitkat");
        words.add(9,"whatsapp");

    }
    public void checkAnswer(View view){
        TextView response = new TextView(this);
        Button checkButton = (Button) findViewById(R.id.checkButton);
        EditText answer = (EditText) findViewById(R.id.editText);
        String ans = answer.getText().toString();
        answer.setText("");
        if(ans.length() == 0) {
            Toast.makeText(this, "You've not guessed any word!", Toast.LENGTH_SHORT).show();
        }
        else if(ans.equalsIgnoreCase(curWord)){
            numAttempts++;
            response.setText("Bulls : " + curWord.length() + ". You win!");
            response.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            response.setTextColor(Color.GREEN);
            response.setPadding(10,10,10,10);
            checkButton.setEnabled(false);
            answer.setEnabled(false);
            int prevBest = mPref.getInt("highscore",-1);
            if(prevBest == -1 || numAttempts < prevBest){
                mEditor = mPref.edit();
                mEditor.putInt("highscore",numAttempts);
                mEditor.commit();
                Toast.makeText(this,"New Record!",Toast.LENGTH_SHORT).show();
            }


        }
        else{
            int bulls = 0,cows = 0;
            numAttempts++;
            ArrayList<Integer> flag = new ArrayList<Integer>();
            for(int i= 0;i<curWord.length();i++)
                flag.add(i,0);
            for(int i=0;i<curWord.length() && i<ans.length();i++){
                if(curWord.charAt(i) == ans.charAt(i))
                    bulls++;
                else {
                    for(int j=0;j<curWord.length();j++){
                        if(flag.get(j) == 0 && ans.charAt(i) == curWord.charAt(j)){
                            cows++;
                            flag.add(j,1);
                        }
                    }
                }
            }
            response.setText(ans +"        Cows : " + cows + " Bulls : " + bulls);
            response.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            response.setTextColor(Color.BLACK);
            response.setPadding(10,10,10,10);
        }
        attempts.setText("Attempts made : " + numAttempts);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.addView(response);
    }
}
