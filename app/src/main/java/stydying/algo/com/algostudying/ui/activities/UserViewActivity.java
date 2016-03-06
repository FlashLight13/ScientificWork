package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment;
import stydying.algo.com.algostudying.utils.BundleBuilder;

/**
 * Created by Anton on 23.02.2016.
 */
public class UserViewActivity extends BaseActivity {

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

    public static void startMe(Context context, User user) {
        Intent intent = new Intent(context, UserViewActivity.class);
        intent.putExtra(USER_EXTRA, user);
        context.startActivity(intent);
    }
}
