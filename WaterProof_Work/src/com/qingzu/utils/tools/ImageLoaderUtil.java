package com.qingzu.utils.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import org.xutils.x;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import com.qingzu.application.AppManager;
import com.qingzu.waterproof_work.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * 图片加载工具类
 * 
 * @author tml
 */
public class ImageLoaderUtil {
	/**
	 * 不保存在本地的图片加载方法
	 * 
	 * @param uri
	 * @param imageview
	 */
	// private static BitmapUtils goFinalBitmap = null;
	private static ImageOptions imageOptions = null;
	private static ImageOptions noImageOptions = null;
	

	public static void CheckMemoryGC() {
		long llMax = Runtime.getRuntime().maxMemory();
		long llCurMem = Runtime.getRuntime().totalMemory();
		long llFreeMem = Runtime.getRuntime().freeMemory();
		double ldPercentUsed = (double) llCurMem / (double) llMax;
		// double ldPercentUsed = (1-((double)llFreeMem/(double)llCurMem));
		// Log.i("nbzhou", "----gc------------gc---------gc---free"+llFreeMem
		// +"max"+llMax+"cur"+llCurMem+"="+ldPercentUsed);
		if (ldPercentUsed > 0.5) {
			// ClearImageCache();
			// Runtime.getRuntime().gc();
		}

	}

	public static void LoadLocalImg(ImageView aoView, Context aoContext,
			String astrPath) {
		Bitmap loBitMap = GetBitmapSoftReference(aoContext, astrPath);
		aoView.setImageBitmap(loBitMap);
	}

	public static Bitmap GetBitmapSoftReference(Context aoContext,
			String astrPath) {
		InputStream loStream;
		try {
			loStream = aoContext.getResources().getAssets().open(astrPath);
			SoftReference<Bitmap> bmpDrawable = new SoftReference<Bitmap>(
					BitmapFactory.decodeStream(loStream));

			return bmpDrawable.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static BitmapDrawable GetBitmapDrawableSoftReference(
			Context aoContext, String astrPath) {
		SoftReference<BitmapDrawable> bmpDrawable = new SoftReference<BitmapDrawable>(
				new BitmapDrawable(aoContext.getResources(), astrPath));
		return bmpDrawable.get();
	}

	public static void loadImage(String uri, ImageView imageview) {
		if (uri != null && uri.length() > 0) {
			Picasso.with(AppManager.getAppManager().currentActivity())
					.load(uri)
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
					.config(Config.RGB_565).into(imageview);

		}

		// if(imageview != null)
		// imageview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// if(goFinalBitmap == null)
		// {
		// Activity loActivity = AppManager.getAppManager().currentActivity();
		// if(loActivity == null)
		// return;
		// goFinalBitmap = new BitmapUtils(loActivity.getApplicationContext());
		//
		// //goFinalBitmap.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// goFinalBitmap.configDefaultLoadingImage(R.drawable.ic_launcher_gray);
		// goFinalBitmap.configDiskCacheEnabled(true);
		// goFinalBitmap.configMemoryCacheEnabled(true);
		// }
		//
		// if(goFinalBitmap != null)
		// {
		// goFinalBitmap.display(imageview,uri);
		// }

		// String lstrFileName = "";
		// if (uri != null)
		// lstrFileName = uri.substring(uri.lastIndexOf("/") + 1);
		// try {
		// if (AppManager.getAppManager().currentActivity() != null
		// && lstrFileName.isEmpty() == false)
		// loadImage(uri, imageview, true, AppManager.getAppManager()
		// .currentActivity(), lstrFileName);
		// else
		// loadImage(uri, imageview, true, null, null);
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

	}

	// public static void loadGifImage(Activity mActivity, String uri, final
	// ImageView imageview){
	// Glide.with(mActivity).load(uri).into(imageview);
	// }

	public static void loadImageNoDefault(String uri, final ImageView imageview) {
		if (uri != null && uri.length() > 0) {
			Picasso.with(AppManager.getAppManager().currentActivity())
					.load(uri)
					.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
					.config(Config.RGB_565).into(imageview);

		}
	}

	/**
	 * 有圓角
	 * 
	 * @param uri
	 * @param imageview
	 */
	public static void loadXUtilImage(String uri, final ImageView imageview) {
		if (imageOptions == null) {
			imageOptions = new ImageOptions.Builder()
					.setLoadingDrawableId(R.drawable.icon_stub)
					// 加载中默认显示图片
					.setUseMemCache(true)
					// 设置使用缓存
					 .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))//图片大小
					.setRadius(DensityUtil.dip2px(35)).setCrop(true)
					.setFailureDrawableId(R.drawable.icon_error)// 加载失败后默认显示图片
					.build();
		}

		if (imageOptions != null) {
			x.image().bind(imageview, uri, imageOptions);
		}

	}

	/**
	 * 沒有圓角
	 * 
	 * @param uri
	 * @param imageview
	 */
	public static void loadNoXUtilImage(String uri, final ImageView imageview) {
		if (noImageOptions == null) {
			noImageOptions = new ImageOptions.Builder() 
	        .setLoadingDrawableId(R.drawable.icon_stub)//加载中默认显示图片  
	        .setImageScaleType(ImageView.ScaleType.FIT_XY)
	        .setUseMemCache(true)//设置使用缓存  
	        .setFailureDrawableId(R.drawable.icon_error)//加载失败后默认显示图片  
	        .build();
		}
		if (noImageOptions != null) {
			x.image().bind(imageview, uri, noImageOptions);
		}

	}

	public static void loadAssetImage(final Context mContext, String path,
			ImageView imageview) {
		if (path != null && path.length() > 0) {
			String assetImagePath = "file:///android_asset/" + path;
			Picasso.with(mContext).load(assetImagePath).config(Config.RGB_565)
					.into(imageview);
		}

	}

	public static void loadImage(int drawable, ImageView imageview) {
		Picasso.with(AppManager.getAppManager().currentActivity())
				.load(drawable).config(Config.RGB_565).into(imageview);
	}

	/**
	 * 保存到本地的图片加载方法
	 * 
	 * @param uri
	 * @param imageview
	 * @param isSave
	 *            保存为true
	 * @param mContext
	 *            上下文Context
	 * @param fileName
	 *            保存的图片名字
	 * 
	 *            ImageView 加载本地图片的方法 BitmapFactory factory = new
	 *            BitmapFactory(); Bitmap bitmap =
	 *            factory.decodeFile(getFilesDir() + 图片名);
	 *            image.setImageBitmap(bitmap);
	 * 
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static BitmapFactory.Options Imagecompess(String astrPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(astrPath, options);

		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {

			return null;
		}

		options.inSampleSize = computeSampleSize(options, 600,
				(int) (0.1 * 1024 * 1024));
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return options;
	}
}
