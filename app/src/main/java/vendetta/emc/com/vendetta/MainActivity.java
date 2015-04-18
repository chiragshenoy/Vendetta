package vendetta.emc.com.vendetta;


import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {

    HttpClient client;

    String URL = "http://evilmc.comlu.com/result.php";

    JSONObject json;
    JSONArray json_array_got;

    JSONArray final_json_array;

    String id = "";
    String url1 = "";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        client = new DefaultHttpClient();
        new Read().execute();
    }

    //Json Part
    public JSONArray get_entire_json() throws IOException, JSONException {

//        StringBuilder url = new StringBuilder(URL + email + PASSWORD);
        StringBuilder url = new StringBuilder(URL);
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
        /**
         * progress dialog to show user that the backup is processing.
         */


        int i = 0;

        //Only getting the list of the subjects and getting basic info
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

            tv.setText(id + url1);
        }
    }
}
