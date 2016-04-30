package com.example.aravind.audiorecorder;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by aravind on 11/4/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int numTabs;
    public PagerAdapter(FragmentManager fm,int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :    RecorderFragment tab1 = new RecorderFragment();
                        return tab1;
            case 1 :    RecordingsListFragment tab2 = new RecordingsListFragment();
                        return tab2;
            default:    return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
