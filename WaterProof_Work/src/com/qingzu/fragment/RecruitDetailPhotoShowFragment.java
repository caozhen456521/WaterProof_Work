package com.qingzu.fragment;

import java.util.ArrayList;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 招工详情 部位图展示页面
 * 
 * @author Administrator
 * 
 */
public class RecruitDetailPhotoShowFragment extends Fragment {
	private View v = null;
	private GridView griRecruitDetailPhoto = null;
	private LinearLayout llRecruitDetailPhoto = null;
	private ArrayList<CreateFindWorkPhotoInfo> RemarkList = null;
	private ArrayList<String> urLlist = null;
	private MyAdapter<String> adapter = null;
	private String UserToken = null;
	private Integer InfoId = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_recruit_detail_photo_show,
				container, false);
		initView();

		return v;
	}

	private void initView() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		InfoId = Integer.parseInt(RecruitDetailActivity.id);
		RemarkList = new ArrayList<CreateFindWorkPhotoInfo>();
		urLlist = new ArrayList<String>();
		griRecruitDetailPhoto = (GridView) v
				.findViewById(R.id.recruit_detail_photo_show);
		llRecruitDetailPhoto = (LinearLayout) v
				.findViewById(R.id.recruit_detail_photo_show_ll);
		adapter = new MyAdapter<String>(getActivity(), urLlist,
				R.layout.project_detail_gridview_item) {
			ImageView showImg = null; // 显示的图片

			@Override
			public void convert(ViewHolder view, int position, String item) {
				showImg = view.getView(R.id.showImg);

				ImageLoaderUtil.loadNoXUtilImage(item
						+ "?imageView2/0/w/480/h/800", showImg);
			}
		};
		PhotoByInfoID(InfoId);
		griRecruitDetailPhoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Tools.imageBrower(getActivity(), arg2, RemarkList);
			}
		});
	}

	private void PhotoByInfoID(int InfoID) {
		RequestParams params = new RequestParams(
				HttpUtil.PhotoByInfoID.replace("{InfoID}", InfoID + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<CreateFindWorkPhotoInfo> interfaceReturn = new InterfaceReturn<CreateFindWorkPhotoInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						CreateFindWorkPhotoInfo.class);
				if (interfaceReturn.isStatus()) {
					CreateFindWorkPhotoInfo workPhotoInfo = null;
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						workPhotoInfo = new CreateFindWorkPhotoInfo();
						urLlist.add(interfaceReturn.getResults().get(i)
								.getPhotoUrl());

						workPhotoInfo.setPhotoUrl(interfaceReturn.getResults()
								.get(i).getPhotoUrl());
						workPhotoInfo.setPhotoTitle(interfaceReturn
								.getResults().get(i).getPhotoTitle());
						workPhotoInfo.setId(interfaceReturn.getResults().get(i)
								.getId());
						RemarkList.add(workPhotoInfo);
					}
					griRecruitDetailPhoto.setAdapter(adapter);
					if (RemarkList.size() == 0) {
						griRecruitDetailPhoto.setVisibility(View.GONE);
						llRecruitDetailPhoto.setVisibility(View.VISIBLE);
					} else {
						griRecruitDetailPhoto.setVisibility(View.VISIBLE);
						llRecruitDetailPhoto.setVisibility(View.GONE);
					}
				} else {
					griRecruitDetailPhoto.setVisibility(View.GONE);
					llRecruitDetailPhoto.setVisibility(View.VISIBLE);
					// T.showToast(getActivity(), interfaceReturn.getMessage());
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});
	}
}
