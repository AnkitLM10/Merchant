package com.example.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Button categorySelected;
    String token = "";
    HashMap<Integer, Boolean> isChecked = new HashMap<>();
    Set<Integer> servicesAlreadyOffered;
    HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap;

    // When Button will be clicked then this hashmap will contain info about which ServiceId are Clicked and What Are Corresponding Data mapped to it.
    HashMap<Integer, Data> serviceIdToData;
    int selected = 0;

    class ViewHolder {
        private HashMap<Integer, View> storedViews = new HashMap<Integer, View>();

        public ViewHolder() {
        }

        /**
         * @param view The view to add; to reference this view later, simply refer to its id.
         * @return This instance to allow for chaining.
         */
        public ViewHolder addView(View view) {
            int id = view.getId();
            storedViews.put(id, view);
            return this;
        }

        public View getView(int id) {
            return storedViews.get(id);
        }
    }


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Data>> _listDataChild;

    public ExpandableListAdapter(final Context context, List<String> listDataHeader,
                                 HashMap<String, List<Data>> listChildData, Button categorySelected, final HashMap<Integer, Data> serviceIdToData, final String token, Set<Integer> servicesAlreadyOffered, final HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.categorySelected = categorySelected;
        this.serviceIdToData = serviceIdToData;
        this.token = token;
        this.servicesAlreadyOffered = servicesAlreadyOffered;
        this.serviceOfferedToDataMappingHashMap = serviceOfferedToDataMappingHashMap;
        this.categorySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Data> serviceSelected = new ArrayList<>();
                for (int serviceID : isChecked.keySet()) {
                    //Now if this serviceId is selected then add to ListView....................
                    if (isChecked.get(serviceID)) {
                        serviceSelected.add(serviceIdToData.get(serviceID));
                        Log.d("selected", serviceIdToData.get(serviceID).service);
                    }
                }

                if (serviceSelected.size() == 0) {
                    //means no service is selected..............bluffing......
                    return;
                }
                Intent intent = new Intent(context, AddServiceDetail.class);

                intent.putExtra("Token", token);
                Bundle args = new Bundle();
                args.putSerializable("ListData", (Serializable) serviceSelected);
                args.putSerializable("serviceOfferedToDataMappingHashMap", (Serializable) serviceOfferedToDataMappingHashMap);
                intent.putExtra("BUNDLE", args);
                context.startActivity(intent);

            }
        });


    }


    @Override
    public Data getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Data data = getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_list, null);
        }
//
//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.lblListItem);
//
//        txtListChild.setText(childText);

        TextView service = (TextView) convertView.findViewById(R.id.childServiceTitle);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.parentCategoryAddPortfolioCheckbox);
        checkBox.setFocusable(false);
        checkBox.setOnCheckedChangeListener(null);
        if (!isChecked.containsKey(data.serviceId))
            isChecked.put(data.serviceId, false);
        checkBox.setChecked(isChecked.get(data.serviceId));

        if (isChecked.get(data.serviceId))
            checkBox.setText("");
        else if (servicesAlreadyOffered.contains(data.serviceId))
            checkBox.setText("Already Offered");
        else
            checkBox.setText("ADD +");

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked.put(data.serviceId, b);
                if (b) {
                    selected++;
                    checkBox.setText("");
                } else {
                    selected--;
                    if (servicesAlreadyOffered.contains(data.serviceId))
                        checkBox.setText("Already Offered");
                    else
                        checkBox.setText("ADD +");

                }
                if (selected > 0)
                    categorySelected.setText("Continue with (" + selected + ") selected service");
                else
                    categorySelected.setText("");
            }
        });


//        if(isChecked.get(data.serviceId) == true)
//        {
//            checkBox.setText("");
//        }
//        else
//        {
//            checkBox.setText("ADD +");
//        }


        service.setText(data.service);


        return convertView;


        /*
          Toast.makeText(_context, childPosition + "", Toast.LENGTH_SHORT).show();
        Data data = getChild(groupPosition, childPosition);
        View row = view;
        MyViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.child_list, null);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        } else
            holder = (MyViewHolder) row.getTag();

        holder.service.setText(data.service);


        return row;
         */
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public java.lang.Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_list, null);
        }

//        TextView lblListHeader = (TextView) convertView
//                .findViewById(R.id.lblListHeader);
//        lblListHeader.setTypeface(null, Typeface.BOLD);
//        lblListHeader.setText(headerTitle);

        TextView categoryTitle = (TextView) convertView.findViewById(R.id.parentCategoryPortfolioTitle);
        TextView categorySize = (TextView) convertView.findViewById(R.id.parentCategorySizePortfolio);
        categorySize.setText(_listDataChild.get(headerTitle).size() + " Items");

        categoryTitle.setText(headerTitle);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class OnClickChangedList {
        int serviceId;
        CheckBox checkBox;

        OnClickChangedList(int serviceId, CheckBox checkBox) {
            this.serviceId = serviceId;
            this.checkBox = checkBox;
        }


        void setClickList() {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    isChecked.put(serviceId, b);
                }
            });
        }


        void remove() {
            checkBox.setOnCheckedChangeListener(null);
        }

    }
}

