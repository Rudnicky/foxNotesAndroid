package com.example.arakjel.foxsnotes.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arakjel.foxsnotes.managers.LoggerManager;
import com.example.arakjel.foxsnotes.R;

public class WriteActivity extends AppCompatActivity {

    private LoggerManager logger;
    private EditText editText;
    private Button saveButton;
    private Button goBackButton;
    private View customToastView;
    private TextView toastText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_write);
        initialize();
        listeners();
    }

    //------------------------------------------ INITIALIZE --------------------------------------//
    private void initialize() {
        editText = (EditText) findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.saveButton);
        goBackButton = (Button) findViewById(R.id.goBackButton);
        logger = new LoggerManager();
        initializeToasts();
    }

    private void initializeToasts() {
        LayoutInflater inflater = getLayoutInflater();
        customToastView = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) findViewById(R.id.customToast));
        toastText = (TextView) customToastView.findViewById(R.id.toastText);
    }
    //------------------------------------------ INITIALIZE --------------------------------------//

    //------------------------------------------ LISTENERS ---------------------------------------//
    private void listeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = editText.getText().toString();
                if (tmp.isEmpty()) {
                    setCustomToast("Message cannot be empty");
                    return;
                } else {
                    setCustomToast("Message saved!");
                    logger.write(tmp);
                }
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(true);
                    hideSoftKeyboard(WriteActivity.this);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    //------------------------------------------ LISTENERS ---------------------------------------//

    //------------------------------------------ METHODS -----------------------------------------//
    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setCustomToast(String message) {
        toastText.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToastView);
        toast.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    //------------------------------------------ METHODS -----------------------------------------//
}
