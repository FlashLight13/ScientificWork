package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;

/**
 * Created by Anton on 22.07.2015.
 */
public class AboutListGroupView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    public AboutListGroupView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AboutListGroupView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public AboutListGroupView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AboutListGroupView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(context, attributeSet, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this.setOrientation(HORIZONTAL);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.about_group_view_padding);
        this.setPadding(padding, padding, padding, padding);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        imageView = new ImageView(context, attributeSet, defStyleAttr);
        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_size_small);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageParams.setMargins(
                context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_margin), 0,
                context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_margin), 0);
        imageView.setLayoutParams(imageParams);
        addView(imageView);

        textView = new TextView(context, attributeSet, defStyleAttr);
        textView.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(textParams);
        addView(textView);
    }

    public void setCommand(Command command) {
        this.textView.setText(command.getTitleId());
        this.imageView.setImageResource(command.getIconId());
    }
}
