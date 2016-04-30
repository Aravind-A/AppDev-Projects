package com.example.aravind.audiorecorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aravind on 20/4/16.
 */
public class DataManager {
    private SQLiteDatabase db;
    public DataManager(Context context){
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    public void insert(String name,String outputFile,String length,String created){

        String query = "INSERT INTO recordings (name,outputFile,length,created) VALUES ('" + name
                        + "','" + outputFile + "','" + length + "','" + created + "');";
        Log.i("insert() = ",query);
        db.execSQL(query);
    }
    public void delete(String name){

        String query = "DELETE FROM recordings WHERE name='" + name + "';";
        Log.i("delete() = ",query);
        db.execSQL(query);
    }
    public void update(String outputFile,String newName){
        String query = "UPDATE recordings SET name='" + newName + "' WHERE outputFile='" + outputFile + "';";
        Log.i("update() = ",query);
        db.execSQL(query);
    }
    public Cursor selectAll() {

        Cursor c = db.rawQuery("SELECT * FROM recordings", null);
        return c;
    }


    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context,"audio_recorder",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        String str = "create table recordings ( _id integer primary key autoincrement not null," +
                " name text not null, outputFile text not null, length text not null, created text);";
        db.execSQL(str);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
