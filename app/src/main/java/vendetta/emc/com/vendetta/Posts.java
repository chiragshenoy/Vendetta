package vendetta.emc.com.vendetta;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
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
import java.util.List;


public class Posts extends Fragment {

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
    ArrayList<RowData> rowDataList;
    ArrayAdapter<String> adapter;
    ListView listView;
    Button upvote, downvote;
    TextView updownCount;
    int counter = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.posts, container, false);

        client = new DefaultHttpClient();

        array_list_posts = new ArrayList<String>();

        Context context = getActivity();
        AppPrefs appPrefs = new AppPrefs(context);

        college = appPrefs.getcollege_saved();
        roll = appPrefs.getroll_saved();

        new Read().execute();
        listView = (ListView) rootView.findViewById(R.id.list);

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
                Toast.makeText(getActivity(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

//End of ListView


        return rootView;
    }

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
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
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
                        Log.d(ob.getTex(), "GOT TEXT BOYSA ");
                        rowDataList.add(ob);

                        listContents.add(name);
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
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            CustomArrayAdapter dataAdapter = new CustomArrayAdapter(getActivity(), R.id.label, rowDataList);
            listView.setAdapter(dataAdapter);
        }
    }
}
