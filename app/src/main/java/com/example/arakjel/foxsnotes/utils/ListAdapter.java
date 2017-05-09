package com.example.arakjel.foxsnotes.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.arakjel.foxsnotes.R;

import java.util.List;



public class ListAdapter extends BaseAdapter {

    private int position;
    private List<String> mItemList;
    private Activity mContext;
    private boolean[] itemChecked;
    public boolean checkAll = false;
    public boolean unCheckAll = false;
    public boolean isCheckingAvailable = false;

    public ListAdapter(Activity context, List<String> itemList) {
        super();
        this.mContext = context;
        this.mItemList = itemList;
        itemChecked = new boolean[itemList.size()];
    }

    private class ViewHolder {
        TextView rowLabel;
        CheckBox checkBox;
    }

    public int getCount() {
        return mItemList.size();
    }

    public Object getItem(int position) {
        return mItemList.get(position);
    }

    public boolean[] getItemChecked() {
        return itemChecked;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_layout, null);
            holder = new ViewHolder();
            holder.rowLabel = (TextView) convertView.findViewById(R.id.rowLabel);
            String[] stringArray = mItemList.toArray(new String[0]);
            holder.rowLabel.setText(stringArray[position]);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isCheckingAvailable) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        if (itemChecked[position]) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        if (checkAll) {
            holder.checkBox.setChecked(true);
            int max = getCount();
            itemChecked[position] = true;
            if (position == (max - 1)) {
                checkAll = false;
            }
        }

        if (unCheckAll) {
            holder.checkBox.setChecked(false);
            int max = getCount();
            itemChecked[position] = false;
            if (position == (max - 1)) {
                unCheckAll = false;
            }
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    itemChecked[position] = true;
                } else {
                    itemChecked[position] = false;
                }
            }
        });
        return convertView;
    }
}
