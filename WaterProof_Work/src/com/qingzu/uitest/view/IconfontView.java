package com.qingzu.uitest.view;

import com.qingzu.waterproof_work.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconfontView extends RelativeLayout {

	private TextView iconfont_ico;
	private TextView iconfont_tx;
	private RelativeLayout icon_re;

	public IconfontView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null);
	}

	public IconfontView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public IconfontView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public void init(AttributeSet attrs) {
		LayoutInflater.from(getContext()).inflate(R.layout.iconfont_view, this);
		Typeface iconfont = Typeface.createFromAsset(getContext().getAssets(),
				"iconfont.ttf");
		icon_re = (RelativeLayout) findViewById(R.id.icon_re);
		iconfont_ico = (TextView) findViewById(R.id.iconfont_ico);
		iconfont_tx = (TextView) findViewById(R.id.iconfont_tx);
		iconfont_ico.setTypeface(iconfont);
		if (attrs == null) {
			return;
		}
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.IconfontView);
		int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int index = a.getIndex(i);
			switch (index) {
			case R.styleable.IconfontView_iconfont_ico_color:

				int color = a.getColor(index, Color.parseColor("#ffffff"));
				iconfont_ico.setTextColor(color);
				iconfont_tx.setTextColor(color);
				break;

			case R.styleable.IconfontView_iconfont_ico_text:
                String iconfont_ico_text =a.getString(index);
                iconfont_ico.setText(iconfont_ico_text);
                
				break;
			case R.styleable.IconfontView_iconfont_ico_size:

				int iconfont_ico_size = a.getDimensionPixelSize(index, 35);
				iconfont_ico.setTextColor(iconfont_ico_size);
				break;
			case R.styleable.IconfontView_iconfont_tx_text:
                String iconfont_tx_text =a.getString(index);
        		iconfont_tx.setText(iconfont_tx_text);
				break;
			case R.styleable.IconfontView_iconfont_tx_size:

				int iconfont_tx_size = a.getDimensionPixelSize(index, 35);
				iconfont_tx.setTextColor(iconfont_tx_size);
				break;
				
				
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @param IconfontView
	 *            点击事件
	 */
	public void setOnIconfontViewClickListener(OnClickListener onClickListener) {
		icon_re.setOnClickListener(onClickListener);
	}

	/**
	 * 
	 * @param setIconFontIcoText
	 *            修改图标
	 */
	public void setIconFontIcoText(int resId) {
		iconfont_ico.setText(resId);
		// iconfont_ico.setTextColor(Color.parseColor("#00CCFF"));

	}

	/**
	 * 
	 * @param setIconFontIcoText
	 *            修改图标颜色
	 */
	public void setIconFontIcoTextColor(String color) {

		iconfont_ico.setTextColor(Color.parseColor(color));
		iconfont_tx.setTextColor(Color.parseColor(color));
	}

	/**
	 * 
	 * @param setIconFontIcoText
	 *            修改文字
	 */
	public void setIconFont_txText(String resId) {
		iconfont_tx.setText(resId);

	}

//	/**
//	 * 
//	 * @param setIconFontIcoText
//	 *            修改图标颜色
//	 */
//	public void setIconFont_txTextColor(String color) {
//
//		iconfont_tx.setTextColor(Color.parseColor(color));
//
//	}

}
