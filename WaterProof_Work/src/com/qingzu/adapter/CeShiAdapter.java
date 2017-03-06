package com.qingzu.adapter;

import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListNewsModel;
import com.qingzu.entity.News;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.waterproof_work.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CeShiAdapter extends
		RecyclerView.Adapter<CeShiAdapter.MyViewHolder> {
	private InterfaceReturn<ListNewsModel> interfaceReturn;
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private News news;
	private OnItemClickListener mOnItemClickListener;

	public interface OnItemClickListener {
		void onClick(int position);

	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	public CeShiAdapter(InterfaceReturn<ListNewsModel> interfaceReturn,
			Context context) {
		this.context = context;
		this.interfaceReturn = interfaceReturn;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (interfaceReturn.getResults().get(0).getMaterialBrand().size() > 0
				&& interfaceReturn.getResults().get(0).getMaterialBrand() != null) {
			return interfaceReturn.getResults().get(0).getMaterialBrand()
					.size();
		}
		return 0;
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewHoler, final int position) {
		// TODO Auto-generated method stub
		news = new News();
		news = interfaceReturn.getResults().get(0).getMaterialBrand()
				.get(position);
		ImageLoaderUtil.loadNoXUtilImage(news.getNewsPhoto(),
				viewHoler.imageView);

		if (mOnItemClickListener != null) {
			viewHoler.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mOnItemClickListener.onClick(position);
				}
			});
			// viewHoler.imageView.setOnClickListener(l)

		}

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = layoutInflater.inflate(R.layout.horizontai_imageview, null);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	class MyViewHolder extends ViewHolder {
		ImageView imageView;
		CeShiAdapter ceShiAdapter;

		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			imageView = (ImageView) view.findViewById(R.id.imageView);

		}

	}
}
