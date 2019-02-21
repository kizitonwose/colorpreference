package com.kizitonwose.colorpreference;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import androidx.annotation.Nullable;

/**
 * @author Kizito Nwose
 */

public class Utils {
    @Nullable
    public static Activity resolveContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return resolveContext(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
}
