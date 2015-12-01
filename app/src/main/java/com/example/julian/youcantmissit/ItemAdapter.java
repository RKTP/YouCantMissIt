package com.example.julian.youcantmissit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
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
    DBManager dbManager;

    public ItemAdapter(Context context, ArrayList<LocationData> dataList) {
        this.context = context;
        this.dataList = dataList;
        dbManager = DBManager.getInstance(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Switch activeSwitch=null;
        TextView itemName=null;
        if(vi==null) {
            vi = inflater.inflate(R.layout.location_item,parent,false);
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocationDetailActivity.class);
                intent.putExtra("lat",dataList.get(position).getLat());
                intent.putExtra("lng", dataList.get(position).getLng());
                intent.putExtra("name", dataList.get(position).getName());
                context.startActivity(intent);
            }
        });

        vi.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                final int pos = position;
                final int key = dataList.get(pos).getKey();
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                adBuilder.setTitle("Removing Location");
                adBuilder
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.delete(dataList.get(pos).getKey());
                                dataList.remove(pos);
                                ItemAdapter.this.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = adBuilder.create();
                dialog.show();
                return true;
            }
        });

        activeSwitch = (Switch)vi.findViewById(R.id.item_switch);
        itemName = (TextView)vi.findViewById(R.id.item_name);
        activeSwitch.setChecked(dataList.get(position).isActivated());
        itemName.setText(dataList.get(position).getName());

        Switch switchWid = (Switch) vi.findViewById(R.id.item_switch);

        switchWid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationData location = dataList.get(position);
                location.activationSwap();
                dbManager.changeActive(location.getKey(), location.isActivated());
                ItemAdapter.this.notifyDataSetChanged();
                LocationService.updateTargetLocation();
            }
        });
        return vi;
    }

}
