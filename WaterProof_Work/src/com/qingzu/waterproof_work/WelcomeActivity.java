package com.qingzu.waterproof_work;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;

import com.qingzu.application.AppManager;
import com.qingzu.application.StatusTool;
import com.qingzu.callback.OnLocationListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;

import com.qingzu.utils.tools.AMapLocations;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 欢迎界面
 * 
 * @author Johnson
 * 
 */
public class WelcomeActivity extends Activity implements Runnable {
	private String UserToken = null;
	private String tokenExpiredTimeStamp = null;

	private String currentCity = null; // 用于保存定位到的城市
	// 声明AMapLocationClient类对象
	public AMapLocationClient mlocationClient = null;
	// 声明定位回调监听器
	public AMapLocationListener mLocationListener = null;
	// private ConnectionDetector cd;
	private AsyncQueryHandler asyncQueryHandler;
	// 是否第一次启动
	private static AMapLocations AMapLocation = null;
	// private OnLocationListener onLocationListener = null;
    
	private SharedPreferences sps =null;
	private List<String> phoneList = new ArrayList<String>();
	private boolean isFirst;
	private boolean phone;
    private boolean isFristLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		AppManager.getAppManager().addActivity(this);
		SharedPreferences preferences = WelcomeActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");

		sps = getSharedPreferences(
				"LoginFrist", Activity.MODE_PRIVATE);
		isFristLogin=sps.getBoolean("isFristLogin", true);
		
		
		AMapLocations.getAMapLocations(WelcomeActivity.this)
				.setOnLocationListener(onLocationListener);
		new Thread(this).start();

	}

	public OnLocationListener onLocationListener = new OnLocationListener() {

		@Override
		public void OnLocationSuccesed(AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			currentCity = amapLocation.getCity();

			saveCity(currentCity);
		}

		@Override
		public void OnLocationFailed(AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			Log.e("AmapError",
					"location Error, ErrCode:" + amapLocation.getErrorCode()
							+ ", errInfo:" + amapLocation.getErrorInfo());
		}

		@Override
		public void OnLocationSuccesedGetCode(AMapLocation amapLocation) {
			// TODO Auto-generated method stub

		}
	};

	public void saveCity(String city) {
		SharedPreferences sp = getSharedPreferences("city",
				Activity.MODE_PRIVATE);
		if (sp.getString("city", "").equals("")) {
			Editor editor = sp.edit();
			editor.putString("city", city);
			editor.commit();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
			SharedPreferences sp = getSharedPreferences("isFirst", MODE_PRIVATE);
			isFirst = sp.getBoolean("isFirst", true);
			if (isFirst) {
				startActivity(new Intent(WelcomeActivity.this,
						GuideActivity.class));
			} else {
				Message msg = new Message();
				msg.what = 2;
				WelcomeActivity.this.mHandler.sendMessage(msg);
				Intent intent = new Intent();
				if (UserToken != null && !(UserToken.equals(""))) {
					
					
					
					intent.setClass(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					
				} else {
					intent.setClass(WelcomeActivity.this, LoginActivity.class);
					startActivity(intent);
				}

			}

			StatusTool.isWelcome = true;
			Editor editor = sp.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
			finish();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 登录用户刷新TOKEN
	 * 
	 * @author Johnson
	 * @date 2016-1-10
	 */
	public void RefreshToken() {
		RequestParams params = new RequestParams(HttpUtil.RefreshToken);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							SharedPreferences sharedPreferences = getSharedPreferences(
									"UserToken", Activity.MODE_PRIVATE);
							Editor edit = sharedPreferences.edit();
							edit.putString("UserToken", interfaceReturn
									.getResults().get(0).getToken());
							edit.putInt("MemberId", interfaceReturn
									.getResults().get(0).getMember().getId());
							edit.putLong("tokenExpiredTimeStamp",
									interfaceReturn.getResults().get(0)
											.getTokenExpiredTimeStamp());
							edit.putString("Phone", interfaceReturn
									.getResults().get(0).getMember()
									.getContactTel());
							edit.putString("UserName", interfaceReturn
									.getResults().get(0).getMember()
									.getUserName());
							edit.putString("UserPhoto", interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto());
							edit.putInt("IsCheck", interfaceReturn.getResults()
									.get(0).getMember().getIsCheck());
							edit.putBoolean("IsRealName", interfaceReturn
									.getResults().get(0).getMember()
									.isRealName());
							edit.putString("NickName", interfaceReturn
									.getResults().get(0).getMember()
									.getNickName());
							edit.putInt("IntegralNumber", interfaceReturn
									.getResults().get(0).getMember()
									.getIntegralNumber());
							if (interfaceReturn.getResults().get(0).getMember()
									.getDefaultRoleId() == 1) {
								edit.putInt("identity", 2);
							} else if (interfaceReturn.getResults().get(0)
									.getMember().getDefaultRoleId() == 2) {
								edit.putInt("identity", 0);
							} else if (interfaceReturn.getResults().get(0)
									.getMember().getDefaultRoleId() == 6) {
								edit.putInt("identity", 1);
							}
							edit.commit();
							
							LoginActivity.registerIM(interfaceReturn
									.getResults().get(0).getMember()
									.getUserName(), isFristLogin, interfaceReturn
									.getResults().get(0).getMember()
									.getNickName(), interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto(), WelcomeActivity.this, sps);
							
						}
					}
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				// Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
				// .show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				// if (cd.isConnectingToInternet()) {

				RefreshToken();

				// } else {
				// // 无网无数据,提示信息
				// T.showToast(WelcomeActivity.this,
				// getString(R.string.LinkNetwork));
				// }
				// RegionListAll();
				break;

			default:
				break;
			}
		};
	};

	/*
	 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
	 * 这时后面插入data表的数据，只有执行空值插入，才能使插入的联系人在通讯录里面可见
	 */
	public void testInsert() {
		ContentValues values = new ContentValues();
		/*
		 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
		 */
		Uri rawContactUri = this.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// 往data表里写入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); // 内容类型
		values.put(StructuredName.GIVEN_NAME, getString(R.string.app_name));
		this.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 往data表里写入电话数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, "4000042000");
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		this.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		//  添加头像
		values.clear();
		Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ez);
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		rawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
		values.put(Photo.PHOTO, array.toByteArray());
		this.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);
		;

		// // 往data表里写入Email的数据
		// values.clear();
		// values.put(Data.RAW_CONTACT_ID, rawContactId);
		// values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		// values.put(Email.DATA, "jl1335992999@163.com");
		// values.put(Email.TYPE, Email.TYPE_WORK);
		// this.getContentResolver().insert(
		// android.provider.ContactsContract.Data.CONTENT_URI, values);

	}

	public void judge() {
		PackageManager pkm = getPackageManager();
		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm
				.checkPermission("android.permission.READ_CONTACTS",
						"com.qingzu.waterproof_work"));
		if (has_permission) {
			// testReadAll();

		} else {
			// T.showToast(WelcomeActivity.this, "没有授权读取联系人,将影响体验");
		}
	}

	public void testReadAll() {
		// uri = content://com.android.contacts/contacts
		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问raw_contacts表
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { Data._ID }, null,
				null, null); // 获得_id属性
		while (cursor.moveToNext()) {
			StringBuilder buf = new StringBuilder();
			int id = cursor.getInt(0);// 获得id并且在data中寻找数据
			buf.append("id=" + id);
			uri = Uri.parse("content://com.android.contacts/contacts/" + id
					+ "/data"); // 如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
			Cursor cursor2 = resolver.query(uri, new String[] { Data.DATA1,
					Data.MIMETYPE }, null, null, null); // data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
			while (cursor2.moveToNext()) {
				String data = cursor2
						.getString(cursor2.getColumnIndex("data1"));
				// if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){
				// //如果是名字
				// buf.append(",name="+data);
				// }
				if (cursor2.getString(cursor2.getColumnIndex("mimetype"))
						.equals("vnd.android.cursor.item/phone_v2")) { // 如果是电话
					// buf.append(",phone="+data);
					phoneList.add(data);

				}
				// }
				// else
				// if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")){
				// //如果是email
				// buf.append(",email="+data);
				// }
				// else
				// if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")){
				// //如果是地址
				// buf.append(",address="+data);
				// }
				// else
				// if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")){
				// //如果是组织
				// buf.append(",organization="+data);
				// }
			}

			String str = buf.toString();
			Log.i("Contacts", str);
		}

		for (int i = 0; i < phoneList.size(); i++) {
			if (phoneList.get(i).equals("4000042000")) {
				phone = true;

			}

		}
		if (phone == false) {
			testInsert();
		}
	}
	/**
	 * 
	 * @author Administrator
	 * 
	 */

}
