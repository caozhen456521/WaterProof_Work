package com.getui.demo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.qingzu.adapter.ProjectDetailPagerAdapter;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MemberMessage;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ConstructionDetailActivity;
import com.qingzu.waterproof_work.ForumActivity;
import com.qingzu.waterproof_work.HelpActivity;
import com.qingzu.waterproof_work.LoginActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.ProjectPushActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.WebViewActivity;
import com.qingzu.waterproof_work.WorkOrderActivity;

import java.util.HashMap;
import java.util.Iterator;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public NotificationManager nm = null;
	private int count = 0;

	@Override
	public void onReceive(Context context, Intent intent) {

		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		// if (bundle != null) {
		// CustomPushNotificationBuilder builder = new
		// CustomPushNotificationBuilder(
		// context, R.layout.customer_notitfication_layout, R.id.icon,
		// R.id.title, R.id.text);
		// // 指定定制的 Notification Layout
		// builder.statusBarDrawable = R.drawable.ic_launcher;
		// // 指定最顶层状态栏小图标
		// builder.layoutIconDrawable = R.drawable.ic_launcher;
		// // 指定下拉状态栏时显示的通知图标https://www.baidu.com/
		// JPushInterface.setPushNotificationBuilder(3, builder);
		//
		// }

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);

			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

			// Notification n = new Notification(R.drawable.ic_launcher,
			// bundle.getString(JPushInterface.EXTRA_MESSAGE),
			// System.currentTimeMillis());
			// n.flags = Notification.FLAG_AUTO_CANCEL;
			// intent = new Intent(context, MainActivity.class);
			// n.number = count;
			// intent.putExtra("name", "name:" + count);
			// PendingIntent pi = PendingIntent.getActivity(context, 100,
			// intent, PendingIntent.FLAG_CANCEL_CURRENT);
			// n.setLatestEventInfo(context, "标题",
			// "启动多次通知显示多条，每一条通知都能跳转到对应的活动", pi);
			// // 执行通知
			// nm.notify(count, n);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 打开自定义的Activity

			String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
			System.out.println(jsonStr);
			// {"Content":"Extra内容","MsgType":4,"TaskID":"27972"}

			// HashMap<String, Object> data = JsonUtil.parseJsonFinal(jsonStr);
			test message = new test();
			message = InterfaceReturn.getGson().fromJson(jsonStr, test.class);
			System.out.println(message.getContent());

			/**
			 * 
			 消1:纯内容通知;2:网页链接；3：有人报名-通知老板去审核，4：报名审核通过-通知工人，5：给工人推荐项目；6：问答-答案提交-
			 * 答案采纳
			 * ；7：反馈回复；8给老板推荐用工消息订单;9给工人推荐干活消息订单;10给买家推荐商品订单;11给店主推荐商品下单消息;12
			 * 给工长推荐防水工程派单消息
			 * 
			 * 
			 */
			Intent i = new Intent();
			SharedPreferences preferences = context.getSharedPreferences(
					"UserToken", Activity.MODE_PRIVATE);
			String UserToken = preferences.getString("UserToken", "");
			if (!UserToken.equals("")) {

				switch (message.getMsgType()) {
				case 1:
					i.setClass(context, MainActivity.class);
					// i.putExtra("url", message.getContent());

					// i.putExtra("title", message.getTitle());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;
				case 2:
					i.setClass(context, WebViewActivity.class);
					i.putExtra("url", message.getContent());

					// i.putExtra("title", message.getTitle());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 5:
					i.setClass(context, RecruitDetailActivity.class);
					i.putExtra("recruit", message.getContent());
					// i.putExtra("title", message.getTitle());
					i.putExtra("JPush", "JPush");
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 6:
					// ForumActivity.class).putExtra("VIEWPAGE_S", 1)
					i.setClass(context, ForumActivity.class);
					i.putExtra("VIEWPAGE_S", 1);
					// i.putExtra("title", message.getTitle());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 7:
					i.setClass(context, HelpActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 8:
					i.setClass(context, WorkOrderActivity.class);
					i.putExtra("IDENTITY", 1);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 9:
					i.setClass(context, WorkOrderActivity.class);
					i.putExtra("IDENTITY", 0);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 10:

					break;

				case 11:

					break;
				case 12:
					i.setClass(context, ProjectPushActivity.class);

					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;

				case 13:

					break;

				default:
					i.setClass(context, MainActivity.class);
					// i.putExtra("url", message.getContent());

					// i.putExtra("title", message.getTitle());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					break;
				}
			} else {
				i.setClass(context, LoginActivity.class);
				// i.putExtra("url", message.getContent());

				// i.putExtra("title", message.getTitle());
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				T.showToast(context, "请重新登录");
			}
			// message.setContent(Tools.formatString(data.get("Content")));
			// message.setMsgType(Tools.formatNumInt(data.get("MsgType")));
			// message.setTaskID(Tools.formatNumInt(data.get("TaskID")));
			//
			// Intent i = new Intent();
			// if (message.getMsgType() == 2) {
			// // ////////曹振干
			// i.setClass(context, WebViewActivity.class);
			// i.putExtra("url", message.getContent());
			//
			// i.putExtra("title", message.getTitle());
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// } else if (message.getMsgType() == 3) {
			// i.setClass(context, MyReleaseProjectDetailActivity.class);
			// i.putExtra("RPDLInfoId", message.getContent());
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// } else if (message.getMsgType() == 4) {
			// i.setClass(context, ProjectDetailActivity.class);
			// i.putExtra("PDLInfoId", message.getContent());
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// } else if (message.getMsgType() == 5) {
			// i.setClass(context, ProjectDetailActivity.class);
			// i.putExtra("PDLInfoId", message.getContent());
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// }else {
			// i.setClass(context, MainActivity.class);
			//
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// }
			//
			// // Intent i = new Intent(context, MainActivity.class);
			// // i.putExtras(bundle);
			// // //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// // Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// // context.startActivity(i);
			//
			// } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
			// .getAction())) {
			// Log.d(TAG,
			// "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
			// + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// // 打开一个网页等..
			//
			// } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
			// .getAction())) {
			// boolean connected = intent.getBooleanExtra(
			// JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			// Log.w(TAG, "[MyReceiver]" + intent.getAction()
			// + " connected state change to " + connected);
			// } else {
			// Log.d(TAG, "[MyReceiver] Unhandled intent - " +
			// intent.getAction());
			// }
		}
	}

	class test {
		private String Content;
		private Integer MsgType;
		private String TaskID;

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public Integer getMsgType() {
			return MsgType;
		}

		public void setMsgType(Integer msgType) {
			MsgType = msgType;
		}

		public String getTaskID() {
			return TaskID;
		}

		public void setTaskID(String taskID) {
			TaskID = taskID;
		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	public void intent() {

	}
	// send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (MainActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	// if (!ExampleUtil.isEmpty(extras)) {
	// try {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0) {
	// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e) {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }
}
