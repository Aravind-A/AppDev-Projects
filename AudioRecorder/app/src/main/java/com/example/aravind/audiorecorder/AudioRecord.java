package com.example.aravind.audiorecorder;

/**
 * Created by aravind on 20/4/16.
 */
public class AudioRecord {
    private String name,outputFile,length,created;

    public AudioRecord(String name,String outputFile,String length,String created){
        this.name = name;
        this.outputFile = outputFile;
        this.length = length;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getLength() {
        return length;
    }

    public String getCreated() {
        return created;
    }

    public void setName(String name) {
        this.name = name;
    }
}
