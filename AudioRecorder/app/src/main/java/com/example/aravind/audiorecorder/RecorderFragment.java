package com.example.aravind.audiorecorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aravind on 11/4/16.
 */
public class RecorderFragment extends Fragment {
    TextView time;
    long startTime,timeInMilliseconds,timeSwapBuff,updatedTime;
    int secs,mins,milliseconds;
    Handler handler;
    Calendar c;
    SimpleDateFormat df;
    String formattedDate,storageDate;
    String outputFile;
    MediaRecorder mediaRecorder = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recorder, container, false);
        startTime = 0L; timeInMilliseconds = 0L; timeSwapBuff = 0L;
        updatedTime = 0L; secs = 0; mins = 0; milliseconds = 0;
        handler = new Handler();
        final ImageView record = (ImageView) v.findViewById(R.id.record);
        time = (TextView) v.findViewById(R.id.time);

        if(mediaRecorder != null) mediaRecorder.release();
        mediaRecorder = new MediaRecorder();
        record.setTag(R.drawable.mic_icon);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (record.getTag().equals(R.drawable.mic_icon)) {
                    if(mediaRecorder == null) mediaRecorder = new MediaRecorder();

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

                    /*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings/voice recordings/");
                    if(!file.exists())
                        file.mkdirs();
                    Log.i("file path : ",file.getAbsolutePath());*/
                    c = Calendar.getInstance();
                    df  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    formattedDate = df.format(c.getTime());
                    storageDate = formattedDate.replaceAll(" ","_");
                    storageDate = storageDate.replaceAll("-","_");
                    storageDate = storageDate.replaceAll(":","_");

                    Log.i("Storage date : ",storageDate);
                    outputFile  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AR" + storageDate + ".3gp";
                    mediaRecorder.setOutputFile(outputFile);
                    Log.i("Recorder : ",outputFile);
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    try {
                        mediaRecorder.prepare(); mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace(); Log.i("error", "IOexception");
                    } catch (IllegalStateException e) {
                        e.printStackTrace(); Log.i("error", "IllegalStateException");
                    }
                    Toast.makeText(getActivity().getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();
                }
                /*  IF PAUSE FEATURE IS THERE :
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer); */
                else {
                    DataManager dm = new DataManager(getContext());
                    String length = (String) time.getText();

                    startTime = 0L; timeInMilliseconds = 0L; timeSwapBuff = 0L;
                    updatedTime = 0L; secs = 0; mins = 0; milliseconds = 0;

                    handler.removeCallbacks(updateTimer);
                    time.setText("00:00:00");
                    mediaRecorder.stop(); mediaRecorder.reset();
                    Toast.makeText(getActivity().getBaseContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
                    dm.insert("Audio Recording " + formattedDate,outputFile,length,formattedDate);

                }
                if (record.getTag().equals(R.drawable.mic_icon)) {
                    record.setTag(R.drawable.stop);
                    record.setImageResource(R.drawable.stop);
                } else {
                    record.setTag(R.drawable.mic_icon);
                    record.setImageResource(R.drawable.mic_icon);
                }

            }
        });
        return v;
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedTime % 1000);
            time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }};

    public void onPause(){
        super.onPause();
        if(mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
