package com.example.aravind.audiorecorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import android.os.Handler;

/**
 * Created by aravind on 24/4/16.
 */
public class Player extends DialogFragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private MediaPlayer mediaPlayer;
    long startTime,timeInMilliseconds,timeSwapBuff,updatedTime,totalDuration,currentDuration;
    int secs,mins,milliseconds;
    TextView curTime;
    Handler handler;
    Utilities utils;
    SeekBar seekBar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        super.onCreateDialog(savedInstanceState);
        String name = getArguments().getString("playName");
        String time = getArguments().getString("playTime");
        String file = getArguments().getString("playFile");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.player, null);

        builder.setView(v);

        TextView txtName = (TextView) v.findViewById(R.id.name);
        curTime = (TextView) v.findViewById(R.id.curTime);
        TextView totTime = (TextView) v.findViewById(R.id.totTime);
        final ImageView playPause = (ImageView) v.findViewById(R.id.playPause);
        ImageView stop = (ImageView) v.findViewById(R.id.stop);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);

        startTime = 0L; timeInMilliseconds = 0L; timeSwapBuff = 0L;
        updatedTime = 0L; secs = 0; mins = 0; milliseconds = 0;
        handler = new Handler();
        utils = new Utilities();
        playPause.setTag(R.drawable.ic_pause_circle_outline_black_24dp);
        txtName.setText(name);
        totTime.setText(time);
        curTime.setText("00:00:00");
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimer, 0);

        if(mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        try {
            mediaPlayer.setDataSource(file);
        } catch (IOException e) {
            Log.i("Stack : ", "setDataSource() failed");
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.i("Stack : ", "prepare() failed");
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                totalDuration = mp.getDuration();
                currentDuration = mp.getCurrentPosition();
            }
        });
        Toast.makeText(getActivity().getBaseContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
        seekBar.setProgress(0);
        seekBar.setMax(100);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPause.getTag().equals(R.drawable.ic_pause_circle_outline_black_24dp)) {
                    mediaPlayer.pause();
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    playPause.setTag(R.drawable.ic_play_circle_outline_black_24dp);
                    playPause.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                } else if (playPause.getTag().equals(R.drawable.ic_play_circle_outline_black_24dp)) {
                    mediaPlayer.start();
                    updateProgressBar();
                    playPause.setTag(R.drawable.ic_pause_circle_outline_black_24dp);
                    playPause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(updateTimer);
                mediaPlayer.stop();
                mediaPlayer.release(); mediaPlayer = null;
                dismiss();
            }
        });



        return builder.create();
    }
    public Runnable updateTimer = new Runnable() {
        public void run() {
            /*timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedTime % 1000);
            curTime.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);*/
            int progress;
            if (mediaPlayer != null){
                currentDuration = mediaPlayer.getCurrentPosition();
                curTime.setText("" + utils.milliSecondsToTimer(currentDuration));
                progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                seekBar.setProgress(progress);
                handler.postDelayed(this, 100);
            }
        }};

    public void updateProgressBar() {
        handler.postDelayed(updateTimer, 100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimer);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimer);
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), (int) totalDuration);
        mediaPlayer.seekTo(currentPosition);
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        dismiss();
    }
    @Override
   public void onPause(){
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.release(); mediaPlayer = null;
        }
    }
}
