package com.example.aravind.quicknotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private int mAnimOption;

    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPrefs = getSharedPreferences("Quick Notes", MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mAnimOption = mPrefs.getInt("anim option", NONE);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.clearCheck();

        switch (mAnimOption){
            case FAST:  rg.check(R.id.radioFast); break;
            case SLOW:  rg.check(R.id.radioSlow); break;
            case NONE:  rg.check(R.id.radioNone); break;
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              RadioButton rb = (RadioButton) findViewById(checkedId);
                                              if (rb != null) {
                                                  switch (rb.getId()) {
                                                      case R.id.radioFast:
                                                          mAnimOption = FAST;
                                                          break;
                                                      case R.id.radioSlow:
                                                          mAnimOption = SLOW;
                                                          break;
                                                      case R.id.radioNone:
                                                          mAnimOption = NONE;
                                                          break;
                                                      default:
                                                          mAnimOption = -1;
                                                  }
                                                  mEditor.putInt("anim option", mAnimOption);
                                              }
                                          }
                                      });
    }
    @Override
    protected void onPause(){
        super.onPause();
        mEditor.commit();
    }
}
