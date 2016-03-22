package com.glodon.easymedicalapptosvn.activity;
/**
 * Created by shirr on 2015/12/4.
 */

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.service.MyService;


public class MainActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {

    //tab切换
    private TabHost mTabHost;
    private RadioGroup radioderGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
    }

    public void findView() {
        //实例化TabHost
        mTabHost = MainActivity.this.getTabHost();
        radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
        radioderGroup.setOnCheckedChangeListener(this);

        //添加选项卡
        TabHost.TabSpec tabHome = mTabHost.newTabSpec("homepage");
        tabHome.setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_homepage, null)).setContent(new Intent(this, HomePageActivity.class));
        mTabHost.addTab(tabHome);

        TabHost.TabSpec tabCourse = mTabHost.newTabSpec("course");
        tabCourse.setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_course, null)).setContent(new Intent(this, WebviewCourseActivity.class).putExtra("url", "file:///android_asset/www/course.html"));
        mTabHost.addTab(tabCourse);

        TabHost.TabSpec tabPractice = mTabHost.newTabSpec("practice");
        tabPractice.setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_practice, null)).setContent(new Intent(this, WebviewPracticeActivity.class).putExtra("url", "file:///android_asset/www/lianxi.html"));
        mTabHost.addTab(tabPractice);

        TabHost.TabSpec tabMy = mTabHost.newTabSpec("my");
        tabMy.setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_my, null)).setContent(new Intent(this, WebviewMyActivity.class).putExtra("url", "file:///android_asset/www/myinfo.html"));
        //tabMy.setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_my, null)).setContent(new Intent(this, SetActivity.class));
        mTabHost.addTab(tabMy);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.homepage_radiobtn:
                mTabHost.setCurrentTabByTag("homepage");
                break;
            case R.id.course_radiobtn:
                mTabHost.setCurrentTabByTag("course");
                break;
            case R.id.practice_radiobtn:
                mTabHost.setCurrentTabByTag("practice");
                break;
            case R.id.my_radiobtn:
                mTabHost.setCurrentTabByTag("my");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
