package stydying.algo.com.algostudying.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment;
import stydying.algo.com.algostudying.utils.BundleBuilder;

/**
 * Created by Anton on 23.02.2016.
 */
public class UserViewActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1314;
    private static final String USER_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.UserViewActivity.USER_EXTRA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_user_view);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(new BundleBuilder()
                        .putParcelable(ProfileFragment.USER_ARG, getIntent().getParcelableExtra(USER_EXTRA))
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, fragment)
                        .commit();
            }
        });

        if (getSupportActionBar() != null) {
            setTitle(R.string.title_user_view);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void startMe(@Nullable View fromView, @NonNull Activity activity, @NonNull User user) {
        Bundle options = null;
        if (fromView != null) {
            options = ActivityOptionsCompat.makeScaleUpAnimation(
                    fromView, 0, 0, fromView.getWidth(), fromView.getHeight()).toBundle();
        }
        Intent intent = new Intent(activity, UserViewActivity.class);
        intent.putExtra(USER_EXTRA, user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(intent, REQUEST_CODE, options);
        } else {
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
