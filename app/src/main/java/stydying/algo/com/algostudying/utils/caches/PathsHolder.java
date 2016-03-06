package stydying.algo.com.algostudying.utils.caches;

import android.content.Context;

/**
 * Created by anton on 01.09.15.
 */
public class PathsHolder {

    public static String getResDir(Context context) {
        return context.getObbDir().getAbsolutePath() + "/Res";
    }

    public static String getMapsDir(Context context) {
        return context.getObbDir().getAbsolutePath() + "/Maps";
    }
}
