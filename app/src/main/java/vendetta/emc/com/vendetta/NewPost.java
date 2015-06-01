package vendetta.emc.com.vendetta;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chirag Shenoy on 5/31/2015.
 */
public class NewPost extends Fragment {

    EditText newpost;
    Button post;
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
    String content = "";
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
    AppPrefs appPrefs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.new_post, container, false);

        newpost = (EditText) rootView.findViewById(R.id.etNewPost);
        post = (Button) rootView.findViewById(R.id.bPost);


        appPrefs = new AppPrefs(getActivity());
        college = appPrefs.getcollege_saved();
        roll = appPrefs.getroll_saved();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpost.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please type something", Toast.LENGTH_SHORT).show();
                } else {
                    content = newpost.getText().toString();
                    new Inserting().execute();

                }


            }
        });


        return rootView;

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
            Toast.makeText(getActivity(), "Invalid IP Address",
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
                Fragment fragment = new Posts();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_posts, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Log.e("Failed", "" + code);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }
    //End of inserr to DB

}