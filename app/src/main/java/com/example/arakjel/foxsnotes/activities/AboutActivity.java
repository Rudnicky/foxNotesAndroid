package com.example.arakjel.foxsnotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.arakjel.foxsnotes.R;

public class AboutActivity extends AppCompatActivity {

    private TextView textView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_about);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Fox's note is an application for creating your notes, " +
                " which can be found as a text file in your Android device," +
                " just above the Documents folder. Each note has it's own " +
                " date and time that you won't worry about forgeting something! ;)," +
                " Why beautiful fox? the answer is 'Because my Girlfriend has same stunning hair color'." +
                " This application's fully for free. I've made it in order to" +
                " create custom loggers to develop but the purpose is totally up to You Lov." +
                " \n\n\nHave Fun\nPawel Rudnicki.");
        goBackButton = (Button) findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //------------------------------------------ METHODS -----------------------------------------//
    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    //------------------------------------------ METHODS -----------------------------------------//
}
