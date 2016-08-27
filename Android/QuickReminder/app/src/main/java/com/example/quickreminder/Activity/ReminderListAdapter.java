package com.example.quickreminder.Activity;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickreminder.R;

/**
 * Created by Dell on 8/7/2016.
 */
public class ReminderListAdapter extends BaseAdapter {

    private Cursor mCursor;

    private Context mContext;

    public ReminderListAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Cursor getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(ReminderActivity.TAG,"inside getView() of ReminderListAdapter : ");
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reminder_list_items,null,false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mCursor.moveToPosition(position);
        String title = mCursor.getString(mCursor.getColumnIndex(ReminderDatabase.TASK_TITLE));
        long time = mCursor.getInt(mCursor.getColumnIndex(ReminderDatabase.TASK_TIMESTAMP));
        String formattedTime = ReminderHelper.getFromattedDate(time*1000);
        Log.d(ReminderActivity.TAG,"Title : "+title+" time : "+time);
        holder.tvTitle.setText(title);
        holder.tvTimestamp.setText(formattedTime);
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle;

        TextView tvTimestamp;

        ImageView ivEdit;

        ImageView ivDelete;
    }
}
