package com.glodon.easymedicalapptosvn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.bean.CurriculumList;
import com.glodon.easymedicalapptosvn.common.Constants;
import com.glodon.easymedicalapptosvn.utils.StringTools;
import com.glodon.easymedicalapptosvn.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by shirr on 2015/12/7.
 */
public class HistoryCurriculumAdapter extends BasisAdapter<CurriculumList> {

    private Context context;
    private LayoutInflater mInflater = null;
    private ImageLoader imageLoader;

    public HistoryCurriculumAdapter(Context context, List<CurriculumList> list) {
        super(context, list);
        this.mList = list;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.historycurricuitem_layout, null);

            //需要请求服务器数据的部分
            mHolder.mSpeciesImage = (ImageView) convertView.findViewById(R.id.brospecies_imageview);
            mHolder.mCoursenNameTv = (TextView) convertView.findViewById(R.id.brocoursename_textview);
            mHolder.mLecturerNameTv = (TextView) convertView.findViewById(R.id.brolecturername_textview);
            mHolder.mCoursewareCountTv = (TextView) convertView.findViewById(R.id.brocoursewareCount_textview);
            mHolder.mAllAntegralTv = (TextView) convertView.findViewById(R.id.broallAntegral_textview);
            mHolder.mSpeciesTv = (TextView) convertView.findViewById(R.id.brospecies_textview);
            mHolder.mPlayTimeTv = (TextView) convertView.findViewById(R.id.broplaytime_textview);

            mHolder.mSpeciesImage.getLayoutParams().height = Utils.getDisplayMetrics(context).heightPixels / 7;
            mHolder.mSpeciesImage.getLayoutParams().width = Utils.getDisplayMetrics(context).widthPixels / 4;

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        if(!StringTools.isEmpty(mList.get(position).getThumbnailUrl())){
            imageLoader.displayImage(Constants.PicURL + mList.get(position).getThumbnailUrl(), mHolder.mSpeciesImage);
        }

        mHolder.mCoursenNameTv.setText(mList.get(position).getName());
        mHolder.mLecturerNameTv.setText("讲师："+ mList.get(position).getTeacher());
        mHolder.mCoursewareCountTv.setText("课件数量："+ String.valueOf(mList.get(position).getCourseWaresCount()));
        mHolder.mAllAntegralTv.setText("总积分"+ String.valueOf(mList.get(position).getTotalScore()));
        mHolder.mSpeciesTv.setText(mList.get(position).getCourseName());

        return convertView;
    }

    public final class ViewHolder {

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
        //播放时间
        public TextView mPlayTimeTv;
    }
}
