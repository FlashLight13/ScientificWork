package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.fragments.profile.StatsFragment;
import stydying.algo.com.algostudying.ui.fragments.profile.UserDetailsFragment;
import stydying.algo.com.algostudying.ui.interfaces.PagerController;
import stydying.algo.com.algostudying.ui.views.SwipeControlledViewPager;
import stydying.algo.com.algostudying.utils.BundleBuilder;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 25.01.2016.
 */
public class ProfileFragment extends BaseFragment implements PagerController {

    public static final String USER_ARG
            = "stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment.USER_ARG";

    private enum Fragments {
        USER_DETAILS(UserDetailsFragment.class) {
            @Override
            String tabTitle(BaseFragment fragment) {
                Bundle args = fragment.getArguments();
                if (args != null) {
                    User user = args.getParcelable(USER_ARG);
                    if (user != null) {
                        return user.getLogin();
                    }
                }
                return fragment.getString(R.string.tab_login,
                        LoginManager.getInstance(fragment.getContext()).getCurrentUser().getLogin());
            }
        },
        STATS(StatsFragment.class) {
            @Override
            String tabTitle(BaseFragment fragment) {
                return fragment.getResources().getString(R.string.tab_title_stats);
            }
        };

        Class<? extends BaseProfileFragment> fragmentClass;

        Fragments(Class<? extends BaseProfileFragment> fragmentClass) {
            this.fragmentClass = fragmentClass;
        }

        BaseProfileFragment fragment() {
            try {
                return fragmentClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        abstract String tabTitle(BaseFragment context);
    }

    @Bind(R.id.pager)
    SwipeControlledViewPager tasksPager;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.btn_action)
    FloatingActionButton btnAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_profile);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasksPager.setAdapter(new ProfileTabsAdapter(this));
        tasksPager.setOnTouchListener(null);
        tasksPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                BaseProfileFragment currentFragment = getCurrentFragment();
                if (state == ViewPager.SCROLL_STATE_IDLE && currentFragment.shouldUseButton()) {
                    btnAction.show();
                } else {
                    ViewsUtils.hideKeyboard(getActivity());
                    btnAction.hide();
                }
            }
        });
        tasksPager.setEnabled(false);
        tabs.setupWithViewPager(tasksPager);
    }

    @Override
    public void showTabs() {
        tabs.setVisibility(View.VISIBLE);
        /*AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofInt(tabs, "translationY", -tabs.getHeight(), 0),
                ObjectAnimator.ofInt(tasksPager, "top",
                        tasksPager.getTop(), tasksPager.getTop() - tabs.getHeight()));
        set.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        set.start();*/
    }

    @Override
    public void hideTabs() {
        tabs.setVisibility(View.GONE);
    }

    @Override
    public void setPagingEnabled(boolean isPagingEnabled) {
        tasksPager.setSwipeEnabled(isPagingEnabled);
    }

    @OnClick(R.id.btn_action)
    public void onBtnClicked() {
        getCurrentFragment().onButtonPressed();
    }

    private BaseProfileFragment getCurrentFragment() {
        return ((ProfileTabsAdapter) tasksPager.getAdapter())
                .getRegisteredFragment(tasksPager.getCurrentItem());
    }

    private final class ProfileTabsAdapter extends FragmentPagerAdapter {
        private SparseArray<BaseProfileFragment> registeredFragments = new SparseArray<>();
        private BaseFragment fragment;

        public ProfileTabsAdapter(BaseFragment fragment) {
            super(fragment.getChildFragmentManager());
            this.fragment = fragment;
        }

        @Override
        public int getCount() {
            return Fragments.values().length;
        }

        @Override
        public BaseProfileFragment getItem(int position) {
            return Fragments.values()[position].fragment();
        }

        @Override
        public BaseProfileFragment instantiateItem(ViewGroup container, int position) {
            BaseProfileFragment fragment = (BaseProfileFragment) super.instantiateItem(container, position);
            fragment.setButtonAction(btnAction);
            fragment.setPagerController(ProfileFragment.this);
            Bundle args = getArguments();
            if (args == null) {
                args = new BundleBuilder()
                        .putParcelable(USER_ARG, LoginManager.getInstance(getContext()).getCurrentUser())
                        .build();
            }
            fragment.setArguments(args);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Fragments.values()[position].tabTitle(fragment);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public BaseProfileFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    public static abstract class BaseProfileFragment extends BaseFragment {
        public abstract void onButtonPressed();

        public abstract void setButtonAction(FloatingActionButton btn);

        public abstract boolean shouldUseButton();

        public abstract void setPagerController(PagerController pagerController);
    }
}
