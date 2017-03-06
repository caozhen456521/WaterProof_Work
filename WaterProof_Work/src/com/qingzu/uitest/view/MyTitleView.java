package com.qingzu.uitest.view;

import com.qingzu.waterproof_work.R;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTitleView extends RelativeLayout {

	public interface TextChanged {
		public void TextChanged();
	}

	private RelativeLayout rlTitleRoom = null;
	private RelativeLayout Left = null;
	private Button LeftTitel = null;
	private ImageView LeftImg = null;
	private TextView Center = null;
	private Button Right = null;
	private TextChanged textChanged;
	private ImageView my_title_center_img;

	public MyTitleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null);
	}

	public MyTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public MyTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	/**
	 * 初始化
	 * 
	 * @param attrs
	 */
	private void init(AttributeSet attrs) {
		LayoutInflater.from(getContext()).inflate(
				R.layout.layout_my_title_view, this);
		LeftTitel = (Button) findViewById(R.id.my_title_left_title);
		Left = (RelativeLayout) findViewById(R.id.my_title_left);
		Center = (TextView) findViewById(R.id.my_title_center);
		my_title_center_img = (ImageView) findViewById(R.id.my_title_center_img);
		Right = (Button) findViewById(R.id.my_title_right);
		LeftImg = (ImageView) findViewById(R.id.my_title_left_img);
		rlTitleRoom = (RelativeLayout) findViewById(R.id.my_title_room);
		if (attrs == null) {
			return;
		}
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.TitleView);
		int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int index = a.getIndex(i);
			switch (index) {
			case R.styleable.TitleView_left_src:
				Drawable drawable = a.getDrawable(index);
				LeftTitel.setBackgroundDrawable(drawable);
				break;
			case R.styleable.TitleView_left_vidibility:
				int f = a.getInt(index, 0);
				if (f == 0) {
					LeftTitel.setVisibility(View.VISIBLE);
				} else if (f == 1) {
					LeftTitel.setVisibility(View.GONE);
				}
				break;
			case R.styleable.TitleView_left_text:
				String str = a.getString(index);
				LeftTitel.setText(str);
				break;
			case R.styleable.TitleView_left_text_color:
				int color = a.getColor(index, Color.BLACK);
				LeftTitel.setTextColor(color);
				break;
			case R.styleable.TitleView_left_text_size:
				int size = a.getDimensionPixelSize(index, 20);
				LeftTitel.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
				break;
			case R.styleable.TitleView_left_height:
				size = a.getDimensionPixelSize(index, 100);
				LeftTitel.getLayoutParams().height = size;
				break;
			case R.styleable.TitleView_left_width:
				size = a.getDimensionPixelSize(index, 100);
				LeftTitel.getLayoutParams().width = size;
				break;

			case R.styleable.TitleView_left_img_src:
				drawable = a.getDrawable(index);
				LeftImg.setBackgroundDrawable(drawable);
				break;
			case R.styleable.TitleView_left_img_height:
				size = a.getDimensionPixelSize(index, 100);
				LeftImg.getLayoutParams().height = size;
				break;
			case R.styleable.TitleView_left_img_width:
				size = a.getDimensionPixelSize(index, 100);
				LeftImg.getLayoutParams().width = size;
				break;
			case R.styleable.TitleView_left_img_vidibility:
				f = a.getInt(index, 0);
				if (f == 0) {
					LeftImg.setVisibility(View.VISIBLE);
				} else if (f == 1) {
					LeftImg.setVisibility(View.GONE);
				}
				break;

			case R.styleable.TitleView_center_text:
				str = a.getString(index);
				Center.setText(str);
				break;
			case R.styleable.TitleView_center_background:
				drawable = a.getDrawable(index);
				Center.setBackgroundDrawable(drawable);
				break;
			case R.styleable.TitleView_center_text_color:
				color = a.getColor(index, Color.BLACK);
				Center.setTextColor(color);
				break;
			case R.styleable.TitleView_center_text_size:
				size = a.getDimensionPixelSize(index, 20);
				Center.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
				break;
			case R.styleable.TitleView_center_height:
				size = a.getDimensionPixelSize(index, 100);
				Center.getLayoutParams().height = size;
				break;
			case R.styleable.TitleView_center_width:
				size = a.getDimensionPixelSize(index, 100);
				Center.getLayoutParams().width = size;
				break;

			case R.styleable.TitleView_right_src:
				drawable = a.getDrawable(index);
				Right.setBackgroundDrawable(drawable);
				break;
			case R.styleable.TitleView_right_vidibility:
				f = a.getInt(index, 0);
				if (f == 0) {
					Right.setVisibility(View.VISIBLE);
				} else if (f == 1) {
					Right.setVisibility(View.GONE);
				}
				break;
			case R.styleable.TitleView_right_text:
				str = a.getString(index);
				Right.setText(str);
				break;
			case R.styleable.TitleView_right_text_color:
				color = a.getColor(index, Color.BLACK);
				Right.setTextColor(color);
				break;
			case R.styleable.TitleView_right_text_size:
				size = a.getDimensionPixelSize(index, 20);
				Right.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
				break;
			case R.styleable.TitleView_right_height:
				size = a.getDimensionPixelSize(index, 100);
				Right.getLayoutParams().height = size;
				break;
			case R.styleable.TitleView_right_width:
				size = a.getDimensionPixelSize(index, 100);
				Right.getLayoutParams().width = size;
				break;

			case R.styleable.TitleView_title_background:
				color = a.getColor(index, Color.BLACK);
				rlTitleRoom.setBackgroundColor(color);
				break;
			}
		}
	}

	/**
	 * 左侧点击事件
	 * 
	 * @param clickLis
	 */
	public void setOnLeftClickListener(OnClickListener clickLis) {
		LeftTitel.setOnClickListener(clickLis);
	}

	/**
	 * 右侧点击事件
	 * 
	 * @param clickLis
	 */
	public void setOnRightClickListener(OnClickListener clickLis) {
		Right.setOnClickListener(clickLis);
	}

	/**
	 * 中间点击事件
	 * 
	 * @param clickLis
	 */
	public void setOnCenterClickListener(OnClickListener clickLis) {
		Center.setOnClickListener(clickLis);
	}
	/**
	 * 中间点击事件
	 * 
	 * @param clickLis
	 */
	public void setOnImgCenterClickListener(OnClickListener clickLis) {
		my_title_center_img.setOnClickListener(clickLis);
	}
	/**
	 * 设置标题背景
	 * 
	 * @param color
	 */
	public void setTitleBackground(int color) {
		rlTitleRoom.setBackgroundColor(color);
	}

	/**
	 * 设置标题文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		Center.setText(text);
	}

	public void setText(int resId) {
		Center.setText(resId);
	}

	/**
	 * 设置左侧文本
	 * 
	 * @param text
	 */
	public void setLeftText(String text) {
		LeftTitel.setText(text);
	}

	public void setLeftText(int resId) {
		LeftTitel.setText(resId);
	}

	/**
	 * 文本改变监听
	 * 
	 * @author Johnson
	 * @date 2016-1-25
	 */
	public void addLeftTextChangedListener(final TextChanged textChanged) {
		this.textChanged = textChanged;
		LeftTitel.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				textChanged.TextChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 设置右侧文本
	 * 
	 * @param text
	 */
	public void setRightText(String text) {
		Right.setText(text);
	}

	public void setRightText(int resId) {
		Right.setText(resId);
	}

	/**
	 * 右侧隐藏方法
	 * 
	 * @param Vidibility
	 */
	public void setRightVidibility(int Vidibility) {
		if (Vidibility == View.VISIBLE) {
			Right.setVisibility(View.VISIBLE);
		} else if (Vidibility == View.GONE) {
			Right.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置右边图片
	 * 
	 * @author Johnson
	 * @date 2016-1-11
	 * @param drawable
	 */
	public void setRightImg(Drawable drawable) {
		Right.setBackgroundDrawable(drawable);
	}

	// /**
	// * 设置右边图片
	// *
	// * @author Johnson
	// * @date 2016-1-11
	// * @param resId
	// */
	// public void setRightImg(int resId) {
	// Right.setBackgroundResource(resId);
	// }

	/**
	 * 设置标题图片
	 * 
	 * @author Johnson
	 * @date 2016-1-11
	 * @param drawable
	 */
	public void setCenterImg(Drawable drawable) {
		Center.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置标题图片
	 * 
	 * @author Johnson
	 * @date 2016-1-11
	 * @param resId
	 */
//	public void setCenterImg(int resId) {
//		Center.setBackgroundResource(resId);
//	}

	/**
	 * 设置标题图片
	 * 
	 * @author Johnson
	 * @date 2016-1-11
	 * @param resId
	 */
	public void setCenterImg(int resId) {
		my_title_center_img.setBackgroundResource(resId);
	}

	

	
}
