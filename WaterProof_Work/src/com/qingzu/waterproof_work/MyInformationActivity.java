package com.qingzu.waterproof_work;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.model.UserInfo.Field;
import cn.jpush.im.api.BasicCallback;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.loc.cd;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.Member;
import com.qingzu.entity.Region;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.WheelViewActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity.getQiNiuToken;
import com.qingzu.wheel.widget.OnWheelChangedListener;
import com.qingzu.wheel.widget.WheelView;
import com.qingzu.wheel.widget.adapters.ArrayWheelAdapter;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInformationActivity extends WheelViewActivity implements
		OnClickListener, OnWheelChangedListener {

	private MyTitleView mtvMyInformationTitle = null;// 我的信息标题
	private RoundImageView MyInformationUserPhoto = null;// 头像
	private TextView tvMyInformationNickname = null;// 昵称
	private TextView tvMyInformationContactName = null;// 填写联系人
	private TextView tvMyInformationPhonenum = null;// 填写联系电话
	private TextView tvMyInformationBirthday = null;// 选择您的生日时间
	private TextView tvMyInformationLocation = null;// 选择所在地区
	private TextView tvMyInformationContactAddress = null;// 填写详细地址
	private RelativeLayout rlMyInformationPicture = null;// 头像布局
	private RelativeLayout rlMyInformationNickname = null;// 昵称布局
	private RelativeLayout rlMyInformationContactName = null;// 联系人布局
	private RelativeLayout rlMyInformationPhonenum = null;// 联系电话布局
	private RelativeLayout rlMyInformationBirthday = null;// 生日布局
	private RelativeLayout rlMyInformationLocation = null;// 所在地区布局
	private RelativeLayout rlMyInformationContactAddress = null;// 详细地址布局

	private static final int REQUEST_CAMERA = 100;

	protected static final int REQUEST_CAPTURE = 0; // 拍照requestCode
	protected static final int REQUEST_SELECT = 1; // 选照片requestCode
	protected static final int REQUEST_CROP = 3;// 压缩requestCode
	protected Uri mImageCaptureUri;// 图片uri路径
	private String QiniuToken = null;
	private String UserToken = null;
	private File file = null;
	private ShowProgressDialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private WheelMain wheelMain = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private ImageView imageView = null;
	private boolean status = true;// 选择城市布尔变量
	private String UserName = null;
	private File mTmpFile;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		setContentView(R.layout.activity_my_information);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = MyInformationActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		UserName = preferences.getString("UserName", "");
		dialog = getProDialog();
		mtvMyInformationTitle = (MyTitleView) findViewById(R.id.my_information_title);
		MyInformationUserPhoto = (RoundImageView) findViewById(R.id.my_information_user_photo);
		tvMyInformationNickname = (TextView) findViewById(R.id.my_information_nickname_tv);
		tvMyInformationContactName = (TextView) findViewById(R.id.my_information_contact_name_tv);
		tvMyInformationPhonenum = (TextView) findViewById(R.id.my_information_phonenum_tv);
		tvMyInformationBirthday = (TextView) findViewById(R.id.my_information_birthday_tv);
		tvMyInformationLocation = (TextView) findViewById(R.id.my_information_location_tv);
		tvMyInformationContactAddress = (TextView) findViewById(R.id.my_information_contact_address_tv);
		rlMyInformationPicture = (RelativeLayout) findViewById(R.id.my_information_picture_rl);
		rlMyInformationNickname = (RelativeLayout) findViewById(R.id.my_information_nickname_rl);
		rlMyInformationContactName = (RelativeLayout) findViewById(R.id.my_information_contact_name_rl);
		rlMyInformationPhonenum = (RelativeLayout) findViewById(R.id.my_information_phonenum_rl);
		rlMyInformationBirthday = (RelativeLayout) findViewById(R.id.my_information_birthday_rl);
		rlMyInformationLocation = (RelativeLayout) findViewById(R.id.my_information_location_rl);
		rlMyInformationContactAddress = (RelativeLayout) findViewById(R.id.my_information_contact_address_rl);
		imageView = (ImageView) findViewById(R.id.im);

		rlMyInformationPicture.setOnClickListener(this);
		rlMyInformationNickname.setOnClickListener(this);
		rlMyInformationContactName.setOnClickListener(this);
		rlMyInformationPhonenum.setOnClickListener(this);
		rlMyInformationBirthday.setOnClickListener(this);
		rlMyInformationLocation.setOnClickListener(this);
		rlMyInformationContactAddress.setOnClickListener(this);

		getUserInfo();
		getQiniuToken();

		mtvMyInformationTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	class getQiNiuToken {

		private String token;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}

	/**
	 * 获取七牛Token
	 * 
	 * @author Johnson
	 * @date 2016-1-5
	 * @param mobile
	 * @param cateID
	 */
	private void getQiniuToken() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.QiniuToken);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {
				InterfaceReturn<getQiNiuToken> interfaceReturn = new InterfaceReturn<getQiNiuToken>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						getQiNiuToken.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						QiniuToken = interfaceReturn.getResults().get(0)
								.getToken();
					}
				}
			};

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

	class userInfo implements Serializable {
		private Member member;

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}
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
						ImageLoaderUtil.loadXUtilImage(interfaceReturn
								.getResults().get(0).getMember()
								.getMemberPhoto(), imageView);

						tvMyInformationNickname.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss");
						SimpleDateFormat sdf2 = new SimpleDateFormat(
								getString(R.string.Date_Layout));

						try {
							Date date = sdf.parse(interfaceReturn.getResults()
									.get(0).getMember().getBirthday());
							String str = sdf2.format(date);
							tvMyInformationBirthday.setText(str);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						tvMyInformationContactName.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getContactName());
						tvMyInformationPhonenum.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getContactTel());
						//
						// tvMyInformationLocation.setText(interfaceReturn
						// .getResults().get(0).getMember().getRegionId()
						// +"");

						tvMyInformationContactAddress.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getContactAddress());

						RegionListByLastID(interfaceReturn.getResults().get(0)
								.getMember().getRegionId()
								+ "");

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

					}
				} else {
					T.showToast(MyInformationActivity.this,
							interfaceReturn.getMessage());
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_information_picture_rl:
			new PopupWindows(MyInformationActivity.this, v);

			break;
		case R.id.my_information_nickname_rl:
			MyTextDialog("请输入昵称", tvMyInformationNickname,
					tvMyInformationNickname.getText().toString().trim());

			break;
		case R.id.my_information_contact_name_rl:
			MyTextDialog("请输入联系人姓名", tvMyInformationContactName,
					tvMyInformationContactName.getText().toString().trim());
			break;
		case R.id.my_information_phonenum_rl:
			MyTextDialog("请输入您的手机号", tvMyInformationPhonenum,
					tvMyInformationPhonenum.getText().toString().trim());
			break;
		case R.id.my_information_birthday_rl:

			LayoutInflater inflater = LayoutInflater
					.from(MyInformationActivity.this);
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(MyInformationActivity.this);
			wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();

			Calendar calendar = Calendar.getInstance();

			try {
				calendar.setTime(dateFormat.parse(new Date().toLocaleString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int year;
			int month;
			int day;
			if (tvMyInformationBirthday.getText().toString().trim() != null
					&& !tvMyInformationBirthday.getText().toString().trim()
							.equals("")) {
				String Birthday = tvMyInformationBirthday.getText().toString()
						.trim();
				year = Integer.parseInt(Birthday.substring(0,
						Birthday.indexOf(getString(R.string.Year))));
				month = Integer.parseInt(Birthday.substring(
						Birthday.indexOf(getString(R.string.Year)) + 1,
						Birthday.indexOf(getString(R.string.Month))));
				day = Integer.parseInt(Birthday.substring(
						Birthday.indexOf(getString(R.string.Month)) + 1,
						Birthday.indexOf(getString(R.string.Day))));
			} else {
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
			}
			wheelMain.initDateTimePicker(year, month, day);
			new HintDialog.Builder(MyInformationActivity.this)
					.setTitle(R.string.Choose_Birthday)
					.setContentView(timepickerview)
					.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tvMyInformationBirthday.setText(wheelMain
											.getTime());
									UpdateBirthday(wheelMain.getTime());
									dialog.dismiss();
								}

							})
					.setNegativeButton(R.string.No,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();

			break;
		case R.id.my_information_location_rl:
			if (status == true) {
				MyTextDialog("请选择城市", tvMyInformationLocation,
						tvMyInformationLocation.getText().toString().trim());
				status = false;

			}

			break;
		case R.id.my_information_contact_address_rl:
			MyTextDialog("请输入详细地址", tvMyInformationContactAddress,
					tvMyInformationContactAddress.getText().toString().trim());
			break;

		default:
			break;
		}

	}

	/**
	 * edittext dialog
	 * 
	 * @author Johnson
	 * @date 2016-1-5
	 * @param Hint
	 * @param textView
	 * @param Text
	 */
	private void MyTextDialog(final String Hint, final TextView textView,
			String Text) {

		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		final EditText add = (EditText) layout.findViewById(R.id.et_nickname);
		LinearLayout ll = (LinearLayout) layout.findViewById(R.id.det_ll_sex);
		final CheckBox cbMan = (CheckBox) layout.findViewById(R.id.det_cb_man);
		LinearLayout llWheelView = (LinearLayout) layout
				.findViewById(R.id.id_wheelview_ll);
		final CheckBox cbWoman = (CheckBox) layout
				.findViewById(R.id.det_cb_woman);
		cbMan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbWoman.setChecked(false);
					cbMan.setChecked(true);
				} else {
					cbWoman.setChecked(true);
					cbMan.setChecked(false);
				}
			}
		});
		cbWoman.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbWoman.setChecked(true);
					cbMan.setChecked(false);

				} else {
					cbWoman.setChecked(false);
					cbMan.setChecked(true);
				}
			}
		});
		if (Hint.equals(getString(R.string.Please_Choose_City))) {
			llWheelView.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
			mViewCity = (WheelView) layout.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) layout.findViewById(R.id.id_district);
			setUpListener();
			setUpData();
		} else if (Hint.equals(getString(R.string.Choose_Sex))) {
			ll.setVisibility(View.VISIBLE);
			if (Text.equals(getString(R.string.Man))) {
				cbMan.setChecked(true);
			} else if (Text.equals(getString(R.string.Women))) {
				cbWoman.setChecked(true);
			}
			add.setVisibility(View.GONE);
		} else {
			add.setText(Text);
			add.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
			if (Hint.equals(getString(R.string.InputNickName))) {
				add.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						10) });
			} else if (Hint.equals(getString(R.string.Input_Phone))) {
				add.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						11) });
				add.setInputType(InputType.TYPE_CLASS_PHONE);
			} else if (Hint.equals(getString(R.string.Please_Input_Real_Name))) {
				add.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						10) });
			} else if (Hint.equals(getString(R.string.Input_Detail_Site))) {
				add.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						100) });
			} else if (Hint.equals(getString(R.string.Input_Contacts_Name))) {
				add.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						10) });
			}
		}
		new HintDialog.Builder(layout.getContext())
				.setTitle(Hint)
				.setContentView(layout)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (Hint.equals(getString(R.string.Please_Choose_City))) {
									showSelectedResult(textView);
									dialog.dismiss();
									status = true;

								} else if (Hint
										.equals(getString(R.string.Choose_Sex))) {
									if (cbMan.isChecked()) {
										textView.setText(cbMan.getText()
												.toString().trim());
										// UpdateContactSex("0");
										dialog.dismiss();
									} else if (cbWoman.isChecked()) {
										textView.setText(cbWoman.getText()
												.toString().trim());
										// UpdateContactSex("1");
										dialog.dismiss();
									}
								} else {
									textView.setText(add.getText().toString()
											.trim());
									if (Hint.equals(getString(R.string.InputNickName))) {
										if (Tools.isConnectingToInternet()) {
											UpdateNickName(add.getText()
													.toString().trim());
											dialog.dismiss();
										} else {
											// 无网无数据,提示信息
											T.showToast(
													MyInformationActivity.this,
													getString(R.string.LinkNetwork));
										}

									} else if (Hint
											.equals(getString(R.string.Please_Input_Real_Name))) {
										if (!add.getText().toString().trim()
												.equals("")) {
											// UpdateRealName(add.getText()
											// .toString().trim());
											dialog.dismiss();
										} else {
											T.showToast(
													MyInformationActivity.this,
													getString(R.string.Real_Name_Not_Null));
										}
									} else if (Hint
											.equals(getString(R.string.Input_Phone))) {
										if (!add.getText().toString().trim()
												.equals("")) {
											if (Tools.isMobileNO(add.getText()
													.toString().trim())) {
												UpdateContactTel(add.getText()
														.toString().trim());
												dialog.dismiss();
											} else {
												T.showToast(
														MyInformationActivity.this,
														getString(R.string.Input_Number_Error));
											}
										} else {
											T.showToast(
													MyInformationActivity.this,
													getString(R.string.Phone_Not_Null));
										}
									} else if (Hint
											.equals(getString(R.string.Input_Detail_Site))) {
										if (!add.getText().toString().trim()
												.equals("")) {
											UpdateContactAddress(add.getText()
													.toString().trim());
											dialog.dismiss();
										} else {
											T.showToast(
													MyInformationActivity.this,
													getString(R.string.Detail_Site_Not_Null));
										}
									} else if (Hint
											.equals(getString(R.string.Input_Contacts_Name))) {
										if (!add.getText().toString().trim()
												.equals("")) {
											UpdateContactName(add.getText()
													.toString().trim());
											dialog.dismiss();
										} else {
											T.showToast(
													MyInformationActivity.this,

													getString(R.string.Contacts_Name_Not_Null));
										}
									}

								}

								return;

							}

						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								status = true;
								dialog.dismiss();
							}
						}).create().show();

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				MyInformationActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();

	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener((OnWheelChangedListener) this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	/**
	 * 
	 * @ClassName: PopupWindows
	 * @Description: 弹出拍照选择PopupWindow
	 * @author lmw
	 * @date 2015年3月30日 上午9:30:41
	 */
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {
			super(mContext);
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showCameraAction();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPhoto();
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	/**
	 * 选择相机
	 */
	private void showCameraAction() {
		// 跳转到系统照相机
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(MyInformationActivity.this
				.getPackageManager()) != null) {
			// 设置系统相机拍照后的输出路径
			// 创建临时文件
			try {
				mTmpFile = FileUtils.createTmpFile(MyInformationActivity.this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (mTmpFile != null && mTmpFile.exists()) {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(mTmpFile));
				startActivityForResult(cameraIntent, REQUEST_CAMERA);
			} else {
				Toast.makeText(MyInformationActivity.this, "图片错误",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(MyInformationActivity.this, R.string.msg_no_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 照相
	 * 
	 * @param view
	 */
	protected void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_contact_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		try {
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CAPTURE);
		} catch (ActivityNotFoundException e) {
		}
	}

	/**
	 * 从相册选择图片
	 * 
	 * @param view
	 */
	protected void selectPhoto() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, REQUEST_SELECT);

	}

	/**
	 * 裁剪图片
	 * 
	 * @param imageUri
	 *            图片uri路径
	 */
	protected void cropImage(Uri imageUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(imageUri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_CROP);
	}

	@SuppressWarnings("unused")
	private void uploadImg(final File file) {
		new Thread(new Runnable() {
			String fileName = String.valueOf(System.currentTimeMillis());

			@Override
			public void run() {
				fileName = String.valueOf(System.currentTimeMillis());
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, fileName, QiniuToken,
						new UpCompletionHandler() {

							@Override
							public void complete(String arg0,
									ResponseInfo arg1, JSONObject arg2) {
								// TODO Auto-generated method stub
								if (arg1.isOK()) {
									UpdatePortrait("http://7xoiij.com2.z0.glb.qiniucdn.com/"
											+ arg0);
								}
							}

						}, null);

			}
		}).start();

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == MyInformationActivity.this.RESULT_OK) {
				switch (requestCode) {
				case REQUEST_CAPTURE:
					cropImage(mImageCaptureUri);
					break;

				case REQUEST_SELECT:

					mImageCaptureUri = data.getData();
					String mAlbumPicturePath = getPath(
							MyInformationActivity.this, data.getData());
					cropImage(Uri.fromFile(new File(mAlbumPicturePath)));
					break;
				case REQUEST_CROP:
					Bundle extras = data.getExtras();
					if (extras == null) {
						return;
					}
					Bitmap mLogPhoto = Tools.comp((Bitmap) extras.get("data"));

					file = FileUtils.saveBitmap(mLogPhoto, "memberPhoto");
					uploadImg(file);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	private void showSelectedResult(TextView textView) {
		// textView.setText(mCurrentProviceName + "/" + mCurrentCityName + "/"
		// + mCurrentDistrictName);
		UpdateRegionIDByNames(mCurrentProviceName, mCurrentCityName,
				mCurrentDistrictName);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();

	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);

	}

	/**
	 * 当前登录用户更新头像
	 * 
	 * @param MemberPhoto
	 */
	private void UpdatePortrait(final String MemberPhoto) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdatePortrait);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("MemberPhoto", MemberPhoto);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						if (JMessageClient.getMyInfo() == null) {
							JMessageClient.login(UserName, "112233",
									new BasicCallback() {

										@Override
										public void gotResult(int arg0,
												String arg1) {
											// TODO Auto-generated method stub
											if (arg0 == 0) {
												JMessageClient
														.updateUserAvatar(
																new File(
																		FileUtils.SDPATH
																				+ "memberPhoto.png"),
																new BasicCallback() {

																	@Override
																	public void gotResult(
																			int status,
																			String arg1) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		if (status == 0) {
																			Toast.makeText(
																					MyInformationActivity.this,
																					"头像更新====成功",
																					Toast.LENGTH_SHORT)
																					.show();

																		} else {
																			Log.i("LoginController",
																					"status = "
																							+ status);
																			HandleResponseCode
																					.onHandle(
																							MyInformationActivity.this,
																							status,
																							false);
																		}
																	}
																});
											}
										}
									});

						} else {
							JMessageClient.updateUserAvatar(new File(
									FileUtils.SDPATH + "memberPhoto.png"),
									new BasicCallback() {

										@Override
										public void gotResult(int status,
												String arg1) {
											// TODO Auto-generated method stub
											if (status == 0) {
												// Toast.makeText(MyInformationActivity.this,
												// "头像更新====成功",
												// Toast.LENGTH_SHORT).show();

											} else {
												Log.i("LoginController",
														"status = " + status);
												HandleResponseCode
														.onHandle(
																MyInformationActivity.this,
																status, false);
											}
										}
									});
						}

						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户更新用户昵称
	 * 
	 * @param NickName
	 */
	private void UpdateNickName(final String NickName) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdateNickName);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("NickName", NickName);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (JMessageClient.getMyInfo() == null) {
							JMessageClient.login(UserName, "112233",
									new BasicCallback() {

										@Override
										public void gotResult(int arg0,
												String arg1) {
											// TODO Auto-generated method stub
											if (arg0 == 0) {
												UserInfo info = JMessageClient
														.getMyInfo();
												info.setNickname(NickName);
												JMessageClient.updateMyInfo(
														Field.nickname, info,
														new BasicCallback() {

															@Override
															public void gotResult(
																	int status,
																	String arg1) {
																// TODO
																// Auto-generated
																// method stub
																if (status == 0) {
																	T.showToast(
																			MyInformationActivity.this,
																			"修改用户名===成功");
																} else {
																	Log.i("LoginController",
																			"status = "
																					+ status);
																	HandleResponseCode
																			.onHandle(
																					MyInformationActivity.this,
																					status,
																					false);
																}

															}
														});
											}
										}
									});

						} else {
							JMessageClient.updateUserAvatar(new File(
									FileUtils.SDPATH + "memberPhoto.png"),
									new BasicCallback() {

										@Override
										public void gotResult(int status,
												String arg1) {
											// TODO Auto-generated method stub
											if (status == 0) {

												UserInfo info = JMessageClient
														.getMyInfo();
												info.setNickname(NickName);
												JMessageClient.updateMyInfo(
														Field.nickname, info,
														new BasicCallback() {

															@Override
															public void gotResult(
																	int status,
																	String arg1) {
																// TODO
																// Auto-generated
																// method stub
																if (status == 0) {
																	T.showToast(
																			MyInformationActivity.this,
																			"修改用户名===成功");
																} else {
																	Log.i("LoginController",
																			"status = "
																					+ status);
																	HandleResponseCode
																			.onHandle(
																					MyInformationActivity.this,
																					status,
																					false);
																}

															}
														});

											} else {
												Log.i("LoginController",
														"status = " + status);
												HandleResponseCode
														.onHandle(
																MyInformationActivity.this,
																status, false);
											}
										}
									});
						}

						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户更新联系人电话
	 * 
	 * @param ContactTel
	 */
	private void UpdateContactTel(String ContactTel) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.UpdateContactTel);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("ContactTel", ContactTel);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户更新详细地址
	 * 
	 * @param ContactAddress
	 */
	private void UpdateContactAddress(String ContactAddress) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdateContactAddress);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("ContactAddress", ContactAddress);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户更新联系人
	 * 
	 * @param ContactName
	 */
	private void UpdateContactName(String ContactName) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdateContactName);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("ContactName", ContactName);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户修改生日
	 * 
	 * @param UpdateBirthday
	 */
	private void UpdateBirthday(String Birthday) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdateBirthday);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("Birthday", Birthday);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 当前登录用户更新所在地区（请提交[省-市-区县]名称）
	 * 
	 * @param ProvinceName
	 * @param CityName
	 * @param DistrictName
	 */
	private void UpdateRegionIDByNames(String ProvinceName, String CityName,
			String DistrictName) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UpdateRegionIDByNames);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("ProvinceName", ProvinceName);
		params.addBodyParameter("CityName", CityName);
		params.addBodyParameter("DistrictName", DistrictName);
		Xutils.Put(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						tvMyInformationLocation.setText(mCurrentProviceName
								+ "/" + mCurrentCityName + "/"
								+ mCurrentDistrictName);
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 根据RegionID获取上级区域(包括本级区域)
	 * 
	 * @param RegionID
	 */
	private void RegionListByLastID(String RegionID) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(
				HttpUtil.RegionListByLastID.replace("{RegionID}", RegionID));

		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {

				InterfaceReturn<Region> interfaceReturn = new InterfaceReturn<Region>();
				interfaceReturn = InterfaceReturn
						.fromJson(result, Region.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						tvMyInformationLocation.setText(interfaceReturn
								.getResults().get(0).getRegionName()
								+ "/"
								+ interfaceReturn.getResults().get(1)
										.getRegionName()
								+ "/"
								+ interfaceReturn.getResults().get(2)
										.getRegionName());

						// Toast.makeText(MyInformationActivity.this,
						// interfaceReturn.getMessage(),
						// Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MyInformationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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

	private ShowProgressDialog getProDialog() {
		return null;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		}
	}

}
