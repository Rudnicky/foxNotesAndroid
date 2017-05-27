package com.example.arakjel.foxsnotes.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.arakjel.foxsnotes.interfaces.OnCustomDialogClickListener;
import com.example.arakjel.foxsnotes.R;


public class CustomDialog extends Dialog {

    private OnCustomDialogClickListener mListener;
    public Activity activity;
    public Button yesButton;
    public Button noButton;
    public TextView headerTextView;
    public TextView bodyTextView;

    public CustomDialog(Activity activity, OnCustomDialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);

        yesButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);
        headerTextView = (TextView) findViewById(R.id.headerTextView);
        bodyTextView = (TextView) findViewById(R.id.bodyTextView);

        yesButton.setText("Yes");
        noButton.setText("No");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick();
                dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick();
                dismiss();
            }
        });
    }
}
