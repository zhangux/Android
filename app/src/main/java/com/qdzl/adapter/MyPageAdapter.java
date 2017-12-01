package com.qdzl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by QDZL on 2017/11/30.
 */
public class MyPageAdapter extends FragmentPagerAdapter {
    List<Fragment>  fragments;
    public MyPageAdapter(FragmentManager fm,List<Fragment>  fragments) {
        super(fm);
        this.fragments=fragments;
    }



    @Override
    public Fragment getItem(int position) {
        return fragments!=null?fragments.get(position):null;
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }
}
