package com.glodon.easymedicalapptosvn.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.adapter.ViewPagerAdapter;
import com.glodon.easymedicalapptosvn.fragment.Fragment1;
import com.glodon.easymedicalapptosvn.fragment.Fragment2;
import com.glodon.easymedicalapptosvn.fragment.Fragment3;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shirr on 2015/12/21.
 */
public class GuideActivity extends FragmentActivity {
    private ViewPager mVPActivity;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private PagerAdapter mPgAdapter;

    private SharePreferenceUtils mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mSP = new SharePreferenceUtils(GuideActivity.this);
        mSP.put("islogin","yes");
        initView();
    }

    private void initView() {
        mVPActivity = (ViewPager) findViewById(R.id.vp_activity);
        mFragment1 = new Fragment1();
        mFragment2 = new Fragment2();
        mFragment3 = new Fragment3();
        mListFragment.add(mFragment1);
        mListFragment.add(mFragment2);
        mListFragment.add(mFragment3);
        mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
    }
}
