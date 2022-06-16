package com.freeme.smartscan;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final Context mContext;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private final ArrayList<String> mTitleList;

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public TabsAdapter(FragmentActivity activity, ViewPager pager, ArrayList<String> titleList) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = pager;
        mViewPager.addOnPageChangeListener(this);
        this.mTitleList = titleList;
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        mTabs.add(info);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        if (info.fragment == null) {
            info.fragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }
        return info.fragment;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = mTitleList.get(position);
        return title;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i, false);
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
