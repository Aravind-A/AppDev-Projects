package com.example.aravind.cowbull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mPref;
    int highScore;
    TextView curBest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        curBest = (TextView) findViewById(R.id.curBest);
        mPref = getSharedPreferences("Cowbull", MODE_PRIVATE);
        highScore = mPref.getInt("highscore",-1);
        if(highScore == -1)
            curBest.setText("Current Best : N/A");
        else
            curBest.setText("Current Best : " + highScore + " attempts");
        curBest.setTextColor(Color.YELLOW);
        curBest.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
    }

    @Override
    protected void onResume(){
        super.onResume();
        curBest = (TextView) findViewById(R.id.curBest);
        mPref = getSharedPreferences("Cowbull", MODE_PRIVATE);
        highScore = mPref.getInt("highscore",-1);
        if(highScore == -1)
            curBest.setText("Current Best : N/A");
        else
            curBest.setText("Current Best : " + highScore + " attempts");
        curBest.setTextColor(Color.YELLOW);
        curBest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rules) {
            Intent rules = new Intent(this,Rules.class);
            startActivity(rules);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onPlay(View v){
        Intent intent = new Intent(this,GamePage.class);
        startActivity(intent);
    }
}
