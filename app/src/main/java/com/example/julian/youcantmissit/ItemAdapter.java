package com.example.julian.youcantmissit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Initially Created by Julian on 2015-11-25.
 * Lastly modified by Julian on 2015-11-25.
 */
public class ItemAdapter extends BaseAdapter {
    Context context;
    ArrayList<LocationData> dataList;
    private static LayoutInflater inflater = null;

    public ItemAdapter(Context context, ArrayList<LocationData> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi==null) {
            vi = inflater.inflate(R.layout.location_item,parent,false);
            Switch activeSwitch = (Switch)vi.findViewById(R.id.item_switch);
            TextView itemName = (TextView)vi.findViewById(R.id.item_name);

            activeSwitch.setChecked(dataList.get(position).isActivated());
            itemName.setText(dataList.get(position).getName());
        }
        return vi;
    }
}
