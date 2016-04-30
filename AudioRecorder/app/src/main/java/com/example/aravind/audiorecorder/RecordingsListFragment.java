package com.example.aravind.audiorecorder;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by aravind on 11/4/16.
 */
public class RecordingsListFragment extends android.support.v4.app.Fragment {

    private RecordingsAdapter mAdapter;
    TextView heading;
    //MediaPlayer mediaPlayer = null;
    ListView listView = null;
    private int deletePos,editPos;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.recordings,container,false);
        listView = (ListView) v.findViewById(R.id.listView);
        heading = (TextView) v.findViewById(R.id.heading);
        mAdapter = new RecordingsAdapter();
        listView.setAdapter(mAdapter);
        registerForContextMenu(listView);
        deletePos = -1; editPos = -1;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*if (mediaPlayer != null) mediaPlayer.release();
                mediaPlayer = new MediaPlayer();*/

                Bundle play = new Bundle();
                AudioRecord temp = (AudioRecord) mAdapter.getItem(position);
                play.putString("playName", temp.getName());
                play.putString("playTime", temp.getLength());
                play.putString("playFile", temp.getOutputFile());

                Player player = new Player();
                player.setArguments(play);
                player.show(getActivity().getSupportFragmentManager(),"12");

                /*String outputFile = temp.getOutputFile();
                try {
                    mediaPlayer.setDataSource(outputFile);
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
                    }
                });
                Toast.makeText(getActivity().getBaseContext(), "Playing Audio", Toast.LENGTH_SHORT).show();*/
            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible() || this.isResumed()){
            if(listView != null){
                mAdapter = new RecordingsAdapter();
                listView.setAdapter(mAdapter);
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.options, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit :        editPos = info.position;
                                    Bundle args = new Bundle();
                                    args.putString("editName",((AudioRecord) mAdapter.getItem(editPos)).getName());
                                    EditDialog editDialog = new EditDialog();
                                    editDialog.setArguments(args);
                                    editDialog.setTargetFragment(this, 1);
                                    editDialog.show(getActivity().getSupportFragmentManager(),"1234");
                                    return true;

            case R.id.share :       Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("audio/*");
                                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(((AudioRecord) mAdapter.getItem(info.position)).getOutputFile()));
                                    startActivity(Intent.createChooser(share, "Share '" + ((AudioRecord) mAdapter.getItem(info.position)).getName() + "'"));
                                    return true;

            case R.id.delete :      deletePos = info.position;
                                    Bundle delArgs = new Bundle();
                                    delArgs.putString("delName",((AudioRecord) mAdapter.getItem(deletePos)).getName());
                                    DeleteDialog dialog = new DeleteDialog();
                                    dialog.setArguments(delArgs);
                                    dialog.setTargetFragment(this,0);
                                    dialog.show(getActivity().getSupportFragmentManager(),"123");
                                    return true;

            default: return super.onContextItemSelected(item);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && data.getIntExtra("choice",-1) == 1){
            if(deletePos != -1){
                DataManager dm = new DataManager(getActivity().getBaseContext());
                AudioRecord ar = (AudioRecord) mAdapter.getItem(deletePos);
                dm.delete(ar.getName());
                File file = new File(ar.getOutputFile());
                if(file.exists())
                    file.delete();
                deletePos = -1;
                if(listView != null){
                    mAdapter = new RecordingsAdapter();
                    listView.setAdapter(mAdapter);
                }
                Toast.makeText(getActivity().getApplicationContext(),"Deleted successfully !",Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 1 && data.getIntExtra("editChoice",-1) == 1){
            if (editPos != -1) {
                DataManager dm = new DataManager(getActivity().getBaseContext());
                dm.update(((AudioRecord) mAdapter.getItem(editPos)).getOutputFile(),data.getStringExtra("newName"));
                editPos = -1;
                if(listView != null){
                    mAdapter = new RecordingsAdapter();
                    listView.setAdapter(mAdapter);
                }
                Toast.makeText(getActivity().getApplicationContext(),"Updated successfully !",Toast.LENGTH_SHORT).show();
            }
        }
        editPos = -1;
        deletePos = -1;
    }
    public void onEditResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && data.getIntExtra("choice",-1) == 1){
            if(editPos != -1){
                DataManager dm = new DataManager(getActivity().getBaseContext());

            }
        }
    }

    public class RecordingsAdapter extends BaseAdapter{
        ArrayList<AudioRecord> records;
        DataManager dm;
        public RecordingsAdapter(){
            records = new ArrayList<AudioRecord>();
            dm = new DataManager(getContext());
            Cursor c = dm.selectAll();
            AudioRecord temp;
            if(c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    temp = new AudioRecord(c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    records.add(temp);
                    c.moveToNext();
                }
            }
            if(records.size() == 0)
                heading.setText("No Recordings yet!");
            else
                heading.setText("Your Recordings");
        }

        @Override
        public int getCount() {
            return records.size();
        }

        @Override
        public Object getItem(int position) {
            return records.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup parent) {
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, parent, false);
            }
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView length = (TextView) view.findViewById(R.id.length);
            TextView created = (TextView) view.findViewById(R.id.created);
            AudioRecord temp = records.get(whichItem);
            name.setText(temp.getName());
            length.setText(temp.getLength());
            created.setText(temp.getCreated());
            return view;
        }
    }
}
