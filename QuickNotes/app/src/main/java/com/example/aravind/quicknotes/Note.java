package com.example.aravind.quicknotes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aravind on 29/3/16.
 */
public class Note {
    private String mTitle;
    private String mDescription;
    private boolean mIdea;
    private boolean mImportant;

    public Note(JSONObject jo) throws JSONException{
        mTitle = jo.getString("title");
        mDescription = jo.getString("description");
        mIdea = jo.getBoolean("idea");
        mImportant = jo.getBoolean("important");
    }
    public Note(){};

    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();
        jo.put("title",mTitle);
        jo.put("description",mDescription);
        jo.put("idea",mIdea);
        jo.put("important",mImportant);
        return jo;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isIdea() {
        return mIdea;
    }

    public void setIdea(boolean mIdea) {
        this.mIdea = mIdea;
    }


    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean mImportant) {
        this.mImportant = mImportant;
    }
}

