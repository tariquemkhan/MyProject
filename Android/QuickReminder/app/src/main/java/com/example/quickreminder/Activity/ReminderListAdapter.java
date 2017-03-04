package com.example.quickreminder.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quickreminder.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dell on 8/7/2016.
 */
public class ReminderListAdapter extends BaseAdapter {

    private Cursor mCursor;

    private Context mContext;

    private Set<String> isOpenMap;

    private int position ;

    public ReminderListAdapter(Context context, Cursor cursor, Set<String> isOpenMap) {
        mCursor = cursor;
        mContext = context;
        this.isOpenMap = isOpenMap;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public String getItem(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndex(ReminderDatabase.TASK_ID));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(ReminderActivity.TAG, "inside getView() of ReminderListAdapter : ");
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reminder_list_items, null, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTime);
            holder.llEditContainer = (LinearLayout) convertView.findViewById(R.id.llEditContainer);
            holder.llChangeContainer = (LinearLayout) convertView.findViewById(R.id.llChangeContainer);
            holder.llDeleteContainer = (LinearLayout) convertView.findViewById(R.id.llDeleteContainer);
            holder.ivNavigationArrow = (ImageView) convertView.findViewById(R.id.ivNavigationArrow);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mCursor.moveToPosition(position);
        String title = mCursor.getString(mCursor.getColumnIndex(ReminderDatabase.TASK_TITLE));
        String task_id = mCursor.getString(mCursor.getColumnIndex(ReminderDatabase.TASK_ID));
        Log.d(ReminderActivity.TAG,"Id : "+task_id);
        if (isOpenMap.contains(task_id)) {
            holder.llEditContainer.setVisibility(View.VISIBLE);
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_keyboard_arrow_up_black_24dp);
            drawable.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            holder.ivNavigationArrow.setImageDrawable(drawable);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_keyboard_arrow_down_black_24dp);
            holder.ivNavigationArrow.setImageDrawable(drawable);
            holder.llEditContainer.setVisibility(View.GONE);
        }
        long time = mCursor.getInt(mCursor.getColumnIndex(ReminderDatabase.TASK_TIMESTAMP));
        String formattedTime = ReminderHelper.getFromattedDate(time * 1000);
        //Log.d(ReminderActivity.TAG, "Title : " + title + " time : " + time);
        holder.tvTitle.setText(title);
        holder.tvTimestamp.setText(formattedTime);
        holder.llChangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ReminderActivity.TAG,"Position : "+position);
                mCursor.moveToPosition(position);
                String task_id = mCursor.getString(mCursor.getColumnIndex(ReminderDatabase.TASK_ID));
                Log.d(ReminderActivity.TAG,"Task ID : "+task_id);
                Intent intent = new Intent(mContext,CreateReminderActivity.class);
                intent.putExtra("IS_EDIT",true);
                intent.putExtra("TASK_ID",task_id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle;

        TextView tvTimestamp;

        LinearLayout llEditContainer;

        LinearLayout llChangeContainer;

        LinearLayout llDeleteContainer;

        ImageView ivNavigationArrow;

        ImageView ivEdit;

        ImageView ivDelete;
    }
}
