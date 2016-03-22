package com.glodon.easymedicalapptosvn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Timer;

/**
 *
 * 基类Activity
 *
 * Created by shirr on 2015/10/16.
 */
public abstract class BaseActivity extends Activity {

    /**
     * Application
     */
    private BaseApplication mAppManager;

    /**
     * Loading
     */
    public ProgressDialog mProgress = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAppManager = BaseApplication.getInstance();
        mAppManager.mScreenManager.pushActivity(this);
        super.onCreate(savedInstanceState);
        mAppManager.mScreenManager.pushActivity(this);


    }

    /**
     * @param
     * @return void 返回类型
     * @throws
     * @Title: findView
     * @Description: 查找控件
     */
    public abstract void findView();

    /**
     * @param
     * @return void 返回类型
     * @throws
     * @Title: setListener
     * @Description: 设置事件监听
     */
    public abstract void setListener();

    /**
     * @param
     * @return void 返回类型
     * @throws
     * @Title: processLogic
     * @Description: 处理业务逻辑
     */
    public abstract void logic();

    /**
     * @param @param  id 控件id
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     * @Title: getViewById
     * @Description: 根据控件id查找控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getViewById(int id) {
        return (T) this.findViewById(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    public void onDestory() {
        super.onDestroy();
        mAppManager.mScreenManager.popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
        this.finish();
    }

}
