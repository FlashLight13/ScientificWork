package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * Created by Anton on 07.03.2016.
 */
public class NavigationItemView extends IndicatedListItemView {

    private boolean isMain;

    public NavigationItemView(Context context) {
        super(context);
    }

    public NavigationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isMain() {
        return isMain;
    }

    public NavigationItemView setMain(boolean main) {
        isMain = main;
        return this;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isMain = isMain;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        this.isMain = savedState.isMain;
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    private static final class SavedState extends BaseSavedState {

        private boolean isMain;

        public SavedState(Parcel source) {
            super(source);
            isMain = source.readByte() == 1;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(isMain ? (byte) 1 : 0);
        }
    }
}
