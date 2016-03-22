package com.glodon.easymedicalapptosvn.activity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.adapter.RecommendAdapter;
import com.glodon.easymedicalapptosvn.bean.RecommendDetailBean;
import com.glodon.easymedicalapptosvn.common.Constants;
import com.glodon.easymedicalapptosvn.utils.ImgCompressUtil;
import com.glodon.easymedicalapptosvn.utils.ToastUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class PhotoActivity extends Activity {

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/caijian";
    private ImageView imageView = null;
    private Button mImageBtn = null;
    private Button mImageCameBtn = null;
    private Button mSubmitPhotoBtn = null;
    private Button mCancleBtn = null;
    private RelativeLayout mAllRelativeLayout;

    private String imagePath;
    private File tempFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.activity_photo);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏

        imageView = (ImageView) findViewById(R.id.imageID);
        mImageBtn = (Button) findViewById(R.id.btn_image);
        mImageCameBtn = (Button) findViewById(R.id.btn_came);
        mSubmitPhotoBtn = (Button) findViewById(R.id.btn_submitphoto);
        mCancleBtn = (Button) findViewById(R.id.btn_cancle);
        mAllRelativeLayout = (RelativeLayout) findViewById(R.id.all_relative);

        imageView.getLayoutParams().height = Utils.getDisplayMetrics(PhotoActivity.this).heightPixels / 2;
        imageView.getLayoutParams().width = Utils.getDisplayMetrics(PhotoActivity.this).widthPixels / 2;

        //相册
        mImageBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image*//*");
                startActivityForResult(intent, PHOTOZOOM);*/

                Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);// ACTION_GET_CONTENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                    startActivityForResult(intent, PHOTOZOOM);
                }else{
                    startActivityForResult(intent, PHOTOZOOM);
                }

            }
        });

        //拍照
        mImageCameBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                System.out.println("=============" + Environment.getExternalStorageDirectory());
                startActivityForResult(intent, PHOTOHRAPH);
            }
        });
        mSubmitPhotoBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if(!Utils.isEmpty(imagePath)){
                    String imageBase64 = ImgCompressUtil.filePathToString(imagePath);

                    HttpRequestPhoto(imageBase64);
                }else{
                    ToastUtils.showShort(PhotoActivity.this, "请选择照片");

                }

            }
        });
        mCancleBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                PhotoActivity.this.finish();
            }
        });
        mAllRelativeLayout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                PhotoActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
            System.out.println("------------------------" + picture.getPath());
            imagePath = picture.getPath().toString();
            startPhotoZoom(Uri.fromFile(picture));
        }

        if (data == null) {
            return;
        }
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
           String photoPath =  ImgCompressUtil.getPath(PhotoActivity.this,data.getData());
           imagePath = photoPath;
           startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 75, stream);// (0 - 100)压缩文件

                imageView.setImageBitmap(photo);
                //imageView.setImageDrawable(Drawable.createFromPath(tempFile.getAbsolutePath()));
            }
            imagePath = tempFile.getAbsolutePath();

        }
        imageView.getLayoutParams().height = Utils.getDisplayMetrics(PhotoActivity.this).heightPixels / 2;
        imageView.getLayoutParams().width = Utils.getDisplayMetrics(PhotoActivity.this).widthPixels / 2;

    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);

        tempFile=new File("/sdcard/ll1x/"+ Calendar.getInstance().getTimeInMillis()+".jpg"); // 以时间秒为文件名
        File temp = new File("/sdcard/ll1x/");//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        intent.putExtra("output", Uri.fromFile(tempFile));  // 专入目标文件
        intent.putExtra("outputFormat", "JPEG"); //输入文件格式
        startActivityForResult(intent, PHOTORESOULT);

       /* Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image*//*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        startActivityForResult(intent, PHOTORESOULT);*/
    }

    /*
     *获取首页中推荐课程的列表数据
     */
    private void HttpRequestPhoto(String imageGson) {
        if (Utils.isNetworkAvailable(PhotoActivity.this)) {
            /*Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("","data:image/png;base64,"+imageGson);*/
            final Gson gson = new Gson();
            RequestParams requestParams = new RequestParams();
            requestParams.put("icon", "data:image/png;base64,"+imageGson);//不需要转换成json
            AsyncHttpClient client = new AsyncHttpClient();
            //因为服务器数据原因，在此设置带header的http请求；
            client.addHeader("UserInfo", "6AFTTD8oWuSGsiItYRO4mQ==");
            //设置超时时间
            client.setTimeout(10000);
            //请求方式为POST请求
            client.post(Constants.ServiceInterFace.ChangePhotoURL, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {//请求成功的处理
                    String strResponse = null;
                    try {
                        strResponse = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        ToastUtils.showLong(PhotoActivity.this, "服务器数据解析失败！");
                    }
                    if(strResponse.equals("true")){
                        ToastUtils.showLong(PhotoActivity.this, "头像上传成功");
                        Intent intent = new Intent();
                        intent.setClass(PhotoActivity.this, WebviewActivity.class);
                        intent.putExtra("url", "file:///android_asset/www/set.html");
                        PhotoActivity.this.startActivity(intent);

                        PhotoActivity.this.finish();
                    }else{
                        ToastUtils.showLong(PhotoActivity.this, "头像上传失败");
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//请求失败
                    ToastUtils.showShort(PhotoActivity.this, "请求失败");
                }
            });
        } else {
            ToastUtils.showShort(PhotoActivity.this, "无网络连接");
        }

    }

}
