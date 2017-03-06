package com.qingzu.adapter;

import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListNewsModel;
import com.qingzu.entity.News;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.WebViewActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FirstHorizontaiListViewAdapter extends BaseAdapter {
	private InterfaceReturn<ListNewsModel> interfaceReturn;
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private News news;

	public FirstHorizontaiListViewAdapter(
			InterfaceReturn<ListNewsModel> interfaceReturn, Context context) {

		this.context = context;
		this.interfaceReturn = interfaceReturn;
		this.layoutInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (interfaceReturn.getResults().get(0).getMaterialBrand().size() > 0
				&& interfaceReturn.getResults().get(0).getMaterialBrand() != null) {
			return interfaceReturn.getResults().get(0).getMaterialBrand()
					.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHoler viewHoler;
		if (convertView == null) {
			viewHoler = new ViewHoler();
			convertView = layoutInflater.inflate(R.layout.horizontai_imageview,
					null);
			viewHoler.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			
			ViewGroup.LayoutParams params = viewHoler.imageView.getLayoutParams();
			// dishtype，welist为ArrayList
			WindowManager wm = ((Activity) context).getWindowManager();
			int width = wm.getDefaultDisplay().getWidth();
			// 图片之间的距离
			params.width = width / 4;
	//		Log.d("看看这个宽度", params.width + "" + jiaListBitMap.size());
			viewHoler.imageView.setLayoutParams(params);
			
			convertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) convertView.getTag();
		}
		news = new News();
		news = interfaceReturn.getResults().get(0).getMaterialBrand()
				.get(position);
		ImageLoaderUtil.loadNoXUtilImage(news.getNewsPhoto(),
				viewHoler.imageView);



		return convertView;
	}

//	private void hengping() {
//		ViewGroup.LayoutParams params = imageView.getLayoutParams();
//		// dishtype，welist为ArrayList
//		WindowManager wm = this.getWindowManager();
//		int dishtypes = jiaListBitMap.size();
//		// 图片之间的距离
//		params.width = width / 20 * 6 * dishtypes;
//		Log.d("看看这个宽度", params.width + "" + jiaListBitMap.size());
//		noScrollgridview.setLayoutParams(params);
//		// 设置列数为得到的list长度
//		// noScrollgridview.setNumColumns(jiaListBitMap.size());
//	}

	private class ViewHoler {
		ImageView imageView;

	}

}
