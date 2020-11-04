package com.example.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


class ExpandableAdapterListServicesOfferedextends extends BaseExpandableListAdapter {
    String token = "";
    String MerchantId = "";
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    HashMap<String, List<ServiceDataList>> _listDataChild;

    public ExpandableAdapterListServicesOfferedextends(Context context, List<String> listDataHeader,
                                                       HashMap<String, List<ServiceDataList>> listChildData, String token, String merchantId) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.token = token;
        this.MerchantId = merchantId;
    }

    @Override
    public java.lang.Object getChild(int groupPosition, int childPosititon) {
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

        final ServiceDataList serviceDataList = (ServiceDataList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.services_offered_child_list, null);
        }
        String rupee = "\u20B9";
        TextView serviceOfferedChildTitle = (TextView) convertView.findViewById(R.id.serviceOfferedChildTitle);
        TextView serviceOfferedPriceAndDiscount = (TextView) convertView.findViewById(R.id.serviceOfferedPriceAndDiscount);
        ImageView servicesOfferedEdit = (ImageView) convertView.findViewById(R.id.servicesOfferedEdit);
        serviceOfferedChildTitle.setText(serviceDataList.service.service);
        serviceOfferedPriceAndDiscount.setText(rupee + " " + serviceDataList.price + "   " + serviceDataList.discountPercentage + " %     Off");
        servicesOfferedEdit.setOnClickListener(null);
        servicesOfferedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, UpdateSingleService.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("itemId", serviceDataList.service.serviceId+"");
                intent.putExtra("price", serviceDataList.price+"");
                intent.putExtra("discountPercentage", serviceDataList.discountPercentage+"");
                intent.putExtra("serviceTitle", serviceDataList.service.service);
                _context.startActivity(intent);
            }
        });

        return convertView;
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
            convertView = infalInflater.inflate(R.layout.service_offered_parent_list, null);
        }

        TextView categoryTitle = (TextView) convertView
                .findViewById(R.id.serviceOfferedCategoryTitle);
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
}