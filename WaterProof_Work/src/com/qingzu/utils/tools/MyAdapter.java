package com.qingzu.utils.tools;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	protected int selectItem = 0;

	public MyAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;

	}

	public void AddData(List<T> mData) {
		mDatas.addAll(mData);
	}

	public void upData(List<T> mData) {
		mDatas.clear();
		mDatas.addAll(mData);
	}

	@Override
	public int getCount() {
		return mDatas != null ? mDatas.size() : 0;
	}

	@Override
	public T getItem(int position) {
		try {
			if (mDatas.size() <= position || position < 0)
				return null;
			return mDatas.get(position);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void removeItem(int posiTion) {
		try {
			mDatas.remove(posiTion);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, position, getItem(position));
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder view, int position, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

	public void refresh(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}
}
