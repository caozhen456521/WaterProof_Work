package com.qingzu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.qingzu.fragment.ProjectDetailBasciInfoFragment;
import com.qingzu.fragment.ProjectDetailOtherRequestFragment;
import com.qingzu.fragment.TeamMemberDetailPhotoShowFragment;
import com.qingzu.fragment.TeamMemberDetailbasciInfoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TeamMemberDetailPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list_fragments;

	public TeamMemberDetailPagerAdapter(FragmentManager fm,
			List<Fragment> list_fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		// list_fragments = new ArrayList<Fragment>();
		// list_fragments.add(new TeamMemberDetailbasciInfoFragment());
		// list_fragments.add(new TeamMemberDetailPhotoShowFragment());
		this.list_fragments = list_fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_fragments != null ? list_fragments.size() : 0;
	}

}
