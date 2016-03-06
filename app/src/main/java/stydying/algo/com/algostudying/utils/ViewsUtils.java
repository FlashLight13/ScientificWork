package stydying.algo.com.algostudying.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Anton on 06.02.2016.
 */
public class ViewsUtils {

    /**
     * Just because I was completely bored with the Lint warnings!!!
     */
    public static EditText getEditText(TextInputLayout inputLayout) {
        return inputLayout.getEditText();
    }

    public static void hideKeyboard(@Nullable Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboard(@Nullable Activity activity, @Nullable View currentlyFocusedView) {
        if (activity != null && currentlyFocusedView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentlyFocusedView.getWindowToken(), 0);
        }
    }

    @Nullable
    public static View findFocusedView(@Nullable Activity activity) {
        if (activity != null) {
            return activity.getCurrentFocus();
        } else {
            return null;
        }
    }
}
