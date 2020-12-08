package com.example.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class bookingFragment extends Fragment implements MerchantHome.MyInterface{


    BottomNavigationView bottomNavigationView;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.merchantBookingPast:
                    fragment = new bookingPastFragment();
                    break;
                case R.id.merchantBookingNew:
                    fragment = new bookingNewFragment();
                    break;
                case R.id.merchantBookingUpcoming:
                    fragment = new bookingUpcomingFragment();
                    break;
            }
            getFragmentManager().beginTransaction().replace(R.id.merchantBookingNavbarContainer, fragment).commit();
            return true;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookings, container, false);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.merchantBookingNavbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getFragmentManager().beginTransaction().replace(R.id.merchantBookingNavbarContainer, new bookingNewFragment()).commit();
        return view;
    }

    public void update() {
        getFragmentManager().beginTransaction().replace(R.id.merchantBookingNavbarContainer, new bookingNewFragment()).commit();

    }


    @Override
    public void myAction() {
        getFragmentManager().beginTransaction().replace(R.id.merchantBookingNavbarContainer, new bookingNewFragment()).commit();
    }
}
