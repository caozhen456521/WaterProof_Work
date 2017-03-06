package com.qingzu.application;

import android.graphics.Bitmap;
import io.jchat.android.application.JChatDemoApplication;
import io.jchat.android.receiver.NotificationClickEventReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.xutils.x;

import cn.jmessage.android.uikit.chatting.utils.SharePreferenceManager;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

import com.amap.api.services.core.LatLonPoint;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.qingzu.waterproof_work.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @ClassName: MyApplication
 * @Description: Application�?
 * @author luck
 * @date 2015-6-3 上午9:46:07
 */
public class MyApplication extends Application {
	public static int screenWidth;
	public static int screenHeight;
	public static MyApplication context;
	public static double Longitude;// 经度
	public static double Latitude;// 纬度
	public static String Address;// 地址
	public static boolean isLocation;// 地址
	public static boolean isFirst = false;
	public static IWXAPI msgApi;
	public static final String JCHAT_CONFIGS = "JChat_configs";
	// public static LatLonPoint latLonPoint = null;
	public static LatLonPoint latLonPoint = null;
	public static String PICTURE_DIR = "sdcard/JChatQinZu/pictures/";

	public static MyApplication getInstance() {
		if (context == null) {
			context = new MyApplication();

		}
		return context;
	}

	/**
	 * 图片显示设置
	 */
	private DisplayImageOptions myOptions;

	@Override
	public void onCreate() {
		// Ioc.getIoc().init(this);
		super.onCreate();
		context = this;

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		x.Ext.init(this);// Xutils初始化
		init();

		Log.i("JpushDemoApplication", "init");
		// 初始化JMessage-sdk
		JMessageClient.init(getApplicationContext());
		SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
		// 设置Notification的模式
		JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
		// 注册Notification点击的接收器
		new NotificationClickEventReceiver(getApplicationContext());
	

	
		PlatformConfig.setWeixin("wx777b6f6393326373",
				"fe41e7afea118b3786ac9ec3c2fcc17d");

		PlatformConfig.setQQZone("1105017016", "ZdCI17s45I4J5qQv");

	}

	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * @Title: initScreenWidth
	 * @Description: 查看自身的宽�?
	 * @author luck
	 * @return void 返回类型
	 */
	private void initScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		Log.v("屏幕宽高", "宽度" + screenWidth + "高度" + screenHeight);
	}

	/**
	 * 
	 * @Description描述:默认的图片参数配�?
	 * @return
	 * @return DisplayImageOptions
	 */
	// public DisplayImageOptions getOptions() {
	// myOptions = new DisplayImageOptions.Builder()
	// .showImageOnLoading(R.drawable.defaultimg)
	// .showImageForEmptyUri(R.drawable.defaultimg)
	// .showImageOnFail(R.drawable.defaultimg).cacheInMemory(false)
	// .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	// return myOptions;
	// }
	public DisplayImageOptions getOptions() {
		myOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defaultimg)
				.showImageForEmptyUri(R.drawable.defaultimg)
				.showImageOnFail(R.drawable.defaultimg).cacheInMemory(false)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		return myOptions;
	}

	/**
	 * 
	 * @Description描述:默认的图片参数配�?
	 * @return
	 * @return DisplayImageOptions
	 */
	// public DisplayImageOptions getOptions(int resId) {
	// myOptions = new DisplayImageOptions.Builder().showImageOnLoading(resId)
	// .showImageForEmptyUri(resId).showImageOnFail(resId)
	// .cacheInMemory(true).cacheOnDisc(true)
	// .bitmapConfig(Bitmap.Config.RGB_565).build();
	// return myOptions;
	// }
	public DisplayImageOptions getOptions(int resId) {
		myOptions = new DisplayImageOptions.Builder().showImageOnLoading(resId)
				.showImageForEmptyUri(resId).showImageOnFail(resId)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return myOptions;
	}

	/**
	 * 
	 * @Description描述:设置图片参数配�?
	 * @return
	 * @return DisplayImageOptions
	 */
	// public DisplayImageOptions getOptions(int ImageOnLoading,
	// int ImageForEmpty, int ImageOnFail) {
	// myOptions = new DisplayImageOptions.Builder()
	// .showImageOnLoading(ImageOnLoading)
	// .showImageForEmptyUri(ImageForEmpty)
	// .showImageOnFail(ImageOnFail).cacheInMemory(true)
	// .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	// return myOptions;
	// }
	public DisplayImageOptions getOptions(int ImageOnLoading,
			int ImageForEmpty, int ImageOnFail) {
		myOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(ImageOnLoading)
				.showImageForEmptyUri(ImageForEmpty)
				.showImageOnFail(ImageOnFail).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		return myOptions;
	}

	/**
	 * 
	 * @Title: initImageLoader
	 * @Description: 初始化ImageLoader
	 * @author lmw
	 * @return void 返回类型
	 */
	// public static void initImageLoader(Context context) {
	// ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	// context).threadPriority(Thread.NORM_PRIORITY - 2)
	// .denyCacheImageMultipleSizesInMemory()
	// .discCacheFileNameGenerator(new Md5FileNameGenerator())
	// .tasksProcessingOrder(QueueProcessingType.LIFO)
	// .discCacheSize(50 * 1024 * 1024)//
	// .discCacheFileCount(100)// 缓存一百张图片
	// // .writeDebugLogs()
	// .build();
	// ImageLoader.getInstance().init(config);
	// }

	/**
	 * �?��网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		if (ser == null) {
			return false;
		}
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	public void init() {

		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
				.considerExifParams(true) // 调整图片方向
				.resetViewBeforeLoading(true) // 载入之前重置ImageView
				.showImageOnLoading(R.drawable.ic_picture_loading) // 载入时图片设置为黑色
				.showImageOnFail(R.drawable.ic_picture_loadfailed) // 加载失败时显示的图片
				.delayBeforeLoading(0) // 载入之前的延迟时间
				.build(); //
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultDisplayImageOptions)
				.memoryCacheExtraOptions(480, 800).threadPoolSize(5).build();
		ImageLoader.getInstance().init(config);

	}

	/**
	 * 
	 * @Title: clearAppCache
	 * @Description: 清除app缓存
	 * @author lmw
	 * @return void 返回类型
	 */
	// public void clearAppCache() {
	// // 清除webview缓存
	// File file = getCacheDir();
	// if (file != null && file.exists() && file.isDirectory()) {
	// for (File item : file.listFiles()) {
	// item.delete();
	// }
	// file.delete();
	// }
	// deleteDatabase("webview.db");
	// deleteDatabase("webview.db-shm");
	// deleteDatabase("webview.db-wal");
	// deleteDatabase("webviewCache.db");
	// deleteDatabase("webviewCache.db-shm");
	// deleteDatabase("webviewCache.db-wal");
	// // 清除数据缓存
	// clearCacheFolder(getFilesDir(), System.currentTimeMillis());
	// clearCacheFolder(getCacheDir(), System.currentTimeMillis());
	// // 2.2版本才有将应用缓存转移到sd卡的功能
	// if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
	// clearCacheFolder(MethodsCompat.getExternalCacheDir(this),
	// System.currentTimeMillis());
	// }
	// }

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方�?
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

}
