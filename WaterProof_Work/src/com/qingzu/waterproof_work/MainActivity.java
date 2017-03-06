package com.qingzu.waterproof_work;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.model.UserInfo.Field;
import cn.jpush.im.api.BasicCallback;

import com.amap.api.location.AMapLocation;
import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;

import com.google.gson.Gson;

import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.callback.OnLocationListener;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.ConstructionTeamModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Order;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.fragment.BossFifthFragment;
import com.qingzu.fragment.BossFirstFragment;
import com.qingzu.fragment.BossFourthFragment;
import com.qingzu.fragment.BossSecondFragment;
import com.qingzu.fragment.BossThirdFragment;
import com.qingzu.fragment.CaptainFirstFragment;
import com.qingzu.fragment.CaptainFourthFragment;
import com.qingzu.fragment.CaptainSecondFragment;
import com.qingzu.fragment.CaptainThirdFragment;
import com.qingzu.fragment.WorkerFirstFragment;
import com.qingzu.fragment.WorkerSecondFragment;
import com.qingzu.fragment.WorkerThirdFragment;
import com.qingzu.uitest.view.IconfontView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.AMapLocations;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.GuideUtil;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.SystemBarTintManager;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.MyInformationActivity.userInfo;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity implements OnClickListener {

	protected static final int CREATE_TEAM = 7;
	private IconfontView btFirst = null;
	private IconfontView btSecond = null;
	private IconfontView btThird = null;
	private IconfontView btFourth = null;
	private IconfontView btFifth = null;
	private RelativeLayout third_re = null;
	private RelativeLayout second_re = null;
	private String UserToken = null;
	private boolean IsRealName = false;
	private int IsCheck = 0;
	private CaptainFirstFragment firstFragment = null;
	private CaptainSecondFragment secondFragment = null;
	private CaptainThirdFragment thirdFragment = null;
	private CaptainFourthFragment fourthFragment = null;
	private FragmentManager fragmentManager = null;
	private BossFirstFragment bossFirstFragment = null;
	private BossSecondFragment bossSecondFragment = null;
	private BossThirdFragment bossThirdFragment = null;
	private CaptainThirdFragment bossFourthFragment = null;
	private BossFifthFragment bossFifthFragment = null;
	private WorkerFirstFragment workerFirstFragment = null;
	private CaptainThirdFragment workerSecondFragment = null;
	private WorkerThirdFragment workerThirdFragment = null;
	public static ConstructionTeam constructionTeam = null;
	private SharedPreferences preferences = null;
	public Set<String> tags = null;
	private String currentCity = null; // 用于保存定位到的城市
	private String strCity = null;
	private boolean firstS = false;
	private GuideUtil guideUtil = null;
	private static OnCityListener onCityListener = null;
	private int state = 0; // 0:老板 1:工长 2: 工人
	public static final int LOAD_DATA = 33;
	public static final int MAIN_ISSUE_WORK = 17;
	public static final int MAIN_ISSUE_PROJECT = 18;
	private String UserName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		firstS = true;
		preferences = getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		state = preferences.getInt("identity", 0);
		UserName = preferences.getString("UserName", "");

		if (JMessageClient.getMyInfo() == null) {
			JMessageClient.login(UserName, "112233", new BasicCallback() {

				@Override
				public void gotResult(int status, String arg1) {
					// TODO Auto-generated method stub
					if (status == 0) {
						// mContext.startMainActivity();
						// UserInfo info =JMessageClient.getMyInfo();
						// info.setNickname(preferences.getString("NickName",
						// ""));
						// JMessageClient.updateMyInfo(Field.nickname, info, new
						// BasicCallback() {
						//
						// @Override
						// public void gotResult(int status, String arg1) {
						// // TODO Auto-generated method stub
						// if (status==0) {
						// T.showToast(MainActivity.this, "修改用户名成功");
						// }else{
						// Log.i("LoginController", "status = " + status);
						// HandleResponseCode.onHandle(MainActivity.this,
						// status, false);
						// }
						// }
						// });
						//

						// T.showToast(MainActivity.this, "登录成功");
					} else {
						Log.i("LoginController", "status = " + status);
						HandleResponseCode.onHandle(MainActivity.this, status,
								false);
					}

				}
			});
		}

		userToken();
		fragmentManager = getFragmentManager();
		AMapLocations.getAMapLocations(this).setOnLocationListener(
				onLocationListener);
		guideUtil = GuideUtil.getInstance();

		guideUtil.initGuide(this, R.drawable.guide);
		guideUtil.setFirst(false);
		btFirst = (IconfontView) findViewById(R.id.first_bt);
		btSecond = (IconfontView) findViewById(R.id.second_bt);
		btThird = (IconfontView) findViewById(R.id.third_bt);
		btFourth = (IconfontView) findViewById(R.id.fourth_bt);
		btFifth = (IconfontView) findViewById(R.id.fifth_bt);
		third_re = (RelativeLayout) findViewById(R.id.third_re);
		second_re = (RelativeLayout) findViewById(R.id.second_re);

		btFirst.setOnClickListener(this);
		btSecond.setOnClickListener(this);
		btThird.setOnClickListener(this);
		btFourth.setOnClickListener(this);
		btFifth.setOnClickListener(this);

		switch (state) {
		case 0:
			PushTags("老板");
			T.showToast(getApplicationContext(), "老板");
			third_re.setVisibility(View.VISIBLE);
			second_re.setVisibility(View.VISIBLE);
			break;
		case 1:
			PushTags("工长");
			T.showToast(getApplicationContext(), "工长");
			third_re.setVisibility(View.GONE);
			second_re.setVisibility(View.VISIBLE);
			btSecond.setIconFontIcoText(R.string.swith_button6);
			btSecond.setIconFont_txText("组队");
			break;
		case 2:
			PushTags("工人");
			T.showToast(getApplicationContext(), "工人");
			second_re.setVisibility(View.GONE);
			third_re.setVisibility(View.GONE);
			break;

		default:
			break;
		}

		Tools.setNavigationBarColor(this, R.color.title_background_black);

		setTabSelection(0);
	}

	public OnLocationListener onLocationListener = new OnLocationListener() {

		@Override
		public void OnLocationSuccesed(AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			currentCity = amapLocation.getCity();
			judgeCity();

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

	public void judgeCity() {
		if (StatusTool.isWelcome == true) {
			BDAutoUpdateSDK.cpUpdateCheck(getApplicationContext(),
					new MyCPCheckUpdateCallback());
		}
		SharedPreferences preferences = getSharedPreferences("city",
				Activity.MODE_PRIVATE);
		strCity = preferences.getString("city", "");
		final Editor edt = preferences.edit();
		if (strCity.equals("")) {
			edt.putString("city", currentCity);
			edt.commit();
		} else if (StatusTool.isWelcome == true && strCity != ""
				&& !strCity.equals(currentCity) && strCity != null) {
			HintDialog.Builder builder = new HintDialog.Builder(this);
			builder.setMessage(getString(R.string.Your_Current_City_Is)
					+ currentCity + getString(R.string.Wheather_Switch_City));
			builder.setTitle(getString(R.string.Locate_City_Tips));
			// builder.setSingleChoiceItems
			builder.setNegativeButton(R.string.Yes,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							onCityListener.OnCityAddress(currentCity);
							edt.putString("city", currentCity);
							edt.commit();
							dialog.dismiss();
							StatusTool.isWelcome = false;
							delete();
						}
					});
			builder.setPositiveButton(R.string.No,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							delete();
							dialog.dismiss();
							// 设置你的操作事项

							// into();
						}
					});

			builder.create().show();

		}
	}

	private void userToken() {
		getUserInfo();
	}

	@Override
	protected void onStart() {
		super.onStart();
		userToken();
	}

	/**
	 * 设置推送标签
	 * 
	 * @param tag
	 * @author Johnson
	 */
	public void PushTags(final String tag) {
		RequestParams params = new RequestParams(HttpUtil.PushTags);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					org.json.JSONArray jsonArray = (org.json.JSONArray) jsonObject
							.get("results");
					JSONObject jsonObject2 = null;
					tags = new HashSet<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject2 = (JSONObject) jsonArray.get(i);
						System.out.println(jsonObject2.get("tagName"));
						tags.add(jsonObject2.get("tagName") + "");
					}
					tags.add(tag);
					if (MyApplication.isFirst == false
							&& preferences.getInt("MemberId", 0) != 0) {
						JPushInterface.setAliasAndTags(getApplicationContext(),
								preferences.getInt("MemberId", 0) + "", tags,
								mAliasCallback);

						System.out.println(preferences.getInt("MemberId", 0));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				}
			}

		});

	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, final String alias,
				final Set<String> tags) {
			// TODO Auto-generated method stub
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i("TAG", alias);
				System.out.println(tags);
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

				MyApplication.isFirst = true;
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				// Log.i("TAG", logs);
				// 延迟 60 秒来调用 Handler 设置别名

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						JPushInterface.setAliasAndTags(getApplicationContext(),
								alias, tags, mAliasCallback);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				break;
			default:
				logs = "Failed with errorCode = " + code;
				Log.e("TAG", logs);

				break;
			}

		}

	};

	private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

		@Override
		public void onCheckUpdateCallback(AppUpdateInfo info,
				AppUpdateInfoForInstall infoForInstall) {
			if (info != null) {
				BDAutoUpdateSDK.uiUpdateAction(getApplicationContext(),
						new UICheckUpdateCallback() {

							@Override
							public void onCheckComplete() {

							}
						});

			} else {
				// T.showToast(getApplicationContext(), "已是最新版本");
			}

		}

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.first_bt:
			Tools.setNavigationBarColor(this, R.color.title_background_black);
			setTabSelection(0);
			break;
		case R.id.second_bt:

			if (state == 1) {
				if (IsCheck == 1) {
					hintDialog("提示", "您的身份正在审核,请耐心等待", 2);
					return;
				} else if (!IsRealName) {
					hintDialog("提示", "您还有没有实名认证,请认证后再操作", 1);
					return;
				} else {
					MyConstructionTeam();
				}
			} else if (state == 0) {
				MyListByPage();
			} else {
				Tools.setNavigationBarColor(this,
						R.color.title_background_black);
				setTabSelection(1);
			}
			break;
		case R.id.third_bt:
			if (state == 0) {
				ProjectMyList();
			} else {
				Tools.setNavigationBarColor(this,
						R.color.title_background_black);
				setTabSelection(2);
			}
			break;

		case R.id.fourth_bt:
			Tools.setNavigationBarColor(this, R.color.title_background_black);
			setTabSelection(3);
			break;
		case R.id.fifth_bt:
			Tools.setNavigationBarColor(this, R.color.title_background_blue);
			setTabSelection(4);
			break;

		default:
			break;
		}
	}

	/**
	 * 当前登录用户获取防水工程项目列表信息//判断PageCount
	 */
	private void ProjectMyList() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.ProjectMyList
				.replace("{Page}", "1").replace("{Size}", "10"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<String> interfaceReturn = InterfaceReturn
						.fromJsonModel(result);
				int PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn.isStatus()) {
					// if (PageCount == 0) {
					// startActivity(new Intent(MainActivity.this,
					// ReleaseProjectActivity.class));
					// } else
					if (PageCount > 0) {
						Tools.setNavigationBarColor(MainActivity.this,
								R.color.title_background_black);
						setTabSelection(2);
					}
				} else {
					if (PageCount == 0) {
						startActivityForResult(new Intent(MainActivity.this,
								ReleaseProjectActivity.class).putExtra("FROM",
								"MAIN_ACTIVITY"), MAIN_ISSUE_PROJECT);
						// getCertification();
					}
				}

			}

			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
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

	/**
	 * 当前登录用户获取找天工 数据信息列表 判断PageCount
	 */
	private void MyListByPage() {
		RequestParams params = new RequestParams(HttpUtil.MyListByPage.replace(
				"{Page}", "1").replace("{Size}", "10"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<String> interfaceReturn = InterfaceReturn
						.fromJsonModel(result);
				int PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn.isStatus()) {
					if (PageCount > 0) {
						Tools.setNavigationBarColor(MainActivity.this,
								R.color.title_background_black);
						setTabSelection(1);
					}
				} else {
					if (PageCount == 0) {
						Intent intent = new Intent(MainActivity.this,
								WantDayWorkerActivity.class);
						// 添加传送数据
						intent.putExtra("id", "0.101");
						intent.putExtra("FROM", "MAIN_ACTIVITY");
						startActivityForResult(intent, MAIN_ISSUE_WORK);
						// getCertification();
					}
				}

			}

			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
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

	/**
	 * 当前登录用户获取自己的用户信息
	 */
	private void getUserInfo() {
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<userInfo> interfaceReturn = new InterfaceReturn<userInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						userInfo.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						SharedPreferences sharedPreferences = getSharedPreferences(
								"UserToken", Activity.MODE_PRIVATE);
						Editor edit = sharedPreferences.edit();
						edit.putInt("MemberId", interfaceReturn.getResults()
								.get(0).getMember().getId());
						edit.putString("Phone", interfaceReturn.getResults()
								.get(0).getMember().getContactTel());
						edit.putString("UserPhoto", interfaceReturn
								.getResults().get(0).getMember()
								.getMemberPhoto());
						edit.putInt("IsCheck", interfaceReturn.getResults()
								.get(0).getMember().getIsCheck());
						edit.putBoolean("IsRealName", interfaceReturn
								.getResults().get(0).getMember().isRealName());
						edit.putString("NickName", interfaceReturn.getResults()
								.get(0).getMember().getNickName());
						edit.commit();
						IsRealName = interfaceReturn.getResults().get(0)
								.getMember().isRealName();
						IsCheck = interfaceReturn.getResults().get(0)
								.getMember().getIsCheck();
					}
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

	/**
	 * 获取当前登录用户的工程施工队信息
	 * 
	 * @author Johnson
	 */
	private void MyConstructionTeam() {
		RequestParams params = new RequestParams(HttpUtil.MyConstructionTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionTeamModels> interfaceReturn = new InterfaceReturn<ConstructionTeamModels>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeamModels.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						constructionTeam = interfaceReturn.getResults().get(0)
								.getConstructionTeam();
						Tools.setNavigationBarColor(MainActivity.this,
								R.color.title_background_black);
						setTabSelection(1);
					} else {
						startActivityForResult(new Intent(MainActivity.this,
								CreateTeamActivity.class), CREATE_TEAM);
					}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case CREATE_TEAM:
			constructionTeam = (ConstructionTeam) data
					.getSerializableExtra("ConstructionTeam");
			Tools.setNavigationBarColor(MainActivity.this,
					R.color.title_background_black);
			setTabSelection(1);
			break;
		case MAIN_ISSUE_WORK:
			Tools.setNavigationBarColor(MainActivity.this,
					R.color.title_background_black);
			setTabSelection(1);
			break;
		case MAIN_ISSUE_PROJECT:
			Tools.setNavigationBarColor(MainActivity.this,
					R.color.title_background_black);
			setTabSelection(2);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		if (firstS) {
			getUserInfo();
		}
		super.onResume();
	}

	/**
	 * 提示dialog
	 * 
	 * @param title
	 * @param message
	 * @param status
	 * @author Johnson
	 */
	public void hintDialog(String title, String message, final int status) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.Yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (status == 1) {
							startActivity(new Intent(MainActivity.this,
									MemberAttestationActivity.class));
						}
						dialog.dismiss();
					}
				});
		if (status == 1) {
			builder.setNegativeButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});
		}
		builder.create().show();
	}

	public void setTabSelection(int index) {
		switch (state) {
		case 0:
			ChangeBoss(index);

			break;
		case 1:
			ChangeCaptain(index);

			break;
		case 2:
			ChangeWorker(index);

			break;
		}
	}

	public void ChangeCaptain(int index) {

		resetBtn();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hidefragments(transaction);
		switch (index) {
		case 0:
			// btFirst.setBackgroundResource(R.drawable.swith_button1_1);
			btFirst.setIconFontIcoText(R.string.swith_button1);
			btFirst.setIconFontIcoTextColor("#158aff");
			if (firstFragment == null) {
				firstFragment = new CaptainFirstFragment();
				transaction.add(R.id.main_frament, firstFragment);
			} else {
				transaction.show(firstFragment);
			}
			break;
		case 1:
			delete();
			btSecond.setIconFontIcoText(R.string.swith_button6);
			btSecond.setIconFontIcoTextColor("#158aff");
			if (secondFragment == null) {
				secondFragment = new CaptainSecondFragment();
				transaction.add(R.id.main_frament, secondFragment);
			} else {
				transaction.show(secondFragment);
			}

			break;
		case 3:
			delete();
			btFourth.setIconFontIcoText(R.string.swith_button4);
			btFourth.setIconFontIcoTextColor("#158aff");

			// btFourth.setBackgroundResource(R.drawable.swith_button3_1);
			if (thirdFragment == null) {
				thirdFragment = new CaptainThirdFragment();
				transaction.add(R.id.main_frament, thirdFragment);
			} else {
				transaction.show(thirdFragment);
			}

			break;
		case 4:
			delete();
			btFifth.setIconFontIcoText(R.string.swith_button5);
			btFifth.setIconFontIcoTextColor("#158aff");

			// btFifth.setBackgroundResource(R.drawable.swith_button4_1);
			if (fourthFragment == null) {
				fourthFragment = new CaptainFourthFragment();
				transaction.add(R.id.main_frament, fourthFragment);
			} else {
				transaction.show(fourthFragment);
			}
			break;

		}
		transaction.commit();

	}

	private void ChangeWorker(int index) {
		WorkerResetBtn();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Workerhidefragments(transaction);

		switch (index) {
		case 0:
			btFirst.setIconFontIcoText(R.string.swith_button1);
			btFirst.setIconFontIcoTextColor("#158aff");
			if (workerFirstFragment == null) {
				workerFirstFragment = new WorkerFirstFragment();
				transaction.add(R.id.main_frament, workerFirstFragment);
			} else {
				transaction.show(workerFirstFragment);
			}
			break;
		case 3:
			delete();
			btFourth.setIconFontIcoText(R.string.swith_button4);
			btFourth.setIconFontIcoTextColor("#158aff");

			if (thirdFragment == null) {
				thirdFragment = new CaptainThirdFragment();
				transaction.add(R.id.main_frament, thirdFragment);
			} else {
				transaction.show(thirdFragment);
			}

			break;
		case 4:
			delete();
			btFifth.setIconFontIcoText(R.string.swith_button5);
			btFifth.setIconFontIcoTextColor("#158aff");

			if (workerThirdFragment == null) {
				workerThirdFragment = new WorkerThirdFragment();
				transaction.add(R.id.main_frament, workerThirdFragment);
			} else {
				transaction.show(workerThirdFragment);
			}

			break;
		}
		transaction.commit();
	}

	public void ChangeBoss(int index) {

		BossResetBtn();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Bosshidefragments(transaction);
		switch (index) {
		case 0:
			btFirst.setIconFontIcoText(R.string.swith_button1);
			btFirst.setIconFontIcoTextColor("#158aff");
			// btFirst.setBackgroundResource(R.drawable.swith_button1_1);
			if (bossFirstFragment == null) {
				bossFirstFragment = new BossFirstFragment();
				transaction.add(R.id.main_frament, bossFirstFragment);
			} else {
				transaction.show(bossFirstFragment);
			}
			break;
		case 1:
			delete();

			btSecond.setIconFontIcoText(R.string.swith_button2);
			btSecond.setIconFontIcoTextColor("#158aff");

			if (bossSecondFragment == null) {
				bossSecondFragment = new BossSecondFragment();
				transaction.add(R.id.main_frament, bossSecondFragment);
			} else {
				transaction.show(bossSecondFragment);
			}

			break;
		case 2:
			delete();

			btThird.setIconFontIcoText(R.string.swith_button3);
			btThird.setIconFontIcoTextColor("#158aff");
			if (bossThirdFragment == null) {
				bossThirdFragment = new BossThirdFragment();
				transaction.add(R.id.main_frament, bossThirdFragment);
			} else {
				transaction.show(bossThirdFragment);
			}

			break;
		case 3:
			delete();
			btFourth.setIconFontIcoText(R.string.swith_button4);
			btFourth.setIconFontIcoTextColor("#158aff");

			if (bossFourthFragment == null) {
				bossFourthFragment = new CaptainThirdFragment();
				transaction.add(R.id.main_frament, bossFourthFragment);
			} else {
				transaction.show(bossFourthFragment);
			}

			break;
		case 4:
			delete();
			btFifth.setIconFontIcoText(R.string.swith_button5);
			btFifth.setIconFontIcoTextColor("#158aff");

			if (bossFifthFragment == null) {
				bossFifthFragment = new BossFifthFragment();
				transaction.add(R.id.main_frament, bossFifthFragment);
			} else {
				transaction.show(bossFifthFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 判断是否实名认证
	 */
	private void getCertification() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<userInfo> interfaceReturn = new InterfaceReturn<userInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						userInfo.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						IsRealName = interfaceReturn.getResults().get(0)
								.getMember().isRealName();
						if (!IsRealName) {
							HintDialog.Builder builder = new HintDialog.Builder(
									MainActivity.this);
							builder.setMessage("您还没有实名认证,是否去认证 ?");
							builder.setTitle(R.string.Hint);
							builder.setNegativeButton(
									R.string.Yes,
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													MainActivity.this,
													MemberAttestationActivity.class));
											dialog.dismiss();

										}
									});
							builder.setPositiveButton(R.string.No,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							builder.create().show();
						}

					}
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

	private void resetBtn() {
		btFirst.setIconFontIcoText(R.string.swith_button1);
		btSecond.setIconFontIcoText(R.string.swith_button6);
		btFourth.setIconFontIcoText(R.string.swith_button4);
		btFifth.setIconFontIcoText(R.string.swith_button5);
	
		btFirst.setIconFontIcoTextColor("#000000");
		btSecond.setIconFontIcoTextColor("#000000");

		btFourth.setIconFontIcoTextColor("#000000");
		btFifth.setIconFontIcoTextColor("#000000");
	}

	private void BossResetBtn() {
		btFirst.setIconFontIcoText(R.string.swith_button1);
		btSecond.setIconFontIcoText(R.string.swith_button2);
		btThird.setIconFontIcoText(R.string.swith_button3);
		btFourth.setIconFontIcoText(R.string.swith_button4);
		btFifth.setIconFontIcoText(R.string.swith_button5);

		btFirst.setIconFontIcoTextColor("#000000");
		btSecond.setIconFontIcoTextColor("#000000");
		btThird.setIconFontIcoTextColor("#000000");
		btFourth.setIconFontIcoTextColor("#000000");
		btFifth.setIconFontIcoTextColor("#000000");

	}

	private void WorkerResetBtn() {
		btFirst.setIconFontIcoText(R.string.swith_button1);
		btFourth.setIconFontIcoText(R.string.swith_button4);
		btFifth.setIconFontIcoText(R.string.swith_button5);

		btFirst.setIconFontIcoTextColor("#000000");
		btFourth.setIconFontIcoTextColor("#000000");
		btFifth.setIconFontIcoTextColor("#000000");
	}

	private void hidefragments(FragmentTransaction transaction) {
		if (firstFragment != null) {
			transaction.hide(firstFragment);
		}
		if (secondFragment != null) {
			transaction.hide(secondFragment);
		}
		if (thirdFragment != null) {
			transaction.hide(thirdFragment);
		}
		if (fourthFragment != null) {
			transaction.hide(fourthFragment);
		}

	}

	private void Bosshidefragments(FragmentTransaction transaction) {
		if (bossFirstFragment != null) {
			transaction.hide(bossFirstFragment);
		}
		if (bossSecondFragment != null) {
			transaction.hide(bossSecondFragment);
		}
		if (bossThirdFragment != null) {
			transaction.hide(bossThirdFragment);
		}
		if (bossFourthFragment != null) {
			transaction.hide(bossFourthFragment);
		}
		if (bossFifthFragment != null) {
			transaction.hide(bossFifthFragment);
		}

	}

	private void Workerhidefragments(FragmentTransaction transaction) {
		if (workerFirstFragment != null) {
			transaction.hide(workerFirstFragment);

		}
		if (thirdFragment != null) {
			transaction.hide(thirdFragment);

		}
		if (workerThirdFragment != null) {
			transaction.hide(workerThirdFragment);

		}

	}

	public static void setOnCityListener(OnCityListener listener) {
		onCityListener = listener;
	}

	public interface OnCityListener {

		public void OnCityAddress(String city);
	}

	// 清除本地
	private void delete() {
		SharedPreferences sharedPreferences = getSharedPreferences("qwertyu",
				MODE_PRIVATE);
		if (!sharedPreferences.getString("qwertyu", "").equals("")) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.remove("qwertyu");
			editor.commit();
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
			JPushInterface.stopPush(MainActivity.this);
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;
	public static boolean isForeground;

	@SuppressWarnings("static-access")
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, R.string.Again_Pressed_Exit_Program,
					Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			AppManager.getAppManager().AppExit(MainActivity.this);
		}
	}

}
