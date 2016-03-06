package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
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
public class ImageTextView extends LinearLayout {

    public enum Mode {
        BIG_IMAGE, SMALL_IMAGE
    }

    private ImageView imageView;
    private TextView textView;

    public ImageTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ImageTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public ImageTextView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageTextView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(context, attributeSet, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this.setOrientation(HORIZONTAL);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.about_group_view_padding);
        this.setPadding(padding, padding, padding, padding);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        imageView = new ImageView(context, attributeSet, defStyleAttr);
        textView = new TextView(context, attributeSet, defStyleAttr);
        setModeSmallImage(context);
    }

    public void setMode(Mode mode) {
        Context context = getContext();
        switch (mode) {
            case BIG_IMAGE:
                setModeBigImage(context);
                break;
            case SMALL_IMAGE:
                setModeSmallImage(context);
                break;
        }
    }

    private void setModeSmallImage(Context context) {
        if (getChildCount() > 0) {
            this.removeAllViews();
        }
        this.setOrientation(HORIZONTAL);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.img_list_item_sides_margin);
        this.setPadding(padding, 0, padding, 0);
        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.img_list_item_img_size);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageParams.setMargins(
                context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_margin), 0,
                context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_margin), 0);
        imageView.setLayoutParams(imageParams);
        addView(imageView);

        textView.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(textParams);
        addView(textView);
    }

    private void setModeBigImage(Context context) {
        if (getChildCount() > 0) {
            this.removeAllViews();
        }
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.about_group_view_padding);
        this.setPadding(padding, padding, padding, padding);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(textParams);
        addView(textView);

        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_size_big);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
        int margin = (context.getResources().getDimensionPixelSize(R.dimen.about_group_view_image_margin) / 2);
        imageParams.setMargins(
                0, margin,
                0, margin);
        imageView.setLayoutParams(imageParams);
        addView(imageView);
    }

    public void setData(Bitmap image, String text) {
        this.textView.setText(text);
        this.imageView.setImageBitmap(image);
    }

    public void setCommand(Command command) {
        this.textView.setText(command.getTitleId());
        this.imageView.setImageResource(command.getIconId());
    }
}
