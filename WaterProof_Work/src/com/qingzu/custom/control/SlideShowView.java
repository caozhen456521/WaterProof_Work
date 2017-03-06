package com.qingzu.custom.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.qingzu.entity.ImsInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.WebViewActivity;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 * 
 * 
 */

public class SlideShowView extends FrameLayout {

	// 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
	// private ImageLoader imageLoader = ImageLoader.getInstance();

	// 轮播图图片数量
	private final static int IMAGE_COUNT = 5;
	// 自动轮播的时间间隔
	private final static int TIME_INTERVAL = 5;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = true;

	// 自定义轮播图的资源
	// private String[] imageUrls;

	private List<ImsInfo> imsInfos = new ArrayList<ImsInfo>();
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;
	private ImageView view = null;

	private String UserToken = null;
	// Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
			imageViewsList.get(currentItem).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(context, WebViewActivity.class);
							intent.putExtra("url", imsInfos.get(currentItem)
									.getAdUrl());

							intent.putExtra("title", imsInfos.get(currentItem)
									.getAdTitle());
							context.startActivity(intent);
//							Toast.makeText(context,
//									"第" + currentItem + "" + "张",
//									Toast.LENGTH_SHORT).show();
						}
					});

		}

	};

	public SlideShowView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		// initImageLoader(context);

		initData();
		if (isAutoPlay) {
			startPlay();
		}

	}

	/**
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 4, 4,
				TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {

		SharedPreferences preferences = context.getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();

		// 一步任务获取图片
		new GetListTask().execute("");
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		if (imsInfos == null || imsInfos.size() == 0)
			return;

		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();

		// 热点个数与图片特殊相等
		for (int i = 0; i < imsInfos.size(); i++) {
			view = new ImageView(context);
			view.setTag(imsInfos.get(i).getImageUrl());
			// if (i == 0)// 给一个默认图
			// view.setBackgroundResource(R.drawable.appmain_subject_1);
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		viewPager.setAdapter(new MyPagerAdapter());
	

	}

	/**
	 * 加载数据
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	@SuppressWarnings("unchecked")
	public void getMessage(final String Page, String Size, final Context context) {
		RequestParams params = new RequestParams(
				HttpUtil.GetAdvertisementListByPage.replace("{Page}", Page)
						.replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		x.http().get(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ImsInfo> interfaceReturn = new InterfaceReturn<ImsInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ImsInfo.class);
				if (interfaceReturn != null && interfaceReturn.isStatus()) {
					if (interfaceReturn.getResults() != null) {
						imsInfos = interfaceReturn.getResults();
					}
				}
				if (imsInfos != null) {
					initUI(context);
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
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});
		// BaseGetDataController controller = new BaseGetDataController(context,
		// new OnDataGetListener() {
		//
		// @Override
		// public void onGetDataSuccesed(
		// ResponseInfo<JSONObject> status) {
		// // ac_mHandler.sendEmptyMessage(CLOSE_DIALOG);
		// // dialog.dismiss();
		// String jsonStr = Tools.formatString(status.result);
		// InterfaceReturn<ImsInfo> interfaceReturn = new
		// InterfaceReturn<ImsInfo>();
		// interfaceReturn = InterfaceReturn.fromJson(jsonStr,
		// ImsInfo.class);
		// if (interfaceReturn != null
		// && interfaceReturn.isStatus()) {
		// if (interfaceReturn.getResults() != null) {
		// imsInfos = interfaceReturn.getResults();
		// }
		// }
		// if (imsInfos != null) {
		// initUI(context);
		// }
		// }
		//
		// @Override
		// public void onGetDataFailed(HttpException status) {
		// // TODO Auto-generated method stub
		// // dialog.dismiss();
		// Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT)
		// .show();
		// }
		// });
		//
		// RequestParams params = new RequestParams();
		// params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		//
		// controller.getDatas(
		// HttpUtil.GetAdvertisementListByPage.replace("{Page}", Page)
		// .replace("{Size}", Size), params);

	}

	/**
	 * 填充ViewPager的页面适配器
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// ((ViewPag.er)container).removeView((View)object);
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			ImageView imageView = imageViewsList.get(position);

			ImageOptions imageOptions = new ImageOptions.Builder()
					.setLoadingDrawableId(R.drawable.icon_stub)// 加载中默认显示图片
					.setUseMemCache(true)// 设置使用缓存
					.setFailureDrawableId(R.drawable.icon_error)// 加载失败后默认显示图片
					.build();
			x.image().bind(imageView, imsInfos.get(position).getImageUrl(),
					imageOptions);

	
			
			// ImageLoader.getInstance().displayImage(
			// imageView.getTag() + "",
			// imageView,
			// MyApplication.getInstance().getOptions(
			// R.drawable.icon_stub, R.drawable.icon_empty,
			// R.drawable.icon_error));
			((ViewPager) container).addView(imageViewsList.get(position));

			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub

			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(pos))
							.setBackgroundResource(R.drawable.dot_focus);
				} else {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}

	}

	/**
	 * 执行轮播图切换任务
	 * 
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	/**
	 * 销毁ImageView资源，回收内存
	 * 
	 */

	private void destoryBitmaps() {

		for (int i = 0; i < IMAGE_COUNT; i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// 解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}

	public void println(String string) {
		// TODO Auto-generated method stub

	}

	/**
	 * 异步任务,获取数据
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片
				getMessage("1", "4", context);
				// imageUrls = new String[] {
				// "http://image.zcool.com.cn/56/35/1303967876491.jpg",
				// "http://image.zcool.com.cn/59/54/m_1303967870670.jpg",
				// "http://image.zcool.com.cn/47/19/1280115949992.jpg",
				// "http://image.zcool.com.cn/59/11/m_1303967844788.jpg" };
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
			//	initUI(context);
			}
		}
	}

	// /**
	// * ImageLoader 图片组件初始化
	// *
	// * @param context
	// */
	// public static void initImageLoader(Context context) {
	// // This configuration tuning is custom. You can tune every option, you
	// // may tune some of them,
	// // or you can create default configuration by
	// // ImageLoaderConfiguration.createDefault(this);
	// // method.
	//
	// DisplayImageOptions options = new DisplayImageOptions.Builder()
	// .showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
	// .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
	// .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
	// .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
	// .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
	// // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
	// .build(); // 创建配置过得DisplayImageOption对象
	//
	// ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	// context.getApplicationContext())
	// .defaultDisplayImageOptions(options)
	// .threadPriority(Thread.NORM_PRIORITY - 2)
	// .denyCacheImageMultipleSizesInMemory()
	// .discCacheFileNameGenerator(new Md5FileNameGenerator())
	// .tasksProcessingOrder(QueueProcessingType.LIFO).build();
	// ImageLoader.getInstance().init(config);
	// }
	// ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	// context).threadPriority(Thread.NORM_PRIORITY - 2)
	// .denyCacheImageMultipleSizesInMemory()
	// .discCacheFileNameGenerator(new Md5FileNameGenerator())
	// .tasksProcessingOrder(QueueProcessingType.LIFO)
	// .writeDebugLogs() // Remove
	// // for
	// // release
	// // app
	// .build();
	// // Initialize ImageLoader with configuration.
	// ImageLoader.getInstance().init(config);
}
