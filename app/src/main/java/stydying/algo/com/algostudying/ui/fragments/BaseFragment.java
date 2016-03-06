package stydying.algo.com.algostudying.ui.fragments;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Anton on 08.07.2015.
 */
public class BaseFragment extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @LayoutRes int res) {
        View view = inflater.inflate(res, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected void setTitle(String title) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle(title);
        }
    }

    protected void supportInvalidateOptionsMenu() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
