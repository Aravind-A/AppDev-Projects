package com.example.aravind.currencyconverter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String urlString = "http://api.fixer.io/latest?base=";
    private String toCurrency = "";
    private String fromCurrency = "";
    private TextView answer;
    Double val = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        answer = (TextView) findViewById(R.id.answer);

        Spinner from_spinner = (Spinner) findViewById(R.id.fromSpinner);
        from_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> from_adapter = ArrayAdapter.createFromResource(this,R.array.from_array,R.layout.support_simple_spinner_dropdown_item);
        from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from_spinner.setAdapter(from_adapter);

        Spinner to_spinner = (Spinner) findViewById(R.id.toSpinner);
        to_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> to_adapter = ArrayAdapter.createFromResource(this,R.array.from_array,R.layout.support_simple_spinner_dropdown_item);
        to_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to_spinner.setAdapter(to_adapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


        } else {
            Toast.makeText(this,"No network connection available.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.fromSpinner){
            String str = parent.getItemAtPosition(position).toString();
            fromCurrency = str.substring(str.length() - 3);
        }
        else if(spinner.getId() == R.id.toSpinner){
            String str = parent.getItemAtPosition(position).toString();
            toCurrency = str.substring(str.length() - 3);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void convert(View v){
        EditText from_value = (EditText) findViewById(R.id.from_value);
        if(from_value.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Value field is empty.", Toast.LENGTH_SHORT).show();
        }else{
            //Log.i("length = ", String.valueOf(from_value.getText().toString().trim().length()));
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                val = Double.parseDouble(String.valueOf(from_value.getText()));
                urlString = "http://api.fixer.io/latest?base=";
                urlString += fromCurrency;
                new JSONParse().execute();
            }else
                Toast.makeText(this,"No network connection available.",Toast.LENGTH_SHORT).show();

        }
    }


    private class JSONParse extends AsyncTask<Void,String,JSONObject>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.i("url : ",urlString);
            Log.i("tocur :",toCurrency);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject obj = null;
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null)
                    sb.append(line);
                obj = new JSONObject(sb.toString());
                br.close();


            } catch (UnsupportedEncodingException e) {
                Log.i("Exception : ","UnsupportedEncodingException");
            }  catch (IOException e) {
                Log.i("Exception : ","IOException");
            } catch (JSONException e) {
                Log.i("error while converting to : ","JSONObject");
            } finally {
                try {
                    if(inputStream != null)
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(connection != null)
                connection.disconnect();
            }


            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
            progressDialog.dismiss();
            JSONObject rates = null;
            try {
                if(object != null) {
                    rates = object.getJSONObject("rates");
                    rates = rates.put(fromCurrency, 1.0);
                }
            } catch (JSONException e) {
                Log.i("Error while :","getting rates object");
            }
            try {
                if(rates != null){
                    Double ans = val*rates.getDouble(toCurrency);
                    answer.setText(new DecimalFormat("#.000").format(ans));
                }
            } catch (JSONException e) {
                Log.i("error : ","Not a  double ?");
            }
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


