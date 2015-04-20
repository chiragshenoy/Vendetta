package vendetta.emc.com.vendetta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Chirag on 20-04-2015.
 */
public class LoginPage extends ActionBarActivity {

    EditText college_getter, roll_getter;
    Button go;
    String college_name, roll_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);


        college_getter = (EditText) findViewById(R.id.college_getter);
        roll_getter = (EditText) findViewById(R.id.roll_getter);
        go = (Button) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                college_name = college_getter.getText().toString();
                roll_number = roll_getter.getText().toString();

                Context context = getApplicationContext();
                AppPrefs appPrefs = new AppPrefs(context);

                appPrefs.setcollege_saved(college_name);
                appPrefs.setroll_saved(roll_number);


                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                intent.putExtra("college_name", college_name);
                intent.putExtra("roll_number", roll_number);
                startActivity(intent);

            }
        });


    }
}
