package com.example.letstalk.chatwindow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.letstalk.R;
import com.example.letstalk.gallery.GalleryExplorerActivity;
import com.example.letstalk.helpers.CustomLogger;
import com.example.letstalk.helpers.PreferenceHelper;
import com.example.letstalk.helpers.StaticConstants;


public class ChatWindowActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private PreferenceHelper preferenceHelper;

    private Context mContext;

    //UI variables
    private Toolbar mToolbar;

    private ListView mLvMessageList;

    private ImageView mIvSend;

    private EditText mEtComposeField;

    private RelativeLayout mRlAttachmentContainer;

    private RelativeLayout mRlParentLayout;

    //Divider Textview
    private TextView mTvDivider;

    //Drawable for imageviews
    private Drawable attachmentDrawable, sendDrawable, keyboardDrawable;

    private Drawable emoticonsDrawable, galleyDrawable, cameraDrawable, videoRecordingDrawable;

    private Drawable musicDrawable, fileDrawable;

    private ImageView mIvEmotiocns, mIvGallery, mIvCamera, mIvVideoRecording, mIvMusic, mIvFile;

    //color used in this class
    private int primaryColor;

    private int attachmentIconsDefaultColor;

    //Variable for keyboard visibilty
    private boolean isKeyboardVisible = false;
    //Variable to calculate height of keyboard;
    private int previousHeightDiffrence;
    //Keyboard height stored in preference
    private int keyboardHeight;

    private int lastSelectedImageviewId = 0;

    private static final String TAG = "ChatWindowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        CustomLogger.log(TAG, "onCreate()", "inside method");
        initHelpersClass(); // Init helpers object
        initComponents(); //Initialize UI components
    }

    @Override
    protected void onStart() {
        super.onStart();
        CustomLogger.log(TAG, "onStart()", "inside method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomLogger.log(TAG, "onResume()", "inside method");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CustomLogger.log(TAG, "onRestart()", "inside method");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomLogger.log(TAG, "onPause()", "inside method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        CustomLogger.log(TAG, "onStop()", "inside method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomLogger.log(TAG, "onDestroy()", "inside method");
    }

    /**
     * Initialize helper class object like Preference, databases
     */
    private void initHelpersClass() {
        mContext = this;
        preferenceHelper = PreferenceHelper.getInstance(mContext);
        primaryColor = getResources().getColor(R.color.colorPrimary);
        attachmentIconsDefaultColor = getResources().getColor(R.color.attachment_default_color);
        keyboardHeight = preferenceHelper.getInt(StaticConstants.Utils.KEYBOARD_HEIGHT);
        CustomLogger.log(TAG, "initHelperClass()", "KeyboardHeight : "+keyboardHeight);
    }

    /**
     * Initialize all UI components of this activity
     */
    private void initComponents() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlParentLayout = (RelativeLayout) findViewById(R.id.rlParentLayout);
        mLvMessageList = (ListView) findViewById(R.id.lvMessageList);
        mEtComposeField = (EditText) findViewById(R.id.etComposeField);
        mRlAttachmentContainer = (RelativeLayout) findViewById(R.id.rlAttachmentCover);
        initializeDrawables();  //Initialize all drawables
        mEtComposeField.setOnFocusChangeListener(this);
        mIvSend = (ImageView) findViewById(R.id.ivSend);
        mIvSend.setImageDrawable(sendDrawable);
        mTvDivider = (TextView) findViewById(R.id.tvDivider);
        //Initialize all attachment image view
        initializeAttachmentImageview();
        //Add globalLayoutListener to identify visibility of keyboard
        mRlParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
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
        emoticonsDrawable = getResources().getDrawable(R.mipmap.ic_insert_emoticon_black_24dp);
        emoticonsDrawable.setColorFilter(
                attachmentIconsDefaultColor, PorterDuff.Mode.SRC_ATOP);
        galleyDrawable = getResources().getDrawable(R.mipmap.ic_photo_black_24dp);
        galleyDrawable.setColorFilter(attachmentIconsDefaultColor
                , PorterDuff.Mode.SRC_ATOP);
        cameraDrawable = getResources().getDrawable(R.mipmap.ic_camera_alt_black_24dp);
        cameraDrawable.setColorFilter(attachmentIconsDefaultColor
                , PorterDuff.Mode.SRC_ATOP);
        videoRecordingDrawable = getResources().getDrawable(R.mipmap.ic_videocam_black_24dp);
        videoRecordingDrawable.setColorFilter(attachmentIconsDefaultColor
                , PorterDuff.Mode.SRC_ATOP);
        musicDrawable = getResources().getDrawable(R.mipmap.ic_music_note_black_24dp);
        musicDrawable.setColorFilter(attachmentIconsDefaultColor
                , PorterDuff.Mode.SRC_ATOP);
        fileDrawable = getResources().getDrawable(R.mipmap.ic_insert_drive_file_black_24dp);
        fileDrawable.setColorFilter(attachmentIconsDefaultColor
                , PorterDuff.Mode.SRC_ATOP);
    }

    private void initializeAttachmentImageview() {
        mIvEmotiocns = (ImageView) findViewById(R.id.ivEmoticon);
        mIvEmotiocns.setImageDrawable(emoticonsDrawable);
        mIvEmotiocns.setOnClickListener(this);
        mIvGallery = (ImageView) findViewById(R.id.ivGallery);
        mIvGallery.setImageDrawable(galleyDrawable);
        mIvGallery.setOnClickListener(this);
        mIvCamera = (ImageView) findViewById(R.id.ivCamera);
        mIvCamera.setImageDrawable(cameraDrawable);
        mIvCamera.setOnClickListener(this);
        mIvVideoRecording = (ImageView) findViewById(R.id.ivVideoRecording);
        mIvVideoRecording.setImageDrawable(videoRecordingDrawable);
        mIvVideoRecording.setOnClickListener(this);
        mIvMusic = (ImageView) findViewById(R.id.ivMusic);
        mIvMusic.setImageDrawable(musicDrawable);
        mIvMusic.setOnClickListener(this);
        mIvFile = (ImageView) findViewById(R.id.ivFileAttachment);
        mIvFile.setImageDrawable(fileDrawable);
        mIvFile.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        CustomLogger.log(TAG, "onFocusChange", "hasFocus : "+hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEmoticon :
                addSelectorOnAttachments(R.id.ivEmoticon, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivEmoticon;
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                CustomLogger.log(TAG, "onClick()", "after closing input keyboard : ");
                mRlAttachmentContainer.getLayoutParams().height = keyboardHeight;
                mRlAttachmentContainer.setVisibility(View.VISIBLE);
                break;

            case R.id.ivGallery :
                addSelectorOnAttachments(R.id.ivGallery, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivGallery;
                Intent galleryIntent = new Intent(mContext, GalleryExplorerActivity.class);
                startActivity(galleryIntent);
                break;

            case R.id.ivCamera :
                addSelectorOnAttachments(R.id.ivCamera, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivCamera;
                break;

            case R.id.ivVideoRecording :
                addSelectorOnAttachments(R.id.ivVideoRecording, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivVideoRecording;
                break;

            case R.id.ivMusic :
                addSelectorOnAttachments(R.id.ivMusic, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivMusic;
                break;

            case R.id.ivFileAttachment :
                addSelectorOnAttachments(R.id.ivFileAttachment, lastSelectedImageviewId);
                lastSelectedImageviewId = R.id.ivFileAttachment;
                break;
        }
    }

    /**
     * Add selector to display selected attachment inside attachment container
     * @param selectedImageviewId selected imageview in attachment container
     * @param lastSelectedImageviewId last selected imageview in attachment container
     */
    private void addSelectorOnAttachments(int selectedImageviewId, int lastSelectedImageviewId) {
        ImageView selectedImageview = (ImageView) findViewById(selectedImageviewId);
        Drawable drawable = selectedImageview.getDrawable();
        drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
        if (lastSelectedImageviewId != 0 && lastSelectedImageviewId != selectedImageviewId) {
            ImageView lastSelectedImageview = (ImageView) findViewById(lastSelectedImageviewId);
            Drawable lastDrawable = lastSelectedImageview.getDrawable();
            lastDrawable.setColorFilter(attachmentIconsDefaultColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();

        mRlParentLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = mRlParentLayout.getRootView().getHeight();
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
            mRlAttachmentContainer.setVisibility(View.VISIBLE);
            mTvDivider.setBackgroundColor(getResources()
            .getColor(R.color.chatfooter_divider_color));
        }

        previousHeightDiffrence = keyboardHeight;
        if (keyboardHeight > 100) {
            isKeyboardVisible = true;
            mRlAttachmentContainer.setVisibility(View.GONE);
            mTvDivider.setBackgroundColor(primaryColor);
            if (lastSelectedImageviewId != 0) {
                ImageView lastSelectedImageview = (ImageView) findViewById(lastSelectedImageviewId);
                Drawable drawable = lastSelectedImageview.getDrawable();
                drawable.setColorFilter(attachmentIconsDefaultColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
