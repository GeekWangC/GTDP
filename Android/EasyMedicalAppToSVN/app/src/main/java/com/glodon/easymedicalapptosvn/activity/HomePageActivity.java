package com.glodon.easymedicalapptosvn.activity;
/**
 * Created by shirr on 2015/12/4.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.glodon.easymedicalapptosvn.BaseActivity;
import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.adapter.HistoryCurriculumAdapter;
import com.glodon.easymedicalapptosvn.adapter.HistoryCourseAdapter;
import com.glodon.easymedicalapptosvn.adapter.HistoryExamQuestionAdapter;
import com.glodon.easymedicalapptosvn.adapter.HistoryQuestionAdapter;
import com.glodon.easymedicalapptosvn.adapter.HistorySliceAdapter;
import com.glodon.easymedicalapptosvn.adapter.HistoryVideoAdapter;
import com.glodon.easymedicalapptosvn.adapter.RecommendAdapter;
import com.glodon.easymedicalapptosvn.bean.BannerPcDetailBean;
import com.glodon.easymedicalapptosvn.bean.CourseWareList;
import com.glodon.easymedicalapptosvn.bean.CurriculumList;
import com.glodon.easymedicalapptosvn.bean.ExamQuestionList;
import com.glodon.easymedicalapptosvn.bean.HistoryListBean;
import com.glodon.easymedicalapptosvn.bean.QuestionList;
import com.glodon.easymedicalapptosvn.bean.RecommendDetailBean;
import com.glodon.easymedicalapptosvn.bean.SliceList;
import com.glodon.easymedicalapptosvn.bean.VideoList;
import com.glodon.easymedicalapptosvn.common.Constants;
import com.glodon.easymedicalapptosvn.customview.SlideShowView;
import com.glodon.easymedicalapptosvn.utils.ListViewInScrollView;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;
import com.glodon.easymedicalapptosvn.utils.ToastUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomePageActivity extends BaseActivity {
    //搜索的内容
    private EditText mSearchEt;
    //搜索按钮
    private ImageButton mSearchBut;
    //讨论
    private ImageView mDiscussImg;
    //组织学切片库
    private TextView mZzxqpkTv;
    //病理学切片库
    private TextView mBlxqpkTv;
    //大体病理切片库
    private TextView mDtblqpkTv;
    //寄生虫切片库
    private TextView mJscqpkTv;
    //微生物切片库
    private TextView mWswqpkTv;
    //数字胚胎切片库
    private TextView mSzptqpkTv;

    //历史浏览课件列表
    private ListViewInScrollView mHistoricalCurriculumListView;
    //历史浏览课件列表的适配器
    private HistoryCurriculumAdapter historyCurriculumAdapter;
    //历史浏览课件列表的解析类
    private ArrayList<CurriculumList> historyCurriculumBeanList;
    //历史浏览课件列表的数据源
    private List<CurriculumList> mHistoryCurriculumDatas = new ArrayList<CurriculumList>();

    //历史浏览课程列表
    private ListViewInScrollView mHistoricalCourseWarListView;
    //历史浏览课程列表适配器
    private HistoryCourseAdapter mHistoryCourseAdapter;
    //历史浏览课程列表的解析类
    private ArrayList<CourseWareList> historyCourseWarBeanList;
    //历史浏览课程列表数据源
    private List<CourseWareList> mHistoryCourseDatas = new ArrayList<CourseWareList>();

    //历史浏览题库当前位置试题列表
    private ListViewInScrollView mHistoricalQuestionListView;
    //历史浏览题库当前位置试题列表适配器
    private HistoryQuestionAdapter mHistoryQuestionAdapter;
    //历史浏览题库当前位置试题列表的解析类
    private ArrayList<QuestionList> historyQuestionBeanList;
    //历史浏览题库当前位置试题列表数据源
    private List<QuestionList> mHistoryQuestionDatas = new ArrayList<QuestionList>();

    //历史浏览模拟考试当前位置试题
    private ListViewInScrollView mHistoricalExamQuestionListView;
    //历史浏览模拟考试当前位置试题适配器
    private HistoryExamQuestionAdapter mHistoryExamQuestionAdapter;
    //历史浏览模拟考试当前位置试题的解析类
    private ArrayList<ExamQuestionList> historyExamQuestionBeanList;
    //历史浏览模拟考试当前位置试题数据源
    private List<ExamQuestionList> mHistoryExamQuestionDatas = new ArrayList<ExamQuestionList>();

    //历史浏览切片列表
    private ListViewInScrollView mHistoricalSliceListView;
    //历史浏览切片列表适配器
    private HistorySliceAdapter mHistorySliceAdapter;
    //历史浏览切片列表的解析类
    private ArrayList<SliceList> historySliceBeanList;
    //历史浏览切片列表数据源
    private List<SliceList> mHistorySliceDatas = new ArrayList<SliceList>();

    //历史浏览切片章节视频
    private ListViewInScrollView mHistoricalVideoListView;
    //历史浏览切片章节视频列表适配器
    private HistoryVideoAdapter mHistoryVideoAdapter;
    //历史浏览切片章节视频列表的解析类
    private ArrayList<VideoList> historyVideoBeanList;
    //历史浏览切片章节视频列表数据源
    private List<VideoList> mHistoryVideoDatas = new ArrayList<VideoList>();

    //推荐课程的listview
    private ListViewInScrollView mRecommendedListView;
    //推荐课程的适配器
    private RecommendAdapter recommendAdapter;
    //推荐课程的解析类
    private ArrayList<RecommendDetailBean> recommendBeanList;
    //推荐课程的数据源
    private List<RecommendDetailBean> mRecommendDatas = new ArrayList<RecommendDetailBean>();
    //获取首页banner图片的控件
    private SlideShowView mSlideshowView;
    private SharePreferenceUtils sharePreferenceUtils;
    //获取用户的session
    private String userInfo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findView();
        setListener();
        logic();
    }

    @Override
    public void findView() {
        mSlideshowView = (SlideShowView) findViewById(R.id.slideshowView);
        mSearchEt = (EditText) findViewById(R.id.search_edittext);
        mSearchBut = (ImageButton) findViewById(R.id.search_button);
        mDiscussImg = (ImageView) findViewById(R.id.discuss_Imageview);
        mZzxqpkTv = (TextView) findViewById(R.id.zzxqpk_textview);
        mBlxqpkTv = (TextView) findViewById(R.id.blxqpk_textview);
        mDtblqpkTv = (TextView) findViewById(R.id.dtblqpk_textview);
        mJscqpkTv = (TextView) findViewById(R.id.jscqpk_textview);
        mWswqpkTv = (TextView) findViewById(R.id.wswqpk_textview);
        mSzptqpkTv = (TextView) findViewById(R.id.szptqpk_textview);
        mHistoricalCurriculumListView = (ListViewInScrollView) findViewById(R.id.historicalCurriculum_listview);
        mHistoricalCourseWarListView = (ListViewInScrollView) findViewById(R.id.historicalCourseWare_listview);
        mHistoricalQuestionListView = (ListViewInScrollView) findViewById(R.id.historicalQuestion_listview);
        mHistoricalExamQuestionListView = (ListViewInScrollView) findViewById(R.id.historicalExamQuestion_listview);
        mHistoricalSliceListView = (ListViewInScrollView) findViewById(R.id.historicalSlice_listview);
        mHistoricalVideoListView = (ListViewInScrollView) findViewById(R.id.historicalVideo_listview);
        mRecommendedListView = (ListViewInScrollView) findViewById(R.id.recommended_listview);

        //hintKbTwo();
        //历史记录课件列表的数据绑定
        historyCurriculumAdapter = new HistoryCurriculumAdapter(this, mHistoryCurriculumDatas);
        mHistoricalCurriculumListView.setAdapter(historyCurriculumAdapter);
        historyCurriculumAdapter.notifyDataSetChanged();

        //历史记录课程列表的数据绑定
        mHistoryCourseAdapter = new HistoryCourseAdapter(this, mHistoryCourseDatas);
        mHistoricalCourseWarListView.setAdapter(mHistoryCourseAdapter);
        mHistoryCourseAdapter.notifyDataSetChanged();

        //历史记录题库当前位置试题列表的数据绑定
        mHistoryQuestionAdapter = new HistoryQuestionAdapter(this, mHistoryQuestionDatas);
        mHistoricalQuestionListView.setAdapter(mHistoryQuestionAdapter);
        mHistoryQuestionAdapter.notifyDataSetChanged();

        //历史记录模拟考试当前位置试题的数据绑定
        mHistoryExamQuestionAdapter = new HistoryExamQuestionAdapter(this, mHistoryExamQuestionDatas);
        mHistoricalExamQuestionListView.setAdapter(mHistoryExamQuestionAdapter);
        mHistoryExamQuestionAdapter.notifyDataSetChanged();

        //历史记录切片列表的数据绑定
        mHistorySliceAdapter = new HistorySliceAdapter(this, mHistorySliceDatas);
        mHistoricalSliceListView.setAdapter(mHistorySliceAdapter);
        mHistorySliceAdapter.notifyDataSetChanged();

        //历史记录切片章节视频的数据绑定
        mHistoryVideoAdapter = new HistoryVideoAdapter(this, mHistoryVideoDatas);
        mHistoricalVideoListView.setAdapter(mHistoryVideoAdapter);
        mHistoryVideoAdapter.notifyDataSetChanged();

        historyCurriculumBeanList = new ArrayList<CurriculumList>();
        //推荐课程的数据绑定
        recommendAdapter = new RecommendAdapter(this, mRecommendDatas);
        mRecommendedListView.setAdapter(recommendAdapter);
        recommendBeanList = new ArrayList<RecommendDetailBean>();

        sharePreferenceUtils = new SharePreferenceUtils(HomePageActivity.this);
        userInfo = sharePreferenceUtils.getString("userInfo", null);

        if (Utils.isNetworkAvailable(HomePageActivity.this)) {
            //请求服务器，获取首页的banner图片
            httpRequestPcBanner();
            //获取历史记录的列表
            HttpRequestHistory();
            //获取推荐课程的列表
            HttpRequestRecommed();
        } else {
            ToastUtils.showShort(HomePageActivity.this, "请检查网络!");
        }

    }

    @Override
    public void setListener() {
        final Intent intent = new Intent();
        mSearchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEt.setFocusable(true);
                mSearchEt.setFocusableInTouchMode(true);
                mSearchEt.requestFocus();
                mSearchEt.requestFocusFromTouch();
                InputMethodManager inputManager = (InputMethodManager)mSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mSearchEt, 0);
             }
        });
        mSearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/searchRecord.html?text=" + mSearchEt.getText());
                HomePageActivity.this.startActivity(intent);

            }
        });
        mDiscussImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/messages.html");
                HomePageActivity.this.startActivity(intent);

            }
        });
        mZzxqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Tissue");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mBlxqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Slice");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mDtblqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Pathology");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mJscqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Worm");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mWswqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Worm");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mSzptqpkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/qpkml.html?type=Worm");
                HomePageActivity.this.startActivity(intent);
            }
        });
        mRecommendedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });

        mHistoricalCurriculumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
        mHistoricalCourseWarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
        mHistoricalQuestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
        mHistoricalExamQuestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
        mHistoricalSliceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
        mHistoricalVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.setClass(HomePageActivity.this, WebviewActivity.class);
                intent.putExtra("url", "file:///android_asset/www/courseDetail.html?id=" + (int) id);
                HomePageActivity.this.startActivity(intent);
            }
        });
    }


    @Override
    public void logic() {
        //最近浏览数据
        /*HistoryDetailBean historyBean = new HistoryDetailBean();
        for(int i = 0;i <10;i++){
            historyBean.setCourseName("阿米巴");
            historyBean.setLecturerName("讲师：" + "李教授");
            historyBean.setCoursewareCount("课件数量：3");
            historyBean.setAllAntegral("总积分：40");
            historyBean.setSpecies("寄生虫");
            historyBean.setPlaytime("播放至3分钟15秒");
            mHistoryDatas.add(historyBean);
        }
        historyAdapter.setList(mHistoryDatas);
        historyAdapter.notifyDataSetChanged();*/

        //推荐课程数据
       /* RecommendDetailBean recommendBean = new RecommendDetailBean();
        for(int i = 0;i <10;i++){
            recommendBean.setCourseName("阿米巴");
            recommendBean.setLecturerName("讲师：" + "李教授");
            recommendBean.setCoursewareCount("课件数量：3");
            recommendBean.setAllAntegral("总积分：40");
            recommendBean.setSpecies("寄生虫");
            mRecommendDatas.add(recommendBean);
        }

        recommendAdapter.setList(mRecommendDatas);
        recommendAdapter.notifyDataSetChanged();*/
    }

    /*
    *获取首页Banner图片的请求
    */
    private void httpRequestPcBanner() {
        if (Utils.isNetworkAvailable(HomePageActivity.this)) {
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            final Gson gson = new Gson();
            RequestParams requestParams = new RequestParams();
            requestParams.put("JsonString", gson.toJson(paramsMap));
            AsyncHttpClient client = new AsyncHttpClient();
            //因为服务器数据原因，在此设置带header的http请求；
            client.addHeader("UserInfo", "6AFTTD8oWuSGsiItYRO4mQ==");
            //设置超时时间
            client.setTimeout(10000);
            //请求方式为GET请求
            client.get(Constants.ServiceInterFace.GetBannerPcUrl, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {//请求成功的处理
                    String strResponse = null;
                    try {
                        strResponse = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        ToastUtils.showLong(HomePageActivity.this, "服务器数据解析失败！");
                    }
                    List<BannerPcDetailBean> parsedGSON = new ArrayList<BannerPcDetailBean>();
                    parsedGSON = gson.fromJson(strResponse, new TypeToken<List<BannerPcDetailBean>>() {
                    }.getType());
                    if (parsedGSON != null  && parsedGSON.size() > 0 ) {
                        List<String> imageUrls = new ArrayList<String>();
                        List<String> imageLinkUrl = new ArrayList<String>();
                        //将请求下来的图片循环加入view中
                        for (int j = 0; j < parsedGSON.size(); j++) {
                            imageUrls.add(Constants.BannerPicURL + parsedGSON.get(j).getImageUrl());
                            imageLinkUrl.add(Constants.ServerURL + parsedGSON.get(j).getLinkUrl());
                        }
                       /* imageUrls.add("http://image.zcool.com.cn/56/35/1303967876491.jpg");
                        imageUrls.add("http://image.zcool.com.cn/59/54/m_1303967870670.jpg");
                        imageUrls.add("http://image.zcool.com.cn/47/19/1280115949992.jpg");
                        imageUrls.add("http://image.zcool.com.cn/56/35/1303967876491.jpg");*/
                        mSlideshowView.setImageUrls(imageUrls);
                        mSlideshowView.setImageListLinkUrls(imageLinkUrl);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//请求失败
                    ToastUtils.showShort(HomePageActivity.this, "请求失败");
                }
            });
        } else {
            ToastUtils.showShort(HomePageActivity.this, "无网络连接");
        }
    }

    /*
     *获取首页中历史记录的列表数据
     */
    private void HttpRequestHistory() {
        if (Utils.isNetworkAvailable(HomePageActivity.this)) {
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            final Gson gson = new Gson();
            RequestParams requestParams = new RequestParams();
            requestParams.put("JsonString", gson.toJson(paramsMap));
            AsyncHttpClient client = new AsyncHttpClient();
            //因为服务器数据原因，在此设置带header的http请求；
            client.addHeader("UserInfo", "6AFTTD8oWuSGsiItYRO4mQ==");
            //设置超时时间
            client.setTimeout(10000);
            //请求方式为GET请求
            client.get(Constants.ServiceInterFace.GetHistoryURL, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {//请求成功的处理
                    String strResponse = null;
                    try {
                        strResponse = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        ToastUtils.showLong(HomePageActivity.this, "服务器数据解析失败！");
                    }

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<HistoryListBean>() {}.getType();
                    HistoryListBean parsedGSON = gson.fromJson(strResponse, collectionType);

                    if (parsedGSON != null) {
                        if (parsedGSON.getCurriculumList() != null && parsedGSON.getCurriculumList().size() > 0) {
                            historyCurriculumAdapter = new HistoryCurriculumAdapter(HomePageActivity.this, parsedGSON.getCurriculumList());
                            mHistoricalCurriculumListView.setAdapter(historyCurriculumAdapter);
                            historyCurriculumAdapter.notifyDataSetChanged();
                        }
                        if (parsedGSON.getCourseWareList() != null && parsedGSON.getCourseWareList().size() > 0) {
                            mHistoryCourseAdapter = new HistoryCourseAdapter(HomePageActivity.this, parsedGSON.getCourseWareList());
                            mHistoricalCourseWarListView.setAdapter(mHistoryCourseAdapter);
                            mHistoryCourseAdapter.notifyDataSetChanged();
                        }
                        if (parsedGSON.getQuestionList() != null && parsedGSON.getQuestionList().size() > 0) {
                            mHistoryQuestionAdapter = new HistoryQuestionAdapter(HomePageActivity.this, parsedGSON.getQuestionList());
                            mHistoricalQuestionListView.setAdapter(mHistoryQuestionAdapter);
                            mHistoryQuestionAdapter.notifyDataSetChanged();
                        }
                        if (parsedGSON.getExamQuestionList() != null && parsedGSON.getExamQuestionList().size() > 0) {
                            mHistoryExamQuestionAdapter = new HistoryExamQuestionAdapter(HomePageActivity.this, parsedGSON.getExamQuestionList());
                            mHistoricalExamQuestionListView.setAdapter(mHistoryExamQuestionAdapter);
                            mHistoryExamQuestionAdapter.notifyDataSetChanged();
                        }
                        if (parsedGSON.getSliceList() != null && parsedGSON.getSliceList().size() > 0) {
                            mHistorySliceAdapter = new HistorySliceAdapter(HomePageActivity.this, parsedGSON.getSliceList());
                            mHistoricalSliceListView.setAdapter(mHistorySliceAdapter);
                            mHistorySliceAdapter.notifyDataSetChanged();
                        }
                        if (parsedGSON.getVideoList() != null && parsedGSON.getVideoList().size() > 0) {
                            mHistoryVideoAdapter = new HistoryVideoAdapter(HomePageActivity.this, parsedGSON.getVideoList());
                            mHistoricalVideoListView.setAdapter(mHistoryVideoAdapter);
                            mHistoryVideoAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//请求失败
                    ToastUtils.showShort(HomePageActivity.this, "请求失败");
                }
            });
        } else {
            ToastUtils.showShort(HomePageActivity.this, "无网络连接");
        }

    }

    /*
     *获取首页中推荐课程的列表数据
     */
    private void HttpRequestRecommed() {
        if (Utils.isNetworkAvailable(HomePageActivity.this)) {
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            final Gson gson = new Gson();
            RequestParams requestParams = new RequestParams();
            requestParams.put("JsonString", gson.toJson(paramsMap));
            AsyncHttpClient client = new AsyncHttpClient();
            //因为服务器数据原因，在此设置带header的http请求；
            client.addHeader("UserInfo", "6AFTTD8oWuSGsiItYRO4mQ==");
            //设置超时时间
            client.setTimeout(10000);
            //请求方式为GET请求
            client.get(Constants.ServiceInterFace.GetRecommendURL, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {//请求成功的处理
                    String strResponse = null;
                    try {
                        strResponse = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        ToastUtils.showLong(HomePageActivity.this, "服务器数据解析失败！");
                    }
                    List<RecommendDetailBean> parsedGSON = new ArrayList<RecommendDetailBean>();
                    parsedGSON = gson.fromJson(strResponse, new TypeToken<List<RecommendDetailBean>>() {
                    }.getType());
                    if (parsedGSON.size() > 0 && parsedGSON != null) {
                        recommendAdapter = new RecommendAdapter(HomePageActivity.this, parsedGSON);
                        mRecommendedListView.setAdapter(recommendAdapter);
                        recommendAdapter.notifyDataSetChanged();
                    }
                    hintKbTwo();

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//请求失败
                    ToastUtils.showShort(HomePageActivity.this, "请求失败");
                }
            });
        } else {
            ToastUtils.showShort(HomePageActivity.this, "无网络连接");
        }

    }

    //关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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
