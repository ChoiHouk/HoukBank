package choihouk.houkbank;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {
    private static SharedPref mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "최호욱";


    private SharedPref(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPref(context);
        }
        return mInstance;
    }


    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_NAME,null);

    }

    public void setUsername(String user_name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_NAME, user_name);
        editor.commit();

    }


}