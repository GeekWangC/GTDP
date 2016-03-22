package com.glodon.easymedicalapptosvn.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.glodon.easymedicalapptosvn.BaseActivity;
import com.glodon.easymedicalapptosvn.BaseApplication;
import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;
import com.glodon.easymedicalapptosvn.utils.StringTools;
import com.glodon.easymedicalapptosvn.utils.ToastUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;


/**
 * Created by shirr on 2015/12/8.
 */
public class WebviewMessageActivity extends BaseActivity {

    private WebView mEasyWebview;
    //从主界面出来的url来判断跳转方向
    private String mUrl;

    private SharePreferenceUtils sharePreferenceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }

        findView();
    }

    public void findView() {
        sharePreferenceUtils = new SharePreferenceUtils(WebviewMessageActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEasyWebview = (WebView) findViewById(R.id.html_webview);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(mEasyWebview, true);
        } else {
            mEasyWebview = (WebView) findViewById(R.id.html_webview);
        }

        //如果访问的页面中有 Javascript，则 webview 必须设置支持 Javascript
        WebviewMessageActivity.this.mEasyWebview.getSettings().setJavaScriptEnabled(true);
        // 设置加载进来的页面自适应手机屏幕
        WebviewMessageActivity.this.mEasyWebview.getSettings().setUseWideViewPort(true);
        //任意比例缩放
        WebviewMessageActivity.this.mEasyWebview.getSettings().setLoadWithOverviewMode(true);
        //设置用鼠标激活被选项
        WebviewMessageActivity.this.mEasyWebview.getSettings().setLightTouchEnabled(false);
        //启用内置缩放
        WebviewMessageActivity.this.mEasyWebview.getSettings().setBuiltInZoomControls(false);
        //设置是否支持变焦
        WebviewMessageActivity.this.mEasyWebview.getSettings().setSupportZoom(false);
        //是否显示网络图像(false阻止图片网络数据)
        WebviewMessageActivity.this.mEasyWebview.getSettings().setBlockNetworkImage(false);
        //设置缓存
        WebviewMessageActivity.this.mEasyWebview.getSettings().setDomStorageEnabled(false);
        //设置布局方式
        WebviewMessageActivity.this.mEasyWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        WebviewMessageActivity.this.mEasyWebview.setBackgroundColor(Color.parseColor("#00000000"));
        WebviewMessageActivity.this.mEasyWebview.setBackgroundResource(R.drawable.white);
        WebviewMessageActivity.this.mEasyWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        WebviewMessageActivity.this.mEasyWebview.loadData(null, "text/html", "UTF-8");

        //设置缓存
        WebviewMessageActivity.this.mEasyWebview.getSettings().setDomStorageEnabled(true);
        WebviewMessageActivity.this.mEasyWebview.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        WebviewMessageActivity.this.mEasyWebview.getSettings().setAppCachePath(appCachePath);
        WebviewMessageActivity.this.mEasyWebview.getSettings().setAllowFileAccess(true);
        WebviewMessageActivity.this.mEasyWebview.getSettings().setAppCacheEnabled(true);


        WebviewMessageActivity.this.mEasyWebview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        WebviewMessageActivity.this.mEasyWebview.loadUrl(mUrl);
        //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        WebviewMessageActivity.this.mEasyWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

        });

    }

    @Override
    public void setListener() {

    }

    @Override
    public void logic() {

    }

    //点击web界面进行跳转
    class InJavaScriptLocalObj {
        public void showSource(String html) {
            printMessage(html);
        }

        // 获取登录结果
        @JavascriptInterface
        public void getLoginResult(String loginResult, String userInfo) {
            if (!StringTools.isEmpty(userInfo)) {
                sharePreferenceUtils.put("userInfo", userInfo);
            }
            //if (loginResult == "1") {
                Intent intent = new Intent(WebviewMessageActivity.this, MainActivity.class);
                startActivity(intent);
                WebviewMessageActivity.this.finish();
                BaseApplication.setUserSession(userInfo);
                sharePreferenceUtils.put("islogin","yes");
            //}

        }

        // 传递参数给html页面，确定调用的界面链接
        /*@JavascriptInterface
        public String getHTMLPageURL() {
        mUrl = "file:///android_asset/www/qpkml.html";
        return htmlPage;
        }*/
        //调用js获取返回
        @JavascriptInterface
        public void jsGetBack(String isBack){
            if(!Utils.isEmpty(isBack)){
                WebviewMessageActivity.this.finish();
            }
        }

        /*@JavascriptInterface
        public void getPhoto(String imageOrCame){
            Intent intent = new Intent(WebviewActivity.this,PhotoActivity.class);
            intent.putExtra("imageOrCame",imageOrCame);
            startActivity(intent);
        }*/
        @JavascriptInterface
        public void getPhoto(){
            Intent intent = new Intent(WebviewMessageActivity.this,PhotoActivity.class);
            //intent.putExtra("imageOrCame",imageOrCame);
            startActivity(intent);
        }

        @JavascriptInterface
        public void MessageJump(String url){
            Intent intent = new Intent(WebviewMessageActivity.this,WebviewActivity.class);
            intent.putExtra("url","file:///android_asset/www/"+url);
            startActivity(intent);
        }


        //获取视频播放的url
        @JavascriptInterface
        public void jsMethod(String url,String id){
            Intent intent = new Intent(WebviewMessageActivity.this,VideoPlayerActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("id",id);
            startActivity(intent);
            ToastUtils.showShort(WebviewMessageActivity.this,id);
        }
    }

    public void printMessage(String message) {

    }

    //拦截activity 的后退键处理
  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return  super.onKeyDown(keyCode, event);
    }*/
}
