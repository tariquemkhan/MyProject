package com.example.qucikreminder.module.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.qucikreminder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class CreateReminderFragment extends Fragment implements View.OnClickListener {

    private EditText etTitle;

    private EditText etReminder;

    private ImageButton ivPickDate;

    private Button btnCancel,btnOk;

    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_reminder,container,false);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etReminder = (EditText) view.findViewById(R.id.etReminder);
        ivPickDate = (ImageButton) view.findViewById(R.id.ivPickDate);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        ivPickDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPickDate :
                Log.d("Alpha","on click of Pic Date : ");
                DateTimePickerFragment fragment = DateTimePickerFragment.newInstance();
                fragment.show(getFragmentManager(),"date");
                break;

            case R.id.btnOk :
                Log.d("Alpha","on click of OK : ");
                break;

            case R.id.btnCancel :
                Log.d("Alpha","on click of Cancel : ");
                break;
        }
    }


}
