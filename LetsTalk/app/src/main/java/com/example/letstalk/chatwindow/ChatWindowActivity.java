package com.example.letstalk.chatwindow;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.helpers.CustomLogger;
import com.example.letstalk.helpers.PreferenceHelper;


public class ChatWindowActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private PreferenceHelper preferenceHelper;

    private Context mContext;

    //UI variables
    private Toolbar mToolbar;

    private ListView mLvMessageList;

    private ImageView mIvSend;

    private EditText mEtComposeField;

    private RelativeLayout mRlAttachmentContainer;

    private RelativeLayout mRlParentLayout;

    //Drawable for imageview s
    private Drawable attachmentDrawable, sendDrawable, keyboardDrawable;

    //Primary color
    private int primaryColor;

    //Variable for keyboard visibilty
    private boolean isKeyboardVisible = false;

    private static final String TAG = "ChatWindowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        initHelpersClass(); // Init helpers object
        initComponents(); //Initialize UI components
    }

    /**
     * Initialize helper class object like Preference, databases
     */
    private void initHelpersClass() {
        mContext = this;
        preferenceHelper = PreferenceHelper.getInstance(mContext);
        primaryColor = getResources().getColor(R.color.colorPrimary);
    }

    /**
     * Initialize all UI components of this activity
     */
    private void initComponents() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLvMessageList = (ListView) findViewById(R.id.lvMessageList);
        mIvSend = (ImageView) findViewById(R.id.ivSend);
        mEtComposeField = (EditText) findViewById(R.id.etComposeField);
        mRlAttachmentContainer = (RelativeLayout) findViewById(R.id.rlAttachmentCover);
        initializeDrawables();  //Initialize all drawables
        mEtComposeField.setOnFocusChangeListener(this);
    }

    /**
     * Initialize all drawables
     */
    private void initializeDrawables() {
        attachmentDrawable = getResources().getDrawable(R.mipmap.ic_add_black_24dp);
        attachmentDrawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
        keyboardDrawable = getResources().getDrawable(R.mipmap.ic_keyboard_black_24dp);
        keyboardDrawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
        sendDrawable = getResources().getDrawable(R.mipmap.ic_send_black_24dp);
        sendDrawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        CustomLogger.log(TAG, "onFocusChange", "hasFocus : "+hasFocus);
    }

}
