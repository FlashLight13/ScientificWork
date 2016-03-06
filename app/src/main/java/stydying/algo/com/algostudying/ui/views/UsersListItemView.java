package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;

/**
 * Created by Anton on 01.02.2016.
 */
public class UsersListItemView extends LinearLayout {

    public interface OnUserDeleted {
        void onUserDeleted(int pos);
    }

    @Bind(R.id.text_name)
    TextView textUserName;
    @Bind(R.id.img_clear)
    ImageView imgDeleteUser;

    public UsersListItemView(Context context) {
        super(context);
        init(context);
    }

    public UsersListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UsersListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UsersListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.v_user_list_item, this);
        ButterKnife.bind(this);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        this.imgDeleteUser.setClickable(clickable);
    }

    public void onUserDeletedListener(final OnUserDeleted listener) {
        if (listener != null) {
            imgDeleteUser.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserDeleted((Integer) UsersListItemView.this.getTag(R.integer.position_key));
                }
            });
        }
    }

    public void setData(int position, User user) {
        this.setTag(R.integer.position_key, position);
        this.textUserName.setText(user.getName());
    }
}
