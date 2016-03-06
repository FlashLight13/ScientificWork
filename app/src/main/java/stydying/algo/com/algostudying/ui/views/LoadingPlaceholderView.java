package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.errors.BaseException;

/**
 * Created by Anton on 05.02.2016.
 */
public class LoadingPlaceholderView extends RelativeLayout {

    public interface OnRetryListener {
        void onRetry();
    }

    @Bind(R.id.progress)
    ProgressBar progressBar;

    @Bind(R.id.text_error_message)
    TextView textErrorMessage;
    @Bind(R.id.text_title)
    TextView text;
    @Bind(R.id.image)
    ImageView image;

    private OnRetryListener onRetryListener;

    public LoadingPlaceholderView(Context context) {
        super(context);
        init(context);
    }

    public LoadingPlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingPlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingPlaceholderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.v_loading_placeholder, this);
        ButterKnife.bind(this);
    }

    public void success() {
        image.setVisibility(GONE);
        text.setVisibility(GONE);
        textErrorMessage.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        setVisibility(GONE);
        setClickable(false);
    }

    public void error(BaseException e) {
        image.setVisibility(VISIBLE);
        text.setVisibility(VISIBLE);
        textErrorMessage.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        setVisibility(VISIBLE);
        setClickable(true);
        textErrorMessage.setText(e.message());
    }

    public void loading() {
        image.setVisibility(GONE);
        text.setVisibility(GONE);
        textErrorMessage.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        setClickable(false);
    }

    @Override
    public boolean performClick() {
        if (onRetryListener != null) {
            onRetryListener.onRetry();
        }
        return super.performClick();
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }
}
