package com.qingzu.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private boolean viewIsShow;

	public void setPos(int aiPos) {
		mPosition = aiPos;
	}

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		if (text != null) {
			view.setText(text);
		}
		return this;
	}

	public ViewHolder setNumStars(int viewId, int stars) {
		RatingBar ratingBar = getView(viewId);
		if (ratingBar != null) {
			ratingBar.setNumStars(stars);
		}
		return this;
	}

	/**
	 * 设置view隐藏显示
	 * @param viewId
	 * @param isShow
	 * @return
	 */
	public ViewHolder showView(int viewId, boolean isShow) {
		viewIsShow = isShow;
		if (String.valueOf(viewId) != null) {
			View view = getView(viewId);
			view.setVisibility(viewIsShow ? View.VISIBLE : View.INVISIBLE);
		}
		return this;
	}

	/**
	 * 设置字体颜色TextView
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int color) {
		TextView view = getView(viewId);
		if (String.valueOf(color) != null) {
			view.setTextColor(color);
		}
		return this;
	}

	/**
	 * 设置背景图片TextView
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setBackgroundResource(int viewId, int resId) {
		TextView tv = getView(viewId);
		if (tv != null) {
			tv.setBackgroundResource(resId);
		}
		return this;
	}

	/**
	 * 设置背景颜色TextView
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setBackgroundColor(int viewId, int color) {
		TextView view = getView(viewId);
		if (view != null) {
			view.setBackgroundColor(color);
		}
		return this;
	}

	public ViewHolder setImageBackgroundColor(int viewId, int color) {
		ImageView view = getView(viewId);
		if (view != null) {
			view.setBackgroundColor(color);
		}
		return this;
	}

	/**
	 * 设置layout参数
	 * 
	 * @param viewId
	 * @param context
	 * @return
	 */
	public ViewHolder setLayout(int viewId, Activity context) {
		LinearLayout layout = getView(viewId);
		if (layout != null) {
			DisplayMetrics dm = null;
			if (dm == null) {
				dm = new DisplayMetrics();
			}
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = (dm.widthPixels / 2 - 10);
			int height = width;
			android.view.ViewGroup.LayoutParams params = layout
					.getLayoutParams();
			params.height = height;
			params.width = width;
			layout.setLayoutParams(params);
		}

		return this;
	}

	/**
	 * 为TextView设置tag
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setTag(int viewId, String text) {
		View view = getView(viewId);
		if (text != null) {
			view.setTag(text);
		}
		return this;
	}

	public ViewHolder setTag(int viewId, int text) {
		View view = getView(viewId);
		view.setTag(text);
		return this;
	}

	/**
	 * 为TextView设置visibility
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		View view = getView(viewId);
		view.setBackgroundResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageViewResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置网络图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, String url) {
		ImageView view = getView(viewId);
		if (!TextUtils.isEmpty(url)) {
			ImageLoaderUtil.loadImage(url, view);
		}
		return this;
	}

	public ViewHolder setloadXUtilImage(int viewId, String url) {
		ImageView view = getView(viewId);
		if (!TextUtils.isEmpty(url)) {
			ImageLoaderUtil.loadXUtilImage(url, view);
		}
		return this;
	}

	public ViewHolder setImageXutilBitmap(int viewId, String url) {
		ImageView view = getView(viewId);
		if (!TextUtils.isEmpty(url)) {
			ImageLoaderUtil.loadXUtilImage(url, view);
		}
		return this;
	}

	public ViewHolder setImageBitmapNoDefault(int viewId, String url) {
		ImageView view = getView(viewId);
		if (!TextUtils.isEmpty(url)) {
			ImageLoaderUtil.loadImageNoDefault(url, view);
		}
		return this;
	}

	/**
	 * 为View设置监听
	 * 
	 * @param viewId
	 * @param clickListener
	 * @return
	 */
	public ViewHolder setOnclick(int viewId, OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * 为View设置长按监听
	 * */
	public ViewHolder setOnLongClick(int viewId,
			OnLongClickListener longListener) {
		View view = getView(viewId);
		view.setOnLongClickListener(longListener);
		return this;
	}

	/**
	 * 
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setOnTouchListener(int viewId, OnTouchListener listener) {
		View view = getView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
