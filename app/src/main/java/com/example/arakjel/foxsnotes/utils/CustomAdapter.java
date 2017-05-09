package com.example.arakjel.foxsnotes.utils;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.arakjel.foxsnotes.R;



public class CustomAdapter extends BaseAdapter {
    String[] result;
    Context context;
    Holder holder;
    Boolean aBoolean;
    Boolean mIsSelected;
    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity activity, String[] prgmNameList, boolean isVisible, boolean isSelected) {
        result = prgmNameList;
        context = activity;
        aBoolean = isVisible;
        mIsSelected = isSelected;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new Holder();
        View rowView = inflater.inflate(R.layout.row_layout, null);
        holder.tv = (TextView) rowView.findViewById(R.id.rowLabel);
        holder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        holder.checkBox.setVisibility(View.GONE);
        holder.tv.setText(result[position]);
        setCheckBoxesVisibility(aBoolean);
        selectCheckBoxes(mIsSelected);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked! " + position);
            }
        });

        return rowView;
    }

    public void setCheckBoxesVisibility(boolean isVisible) {
        if (isVisible) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    public void selectCheckBoxes(boolean isSelected) {
        holder.checkBox.setChecked(isSelected);
    }
}
