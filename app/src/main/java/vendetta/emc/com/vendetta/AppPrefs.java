package vendetta.emc.com.vendetta;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    private static final String USER_PREFS = "USER_PREFS";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String college_saved = "college";
    private String roll_saved = "roll";

    public AppPrefs(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(USER_PREFS,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    //college name
    public String getcollege_saved() {
        return appSharedPrefs.getString(college_saved, "");
    }

    public void setcollege_saved(String _college_saved) {
        prefsEditor.putString(college_saved, _college_saved).commit();
    }
    //college name end

    //Roll
    public String getroll_saved() {
        return appSharedPrefs.getString(roll_saved, "");
    }

    public void setroll_saved(String _roll_saved) {
        prefsEditor.putString(roll_saved, _roll_saved).commit();
    }
    //End roll


}