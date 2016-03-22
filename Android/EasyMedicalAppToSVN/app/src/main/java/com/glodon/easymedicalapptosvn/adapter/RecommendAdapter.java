package com.glodon.easymedicalapptosvn.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.bean.BannerPcDetailBean;
import com.glodon.easymedicalapptosvn.bean.RecommendDetailBean;
import com.glodon.easymedicalapptosvn.common.Constants;
import com.glodon.easymedicalapptosvn.utils.ImageManager;
import com.glodon.easymedicalapptosvn.utils.StringTools;
import com.glodon.easymedicalapptosvn.utils.ToastUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shirr on 2015/12/7.
 */
public class RecommendAdapter extends BasisAdapter<RecommendDetailBean> {

    private Context context;
    private LayoutInflater mInflater = null;
    private ImageLoader imageLoader;
    ViewHolder mHolder = null;

    public RecommendAdapter(Context context, List<RecommendDetailBean> list) {
        super(context, list);
        this.mList = list;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyListener myListener = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.recommenditem_layout, null);

            //可以理解为从vlist获取view  之后把view返回给ListView
            myListener=new MyListener(position);

            //需要请求服务器数据的部分
            mHolder.mSpeciesImage = (ImageView) convertView.findViewById(R.id.remspecies_imageview);
            mHolder.mCoursenNameTv = (TextView) convertView.findViewById(R.id.remcoursename_textview);
            mHolder.mLecturerNameTv = (TextView) convertView.findViewById(R.id.remlecturername_textview);
            mHolder.mCoursewareCountTv = (TextView) convertView.findViewById(R.id.remcoursewareCount_textview);
            mHolder.mAllAntegralTv = (TextView) convertView.findViewById(R.id.remallAntegral_textview);
            mHolder.mSpeciesTv = (TextView) convertView.findViewById(R.id.remspecies_textview);
            mHolder.mCollectionTv = (ImageView) convertView.findViewById(R.id.collection_imageview);

            mHolder.mSpeciesImage.getLayoutParams().height = Utils.getDisplayMetrics(context).heightPixels / 7;
            mHolder.mSpeciesImage.getLayoutParams().width = Utils.getDisplayMetrics(context).widthPixels / 4;

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        //imageLoader.displayImage(Constants.URL + mList.get(position).getRestaurantPhoto(), mHolder.mIntroduceImage);

        if (!StringTools.isEmpty(mList.get(position).getThumbnailUrl())) {
            /*ImageManager.imageLoader.displayImage(Constants.PicURL + mList.get(position).getThumbnailUrl(), mHolder.mSpeciesImage, ImageManager.getOptions(
                    R.drawable.ic_launcher, R.drawable.ic_launcher));*/
            imageLoader.displayImage(Constants.PicURL + mList.get(position).getThumbnailUrl(), mHolder.mSpeciesImage);
        }
        mHolder.mCoursenNameTv.setText(mList.get(position).getName());
        mHolder.mLecturerNameTv.setText("讲师：" + mList.get(position).getTeacher());
        mHolder.mCoursewareCountTv.setText("课件数量：" + String.valueOf(mList.get(position).getCourseWaresCount()));
        mHolder.mAllAntegralTv.setText("总积分：" + String.valueOf(mList.get(position).getTotalScore()));
        mHolder.mSpeciesTv.setText(mList.get(position).getCourseName());
        Boolean iscollect = mList.get(position).getIsCollect();
        if (iscollect) {
            //imageLoader.displayImage(Constants.PicURL + R.drawable.star_gray1, mHolder.mSpeciesImage);
            mHolder.mCollectionTv.setImageResource(R.drawable.star_gray1);
        } else {
            //imageLoader.displayImage(Constants.PicURL + R.drawable.star_gray, mHolder.mSpeciesImage);
            mHolder.mCollectionTv.setImageResource(R.drawable.star_gray);
        }
        //mHolder.mCollectionTv.setOnClickListener(myListener);
        mHolder.mCollectionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition;
                mPosition= position;
                String url = null;
                int id;
                id=mList.get(mPosition).getId();
                if(mList.get(mPosition).getIsCollect()){
                    url = Constants.ServiceInterFace.DeleteCollectionURL;
                }else{
                    url = Constants.ServiceInterFace.AddCollectionURL;
                }
                httpRequestCollect(url,id,mPosition);

            }
        });

        return convertView;
    }

    private class MyListener implements View.OnClickListener {
        int mPosition;
        public MyListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            String url = null;
            int id;
            id=mList.get(mPosition).getId();
            if(mList.get(mPosition).getIsCollect()){
                url = Constants.ServiceInterFace.DeleteCollectionURL;
            }else{
                url = Constants.ServiceInterFace.AddCollectionURL;
            }
            httpRequestCollect(url,id,mPosition);
        }

    }

    private void httpRequestCollect(final String url,int id,final int position) {
        if (Utils.isNetworkAvailable(context)) {
            final Gson gson = new Gson();
            RequestParams requestParams = new RequestParams();
            //requestParams.put("", id + "?collectType=4");
            //requestParams.setContentEncoding(id + "?collectType=4");
            AsyncHttpClient client = new AsyncHttpClient();
            //因为服务器数据原因，在此设置带header的http请求；
            client.addHeader("UserInfo", "6AFTTD8oWuSGsiItYRO4mQ==");
            //设置超时时间
            client.setTimeout(10000);
            //请求方式为GET请求
            client.post(url + id + "?collectType=4", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {//请求成功的处理
                    String strResponse = null;
                    try {
                        strResponse = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        ToastUtils.showLong(context, "服务器数据解析失败！");
                    }
                    if(strResponse.equals("true")){
                        if(url.contains("AddCollection")){
                            mHolder.mCollectionTv.setImageResource(R.drawable.star_gray1);
                           /* Resources resources=context.getResources();
                            Drawable drawable=resources.getDrawable(R.drawable.star_gray1);
                            mHolder.mCollectionTv.setBackgroundDrawable(drawable);*/
                            mList.get(position).setIsCollect(true);
                        }else{
                            mHolder.mCollectionTv.setImageResource(R.drawable.star_gray);
                           /* Resources resources=context.getResources();
                            Drawable drawable=resources.getDrawable(R.drawable.star_gray);
                            mHolder.mCollectionTv.setBackgroundDrawable(drawable);*/
                            mList.get(position).setIsCollect(false);
                        }
                    }
                    //RecommendAdapter.this.notifyDataSetChanged();
                    RecommendAdapter.this.notifyDataSetInvalidated();
                }
                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//请求失败
                    ToastUtils.showShort(context, "请求失败");
                }
            });
        } else {
            ToastUtils.showShort(context, "无网络连接");
        }
    }

     static  class ViewHolder {
        //显示图片
        public ImageView mSpeciesImage;
        //课程名称
        public TextView mCoursenNameTv;
        //讲师
        public TextView mLecturerNameTv;
        //课件数量
        public TextView mCoursewareCountTv;
        //总积分
        public TextView mAllAntegralTv;
        //分类
        public TextView mSpeciesTv;
        //收藏
        public ImageView mCollectionTv;
    }
}
