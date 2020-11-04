package com.example.merchant;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bookingUpcomingFragment extends Fragment {

    Context context = null;
    Dialog settingsDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_past, container, false);
        context = container.getContext();


        ListView listView = (ListView) view.findViewById(R.id.listViewPastBooking);

        CustomListView customListView = new CustomListView(MerchantHome.upcomingBooking.size(), context, MerchantHome.upcomingBooking);
        listView.setAdapter(customListView);

        return view;
    }

    class CustomListView extends BaseAdapter {
        int size = 0;
        Context context;
        List<Booking> bookingList;

        class MyViewHolder {
            TextView bookingDate;
            TextView bookingAmount;
            RelativeLayout mainLayoutBooking;

            MyViewHolder(View view) {
                bookingDate = (TextView) view.findViewById(R.id.listViewPastBookingDate);
                bookingAmount = (TextView) view.findViewById(R.id.listViewPastBookingAmount);
                mainLayoutBooking = (RelativeLayout) view.findViewById(R.id.mainLayoutBooking);
            }

        }


        CustomListView(int size, Context context, List<Booking> bookingList) {
            this.size = size;
            this.context = context;
            this.bookingList = bookingList;

        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View row = view;
            MyViewHolder holder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.listview_past_booking, null);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            } else
                holder = (MyViewHolder) row.getTag();
            String dateString = android.text.format.DateFormat.format("hh:mm aa dd MMMM", bookingList.get(i).date).toString();
            holder.bookingDate.setText(dateString);
            //            holder.bookingDate.setText(bookingList.get(i).date.toString());
            holder.bookingAmount.setText(getResources().getString(R.string.rupee) +" "+ bookingList.get(i).payableAmount);


            return row;
        }


    }
}




