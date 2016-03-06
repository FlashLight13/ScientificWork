package stydying.algo.com.algostudying.ui.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 06.06.2015.
 */
public class AlertDialogFragment extends DialogFragment implements DialogInterface {
    Context context;
    private DataHolder dataHolder;

    public AlertDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.f_alert_dialog, container);

        TextView ok = (TextView) root.findViewById(R.id.alert_dialog_ok);
        if (dataHolder.okBtnTextRes > 0) {
            ok.setText(dataHolder.okBtnTextRes);
        }
        if (dataHolder.okListener != null) {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataHolder.okListener.onClick(AlertDialogFragment.this, DialogInterface.BUTTON_POSITIVE);
                }
            });
        }

        TextView cancel = (TextView) root.findViewById(R.id.alert_dialog_cancel);
        if (dataHolder.cancelBtnTextRes > 0) {
            cancel.setText(dataHolder.cancelBtnTextRes);
        }
        if (dataHolder.cancelListener != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataHolder.cancelListener.onCancel(AlertDialogFragment.this);
                }
            });
        }


        return root;
    }

    @Override
    public void cancel() {
        dismiss();
    }

    public static class AlertDialogFragmentBuilder {
        public AlertDialogFragmentBuilder() {
            dataHolder = new DataHolder();
        }

        private DataHolder dataHolder;

        public AlertDialogFragmentBuilder setOkBtnTextRes(int okBtnTextRes) {
            this.dataHolder.okBtnTextRes = okBtnTextRes;
            return this;
        }

        public AlertDialogFragmentBuilder setOkListener(DialogInterface.OnClickListener okListener) {
            this.dataHolder.okListener = okListener;
            return this;
        }

        public AlertDialogFragmentBuilder setCancelBtnTextRes(int cancelBtnTextRes) {
            this.dataHolder.cancelBtnTextRes = cancelBtnTextRes;
            return this;
        }

        public AlertDialogFragmentBuilder setCancelListener(DialogInterface.OnCancelListener cancelListener) {
            this.dataHolder.cancelListener = cancelListener;
            return this;
        }

        public AlertDialogFragmentBuilder setContent(View content) {
            this.dataHolder.content = content;
            return this;
        }

        public AlertDialogFragmentBuilder setMessageRes(int messageRes) {
            this.dataHolder.messageRes = messageRes;
            return this;
        }

        public AlertDialogFragmentBuilder setTitleRes(int titleRes) {
            this.dataHolder.titleRes = titleRes;
            return this;
        }

        public AlertDialogFragment build(Context context) {
            AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
            alertDialogFragment.context = context;
            alertDialogFragment.dataHolder = dataHolder;
            return alertDialogFragment;
        }
    }

    private static class DataHolder {

        private int okBtnTextRes;
        private DialogInterface.OnClickListener okListener;

        int cancelBtnTextRes;
        private DialogInterface.OnCancelListener cancelListener;

        private View content;
        private int messageRes;

        private int titleRes;

    }
}
