package com.qingzu.utils.tools;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

/**
 * @ClassName: T
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lmw
 * @date 2015�?�?8�?下午5:34:58
 */
public class T {
	private static TipsToast tipsToast;

	public static void showTips(int iconResId, int msgResId, Context context) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(context, msgResId,
					TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msgResId);
	}

	public static void showTips(int iconResId, CharSequence s, Context context) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(context, s, TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setText(s);
		tipsToast.setIcon(iconResId);
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
