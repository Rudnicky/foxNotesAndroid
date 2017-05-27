package com.example.arakjel.foxsnotes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arakjel.foxsnotes.interfaces.OnCustomDialogClickListener;
import com.example.arakjel.foxsnotes.managers.LoggerManager;
import com.example.arakjel.foxsnotes.R;
import com.example.arakjel.foxsnotes.utils.CustomDialog;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_EXTERNAL_STORAGE = 1;

    // ui variables
    private LoggerManager logger;
    private ImageView aboutImageView;
    private Button writeButton;
    private Button readButton;
    private Button cleanButton;
    private Button exitButton;

    // custom toast
    private View customToastView;
    private TextView toastText;

    // custom dialog
    private CustomDialog dialog;
    private OnCustomDialogClickListener mListener;
    private boolean isCleanDialog;
    private boolean isPermissionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        fullScreen();
        setContentView(R.layout.activity_main);
        initialize();
        listeners();
        checkPermissions();
    }

    //------------------------------------------ INITIALIZE --------------------------------------//
    private void initialize() {
        aboutImageView = (ImageView) findViewById(R.id.aboutImageView);
        writeButton = (Button) findViewById(R.id.writeButton);
        readButton = (Button) findViewById(R.id.readButton);
        cleanButton = (Button) findViewById(R.id.cleanButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        logger = new LoggerManager();
        initializeToasts();
        setupDialog();
    }

    private void initializeToasts() {
        LayoutInflater inflater = getLayoutInflater();
        customToastView = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) findViewById(R.id.customToast));
        toastText = (TextView) customToastView.findViewById(R.id.toastText);
    }

    private void setupDialog() {
        mListener = new OnCustomDialogClickListener() {
            @Override
            public void onDialogPositiveClick() {
                if (isCleanDialog) {
                    setCustomToast("the mess's cleaned up");
                    logger.clean();
                    isCleanDialog = false;
                } else if (isPermissionDialog) {
                    isPermissionDialog = false;
                    finish();
                }
            }

            @Override
            public void onDialogNegativeClick() {
                if (isPermissionDialog) {
                    isPermissionDialog = false;
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_EXTERNAL_STORAGE);
                }
            }
        };

        dialog = new CustomDialog(this, mListener);
    }
    //------------------------------------------ INITIALIZE --------------------------------------//

    //------------------------------------------ LISTENERS ---------------------------------------//
    private void listeners() {
        aboutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);
            }
        });

        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCleanDialog = true;
                if (logger.isFileEmpty()) {
                    setCustomToast("There's no notes to clean up");
                } else {
                    setDialog("Warning!", "Are you sure that you want to clean whole text file? " +
                            "You won't get back your old diary.", "No", "Yes");
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //------------------------------------------ LISTENERS ---------------------------------------//

    //------------------------------------------ PERMISSION --------------------------------------//
    private void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                setDialog("Permission", "Hi! without your permission this application" +
                        " is unable to create a txt file in your Documents. If you won't allow" +
                        " this application to use an external device in order to write in to a file," +
                        " you won't be able to write your notes. Are you sure you want to deny " +
                        "this permission?", "Retry", "Exit");
                isPermissionDialog = true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // if permission is already granted
            logger.create();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    logger.create();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    setDialog("Permission", "Hi! without your permission this application" +
                            " is unable to create a txt file in your Documents. If you won't allow" +
                            " this application to use an external device in order to write in to a file," +
                            " you won't be able to write your notes. Are you sure you want to deny " +
                            "this permission?", "Retry", "Exit");
                    isPermissionDialog = true;
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //------------------------------------------ PERMISSION --------------------------------------//

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

    private void setDialog(String header, String body, String noButtonText, String yesButtonText) {
        dialog.show();
        dialog.headerTextView.setText(header);
        dialog.bodyTextView.setText(body);
        dialog.noButton.setText(noButtonText);
        dialog.yesButton.setText(yesButtonText);
    }
    //------------------------------------------ METHODS -----------------------------------------//
}
