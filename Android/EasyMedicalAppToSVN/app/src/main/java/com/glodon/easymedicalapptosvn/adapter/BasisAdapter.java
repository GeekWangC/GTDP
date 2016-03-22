package com.glodon.easymedicalapptosvn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基础适配器
 *
 * @param <T>
 * @author shirr
 */
public abstract class BasisAdapter<T> extends BaseAdapter {

    /**
     * 使用的List
     */
    protected List<T> mList;

    /**
     * 使用的加载器
     */
    protected LayoutInflater mInflater;

    public BasisAdapter(Context context, List<T> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }

        if (position < 0 || position >= mList.size()) {
            return null;
        }

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 再次赋值
     *
     * @param list
     */
    public void setList(List<T> list) {
        mList = list;
    }
}
