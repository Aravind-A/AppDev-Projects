package com.example.aravind.quicknotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private int mAnimOption;
    private SharedPreferences mPrefs;
    Animation mAnimFlash;
    Animation mFadeIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        listNote.setLongClickable(true);
        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mNoteAdapter.deleteNote(position);
                return true;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                Note temp = (Note) mNoteAdapter.getItem(whichItem);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(temp);
                dialog.show(getFragmentManager(), "123");
            }
        });
    }

    public void createNewNote(Note n){
        mNoteAdapter.addNote(n);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPrefs = getSharedPreferences("Quick Notes", MODE_PRIVATE);
        mAnimOption = mPrefs.getInt("anim option", SettingsActivity.NONE);

        mAnimFlash = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        if(mAnimOption == SettingsActivity.FAST)
            mAnimFlash.setDuration(100);
        else if(mAnimOption == SettingsActivity.SLOW)
            mAnimFlash.setDuration(1000);
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "123");
            return true;
        }
        if(id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class NoteAdapter extends BaseAdapter {
        private JSONSerializer mSerializer;
        List<Note> noteList = new ArrayList<Note>();

        public NoteAdapter() {
            mSerializer = new JSONSerializer("QuickNotes.json", MainActivity.this.getApplicationContext());
            try {
                noteList = mSerializer.load();
            } catch (Exception e) {
                noteList = new ArrayList<Note>();
            }
        }

        public void saveNotes() {
            try {
                mSerializer.save(noteList);
            }
            catch (Exception e){
                Log.i("Error","while saving note");
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int position) {
            return noteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, viewGroup, false);
            }
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            Note tempNote = noteList.get(whichItem);
            if(tempNote.isImportant() && mAnimOption != SettingsActivity.NONE)
                view.setAnimation(mAnimFlash);
            else
                view.setAnimation(mFadeIn);
            txtTitle.setText(tempNote.getTitle());
            return view;
        }
        public void addNote(Note n){
            noteList.add(n);
            notifyDataSetChanged();
        }
        public void deleteNote(int n){
            noteList.remove(n);
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mNoteAdapter.saveNotes();
    }
}
