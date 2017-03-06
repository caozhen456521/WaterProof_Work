package com.qingzu.fragment;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.qingzu.waterproof_work.R;

public class WorkerSecondFragment extends Fragment implements OnClickListener{
	/**
	 * 工人第二个fragment
	 */
	
private View v = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_worker_second, container, false);
		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
