package com.example.merchant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bookingPastFragment extends Fragment {


    Context context;
    ListView listView;
    CustomListView customListView;
    List<Booking> pastBooking;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_past, container, false);
//        TextView textView = (TextView) view.findViewById(R.id.pastBookingTextView);
//        textView.setText(MerchantHome.pastBooking.size() + "");
        pastBooking = new ArrayList<>();
        context = container.getContext();
        collectingPastBooking(view);

        return view;
    }


    private void collectingPastBooking(final View view) {


        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Fetching Merchant Booking Details...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        System.out.println("inside booking merchant " + MerchantHome.token);
        Map<String, String> headers = new HashMap<>();
        Log.d("header from Details", MerchantHome.token);
        headers.put("token", MerchantHome.token);
        Call<BookingResponse> loginResponse = Api_booking.getService().getAllBooking(headers);
        loginResponse.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    Toast.makeText(getContext(), "Problem Fetching Merchant Details", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                dialog.dismiss();
                Log.d("tag", response.toString());
                Log.d("tagSize", response.body().bookings.size() + "");

                for (int i = 0; i < response.body().bookings.size(); i++) {

                    if (response.body().bookings.get(i).status.equals("expired") || response.body().bookings.get(i).status.equals("completed") || response.body().bookings.get(i).status.equals("denied"))
                        pastBooking.add(response.body().bookings.get(i));
                }

                listView = (ListView) view.findViewById(R.id.listViewPastBooking);

                customListView = new CustomListView(pastBooking.size(), context, pastBooking);
                listView.setAdapter(customListView);


            }


            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Problem Fetching Merchant Booking Details", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                System.out.println(t.getCause().getMessage());
                System.out.println("fail......... " + call.request().url() + "");
                System.out.println("fail......... " + call.toString() + "pending");

            }
        });


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
            holder.bookingAmount.setText(getResources().getString(R.string.rupee) + " " + bookingList.get(i).payableAmount);

//
//            holder.service.setText(dataList.get(i).service);
//            holder.serviceId.setText(dataList.get(i).serviceId + "");
//            holder.serviceCategoryId.setText(dataList.get(i).serviceCategoryId + "");


            return row;
        }
    }


}
