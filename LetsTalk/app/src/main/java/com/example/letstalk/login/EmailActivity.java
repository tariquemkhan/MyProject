package com.example.letstalk.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.letstalk.R;
import com.example.letstalk.chatwindow.ChatWindowActivity;
import com.example.letstalk.helpers.PreferenceHelper;
import com.example.letstalk.helpers.StaticConstants;

import java.util.logging.Logger;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    // variables for UI components
    private RelativeLayout rlParentLayout;

    private Context mContext;

    private int primaryColor;

    private PreferenceHelper mPreferenceHelper;

    private Button mBtnEmail;

    private boolean isKeyBoardVisible = false;

    private int previousHeightDiffrence;

    private int keyboardHeight;

    private static final String TAG = "EmailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        initComponents();
    }

    private void initComponents() {
        rlParentLayout = (RelativeLayout) findViewById(R.id.rlParentLayout);
        mBtnEmail = (Button) findViewById(R.id.btnEmail);
        mBtnEmail.setOnClickListener(this);

        initHelpersClass();

        //check height of keyboard
        checkKeyboardHeight();
    }

    /**
     * Initialize helper class object like Preference, databases
     */
    private void initHelpersClass() {
        mContext = this;
        mPreferenceHelper = PreferenceHelper.getInstance(mContext);
        primaryColor = getResources().getColor(R.color.colorPrimary);
    }


    /**
     * Check Keyboard height
     */
    private void checkKeyboardHeight() {
        rlParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                rlParentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rlParentLayout.getRootView().getHeight();
                int keyboardHeight = screenHeight - (r.bottom);

                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                    int bottomNavigationHeight;
                    Resources resources = getResources();
                    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        bottomNavigationHeight = resources.getDimensionPixelSize(resourceId);
                    } else {
                        bottomNavigationHeight = 100;
                    }
                    keyboardHeight = keyboardHeight - bottomNavigationHeight;
                }

                if (previousHeightDiffrence - keyboardHeight > 50) {
                    // Do some stuff here
                }

                previousHeightDiffrence = keyboardHeight;
                if (keyboardHeight > 100) {
                    if (!isKeyBoardVisible) {
                        changeKeyboardHeight(keyboardHeight);
                    }
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
    }

    /**
     * Change the value of keyboard height
     *
     * @param height measured hieght of keyboard
     */
    private void changeKeyboardHeight(int height) {
        if (height > 100) {
            keyboardHeight = height;
            Log.d(TAG, "Keyboard height : " + keyboardHeight);
            mPreferenceHelper.saveInt(StaticConstants.Utils.KEYBOARD_HEIGHT,
                    keyboardHeight);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEmail: //when clicks on Button after entering email
                Intent intent = new Intent(EmailActivity.this, ChatWindowActivity.class);
                startActivity(intent);
                break;
        }
    }
}
