package com.qingzu.fragment;

import com.qingzu.waterproof_work.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 老板第四
 * @author Johnson
 *
 */
public class BossFourthFragment extends Fragment{

	private View v = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_fourth, container,false);
		return v;
	}

}