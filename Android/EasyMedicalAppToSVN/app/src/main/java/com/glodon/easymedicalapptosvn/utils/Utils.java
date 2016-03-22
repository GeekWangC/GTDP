package com.glodon.easymedicalapptosvn.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 工具类
 */
public class Utils {

    /**
     * @author shirr
     * 判断是否是wifi 还是流量
     * 0: 没网， 1：流量 2：wifi
     */
    public static final int NET_UNAVAIL = 0;
    public static final int NET_DATA = 1;
    public static final int NET_WIFI = 2;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm;
    }

    /**
     * 获取分辨率
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetricsForActivity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * shirr
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    //密码加密
    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //从share中获取用户登录成功以后返回的数据
    //获取用户Id
    public static String userId(Context context) {
        String u_id = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_id = share.getString("u_id", "");
        return u_id;
    }

    //获取域账号
    public static String getAdomainaccount(Context context) {
        String name = "";
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context);
        name = sharePreferenceUtils.getString("account", "");
        return name;
    }

    //获取用户名
    public static String userName(Context context) {
        String u_name = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_name = share.getString("u_name", "");
        return u_name;
    }

    //获取部门名字
    public static String userPart(Context context) {
        String u_partname = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_partname = share.getString("u_part", "");
        return u_partname;
    }

    //获取电子邮箱
    public static String userEmail(Context context) {
        String u_email = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_email = share.getString("u_email", "");
        return u_email;
    }

    //获取性別
    public static String userSex(Context context) {
        String u_sex = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_sex = share.getString("u_sex", "");
        return u_sex;
    }

    //获取用戶头像
    public static String userPhoto(Context context) {
        String u_photo = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_photo = share.getString("u_photo", "");
        return u_photo;
    }

    //获取手机号码
    public static String userPhone(Context context) {
        String u_phonenum = "";
        SharePreferenceUtils share = new SharePreferenceUtils(context);
        u_phonenum = share.getString("u_phone", "");
        return u_phonenum;
    }

    //日期转化
    public static String strToDateLong(String strDate) {
        strDate = strDate.substring(6, strDate.length() - 2);
        Date _data = new Date(Long.valueOf(strDate));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:m:s");
        return formatter.format(_data).toString();
    }

    //日期转化
    public static String strToDateLong2(String strDate) {
        strDate = strDate.substring(6, strDate.length() - 2);
        Date _data = new Date(Long.valueOf(strDate));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(_data).toString();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.glodon.costsummary", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName ：应用包名
     * @return
     */
    public static boolean havePackage(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 获取sd卡根目录
     *
     * @return
     */
    public static String getSDCardPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public static int getNetType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info == null || !connectivity.getBackgroundDataSetting()) {
            return NET_UNAVAIL;
        }
        // 检查网络类型
        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            return NET_WIFI;
        } else if (netType == ConnectivityManager.TYPE_MOBILE) {   //MOBILE
            return NET_DATA;
        } else {
            return NET_UNAVAIL;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

}

