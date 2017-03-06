package com.qingzu.utils.tools;

import io.jchat.android.application.JChatDemoApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xutils.x;

import cn.jmessage.android.uikit.chatting.ChatActivity;
import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import com.example.imagedemo.ImagePagerActivity;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.Member;
import com.qingzu.utils.http.HttpUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 工具类
 * 
 * @author TangWei 2013-10-24上午10:38:01
 */
public class Tools {
	public static void getUserInfo(final String member ,final Context context) {
		final Intent intent = new Intent();
		JMessageClient.getUserInfo(member,
				new GetUserInfoCallback() {
					@Override
					public void gotResult(final int status, String desc,
							final UserInfo userInfo) {
						// mLoadingDialog.dismiss();

						if (status == 0) {
							Conversation conv = Conversation
									.createSingleConversation(member);
							if (conv != null) {
								String targetId = ((UserInfo) conv
										.getTargetInfo()).getUserName();
								intent.putExtra(JChatDemoApplication.TARGET_ID,
										targetId);
								intent.putExtra(
										JChatDemoApplication.TARGET_APP_KEY,
										conv.getTargetAppKey());
								Log.d("ConversationList",
										"Target app key from conversation: "
												+ conv.getTargetAppKey());
								// intent.putExtra(JChatDemoApplication.DRAFT,
								// member.getNickName());
								intent.setClass(context,
										ChatActivity.class);
								context.startActivity(intent);
							}
					

						} else if (status == 898002) {
							JMessageClient.register(member,
									"112233", new BasicCallback() {

										@Override
										public void gotResult(int arg0,
												String arg1) {
											// TODO Auto-generated method stub
											if (arg0 == 0) {
												JMessageClient.getUserInfo(
														member,
														new GetUserInfoCallback() {

															@Override
															public void gotResult(
																	int arg0,
																	String arg1,
																	UserInfo arg2) {
																// TODO
																// Auto-generated
																// method stub
																if (arg0 == 0) {

																	Conversation conv = Conversation
																			.createSingleConversation(member);
																	if (conv != null) {
																		String targetId = ((UserInfo) conv
																				.getTargetInfo())
																				.getUserName();
																		intent.putExtra(
																				JChatDemoApplication.TARGET_ID,
																				targetId);
																		intent.putExtra(
																				JChatDemoApplication.TARGET_APP_KEY,
																				conv.getTargetAppKey());
																		Log.d("ConversationList",
																				"Target app key from conversation: "
																						+ conv.getTargetAppKey());
																		// intent.putExtra(JChatDemoApplication.DRAFT,
																		// member.getNickName());
																		intent.setClass(
																				context,
																				ChatActivity.class);
																		context
																				.startActivity(
																						intent);
																	}
																}
															}
														});
											}
										}
									});
						}

						else {
							HandleResponseCode.onHandle(context, status,
									true);
						}
					}
				});
	}
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 * @return
	 */
	public static void imageBrower(Context mContext, int position,
			ArrayList<CreateFindWorkPhotoInfo> RemarkList) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, RemarkList);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	
	
	
	
	
	/**
	 * 将文件转换成byet
	 * 
	 * @author TangWei 2013-10-24上午10:38:01
	 */

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 压缩图片
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */

	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.postRotate(0);

		return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
	}

	/**
	 * 格式化价格
	 * 
	 * @param argStr
	 * @return
	 */
	public static String getFloatDotStr(String argStr) {
		float arg = Float.valueOf(argStr);
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(arg);
	}

	public static String formatNumStr(Object arg) {
		try {
			String argStr = formatString(arg);
			Double d = Double.valueOf(argStr);
			DecimalFormat fnum = new DecimalFormat("##0.00");
			return fnum.format(d);
		} catch (Exception e) {
			return "0.00";
		}
	}

	public static int formatNumInt(Object arg) {
		try {
			String argStr = formatString(arg);
			Integer d = Integer.valueOf(argStr);
			return d;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String formatIntToString(int arg) {
		if (arg == 0) {
			return "";
		} else {
			return formatString(arg);
		}
	}

	public static void Log(String s) {
		if (s == null) {
			s = "传进来的是null";
		}

		Log.i("info", s);
	}

	public static void Toast(Context context, String s) {
		// if (context == null)
		// context = ShiQiangApplication.getInstance().getApplicationContext();
		if (s != null) {
			android.widget.Toast.makeText(context, s,
					android.widget.Toast.LENGTH_SHORT).show();
		}
	}

	public static void Toast(Context context, int res) {
		if (context != null) {
			Toast(context, context.getString(res));
		}
	}

	public static boolean IsHaveInternet(final Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manger.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			return false;
		}
	}

	// 得到versionName
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;

	}

	public static String millisToString(long millis) {
		boolean negative = millis < 0;
		millis = java.lang.Math.abs(millis);

		millis /= 1000;
		int sec = (int) (millis % 60);
		millis /= 60;
		int min = (int) (millis % 60);
		millis /= 60;
		int hours = (int) millis;

		String time;
		DecimalFormat format = (DecimalFormat) NumberFormat
				.getInstance(Locale.US);
		format.applyPattern("00");
		if (millis > 0) {
			time = (negative ? "-" : "")
					+ (hours == 0 ? 00 : hours < 10 ? "0" + hours : hours)
					+ ":" + (min == 0 ? 00 : min < 10 ? "0" + min : min) + ":"
					+ (sec == 0 ? 00 : sec < 10 ? "0" + sec : sec);
		} else {
			time = (negative ? "-" : "") + min + ":" + format.format(sec);
		}
		return time;
	}

	/**
	 * 
	 * @Title: getPopedom
	 * @Description: 获取权限
	 * @author jinqs
	 * @return boolean 返回类型
	 * @param popedom
	 *            输入本页面权限
	 * @return 0:有匹配，可以进 1：无匹配，返回主页面 2：无任何权限，说明没登录
	 */

	// 得到versionName
	public static int getVerCode(Context context) {
		int verCode = 0;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;

	}

	/**
	 * 判断 多个字段的值否为空
	 * 
	 * @author Michael.Zhang 2013-08-02 13:34:43
	 * @return true为null或空; false不null或空
	 */
	public static boolean isNull(String... ss) {
		for (int i = 0; i < ss.length; i++) {
			if (null == ss[i] || ss[i].equals("")
					|| ss[i].equalsIgnoreCase("null")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断 一个字段的值否为空
	 * 
	 * @author Michael.Zhang 2013-9-7 下午4:39:00
	 * @param s
	 * @return
	 */
	public static boolean isNull(String s) {
		if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断 多个字段的值否为空
	 * 
	 * @author Michael.Zhang 2013-08-02 13:34:43
	 * @return true为null或空; false不null或空
	 */
	public static boolean isNull(TextView... vv) {
		for (int i = 0; i < vv.length; i++) {
			if (null == vv[i] || Tools.isNull(Tools.getText(vv[i]))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断 一个字段的值否为空
	 * 
	 * @author Michael.Zhang 2013-9-7 下午4:39:00
	 * @param s
	 * @return
	 */
	public static boolean isNull(TextView v) {
		if (null == v || Tools.isNull(Tools.getText(v))) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * 判断 一个字段的值否为空
	 * 
	 * @author Michael.Zhang 2013-9-7 下午4:39:00
	 * @param s
	 * @return
	 */
	public static boolean isNull(EditText v) {
		if (null == v || Tools.isNull(Tools.getText(v))) {
			return true;
		}

		return false;
	}

	/**
	 * 判断两个字段是否一样
	 * 
	 * @author Michael.Zhang 2013-08-02 13:32:51
	 */
	public static boolean judgeStringEquals(String s0, String s1) {
		return s0 != null && s0.equals(s1);
	}

	/**
	 * 将dp类型的尺寸转换成px类型的尺寸
	 * 
	 * @param size
	 * @param context
	 * @return
	 */
	public static int DPtoPX(int size, Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metrics);
		return (int) ((float) size * metrics.density + 0.5);
	}

	/**
	 * 屏幕宽高
	 * 
	 * @author TangWei 2013-11-5上午10:27:54
	 * @param context
	 * @return 0:width，1:height
	 */
	public static int[] ScreenSize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metrics);
		return new int[] { metrics.widthPixels, metrics.heightPixels };
	}

	/**
	 * double 整理
	 * 
	 * @return
	 */
	public static Double roundDouble(double val, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = ((0 == val) ? new BigDecimal("0.0") : new BigDecimal(
				Double.toString(val)));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 判断 列表是否为空
	 * 
	 * @return true为null或空; false不null或空
	 */
	public static boolean isEmptyList(List list) {
		return list == null || list.size() == 0;
	}

	/**
	 * 判断 列表是否为空
	 * 
	 * @return true为null或空; false不null或空
	 */
	public static boolean isEmptyList(List... list) {
		for (int i = 0; i < list.length; i++) {
			if (isEmptyList(list[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断 列表是否为空
	 * 
	 * @return true为null或空; false不null或空
	 */
	public static boolean isEmptyList(Object[] list) {
		return list == null || list.length == 0;
	}

	/**
	 * 判断 列表是否为空
	 * 
	 * @return true为null或空; false不null或空
	 */
	public static boolean isEmptyList(Object[]... list) {
		for (int i = 0; i < list.length; i++) {
			if (isEmptyList(list[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断sd卡是否存在
	 * 
	 * @author Michael.Zhang 2013-07-04 11:30:54
	 * @return
	 */
	public static boolean judgeSDCard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 判断 http 链接
	 * 
	 * @author Michael.Zhang
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		return url != null && url.startsWith("http://");
	}

	/**
	 * 获取保存到View的Tag中的字符串
	 * 
	 * @param v
	 * @return
	 */
	public static String getTagString(View v) {
		try {
			return v.getTag().toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取文本控件上显示的文字
	 * 
	 * @author TangWei 2013-9-29下午2:40:52
	 * @param tv
	 * @return
	 */
	public static String getText(TextView tv) {
		if (tv != null)
			return tv.getText().toString().trim();
		return "";
	}

	/**
	 * 获取文本控件上显示的文字
	 * 
	 * @author TangWei 2013-9-29下午2:40:52
	 * @param tv
	 * @return
	 */
	public static String getText(EditText tv) {
		if (tv != null)
			return tv.getText().toString().trim();
		return "";
	}

	/**
	 * 隐藏键盘
	 * 
	 * @author TangWei 2013-9-13下午7:51:32
	 */
	public static void hideKeyboard(Activity activity) {
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// public static void playSound(int raw, Context context) {
	// SoundPool sp;
	// sp = new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
	// int task = sp.load(context, raw, 1);
	// sp.play(task, 1, 1, 0, 0, 1);
	// }

	/**
	 * 显示纯汉字的星期名称
	 * 
	 * @author TangWei 2013-10-25上午11:31:51
	 * @param i
	 *            星期：1,2,3,4,5,6,7
	 * @return
	 */
	public static String changeWeekToHanzi(int i) {
		switch (i) {
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		case 7:
			return "星期日";
		default:
			return "";
		}
	}

	/**
	 * 验证身份证号码
	 * 
	 * @author TangWei
	 * @param idCard
	 * @return
	 */
	public static boolean validateIdCard(String idCard) {
		if (isNull(idCard))
			return false;
		String pattern = "^[0-9]{17}[0-9|xX]{1}$";
		return idCard.matches(pattern);
	}

	/**
	 * 验证手机号码
	 * 
	 * @author TangWei
	 * @param phone
	 * @return
	 */
	public static boolean validatePhone(String phone) {
		if (isNull(phone))
			return false;
		String pattern = "^1[3,4,5,6,8]+\\d{9}$";
		return phone.matches(pattern);
	}

	/**
	 * 简单的验证一下银行卡号
	 * 
	 * @param bankCard
	 *            信用卡是16位，其他的是13-19位
	 * @return
	 */
	public static boolean validateBankCard(String bankCard) {
		if (isNull(bankCard))
			return false;
		String pattern = "^\\d{13,19}$";
		return bankCard.matches(pattern);
	}

	/**
	 * 验证邮箱
	 * 
	 * @author TangWei 2013-12-13下午2:33:16
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {
		if (isNull(email))
			return false;
		String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
		return email.matches(pattern);
	}

	/**
	 * 去掉前后空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trimString(String str) {
		if (!Tools.isNull(str)) {
			return str.trim();
		}
		return "";
	}

	/**
	 * 转换为Int
	 * 
	 * @param str
	 * @return
	 */
	public static int StringToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * String字符串转换成float
	 * 
	 * @param str
	 * @return
	 */
	public static float StringToFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return 0.00f;
		}
	}

	/**
	 * 转换为String
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatString(Object obj) {
		try {
			if (!Tools.isNull(obj.toString())) {
				return obj.toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean formatBoolean(Object obj) {
		try {
			if (!Tools.isNull(obj.toString())) {
				return Boolean.valueOf(obj.toString());
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化money，当返回数据为空时，返回0.00
	 * 
	 * @author TangWei 2013-11-23上午11:42:33
	 * @param obj
	 * @return
	 */
	public static String formatMoney(Object obj) {
		String moneyStr = formatString(obj);
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,###.00");
		try {
			double money = Double.valueOf(moneyStr);
			String text = myformat.format(money);
			if (text.indexOf(".") == 0) {
				text = "0" + text;
			}
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			return "0.00";
		}
	}

	/**
	 * 格式化日期，针对于传过来的日期是毫秒数
	 * 
	 * @author TangWei 2013-11-29上午11:31:49
	 * @param date
	 *            日期毫秒数
	 * @param format
	 *            格式化样式 示例：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDate(Object date, String format) {
		try {
			return new SimpleDateFormat(format).format(new Date(Long
					.parseLong(formatString(date)) * 1000));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期，针对于传过来的日期是毫秒数<br>
	 * 转换样式：2013-11-12 11:12:13
	 * 
	 * @author TangWei 2013-11-22上午11:38:13
	 * @param date
	 *            日期毫秒数
	 * @return
	 */
	public static String formatTime(Object date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String subFormatDate(Object date) {
		String data = formatString(date);
		try {
			return data.split(" ")[0];
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期，针对于传过来的日期是毫秒数<br>
	 * 转换样式：2013-11-12
	 * 
	 * @author TangWei 2013-11-22上午11:38:13
	 * @param date
	 *            日期毫秒数
	 * @return
	 */
	public static String formatDate(Object date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 获取屏幕像素尺寸
	 * 
	 * @author TangWei 2013-10-31下午1:08:22
	 * @return 数组：0-宽，1-高
	 */
	public static int[] getScreenSize(Context context) {
		int[] size = new int[2];
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metrics);
		size[0] = metrics.widthPixels;
		size[1] = metrics.heightPixels;
		return size;
	}

	/**
	 * 设置圆角的图片
	 * 
	 * @author TangWei 2013-12-10下午4:43:33
	 * @param bitmap
	 *            图片
	 * @param pixels
	 *            角度
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		try {
			if (bitmap != null) {
				Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
						bitmap.getHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

				final int color = 0xff424242;
				final Paint paint = new Paint();
				final Rect rect = new Rect(0, 0, bitmap.getWidth(),
						bitmap.getHeight());
				final RectF rectF = new RectF(rect);
				final float roundPx = pixels;

				paint.setAntiAlias(true);
				canvas.drawARGB(0, 0, 0, 0);
				paint.setColor(color);
				canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(bitmap, rect, rect, paint);

				return output;
			}
		} catch (Exception e) {
		}

		return bitmap;
	}

	/**
	 * 将图片转换为圆形的
	 * 
	 * @author TangWei 2013-12-10下午4:45:47
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			bitmap = cutSquareBitmap(bitmap);
			return toRoundCorner(bitmap, bitmap.getWidth() / 2);
		}
		return bitmap;
	}

	/**
	 * 把图片切成正方形的
	 * 
	 * @author TangWei 2013-12-10下午5:16:18
	 * @param bitmap
	 * @return
	 */
	public static Bitmap cutSquareBitmap(Bitmap bitmap) {
		try {
			if (bitmap != null) {
				Bitmap result;
				int w = bitmap.getWidth();// 输入长方形宽
				int h = bitmap.getHeight();// 输入长方形高
				int nw;// 输出正方形宽
				if (w > h) {
					// 宽大于高
					nw = h;
					result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw,
							nw);
				} else {
					// 高大于宽
					nw = w;
					result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw,
							nw);
				}
				return result;
			}
		} catch (Exception e) {
		}
		return bitmap;
	}

	/**
	 * 获取在GridView中一行中一张正方形图片的尺寸大小
	 * 
	 * @author TangWei 2013-12-12下午1:15:49
	 * @param context
	 *            上下文，用于计算屏幕的宽度
	 * @param offset
	 *            界面上左右两边的偏移量，dp值
	 * @param spac
	 *            水平方向，图片之间的间距，dp值
	 * @param count
	 *            一行中图片的个数
	 * @return
	 */
	public static int getImageSize(Context context, int offset, int spac,
			int count) {
		int width = getScreenSize(context)[0] - Tools.DPtoPX(offset, context)
				- (Tools.DPtoPX(spac, context) * (count - 1));
		return width / count;
	}

	/**
	 * 获取一个圆弧上等分点的坐标列表
	 * 
	 * @author TangWei 2013-12-16下午5:06:31
	 * @param radius
	 *            半径
	 * @param count
	 *            等分点个数
	 * @param start_angle
	 *            开始角度
	 * @param end_angle
	 *            结束角度
	 * @return
	 */
	public static ArrayList<String[]> getDividePoints(double radius, int count,
			double start_angle, double end_angle) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		double sub_angle = (start_angle - end_angle) / ((double) (count - 1));
		for (int i = 0; i < count; i++) {
			double angle = (start_angle - sub_angle * i) * Math.PI / 180;
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);
			list.add(new String[] { x + "", y + "" });
		}
		return list;
	}

	/**
	 * 判断字符串是邮箱还是手机号码
	 * 
	 * @author TangWei 2013-12-19下午1:59:16
	 * @param str
	 * @return 1-手机号码，2-邮箱，如果都不是则返回0
	 */
	public static int validatePhoneOrEmail(String str) {
		if (validatePhone(str))
			return 1;
		if (validateEmail(str))
			return 2;
		return 0;
	}

	/**
	 * 播放动画
	 * 
	 * @param layout
	 * @param img
	 * @param isWhiteBg
	 * @param isCollect
	 *            true（点击）、false（取消）
	 */
	public static void startAnimation(final View layout, ImageView img,
			int drawableBefore, int drawableClick, boolean isClicked) {
		if (isClicked) {
			img.setBackgroundResource(drawableClick);
		} else {
			img.setBackgroundResource(drawableBefore);
		}

		// 播放动画
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.2f, 1, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation1.setStartOffset(0);
		scaleAnimation1.setDuration(50);
		scaleAnimation2.setStartOffset(50);
		scaleAnimation2.setDuration(50);
		animationSet.addAnimation(scaleAnimation1);
		animationSet.addAnimation(scaleAnimation2);
		animationSet.setFillAfter(true);
		img.startAnimation(animationSet);
		animationSet.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				layout.setEnabled(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout.setEnabled(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	/*
	 * public static String bitmapToBase64(Bitmap bitmap) {
	 * 
	 * String result = null; ByteArrayOutputStream baos = null; try { if (bitmap
	 * != null) { baos = new ByteArrayOutputStream();
	 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	 * 
	 * baos.flush(); baos.close();
	 * 
	 * byte[] bitmapBytes = baos.toByteArray(); result =
	 * Base64.encode(bitmapBytes); } } catch (IOException e) {
	 * e.printStackTrace(); } finally { try { if (baos != null) { baos.flush();
	 * baos.close(); } } catch (IOException e) { e.printStackTrace(); } } return
	 * result; }
	 */

	/**
	 * SD卡是否存在
	 */
	public static boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 创建文件夹
	 */
	public static void makeDir(String path) {
		File rootFile = new File(path);
		if (!rootFile.exists()) {
			rootFile.mkdir();
		}
	}

	/**
	 * 根据Uri返回文件路径
	 * 
	 * @author gdpancheng@gmail.com 2013-3-18 上午10:17:55
	 * @param mUri
	 * @return String
	 */
	public static String getFilePath(ContentResolver mContentResolver, Uri mUri) {
		try {
			if (mUri.getScheme().equals("file")) {
				return mUri.getPath();
			} else {
				return getFilePathByUri(mContentResolver, mUri);
			}
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	/**
	 * 将100以内的阿拉伯数字转换成中文汉字（15变成十五）
	 * 
	 * @param round
	 *            最大值50
	 * @return >99的，返回“”
	 */
	public static String getHanZi1(int round) {
		if (round > 99 || round == 0) {
			return "";
		}
		int ge = round % 10;
		int shi = (round - ge) / 10;
		String value = "";
		if (shi != 0) {
			if (shi == 1) {
				value = "十";
			} else {
				value = getHanZi2(shi) + "十";
			}

		}
		value = value + getHanZi2(ge);
		return value;
	}

	/**
	 * 将0-9 转换为 汉字（ _一二三四五六七八九）
	 * 
	 * @param round
	 * @return
	 */
	public static String getHanZi2(int round) {
		String[] value = { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		return value[round];
	}

	/**
	 * 将content: 开通的系统uri转换成绝对路径
	 * 
	 * @param mContentResolver
	 * @param mUri
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getFilePathByUri(ContentResolver mContentResolver,
			Uri mUri) throws FileNotFoundException {

		String imgPath;
		Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
		cursor.moveToFirst();
		imgPath = cursor.getString(1); // 图片文件路径
		return imgPath;
	}

	/**
	 * 去除字符串中的 ":"
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteColon(String str) {
		if (str == null) {
			return null;
		} else {
			return str.replace(":", "");
		}
	}

	/**
	 * 将 1800 加个":",变成 18:00
	 * 
	 * @param str
	 * @return
	 */
	public static String addColon(String str) {
		if (str == null || str.length() != 4) {
			return null;
		}
		return str.substring(0, 2) + ":" + str.substring(2, 4);
	}

	/**
	 * @author ligt 2013-6-9 上午11:53:19
	 * @param email_str
	 * @return
	 * @return boolean
	 */
	public boolean is_Email(String email_str) {

		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher m = pattern.matcher(email_str);
		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1[3,4,5,7,8]\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 是否是数字
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isInt(String mobiles) {
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 是否是身份证(二代)
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isIdCard(String mobiles) {
		Pattern p = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证密码格式
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isPassWord(String mobiles) {
		Pattern p = Pattern.compile("[0-9]+[a-zA-Z]+[*/+]{6}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isNumner(String numner) {
		Pattern p = Pattern.compile("[0-9]+[a-zA-Z]+[*/+]{4}");
		Matcher m = p.matcher(numner);
		return m.matches();
	}

	public static boolean isCode(String mobiles) {
		Pattern p = Pattern.compile("[0-9]{4}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 
	 * @Title: isMoneyNO
	 * @Description:
	 * @author luck
	 * @param money
	 * @return boolean 返回类型
	 */
	public static boolean isMoneyNO(String money) {
		String text = "^[1-9][0-9]+\\.?[0-9]{0,2}$";
		if (money.indexOf(".") == 1 && money.startsWith("0")) {
			text = "^[0-9]+\\.?[0-9]{0,2}$";
		}
		Pattern p = Pattern.compile(text);
		Matcher m = p.matcher(money);
		return m.matches();
	}

	/**
	 * 
	 * @Title: makeTextColor
	 * @Description: 此方法用来使textView局部字体变色
	 * @author lip
	 * @return void 返回类型
	 */
	public static void makeTextColor(TextView tv, int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(tv.getText()
				.toString().trim());
		style.setSpan(new ForegroundColorSpan(Color.rgb(255, 121, 76)), start,
				end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv.setText(style);
	}

	public static void makeTextColor(EditText tv, int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(tv.getText()
				.toString().trim());
		style.setSpan(new ForegroundColorSpan(Color.rgb(255, 121, 76)), start,
				end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv.setText(style);
	}

	public static void makeTextColor(int red, int green, int blue, TextView tv,
			int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(tv.getText()
				.toString().trim());
		style.setSpan(new ForegroundColorSpan(Color.rgb(red, green, blue)),
				start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv.setText(style);
	}

	public static void makeTextColor(int red, int green, int blue, EditText tv,
			int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(tv.getText()
				.toString().trim());
		style.setSpan(new ForegroundColorSpan(Color.rgb(red, green, blue)),
				start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv.setText(style);
	}

	public static boolean isPass(String text) {
		if (isHaveBig(text) && isHaveSmall(text) && isHaveNum(text)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHaveBig(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				return true;
			}
		}
		return false;
	}

	public static boolean isHaveSmall(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 'a' && c <= 'z') {
				return true;
			}
		}
		return false;
	}

	public static boolean isHaveNum(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= '0' && c <= '9') {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Title: isNull
	 * @Description:
	 * @author luck
	 * @param serializableExtra
	 * @return
	 * @return boolean 返回类型
	 */
	public static boolean isNull(Serializable s) {
		if (null == s || s.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前的时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static String getStringToDate(String time) {
		Date date = null;
		String substring = null;
		try {
			date = new Date();
			long l = date.getTime();
			String str = String.valueOf(l);
			substring = str.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return substring;
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 转大写
	 * 
	 * @author Johnson
	 * @date 2015-12-30
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * MD5大写
	 * 
	 * @author Johnson
	 * @date 2015-12-30
	 * @param s
	 * @return
	 */
	public static String getMd5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * MD5加密32位小写
	 * 
	 * @author Johnson
	 * @date 2015-12-30
	 * @param sSecret
	 * @return
	 */
	public static String getMd5Value(String sSecret) {
		try {
			MessageDigest bmd5 = MessageDigest.getInstance("MD5");
			bmd5.update(sSecret.getBytes());
			int i;
			StringBuffer buf = new StringBuffer();
			byte[] b = bmd5.digest();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	// public static String getBASE64(String s) {
	// if (s == null)
	// return null;
	// return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	// }
	//
	// // 将 BASE64 编码的字符串 s 进行解码 解密
	// public static String getFromBASE64(String s) {
	// if (s == null)
	// return null;
	// sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	// try {
	// byte[] b = decoder.decodeBuffer(s);
	// return new String(b);
	// } catch (Exception e) {
	// return null;
	// }
	// }
	//
	// /**
	// * 加密
	// * @author Johnson
	// * @date 2015-12-30
	// * @param ming
	// * @return
	// */
	// public static String mTOa(Object ming) {
	// return getBASE64(getBASE64(getBASE64((String) ming)));
	// }
	//
	// /**
	// * 解密
	// * @author Johnson
	// * @date 2015-12-30
	// * @param an
	// * @return
	// */
	// public static String aTOm(String an) {
	// return getFromBASE64(getFromBASE64(getFromBASE64(an)));
	// }

	/**
	 * base64加密UTF-8
	 * 
	 * @author Johnson
	 * @date 2015-12-30
	 * @param an
	 * @return
	 */
	public static String mTOa(String an) {
		String save_utf8;
		try {
			sun.misc.BASE64Encoder bs64en = new sun.misc.BASE64Encoder();
			String save = bs64en.encode(an.getBytes());
			save_utf8 = URLEncoder.encode(save, "utf-8");
			return save_utf8;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取EZFSToken
	 * 
	 * @author Johnson
	 * @date 2015-12-30
	 * @param UserToken
	 * @return
	 */
	public static String getEZFSToken(String UserToken) {
		String AppToken = getMd5("1D2AB280-4C08-47DB-A46B-428B7703B881");
		String ApiId = "5";
		long Timestamp = System.currentTimeMillis() / 1000;
		String EZFSToken = mTOa(Timestamp + "@" + UserToken + "@" + ApiId + "@"
				+ AppToken);
		return EZFSToken.replace("%3D", "=").replace("%0A", "");
	}

	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1280f;// 这里设置高度为800f
		float ww = 720f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressBmpFromBmp(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressBmpFromBmp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * 时间工具类 (小季)
	 * 
	 * @param paramDate
	 * @return
	 */
	public static String getTimestampString(java.util.Date paramDate) {
		String str = null;
		long l = paramDate.getTime();
		Calendar localCalendar = GregorianCalendar.getInstance();
		localCalendar.setTime(paramDate);
		int year = localCalendar.get(Calendar.YEAR);
		if (!isSameYear(year)) { // 去年，直接返回
			String paramDate2str = new SimpleDateFormat("yyyy MM dd",
					Locale.CHINA).format(paramDate);
			return paramDate2str;
		}

		if (isSameDay(l)) {
			int i = localCalendar.get(Calendar.HOUR_OF_DAY);
			if (i > 17) {
				str = "晚上 HH:mm";// HH表示24小时,hh表示12小时进制，
			} else if ((i >= 0) && (i <= 6)) {
				str = "凌晨 HH:mm";
			} else if ((i > 11) && (i <= 17)) {
				str = "下午 HH:mm";
			} else {
				str = "上午 HH:mm";
			}
		} else if (isYesterday(l)) {
			str = "昨天 HH:mm";
		} else if (isBeforeYesterday(l)) {
			str = "前天 HH:mm";
		} else {
			str = "M月d日 HH:mm";
		}
		String paramDate2str = new SimpleDateFormat(str, Locale.CHINA)
				.format(paramDate);
		return paramDate2str;
	}

	// 获取 今天开始结束 时间

	public static TimeInfo getTodayStartAndEndTime() {

		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
		localCalendar1.set(Calendar.MINUTE, 0);
		localCalendar1.set(Calendar.SECOND, 0);
		localCalendar1.set(Calendar.MILLISECOND, 0);
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
		localCalendar2.set(Calendar.MINUTE, 59);
		localCalendar2.set(Calendar.SECOND, 59);
		localCalendar2.set(Calendar.MILLISECOND, 999);
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();

		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	// 获取 昨天开始结束 时间

	public static TimeInfo getYesterdayStartAndEndTime() {
		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.add(Calendar.DAY_OF_MONTH, -1);// 5
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);// 11
		localCalendar1.set(Calendar.MINUTE, 0);// 12
		localCalendar1.set(Calendar.SECOND, 0);// 13
		localCalendar1.set(Calendar.MILLISECOND, 0);// Calendar.MILLISECOND
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.add(Calendar.DAY_OF_MONTH, -1);// 5
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);// 11
		localCalendar2.set(Calendar.MINUTE, 59);// 12
		localCalendar2.set(Calendar.SECOND, 59);// 13
		localCalendar2.set(Calendar.MILLISECOND, 999);// Calendar.MILLISECOND
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();

		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	// 获取 前天开始结束 时间

	public static TimeInfo getBeforeYesterdayStartAndEndTime() {
		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.add(Calendar.DAY_OF_MONTH, -2);
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
		localCalendar1.set(Calendar.MINUTE, 0);
		localCalendar1.set(Calendar.SECOND, 0);
		localCalendar1.set(Calendar.MILLISECOND, 0);
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.add(Calendar.DAY_OF_MONTH, -2);
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
		localCalendar2.set(Calendar.MINUTE, 59);
		localCalendar2.set(Calendar.SECOND, 59);
		localCalendar2.set(Calendar.MILLISECOND, 999);
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();
		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	private static boolean isSameDay(long paramLong) {
		TimeInfo localTimeInfo = getTodayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime())
				&& (paramLong < localTimeInfo.getEndTime());
	}

	private static boolean isYesterday(long paramLong) {
		TimeInfo localTimeInfo = getYesterdayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime())
				&& (paramLong < localTimeInfo.getEndTime());
	}

	private static boolean isBeforeYesterday(long paramLong) {
		TimeInfo localTimeInfo = getBeforeYesterdayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime())
				&& (paramLong < localTimeInfo.getEndTime());
	}

	public static boolean isSameYear(int year) {
		Calendar cal = Calendar.getInstance();
		int CurYear = cal.get(Calendar.YEAR);
		// Log.e("","CurYear="+CurYear);//2015
		return CurYear == year;
	}

	public static class TimeInfo {
		private long startTime;
		private long endTime;

		public long getStartTime() {
			return this.startTime;
		}

		public void setStartTime(long paramLong) {
			this.startTime = paramLong;
		}

		public long getEndTime() {
			return this.endTime;
		}

		public void setEndTime(long paramLong) {
			this.endTime = paramLong;
		}
	}

	/**
	 * END
	 */

	/**
	 * UDate转字符串
	 * 
	 * @param dateDate
	 * @return
	 * @author Johnson
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static java.util.Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		java.util.Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * T
	 * 
	 * @param strDate
	 * @return
	 */
	public static java.util.Date strToDateT(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		java.util.Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	// /**
	// * 获取键盘高度
	// *
	// * @param activity
	// * @param listener
	// */
	// public static void observeSoftKeyboard(Activity activity,
	// final OnSoftKeyboardChangeListener listener) {
	// final View decorView = activity.getWindow().getDecorView();
	// decorView.getViewTreeObserver().addOnGlobalLayoutListener(
	// new ViewTreeObserver.OnGlobalLayoutListener() {
	// int previousKeyboardHeight = -1;
	//
	// public void onGlobalLayout() {
	// Rect rect = new Rect();
	// decorView.getWindowVisibleDisplayFrame(rect);
	// int displayHeight = rect.bottom - rect.top;
	// int height = decorView.getHeight();
	// int keyboardHeight = height - displayHeight;
	// if (previousKeyboardHeight != keyboardHeight) {
	// boolean hide = (double) displayHeight / height > 0.8;
	// listener.onSoftKeyBoardChange(keyboardHeight, !hide);
	// }
	//
	// previousKeyboardHeight = height;
	//
	// }
	// });
	// }
	//
	// /**
	// * 获取键盘高度接口
	// *
	// * @author Johnson
	// *
	// */
	// public interface OnSoftKeyboardChangeListener {
	// void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
	// }

	/**
	 * 图片压缩
	 * 
	 * @author Johnson
	 * @param fileUri
	 * @return
	 */
	public static File scal(Uri fileUri) {
		String path = fileUri.getPath();
		File outputFile = new File(path);
		long fileSize = outputFile.length();
		final long fileMaxSize = 200 * 1024;
		if (fileSize >= fileMaxSize) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int height = options.outHeight;
			int width = options.outWidth;

			double scale = Math.sqrt((float) fileSize / fileMaxSize);
			options.outHeight = (int) (height / scale);
			options.outWidth = (int) (width / scale);
			options.inSampleSize = (int) (scale + 0.5);
			options.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			outputFile = new File(createImageFile().getPath());
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(outputFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("************", "ok" + outputFile.length());
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			} else {
				File tempFile = outputFile;
				outputFile = new File(createImageFile().getPath());
				copyFileUsingFileChannels(tempFile, outputFile);
			}

		}
		return outputFile;

	}

	public static Uri createImageFile() {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = null;
		try {
			image = File.createTempFile(imageFileName, /* prefix */
					".jpg", /* suffix */
					storageDir /* directory */
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Save a file: path for use with ACTION_VIEW intents
		return Uri.fromFile(image);
	}

	public static void copyFileUsingFileChannels(File source, File dest) {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			try {
				inputChannel = new FileInputStream(source).getChannel();
				outputChannel = new FileOutputStream(dest).getChannel();
				outputChannel
						.transferFrom(inputChannel, 0, inputChannel.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回首字母
	 * 
	 * @param strChinese
	 * @param bUpCase
	 * @return
	 */
	public static String getPYIndexStr(String strChinese, boolean bUpCase) {
		try {
			StringBuffer buffer = new StringBuffer();
			byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 255) > 128) {
					int char1 = b[i++] & 255;
					char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
					int chart = char1 + (b[i] & 255);
					buffer.append(getPYIndexChar((char) chart, bUpCase));
					continue;
				}
				char c = (char) b[i];
				// 确定指定字符是否可以是 Java 标识符中首字符以外的部分。
				if (!Character.isJavaIdentifierPart(c))
					c = 'A';
				buffer.append(c);
			}
			return buffer.toString();
		} catch (Exception e) {
			System.out.println((new StringBuilder())
					.append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
					.append(e.getMessage()).toString());
		}
		return null;
	}

	/**
	 * 得到首字母
	 * 
	 * @param strChinese
	 * @param bUpCase
	 * @return
	 */
	private static char getPYIndexChar(char strChinese, boolean bUpCase) {
		int charGBK = strChinese;
		char result;
		if (charGBK >= 45217 && charGBK <= 45252)
			result = 'A';
		else if (charGBK >= 45253 && charGBK <= 45760)
			result = 'B';
		else if (charGBK >= 45761 && charGBK <= 46317)
			result = 'C';
		else if (charGBK >= 46318 && charGBK <= 46825)
			result = 'D';
		else if (charGBK >= 46826 && charGBK <= 47009)
			result = 'E';
		else if (charGBK >= 47010 && charGBK <= 47296)
			result = 'F';
		else if (charGBK >= 47297 && charGBK <= 47613)
			result = 'G';
		else if (charGBK >= 47614 && charGBK <= 48118)
			result = 'H';
		else if (charGBK >= 48119 && charGBK <= 49061)
			result = 'J';
		else if (charGBK >= 49062 && charGBK <= 49323)
			result = 'K';
		else if (charGBK >= 49324 && charGBK <= 49895)
			result = 'L';
		else if (charGBK >= 49896 && charGBK <= 50370)
			result = 'M';
		else if (charGBK >= 50371 && charGBK <= 50613)
			result = 'N';
		else if (charGBK >= 50614 && charGBK <= 50621)
			result = 'O';
		else if (charGBK >= 50622 && charGBK <= 50905)
			result = 'P';
		else if (charGBK >= 50906 && charGBK <= 51386)
			result = 'Q';
		else if (charGBK >= 51387 && charGBK <= 51445)
			result = 'R';
		else if (charGBK >= 51446 && charGBK <= 52217)
			result = 'S';
		else if (charGBK >= 52218 && charGBK <= 52697)
			result = 'T';
		else if (charGBK >= 52698 && charGBK <= 52979)
			result = 'W';
		else if (charGBK >= 52980 && charGBK <= 53688)
			result = 'X';
		else if (charGBK >= 53689 && charGBK <= 54480)
			result = 'Y';
		else if (charGBK >= 54481 && charGBK <= 55289)
			result = 'Z';
		else
			result = (char) (65 + (new Random()).nextInt(25));
		if (!bUpCase)
			result = Character.toLowerCase(result);
		return result;
	}

	/**
	 * 获取字符串中的数字
	 * 
	 * @param string
	 * @return
	 */
	public static String getIntger(String string) {
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(string);
		return m.replaceAll("").trim();
	}

	/**
	 * 改变标题栏颜色
	 * 
	 * @param activity
	 * @param res
	 * @author Johnson
	 */
	public static void setNavigationBarColor(Activity activity, int res) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT)

		{
			setTranslucentStatus(activity, true);
			SystemBarTintManager tintManager = new SystemBarTintManager(
					activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(res);// 通知栏所需颜色
		}
	}

	public static void setTranslucentStatus(Activity activity, boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 检查网络
	 * 
	 * @return
	 * @author Johnson
	 */
	public static boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) MyApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
	
	/**
	 * ScrollView中嵌套ListView
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 获取ip地址
	 * 
	 * @param context
	 * @return
	 * @author Johnson
	 */
	// <uses-permission
	// android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
	// <uses-permission
	// android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
	// <uses-permission
	// android:name="android.permission.WAKE_LOCK"></uses-permission>
	public static String getLocalIpAddress(Context context) {

		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			// wifiManager.setWifiEnabled(true);
			return "8.8.8.8";
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return intToIp(ipAddress);
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 使用GPRS获取ip地址
	 * 
	 * @return
	 * @author Johnson
	 */
	// <uses-permission
	// android:name="android.permission.INTERNET"></uses-permission>
	public static String getLocalIpAddressGPRS() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	

}
