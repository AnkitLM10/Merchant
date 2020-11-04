package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Merchant_Category_id extends AppCompatActivity {
    String token = "";
    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant__category_id);
        token = getIntent().getStringExtra("Token");
        categoryId = getIntent().getStringExtra("Id");





    }


//    class CustomListView extends BaseAdapter {
//        int size = 0;
//        Context context;
//        List<CategoryIdData> dataList;
//
//        class MyViewHolder {
//            TextView service;
//            TextView serviceId;
//            TextView serviceCategoryId;
//            CheckBox checkBox;
//            EditText price, discount;
//            Button saveServiceDetailButton;
//
//            MyViewHolder(View view) {
//                service = (TextView) view.findViewById(R.id.service);
//                serviceId = (TextView) view.findViewById(R.id.serviceId);
//                serviceCategoryId = (TextView) view.findViewById(R.id.serviceCategoryId);
//                checkBox = (CheckBox) view.findViewById(R.id.currentCheckBox);
//                price = (EditText) view.findViewById(R.id.categoryPrice);
//                discount = (EditText) view.findViewById(R.id.categoryDiscount);
//                saveServiceDetailButton = (Button) view.findViewById(R.id.saveServiceDetail);
//                price.setVisibility(View.GONE);
//                discount.setVisibility(View.GONE);
//                saveServiceDetailButton.setVisibility(View.GONE);
//            }
//
//        }
//
//
//        CustomListView(int size, Context context, List<CategoryIdData> dataList) {
//            this.size = size;
//            this.context = context;
//            this.dataList = dataList;
//        }
//
//        @Override
//        public int getCount() {
//            return size;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int i, View view, ViewGroup viewGroup) {
//            View row = view;
//            MyViewHolder holder = null;
//            if (view == null) {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.category_service_id, null);
//                holder = new MyViewHolder(row);
//                row.setTag(holder);
//            } else
//                holder = (MyViewHolder) row.getTag();
//
//            holder.service.setText(dataList.get(i).service);
//            holder.serviceId.setText(dataList.get(i).serviceId + "");
//            holder.serviceCategoryId.setText(dataList.get(i).serviceCategoryId + "");
//            final MyViewHolder finalHolder = holder;
//            holder.saveServiceDetailButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ServicePriceDiscount servicePriceDiscount = listViewInfo.get(i);
//                    if (finalHolder.price.getText().toString().equals("") ||
//                            finalHolder.discount.getText().toString().equals(""))
//                        return;
//                    servicePriceDiscount.price = Integer.valueOf(finalHolder.price.getText().toString());
//                    servicePriceDiscount.serviceId = Integer.valueOf(dataList.get(i).serviceId);
//                    servicePriceDiscount.discount = Integer.valueOf(finalHolder.discount.getText().toString());
//                }
//            });
//
//
//            holder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (finalHolder.checkBox.isChecked()) {
//                        finalHolder.price.setVisibility(View.VISIBLE);
//                        finalHolder.discount.setVisibility(View.VISIBLE);
//                        finalHolder.saveServiceDetailButton.setVisibility(View.VISIBLE);
//                    } else {
//                        finalHolder.price.setVisibility(View.GONE);
//                        finalHolder.discount.setVisibility(View.GONE);
//                        finalHolder.saveServiceDetailButton.setVisibility(View.GONE);
//
//                    }
//                }
//            });
//
//
//            return row;
//        }
//    }


}