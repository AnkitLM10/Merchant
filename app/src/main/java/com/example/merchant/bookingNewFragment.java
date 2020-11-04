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
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_past, container, false);
        context = container.getContext();


        listView = (ListView) view.findViewById(R.id.listViewPastBooking);

        customListView = new CustomListView(MerchantHome.newBooking.size(), context, MerchantHome.newBooking);
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
            holder.bookingAmount.setText(getResources().getString(R.string.rupee) + " " + bookingList.get(i).payableAmount);
            holder.mainLayoutBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingsDialog = new Dialog(context);
                    settingsDialog.setTitle("Booking Action!");
                    settingsDialog.setContentView(R.layout.dialog_box_layout);
                    settingsDialog.getWindow().setLayout(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Button cancelButton = (Button) settingsDialog.findViewById(R.id.cancelBooking);
                    Button approveButton = (Button) settingsDialog.findViewById(R.id.approveBooking);
                    Button denyButton = (Button) settingsDialog.findViewById(R.id.denyBooking);
                    Button completedButton = (Button) settingsDialog.findViewById(R.id.completedBooking);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "cancel Clicked " + i, Toast.LENGTH_SHORT).show();

                            bookingAction("cancel", bookingList.get(i), settingsDialog);

                        }
                    });


                    approveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "approve Clicked " + i, Toast.LENGTH_SHORT).show();
                            bookingAction("approve", bookingList.get(i), settingsDialog);

                        }
                    });

                    denyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "deny Clicked " + i, Toast.LENGTH_SHORT).show();
                            bookingAction("deny", bookingList.get(i), settingsDialog);

                        }
                    });


                    completedButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "completed Clicked " + i, Toast.LENGTH_SHORT).show();
                            bookingAction("completed", bookingList.get(i), settingsDialog);

                        }
                    });


                    settingsDialog.show();
                }
            });


            return row;
        }

        private void bookingAction(final String action, final Booking booking, final Dialog settingsDialog) {

            BookingDataAction bookingDataAction = new BookingDataAction(booking.bookingId, action);
            Map<String, String> headers = new HashMap<>();
            headers.put("token", MerchantHome.token);
            Call<UpdateBookingStatus> loginResponse = Api_booking.getService().putBookingData(headers, bookingDataAction);
            loginResponse.enqueue(new Callback<UpdateBookingStatus>() {
                @Override
                public void onResponse(Call<UpdateBookingStatus> call, Response<UpdateBookingStatus> response) {

                    if (!response.isSuccessful()) {
                        Log.d("tag", response.toString());
                        return;
                    }

                    if (action.equals("approve")) {

                        // means this must now come in upcoming ones.............
                        MerchantHome.upcomingBooking.add(booking);
                        MerchantHome.newBooking.remove(booking);
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




