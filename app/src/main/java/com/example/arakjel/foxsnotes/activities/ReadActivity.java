package com.example.arakjel.foxsnotes.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arakjel.foxsnotes.managers.LoggerManager;
import com.example.arakjel.foxsnotes.R;
import com.example.arakjel.foxsnotes.utils.CustomAdapter;
import com.example.arakjel.foxsnotes.utils.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;

public class ReadActivity extends AppCompatActivity {

    private LoggerManager logger;
    private Button goBackButton;
    private Button cleanButton;
    private Button editButton;
    private Button updateButton;
    private EditText editText;
    private ListView listView;
    private ScrollView listViewContainer;
    private ScrollView editTextContainer;
    private ScrollView textViewContainer;
    private ArrayList<String> list;
    private CheckBox selectAllCheckBox;
    private TextView selectAllTextView;
    private TextView textView;
    private View customToastView;
    private TextView toastText;
    private ListAdapter adapter;
    private boolean isRemoveActive = false;
    private boolean isListItemClicked = false;
    private boolean isEditListItemClicked = false;
    private int positionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_read);
        initialize();
        listeners();
    }
    //------------------------------------------ INITIALIZE --------------------------------------//
    private void initialize() {
        goBackButton = (Button) findViewById(R.id.goBackButton);
        cleanButton = (Button) findViewById(R.id.cleanButton);
        editButton = (Button) findViewById(R.id.editButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        editText = (EditText) findViewById(R.id.editText);
        listViewContainer = (ScrollView) findViewById(R.id.listViewContainer);
        editTextContainer = (ScrollView) findViewById(R.id.editTextContainer);
        textViewContainer = (ScrollView) findViewById(R.id.textViewContainer);
        selectAllCheckBox = (CheckBox) findViewById(R.id.selectAllCheckBox);
        selectAllTextView = (TextView) findViewById(R.id.selectAllTextView);
        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        cleanButton.setText("Clean");
        listView.setLongClickable(true);
        selectAllCheckBox.setVisibility(GONE);
        selectAllTextView.setVisibility(GONE);
        logger = new LoggerManager();
        logger.read();
        if (LoggerManager.listOfDiaryLines != null) {
            list = new ArrayList<>(LoggerManager.listOfDiaryLines);
            initializeListView();
        }
        initializeToasts();
    }

    private void initializeListView() {
        adapter = new ListAdapter(this, list);
        listView.setAdapter(adapter);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewContainer.setVisibility(View.INVISIBLE);
                textViewContainer.setVisibility(View.VISIBLE);
                cleanButton.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.VISIBLE);
                editText.setText(getActualItem(position));
                textView.setText(getActualItem(position));
                isListItemClicked = true;
                positionIndex = position;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.isCheckingAvailable = true;
                adapter.notifyDataSetChanged();
                selectAllCheckBox.setVisibility(View.VISIBLE);
                selectAllTextView.setVisibility(View.VISIBLE);
                cleanButton.setText("Remove selected");
                isRemoveActive = true;
                return true;
            }
        });

        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.checkAll = true;
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.checkAll = false;
                    adapter.unCheckAll = true;
                    adapter.notifyDataSetChanged();
                }
            }
        });

        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRemoveActive) {
                    boolean[] tmp = adapter.getItemChecked();
                    int positionOfTrue = 0;
                    int[] array = new int[tmp.length];

                    if (LoggerManager.listOfDiaryLines != null) {
                        logger.read();
                        list = new ArrayList<>(LoggerManager.listOfDiaryLines);
                    }

                    // iterate through each index of the boolean array
                    // in order to find true/falses
                    // when true's found then add it to the array
                    // find the string beneath an index and add it to the array
                    ArrayList<String> tmpList = new ArrayList<>();
                    for (int i=0; i<tmp.length; i++) {
                        if (tmp[i]) {
                            positionOfTrue = i;
                            array[i] = positionOfTrue;
                            tmpList.add(list.get(positionOfTrue));
                        }
                    }

                    // Iterate through the array with strings in order
                    // to find same string in default list<string>
                    // if found then remove it from the default list<string>
                    for (int i=0; i<tmpList.size(); i++) {
                        for (int j=0; j<list.size(); j++) {
                            if (list.get(j).contains(tmpList.get(i))) {
                                System.out.println("Found it: " + tmpList.get(i));
                                list.remove(j);
                            }
                        }
                    }

                    // update list, view & adapter
                    logger.removeAndCreate(list);
                    initializeListView();
                    adapter.notifyDataSetChanged();

                    adapter.isCheckingAvailable = false;
                    selectAllCheckBox.setVisibility(View.INVISIBLE);
                    selectAllTextView.setVisibility(View.INVISIBLE);
                    cleanButton.setText("Clean");
                    isRemoveActive = false;
                } else {
                    adapter.isCheckingAvailable = true;
                    adapter.notifyDataSetChanged();
                    selectAllCheckBox.setVisibility(View.VISIBLE);
                    selectAllTextView.setVisibility(View.VISIBLE);
                    cleanButton.setText("Remove selected");
                    isRemoveActive = true;
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditListItemClicked = true;
                textViewContainer.setVisibility(View.INVISIBLE);
                editTextContainer.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewContainer.setVisibility(View.INVISIBLE);
                editTextContainer.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.INVISIBLE);
                listViewContainer.setVisibility(View.VISIBLE);
                cleanButton.setVisibility(View.VISIBLE);

                String tmp = editText.getText().toString();
                if (tmp.equals(list.get(positionIndex))) {
                    setCustomToast("You haven't change anything");
                    isListItemClicked = false;
                    isEditListItemClicked = false;
                    return;
                } else {
                    setCustomToast("Message updated");
                    if (LoggerManager.listOfDiaryLines != null) {
                        logger.read();
                        list = new ArrayList<>(LoggerManager.listOfDiaryLines);
                    }

                    // find actual item and replace with the new one
                    for (int i=0; i<list.size(); i++) {
                        if (list.get(i).contains(getActualItem(positionIndex))) {
                            System.out.println("Found it at position: " + i);
                            list.remove(i);
                            list.add(i, tmp);
                        }
                    }

                    // update list, view & adapter
                    logger.removeAndCreate(list);
                    initializeListView();
                    adapter.notifyDataSetChanged();
                    isListItemClicked = false;
                    isEditListItemClicked = false;
                }
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackPress();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(true);
                    hideSoftKeyboard(ReadActivity.this);
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

    private String getActualItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBackPressed() {
        handleBackPress();
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

    private void handleBackPress() {
        if (isListItemClicked || isEditListItemClicked) {
            listViewContainer.setVisibility(View.VISIBLE);
            editTextContainer.setVisibility(View.INVISIBLE);
            textViewContainer.setVisibility(View.INVISIBLE);
            cleanButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            updateButton.setVisibility(View.INVISIBLE);
            isListItemClicked = false;
            isEditListItemClicked = false;
        } else if (isRemoveActive) {
            adapter.isCheckingAvailable = false;
            adapter.notifyDataSetChanged();
            selectAllCheckBox.setVisibility(View.INVISIBLE);
            selectAllTextView.setVisibility(View.INVISIBLE);
            cleanButton.setText("Clean");
            isRemoveActive = false;
        } else {
            finish();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void setCustomToast(String message) {
        toastText.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToastView);
        toast.show();
    }
    //------------------------------------------ METHODS -----------------------------------------//
}
