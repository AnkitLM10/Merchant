package com.example.merchant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bookingNewFragment extends Fragment {

    Context context = null;
    Dialog settingsDialog;
    CustomListView customListView;
    List<Booking> newBooking;
    ListView listView;


    Activity activity;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_past, container, false);
        context = container.getContext();
        newBooking = new ArrayList<>();
        // getting all new booking...................................
        collectingAllNewBooking(view);


        return view;
    }

    private void collectingAllNewBooking(final View view) {


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
                    Toast.makeText(context, "Problem Fetching Merchant Details", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                dialog.dismiss();
                Log.d("tag", response.toString());
                Log.d("tagSize", response.body().bookings.size() + "");

                for (int i = 0; i < response.body().bookings.size(); i++) {

                    if (response.body().bookings.get(i).status.equals("pending"))
                        newBooking.add(response.body().bookings.get(i));
                }


                listView = (ListView) view.findViewById(R.id.listViewPastBooking);

                customListView = new CustomListView(newBooking.size(), context, newBooking);
                listView.setAdapter(customListView);


            }


            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Problem Fetching Merchant Booking Details", Toast.LENGTH_LONG).show();
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
            return bookingList.size();
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
            String dateString = android.text.format.DateFormat.format("hh:mm aa dd MMM", bookingList.get(i).date).toString();
            holder.bookingDate.setText(dateString);
            holder.bookingAmount.setText(getResources().getString(R.string.rupee) + " " + bookingList.get(i).payableAmount);
            holder.mainLayoutBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingsDialog = new Dialog(context);
                    settingsDialog.setTitle("Booking Action!");
                    settingsDialog.setContentView(R.layout.dialog_box_layout);
                    settingsDialog.getWindow().setLayout(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    Button cancelButton = (Button) settingsDialog.findViewById(R.id.cancelBooking);
                    Button approveButton = (Button) settingsDialog.findViewById(R.id.approveBooking);
                    Button denyButton = (Button) settingsDialog.findViewById(R.id.denyBooking);
//                    Button completedButton = (Button) settingsDialog.findViewById(R.id.completedBooking);

//                    cancelButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(context, "cancel Clicked " + i, Toast.LENGTH_SHORT).show();
//
//                            bookingAction("cancel", bookingList.get(i), settingsDialog);
//
//                        }
//                    });


                    approveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "approve Clicked " + i, Toast.LENGTH_SHORT).show();
                            bookingAction("accepted", bookingList.get(i), settingsDialog);

                        }
                    });

                    denyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "deny Clicked " + i, Toast.LENGTH_SHORT).show();
                            bookingAction("denied", bookingList.get(i), settingsDialog);

                        }
                    });


//                    completedButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(context, "completed Clicked " + i, Toast.LENGTH_SHORT).show();
//                            bookingAction("completed", bookingList.get(i), settingsDialog);
//
//                        }
//                    });


                    settingsDialog.show();
                }
            });


            return row;
        }

        private void bookingAction(final String action, final Booking booking, final Dialog settingsDialog) {
            System.out.println(booking.bookingId + " Booking Id");
            System.out.println("Header :" + MerchantHome.token);
            BookingDataAction bookingDataAction = new BookingDataAction(booking.bookingId, action);
            Map<String, String> headers = new HashMap<>();
            headers.put("token", MerchantHome.token);
            Call<UpdateBookingStatus> loginResponse = Api_booking.getService().putBookingData(headers, bookingDataAction);
            loginResponse.enqueue(new Callback<UpdateBookingStatus>() {
                @Override
                public void onResponse(Call<UpdateBookingStatus> call, Response<UpdateBookingStatus> response) {

                    if (!response.isSuccessful()) {
                        Log.d("tag", response.toString());
                        settingsDialog.dismiss();
                        return;
                    }

                    if (action.equals("accepted")) {

                        // means this must now come in upcoming ones.............

                        System.out.println("Approve is clicked!");
                        System.out.println(bookingList.size()+" Before Removing");
                        bookingList.remove(booking);
                        System.out.println(bookingList.size()+" After Removing");
                        customListView.notifyDataSetChanged();

                    }else if(action.equals("denied"))
                    {
                        System.out.println("cancel is clicked!");
                        System.out.println(bookingList.size()+" Before Removing");
                        bookingList.remove(booking);
                        System.out.println(bookingList.size()+" After Removing");
                        customListView.notifyDataSetChanged();


                    }


                    Log.d("tag", response.toString());
                    Log.d("tag", response.body().object.status);
                    settingsDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UpdateBookingStatus> call, Throwable t) {
                    Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                    settingsDialog.dismiss();

                }
            });

        }


    }
}




