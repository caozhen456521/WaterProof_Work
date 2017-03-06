package com.qingzu.application;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 */
public class AppManager {
	private static Stack<SoftReference<Activity>> activityStack;
	private static AppManager instance;

	public AppManager() {
	}

	/**
	 * 单实例 , UI无需考虑多线程同步问题
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	public void OpenActivity(Context aoContext, Class<?> aoClass,
			Bundle aoParams) {
		if (currentActivity() != null
				&& currentActivity().getClass() == aoClass)
			return;
		Intent intent = new Intent(aoContext, aoClass);
		intent.putExtras(aoParams);
		aoContext.startActivity(intent);
	}

	public void OpenActivity(Context aoContext, Class aoClass) {
		if (currentActivity() != null && aoClass != null
				&& currentActivity().getClass() == aoClass)
			return;
		if (aoClass != null && aoContext != null) {
			Intent intent = new Intent(aoContext, aoClass);
			aoContext.startActivity(intent);
		}
	}

	public void OpenActivity(Context aoContext, Class aoClass,
			Map<String, String> aoParams) {
		if (currentActivity() != null
				&& currentActivity().getClass() == aoClass)
			return;
		Intent intent = new Intent(aoContext, aoClass);
		if (aoParams != null) {
			for (Map.Entry<String, String> entry : aoParams.entrySet()) {

				intent.putExtra(entry.getKey(), entry.getValue());

			}
		}

		aoContext.startActivity(intent);
	}

	public void OpenActivityForResultWithParam(Activity mContext, Class mClass,
			int requestCode, Bundle aoParam) {
		if (currentActivity() != null && currentActivity().getClass() == mClass)
			return;
		Intent intent = new Intent(mContext, mClass);
		intent.putExtras(aoParam);
		mContext.startActivityForResult(intent, requestCode);
	}

	public void OpenActivityForResult(Activity mContext, Class mClass,
			int requestCode) {
		if (currentActivity() != null && currentActivity().getClass() == mClass)
			return;
		Intent intent = new Intent(mContext, mClass);
		mContext.startActivityForResult(intent, requestCode);
	}

	public void OpenActivityForAction(Activity mContext, String action,
			Uri mUri, int requestCode) {
		if (currentActivity() != null)
			return;
		Intent intent = new Intent(action, mUri);
		mContext.startActivityForResult(intent, requestCode);
	}

	/**
	 * 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (activity == null)
			return;
		if (activityStack == null) {
			activityStack = new Stack<SoftReference<Activity>>();
		}
		activityStack.add(new SoftReference(activity));
	}

	/**
	 * 获取当前Activity（栈顶Activity）
	 */
	public Activity currentActivity() {
		if (activityStack == null || activityStack.isEmpty()) {
			return null;
		}
		Activity activity = activityStack.lastElement().get();
		return activity;
	}

	/**
	 * 获取当前Activity（栈顶Activity） 没有找到则返回null
	 */
	public Activity findActivity(Class<?> cls) {
		if (activityStack == null)
			return null;
		Activity activity = null;
		if (activityStack == null)
			return activity;
		for (SoftReference<Activity> aty : activityStack) {
			if (aty != null && aty.get() != null
					&& aty.get().getClass().equals(cls)) {
				activity = aty.get();
				break;
			}
		}
		return activity;
	}

	/**
	 * 获取当前Activity（栈顶Activity） 没有找到则返回null
	 */
	public Activity findActivity(String astrName) {
		Activity activity = null;
		for (SoftReference<Activity> aty : activityStack) {
			if (aty != null && aty.get() != null
					&& aty.get().getClass().getName().equals(astrName)) {
				activity = aty.get();
				break;
			}
		}
		return activity;
	}

	/**
	 * 结束当前Activity（栈顶Activity）
	 */
	public void finishActivity() {
		if (activityStack == null || activityStack.isEmpty()
				|| activityStack.lastElement() == null)
			return;
		Activity activity = activityStack.lastElement().get();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public static void finishActivity(Activity activity) {
		if (activity != null) {
			// activityStack.remove(activity);
			while (activity == AppManager.getAppManager().currentActivity()) {
				activityStack.pop();
			}

			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	private static void finishActivity(SoftReference<Activity> activity) {
		if (activity != null) {
			activityStack.removeElement(activity);
			activity.get().finish();
			activity = null;
		}
	}

	/**
	 * 隐藏指定的View(重载)
	 */
	public static void hideView(Activity activity) {
		if (activity != null) {
			activity.finish();
		}
	}

	/**
	 * 显示指定的View(重载)
	 */
	public static void showView(Activity activity) {
		if (activity != null) {
			activity.finish();
		}
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public static void finishActivity(Class<?> cls) {
		while (getAppManager().findActivity(cls) != null) {
			for (SoftReference<Activity> activity : activityStack) {
				if (activity != null && activity.get() != null
						&& activity.get().getClass().equals(cls)) {
					finishActivity(activity);
					break;
				}
			}
		}

	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(String astrClassName) {
		while (getAppManager().findActivity(astrClassName) != null) {
			for (SoftReference<Activity> activity : activityStack) {
				if (activity == null || activity.get() == null)
					continue;
				if (activity.get().getClass().getName().equals(astrClassName)) {
					finishActivity(activity);
					break;
				}
			}
		}
	}

	/**
	 * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
	 * 
	 * @param cls
	 */
	public static void finishOthersActivity(Class<?> cls) {
		SoftReference<Activity> activity = null;
		for (int i = 0; i < activityStack.size(); i++) {
			if (null != activityStack.get(i)) {
				activity = activityStack.get(i);
				if (!(activity.get().getClass().equals(cls))) {
					finishActivity(activity);
					i--;
				}
			}
		}
	}

	/**
	 * 关闭栈顶activity以外的所有activity InnFinally
	 * 
	 * @author Johnson
	 */
	public static void finishOthersActivityExceptIF() {
		SoftReference<Activity> activity = null;
		for (int i = 0; i < activityStack.size(); i++) {
			if (null != activityStack.get(i)) {
				activity = activityStack.get(i);
				if (i != activityStack.size() - 1) {
					finishActivity(activity);
					i--;
				}
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public static void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)
					&& activityStack.get(i).get() != null) {
				activityStack.get(i).get().finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 结束所有Activity ，只保留了splashActivity
	 */
	public static void finishAllActivity2() {
		if (activityStack == null)
			return;
		while (activityStack.size() > 0) {
			SoftReference<Activity> loTopActivity = activityStack.pop();
			if (null != loTopActivity && loTopActivity.get() != null) {
				loTopActivity.get().finish();
			}
		}
	}

	/**
	 * 应用程序退出
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}