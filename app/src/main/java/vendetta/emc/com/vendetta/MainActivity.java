package vendetta.emc.com.vendetta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    HttpClient client;
    ProgressDialog pDialog;

    String URL = "http://evilmc.comlu.com/result.php?q=";

    JSONArray json_array_got;

    JSONArray final_json_array;

    //    String id = "";
    String url1 = "";

    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    String college = "";
    String content;
    RowData ob;
    String roll = "";
    List<String> listContents;
    ArrayList<String> array_list_posts;
    ArrayList<RowData> rowDataList ;
    ArrayAdapter<String> adapter;
    ListView listView;
    Button upvote,downvote;
    TextView updownCount;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new DefaultHttpClient();


//        Bundle bundle = getIntent().getExtras();

        array_list_posts = new ArrayList<String>();

//        college = bundle.getString("college_name");

        Context context = getApplicationContext();
        AppPrefs appPrefs = new AppPrefs(context);
        college = appPrefs.getcollege_saved();
        roll = appPrefs.getroll_saved();

        new Read().execute();
//ListView Kireeek
        listView = (ListView) findViewById(R.id.list);


        // Defined Array values to show in ListView
//        String[] values = new String[]{"Android List View",
//                "Adapter implementation",
//                "Simple List View In Android",
//                "Create List View Android",
//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
//        };





// I didnt understand this shit.. i have set up the adapter in onPostExecute.. -Kai

            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data

            /*adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);*/


        // Assign adapter to ListView


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

//End of ListView


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        content = (userInput.getText().toString());
                                        new Inserting().execute();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }

        });*/


    }

    //Inserting async
    public class Inserting extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                insert();

            } catch (Exception e) {

            }

            return null;
        }
    }
    //end of inserting

    //Inserting to DB
    public void insert() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("content", content));
        nameValuePairs.add(new BasicNameValuePair("college", college));
        nameValuePairs.add(new BasicNameValuePair("posted_by", roll));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://evilmc.comlu.com/insert.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
                Log.e("Success", "" + code);
            } else {
                Log.e("Failed", "" + code);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }
    //End of inserr to DB


    //Json Part
    public JSONArray get_entire_json() throws IOException, JSONException {
        String temp = URL + college;
        StringBuilder url = new StringBuilder(temp);
        HttpGet get = new HttpGet(url.toString());
        HttpResponse r = client.execute(get);

        int status = r.getStatusLine().getStatusCode();

        if (status == 200) {
            HttpEntity e = r.getEntity();
            String data = EntityUtils.toString(e);

            json_array_got = new JSONArray(data);

            return json_array_got;
        } else {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            return null;

        }
    }

    public class Read extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                final_json_array = get_entire_json();
                try {

                    int length = json_array_got.length();
                    listContents = new ArrayList<String>(length);
                    rowDataList = new ArrayList<RowData>(length);
                    for (int i = 0; i < length; i++)

                    {
                        JSONObject jsonobject = json_array_got.getJSONObject(i);
                        String name = jsonobject.getString("content");
                         ob = new RowData();
                        RowData hi = new RowData();
                        ob.setTex(name);
                        Log.d(ob.getTex(),"GOT TEXT BOYSA ");
                        rowDataList.add(ob);

                        listContents.add(name);
                        //listContents.add(json_array_got.getString(i));
                    }

                    //for (int i = 0; i < final_json_array.length(); i++) {




                       /* JSONObject jsonobject = final_json_array.getJSONObject(i);
//                        id += jsonobject.getString("id");
                       url1 += jsonobject.getString("content");
                        Log.d(url1,url1);

                        array_list_posts.add(jsonobject.getString("content"));*/

                  //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String values[] = new String[array_list_posts.size()];
           // String values[]={"hello","world"};
           // values = array_list_posts.toArray(values);


            CustomArrayAdapter dataAdapter = new CustomArrayAdapter(MainActivity.this, R.id.label, rowDataList);
            listView.setAdapter(dataAdapter);

           /* adapter = new ArrayAdapter<String>(getApplicationContext(),
                   R.layout.row, R.id.label, listContents);
            listView.setAdapter(adapter);*/

// listView.notifyDatasetChanged();
        }
    }
}
