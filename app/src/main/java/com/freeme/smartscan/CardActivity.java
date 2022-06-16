package com.freeme.smartscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.freeme.smartscan.fragment.BuscardFragment;
import com.freeme.smartscan.fragment.CardFragment;
import com.freeme.smartscan.view.CustomViewPager;
import com.freeme.smartscan.view.FreemeTabLayout;

import java.util.ArrayList;

public class CardActivity extends FragmentActivity {

    public CustomViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        initViewPager();
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mViewPager, new ArrayList<String>() {{
            add(getString(R.string.card_identity));
            add(getString(R.string.card_bussiness));
        }});

        mTabsAdapter.addTab(null, CardFragment.class, null);
        mTabsAdapter.addTab(null, BuscardFragment.class, null);
        mViewPager.setAdapter(mTabsAdapter);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            FreemeTabLayout tabs = (FreemeTabLayout) View.inflate(this,
                    R.layout.layout_scrolling_tab_component, null);
            tabs.setupWithViewPager(mViewPager);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER_HORIZONTAL);
            actionBar.setCustomView(tabs, lp);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}