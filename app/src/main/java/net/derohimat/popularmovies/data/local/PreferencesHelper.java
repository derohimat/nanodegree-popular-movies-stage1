package net.derohimat.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import net.derohimat.baseapp.util.BasePreferenceUtils;

public class PreferencesHelper extends BasePreferenceUtils {

    private static SharedPreferences mPref;

    private static final String KEY_USER_ID = "user_id";

    public PreferencesHelper(Context context) {
        mPref = getSharedPreference(context);
    }

    public long getUserId() {
        return mPref.getLong(KEY_USER_ID, 1);
    }

    public void setUserId(long userId) {
        mPref.edit().putLong(KEY_USER_ID, userId).apply();
    }
}
