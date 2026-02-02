package by.instruction.profsouz.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREFS_NAME = "profsouz_prefs";
    private static final String KEY_SELECTED_UNION = "selected_union";

    private final SharedPreferences preferences;

    public UserPreferences(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setSelectedUnionId(String unionId) {
        preferences.edit().putString(KEY_SELECTED_UNION, unionId).apply();
    }

    public String getSelectedUnionId() {
        return preferences.getString(KEY_SELECTED_UNION, null);
    }
}
