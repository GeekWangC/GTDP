package com.glodon.easymedicalapptosvn.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.activity.MainActivity;
import com.glodon.easymedicalapptosvn.activity.WebviewActivity;
import com.glodon.easymedicalapptosvn.utils.AnimationUtil;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;

public class Fragment3 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_5, container, false);

		view.findViewById(R.id.tvInNew).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
                        startActivity(new Intent(getActivity(),WebviewActivity.class).putExtra("url", "file:///android_asset/www/login.html"));
                        AnimationUtil.finishActivityAnimation(getActivity());
					}
				});
		return view;
	}

}
