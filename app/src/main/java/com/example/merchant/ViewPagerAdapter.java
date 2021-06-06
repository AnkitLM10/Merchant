package com.example.merchant;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new bookingNewFragment();
        }
        else if (position == 1)
        {
            fragment = new bookingUpcomingFragment();
        }
        else if (position == 2)
        {
            fragment = new bookingPastFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "NEW";
        }
        else if (position == 1)
        {
            title = "UPCOMING";
        }
        else if (position == 2)
        {
            title = "PAST";
        }
        return title;
    }
}