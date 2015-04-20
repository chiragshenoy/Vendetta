package vendetta.emc.com.vendetta;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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


public class MainActivity extends ActionBarActivity {

    HttpClient client;

    String URL = "http://evilmc.comlu.com/result.php?q=";

    JSONArray json_array_got;

    JSONArray final_json_array;

    String id = "";
    String url1 = "";
    TextView tv;

    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    String college = "";
    EditText input_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        input_et = (EditText) findViewById(R.id.editText1);

        client = new DefaultHttpClient();

        Button insert = (Button) findViewById(R.id.button1);

        insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Inserting().execute();
            }
        });


        Button retrieve = (Button) findViewById(R.id.button2);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                college = input_et.getText().toString();
                new Read().execute();

            }
        });

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

        nameValuePairs.add(new BasicNameValuePair("content", "This is from the app"));
        nameValuePairs.add(new BasicNameValuePair("college", "JSSATE"));


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

                    for (int i = 0; i < final_json_array.length(); i++) {
                        JSONObject jsonobject = final_json_array.getJSONObject(i);
                        id += jsonobject.getString("id");
                        url1 += jsonobject.getString("content");
                    }
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tv.setText(id + " " + url1);
        }
    }
}
