package com.qingzu.waterproof_work;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.multi_image_selector.view.BasePhotoPreviewActivity;
import me.multi_image_selector.view.HorizontalListView;
import multi_image_selector.MultiImageSelectorActivity;

import com.amap.api.services.core.LatLonPoint;
import com.example.multi_image_selector.ProducGridAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.AddressItem;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.RecruitDayWorker;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.WheelViewActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity.getQiNiuToken;
import com.qingzu.waterproof_work.ReleaseProjectActivity.userInfo;
import com.qingzu.wheel.widget.OnWheelChangedListener;
import com.qingzu.wheel.widget.WheelView;
import com.qingzu.wheel.widget.adapters.ArrayWheelAdapter;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 招天工
 * 
 * @author Johnson
 * 
 */
public class WantDayWorkerActivity extends WheelViewActivity implements
		OnClickListener, OnWheelChangedListener {
	public static boolean SELECTMAP = false;
	private boolean IsRealName = false;
	private MyTitleView mtvWantDayworkerTitle = null;// 我要天工标题
	private EditText etDayWorkerProjectName = null;// 项目名称
	private TextView tvDayWorkerConstructionPart = null;// 选择施工部位
	private LinearLayout llDayWorkerConstructionPart = null;// 选择施工部位布局
	private TextView tvDayWorkerUseMaterial = null;// 材料需求
	private LinearLayout llDayWorkerUseMaterial = null;// 材料需求布局
	private EditText etDayWorkerConstructionArea = null;// 施工面积
	private TextView tvDayWorkerStartTtime = null;// 选择开工时间
	private LinearLayout llDayWorkerStartTime = null;// 选择开工时间布局
	private EditText etDayWorkerNeedWorkerNumber = null;// 用工人数
	private TextView tvDayWorkerConstructionPlace = null;// 选择施工地点
	private LinearLayout llDayWorkerConstructionPlace = null;// 选择施工地点布局
	private TextView tvDayWorkerDetailAddress = null;// 定位工程地点
	private LinearLayout llDayWorkerDetailAddress = null;// 定位工程地点布局
	private EditText etDayWorkerContactName = null;// 填写联系人
	private EditText etDayWorkerContactPhone = null;// 填写联系电话
	private TextView tvDayWorkerConstructionParPicture = null;// 上传施工部位图
	private LinearLayout llDayWorkerConstructionPartPicture = null;// 上传施工部位图布局
	private Button btDayWorkerReleaseInformation = null;// 发布信息
	private String ProvinceName, CityName, AreaName = null;
	private List<UploadPhoto> list = new ArrayList<WantDayWorkerActivity.UploadPhoto>();
	private WheelMain wheelMain = null;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	// private ShowProgressDialog dialog;
	private String ContactTel = null;
	private String NickName = null;
	private SharedPreferences preferences = null;
	private String UserToken = null;
	private RecruitDayWorker recruitDayWorker = null;
	private RecruitDayWorker dayWorker = null;
	private String QiniuToken = null;
	// 启动actviity的请求码
	private static final int REQUEST_IMAGE = 2;
	// 存放图片路径的list
	private ArrayList<String> mSelectPath = null;
	// 存放加号的list
	List<Bitmap> jiaListBitMap;
	// 存放所有图片的list(不包括加号)
	List<Bitmap> listBitMap;
	ProducGridAdapter adapter = null;
	private HorizontalListView noScrollgridview;
	private int width;
	private String remark;
	private String id;
	private ShowProgressDialog dialog = null;
	private String from = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_want_day_worker);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
		myOnclick();
		bindGridView();

	}

	private void initView() {
		// TODO Auto-generated method stub

		// 放+的list
		jiaListBitMap = new ArrayList<Bitmap>();
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		preferences = getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		ContactTel = preferences.getString("Phone", "");
		NickName = preferences.getString("NickName", "");

		mtvWantDayworkerTitle = (MyTitleView) findViewById(R.id.want_dayworker_title);
		etDayWorkerProjectName = (EditText) findViewById(R.id.day_worker_project_name_et);
		tvDayWorkerConstructionPart = (TextView) findViewById(R.id.day_worker_construction_part_tv);
		llDayWorkerConstructionPart = (LinearLayout) findViewById(R.id.day_worker_construction_part_ll);
		tvDayWorkerUseMaterial = (TextView) findViewById(R.id.day_worker_use_material_tv);
		llDayWorkerUseMaterial = (LinearLayout) findViewById(R.id.day_worker_use_material_ll);
		etDayWorkerConstructionArea = (EditText) findViewById(R.id.day_worker_construction_area_et);
		tvDayWorkerStartTtime = (TextView) findViewById(R.id.day_worker_start_time_tv);
		llDayWorkerStartTime = (LinearLayout) findViewById(R.id.day_worker_start_time_ll);
		etDayWorkerNeedWorkerNumber = (EditText) findViewById(R.id.day_worker_need_worker_number_et);
		tvDayWorkerConstructionPlace = (TextView) findViewById(R.id.day_worker_construction_place_tv);
		llDayWorkerConstructionPlace = (LinearLayout) findViewById(R.id.day_worker_construction_place_ll);
		tvDayWorkerDetailAddress = (TextView) findViewById(R.id.day_worker_detail_address_tv);
		llDayWorkerDetailAddress = (LinearLayout) findViewById(R.id.day_worker_detail_address_ll);
		etDayWorkerContactName = (EditText) findViewById(R.id.day_worker_contact_name_et);
		etDayWorkerContactPhone = (EditText) findViewById(R.id.day_worker_contact_phone_et);

		etDayWorkerContactName.setText(NickName);
		etDayWorkerContactPhone.setText(ContactTel);
		
		getCertification();

		dialog = getProDialog();
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		from = intent.getStringExtra("FROM");

		if (id != null && !id.equals("0.101") && !id.equals("0")) {

			getMessage(id);

		}

		llDayWorkerConstructionPartPicture = (LinearLayout) findViewById(R.id.day_worker_construction_part_picture_ll);
		btDayWorkerReleaseInformation = (Button) findViewById(R.id.day_worker_release_information_bt);
		// 列表
		noScrollgridview = (HorizontalListView) findViewById(R.id.noScrollgridview);

		btDayWorkerReleaseInformation.setOnClickListener(this);

		llDayWorkerConstructionPart.setOnClickListener(this);
		llDayWorkerUseMaterial.setOnClickListener(this);
		llDayWorkerStartTime.setOnClickListener(this);
		llDayWorkerConstructionPlace.setOnClickListener(this);
		llDayWorkerDetailAddress.setOnClickListener(this);
		llDayWorkerConstructionPartPicture.setOnClickListener(this);
		getQiniuToken();
		tvDayWorkerDetailAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str1 = tvDayWorkerConstructionPlace.getText().toString()
						.trim();
				if (tvDayWorkerConstructionPlace.getText().toString().trim()
						.length() > 0) {
					String[] str = tvDayWorkerConstructionPlace.getText()
							.toString().trim().split("/");
					String mCurrentCityName = str[1];
					if (mCurrentCityName != null
							&& !mCurrentCityName.equals("")) {
						Intent intent = new Intent(WantDayWorkerActivity.this,
								SelectedPositionActivity.class);
						Bundle bundle = new Bundle();
						AddressItem addressItem = new AddressItem();
						addressItem.setCityName(mCurrentCityName);
						if (!tvDayWorkerDetailAddress.getText().toString()
								.equals("")) {
							addressItem.setAddress(tvDayWorkerDetailAddress
									.getText().toString().trim());
							addressItem.setCityName(mCurrentCityName);
							bundle.putSerializable(
									SelectedPositionActivity.FIND_PERSON_NO,
									addressItem);

							intent.putExtra("type",
									SelectedPositionActivity.FIND_PERSON_NO);
							intent.putExtras(bundle);

						} else {
							addressItem.setAddress(tvDayWorkerDetailAddress
									.getText().toString().trim());
							addressItem.setCityName(mCurrentCityName);
							bundle.putSerializable(
									SelectedPositionActivity.FIND_PERSON,
									addressItem);

							intent.putExtra("type",
									SelectedPositionActivity.FIND_PERSON);
							intent.putExtras(bundle);
							// startActivity(intent);

						}
						startActivityForResult(intent,
								StatusTool.BOOS_SELECT_PLACE);
					}
				} else {
					T.showToast(WantDayWorkerActivity.this, "请选择施工地点");
				}

			}
		});

		mtvWantDayworkerTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				WantDayWorkerActivity.this, R.style.progress_dialog,
				"正在上传信息,请稍候");

		return dialog;
	}

	/**
	 * 根据ID获取找天工信息
	 * 
	 * @param Id
	 */
	private void getMessage(String Id) {
		RequestParams params = new RequestParams(
				HttpUtil.RecruitmentInfoByID.replace("{ID}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<RecruitModel> interfaceReturn = new InterfaceReturn<RecruitModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitModel.class);

				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						etDayWorkerProjectName.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getInfoTitle());
						tvDayWorkerConstructionPart.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getConstructionParts());
						tvDayWorkerUseMaterial.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getClaimMaterial());
						etDayWorkerConstructionArea.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getConstructionArea());

						String starttime = dateToStrLong(strToDate(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getStartTime()));
						tvDayWorkerStartTtime.setText(starttime);
						etDayWorkerNeedWorkerNumber.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getPeopleNumber());
						tvDayWorkerConstructionPlace.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getProvinceName()
								+ "/"
								+ interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getCityName()
								+ "/"
								+ interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getAreaName());
						tvDayWorkerDetailAddress.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getAddress());
						etDayWorkerContactName.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getContactName());
						etDayWorkerContactPhone.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getContactTel());
						String str = interfaceReturn.getResults().get(0)
								.getRecruitmentInfo().getLat();
						System.out.println(str);
						MyApplication.latLonPoint = new LatLonPoint(Double
								.valueOf(interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getLat()), Double
								.valueOf(interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getLon()));

						System.out.println(MyApplication.latLonPoint);

					} else {
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
					}
				}
			}

			/**
			 * 时间格式转换
			 * 
			 * @param dateDate
			 * @return
			 */

			public String dateToStrLong(java.util.Date dateDate) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
				String dateString = formatter.format(dateDate);
				return dateString;
			}

			public java.util.Date strToDate(String strDate) {
				SimpleDateFormat formatter = new SimpleDateFormat(

				// "yyyy-MM-dd"
						"yyyy-MM-dd");
				ParsePosition pos = new ParsePosition(0);
				java.util.Date strtodate = formatter.parse(strDate, pos);
				return strtodate;
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

	public void WantWorkerinfo() {
		String[] region = tvDayWorkerConstructionPlace.getText().toString()
				.trim().split("/");
		recruitDayWorker = new RecruitDayWorker();
		recruitDayWorker.setInfoTitle(etDayWorkerProjectName.getText()
				.toString().trim());
		recruitDayWorker.setPeopleNumber(etDayWorkerNeedWorkerNumber.getText()
				.toString().trim());
		recruitDayWorker.setContactName(etDayWorkerContactName.getText()
				.toString().trim());
		recruitDayWorker.setContactTel(etDayWorkerContactPhone.getText()
				.toString().trim());
		recruitDayWorker.setStartTime(tvDayWorkerStartTtime.getText()
				.toString().trim());
		recruitDayWorker.setProvinceName(region[0]);
		recruitDayWorker.setCityName(region[1]);
		recruitDayWorker.setAreaName(region[2]);
		recruitDayWorker.setAddress(tvDayWorkerDetailAddress.getText()
				.toString().trim());
		recruitDayWorker.setConstructionParts(tvDayWorkerConstructionPart
				.getText().toString().trim());
		recruitDayWorker.setClaimMaterial(tvDayWorkerUseMaterial.getText()
				.toString().trim());
		recruitDayWorker.setConstructionArea(etDayWorkerConstructionArea
				.getText().toString().trim());

		recruitDayWorker.setLat(MyApplication.latLonPoint.getLatitude() + "");
		recruitDayWorker.setLon(MyApplication.latLonPoint.getLongitude() + "");
		recruitDayWorker.setAreaUnit("平米");
		if (id != null && !id.equals("0.101") && !id.equals("0")) {
			recruitDayWorker.setId(Integer.parseInt(id));
		}
		Gson gson = new Gson();
		String json = gson.toJson(recruitDayWorker);
		if (id != null && !id.equals("0.101") && !id.equals("0")) {
			UpdateRecruitmentInfo(json);// 当前登录用户编辑找天工信息
			dialog.dismiss();
		} else if (id.equals("0.101")) {
			CreateRecruitmentInfo(json);// 当前登录用户发布找天工信息
			dialog.dismiss();
		}

	}

	/**
	 * 当前登录用户编辑找天工信息
	 * 
	 * @param json
	 */

	private void UpdateRecruitmentInfo(String json) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.UpdateRecruitmentInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<RecruitDayWorker> interfaceReturn = new InterfaceReturn<RecruitDayWorker>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitDayWorker.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						dayWorker = new RecruitDayWorker();
						dayWorker = interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						if (mSelectPath != null) {
							uploadPicture();
							return;
						}
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
						btDayWorkerReleaseInformation.setEnabled(true);
						dialog.dismiss();
						//
						finish();
					} else {
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
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

	public void uploadPicture() {

		for (int i = 0; i < mSelectPath.size(); i++) {
			String PhotoName = UUID.randomUUID() + "";
			if (FileUtils.saveBitmap(
					Tools.comp(BitmapFactory.decodeFile(mSelectPath.get(i))),
					PhotoName) != null)
				;
			new upload().execute(FileUtils.SDPATH + PhotoName + ".png");
			// new upload().execute(mSelectPath.get(i));
		}

	}

	/**
	 * 绑定加号
	 */
	private void bindGridView() {
		// 把加号放到list中并绑定，到gridView中
		jiaListBitMap = bindJia();
		hengping();
		// 把数据绑定一下
		adapter = new ProducGridAdapter(jiaListBitMap, this);
		noScrollgridview.setAdapter(adapter);
	}

	// 绑定加号
	private List<Bitmap> bindJia() {
		// 把加号放到list中并绑定，到gridView中
		InputStream is = getResources().openRawResource(
				R.raw.icon_addpic_unfocused);

		Bitmap myBitmapJia = BitmapFactory.decodeStream(is);
		jiaListBitMap.add(myBitmapJia);
		return jiaListBitMap;
	}

	/**
	 * 图片压缩，如果不压缩会导致图片溢出异常
	 */
	private Bitmap createThumbnail(String filepath, int i) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = i;
		return BitmapFactory.decodeFile(filepath, options);
	}

	private void hengping() {
		ViewGroup.LayoutParams params = noScrollgridview.getLayoutParams();
		// dishtype，welist为ArrayList
		WindowManager wm = this.getWindowManager();
		int dishtypes = jiaListBitMap.size();
		// 图片之间的距离
		params.width = width / 20 * 6 * dishtypes;
		Log.d("看看这个宽度", params.width + "" + jiaListBitMap.size());
		noScrollgridview.setLayoutParams(params);
		// 设置列数为得到的list长度
		// noScrollgridview.setNumColumns(jiaListBitMap.size());
	}

	/**
	 * /** 点击事件
	 * 
	 * 
	 */
	private void myOnclick() {

		noScrollgridview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						adapter.setSelectIndex(position);

						if (position == jiaListBitMap.size() - 1) {
							setImgMode();
						} else {
							if (mSelectPath != null && mSelectPath.size() > 0) {
								Intent intent = new Intent(
										WantDayWorkerActivity.this,
										BasePhotoPreviewActivity.class);
								intent.putExtra(
										BasePhotoPreviewActivity.INTENT_STATE,
										0);
								intent.putExtra(
										MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
										mSelectPath);
								startActivityForResult(intent,
										StatusTool.BOOS_SELECT_PLACE);
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
	}

	// 相册，相机模式设置
	private void setImgMode() {
		Intent intent = new Intent(WantDayWorkerActivity.this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
		// 选择模式，1表示多选,0表示单选
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					BasePhotoPreviewActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, StatusTool.BOOS_SELECT_PLACE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater
				.from(WantDayWorkerActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(WantDayWorkerActivity.this);
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
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.day_worker_release_information_bt:
			release();
			break;
		case R.id.day_worker_construction_part_ll:
			llDayWorkerConstructionPart.setPressed(true);
			intent.setClass(WantDayWorkerActivity.this,
					ConstructionPartActivity.class);
			intent.putExtra("conspart", tvDayWorkerConstructionPart.getText()
					.toString().trim());
			startActivityForResult(intent, 7);
			break;
		case R.id.day_worker_use_material_ll:
			llDayWorkerUseMaterial.setPressed(true);
			intent.setClass(WantDayWorkerActivity.this,
					MaterialRequestActivity.class);
			intent.putExtra("material", tvDayWorkerUseMaterial.getText()
					.toString().trim());
			startActivityForResult(intent, 6);
			break;
		case R.id.day_worker_start_time_ll:
			if (tvDayWorkerStartTtime.getText().toString().trim() != null
					&& !tvDayWorkerStartTtime.getText().toString().trim()
							.equals("")) {
				String StartTime = tvDayWorkerStartTtime.getText().toString()
						.trim();
				year = Integer.parseInt(StartTime.substring(0,
						StartTime.indexOf("年")));
				month = Integer.parseInt(StartTime.substring(
						StartTime.indexOf("年") + 1, StartTime.indexOf("月")));
				day = Integer.parseInt(StartTime.substring(
						StartTime.indexOf("月") + 1, StartTime.indexOf("日")));
			} else {
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
			}
			wheelMain.initDateTimePicker(year, month, day);
			new HintDialog.Builder(WantDayWorkerActivity.this)
					.setTitle("开始时间")
					.setContentView(timepickerview)
					.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tvDayWorkerStartTtime.setText(wheelMain
											.getTime());
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
		case R.id.day_worker_construction_place_ll:
			llDayWorkerConstructionPlace.setPressed(true);
			MyTextDialog(getString(R.string.Select_City),
					tvDayWorkerConstructionPlace);

			break;
		case R.id.day_worker_detail_address_ll:

			break;
		case R.id.day_worker_construction_part_picture_ll:

			break;

		default:
			break;
		}
	}

	private void release() {

		if (etDayWorkerProjectName.getText().toString().trim().length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请输入项目名称");
			return;
		} else if (tvDayWorkerConstructionPart.getText().toString().trim()
				.length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请勾选施工部位");
			return;
		} else if (tvDayWorkerUseMaterial.getText().toString().trim().length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请勾选需要使用的材料");
			return;
		} else if (etDayWorkerConstructionArea.getText().toString().trim()
				.length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请填写施工面积");
			return;
		} else if (tvDayWorkerStartTtime.getText().toString().trim().length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请选择开工时间");
			return;
		} else if (etDayWorkerNeedWorkerNumber.getText().toString().trim()
				.length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请填写用工人数");
			return;
		} else if (tvDayWorkerConstructionPlace.getText().toString().trim()
				.length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请选择施工地点");
			return;
		} else if (tvDayWorkerDetailAddress.getText().toString().trim()
				.length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请定位工程地点");
			return;
		} else if (etDayWorkerContactName.getText().toString().trim().length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请填写联系人");
			return;
		} else if (etDayWorkerContactPhone.getText().toString().trim().length() <= 0) {
			T.showToast(WantDayWorkerActivity.this, "请填写联系电话");
			return;

		} else if (!Tools.isMobileNO(etDayWorkerContactPhone.getText()
				.toString().trim())) {
			Toast.makeText(WantDayWorkerActivity.this, "手机号码输入有误",
					Toast.LENGTH_SHORT).show();
			return;
		}

		else {
			dialog.show();
			btDayWorkerReleaseInformation.setEnabled(false);
			new Thread(new Runnable() {
				public void run() {

					WantWorkerinfo();
				};
			}).start();

			// uploadPicture();
		}

	}

	private void CreateRecruitmentPhotoListInfo(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.CreateRecruitmentPhotoListInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<UploadPhoto> interfaceReturn = new InterfaceReturn<UploadPhoto>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						UploadPhoto.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// T.showToast(WantDayWorkerActivity.this,
						// nterfaceReturn.getMessage());
						// recruitDayWorker =
						// interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						// WantWorkerinfo();
						btDayWorkerReleaseInformation.setEnabled(true);
						dialog.dismiss();
						finish();
					} else {
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
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

	private void UpdateRecruitmentPhotoListInfo(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.UpdateRecruitmentPhotoListInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<UploadPhoto> interfaceReturn = new InterfaceReturn<UploadPhoto>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						UploadPhoto.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// T.showToast(WantDayWorkerActivity.this,
						// nterfaceReturn.getMessage());
						// recruitDayWorker =
						// interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						// WantWorkerinfo();
						btDayWorkerReleaseInformation.setEnabled(true);
						dialog.dismiss();
						finish();
					} else {
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
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
	 * 当前登录用户发布找天工信息
	 * 
	 * @param json
	 * @author Johnson
	 */
	private void CreateRecruitmentInfo(String json) {
		// TODO Auto-generated method stub
		dialog.show();
		RequestParams params = new RequestParams(HttpUtil.CreateRecruitmentInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);

		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<RecruitDayWorker> interfaceReturn = new InterfaceReturn<RecruitDayWorker>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitDayWorker.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						dayWorker = new RecruitDayWorker();
						dayWorker = interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						if (mSelectPath != null) {
							uploadPicture();
							return;
						}
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
						btDayWorkerReleaseInformation.setEnabled(true);
						dialog.dismiss();
						if (null != from && from.equals("MAIN_ACTIVITY")) {
							setResult(MainActivity.MAIN_ISSUE_WORK);
						}
						finish();

					} else {
						T.showToast(WantDayWorkerActivity.this,
								interfaceReturn.getMessage());
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

	private void MyTextDialog(final String Hint, final TextView textView) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		LinearLayout llWheelView = (LinearLayout) layout
				.findViewById(R.id.id_wheelview_ll);
		final EditText add = (EditText) layout.findViewById(R.id.et_nickname);
		if (Hint.equals(getString(R.string.Select_City))) {
			add.setVisibility(View.GONE);
			llWheelView.setVisibility(View.VISIBLE);
			mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
			mViewCity = (WheelView) layout.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) layout.findViewById(R.id.id_district);
			setUpListener();
			setUpData();
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
								}
								dialog.dismiss();
								return;

							}

						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create().show();

	}

	private void showSelectedResult(TextView textView) {
		textView.setText(mCurrentProviceName + "/" + mCurrentCityName + "/"
				+ mCurrentDistrictName);
		if (!tvDayWorkerDetailAddress.getText().toString().equals("")) {
			tvDayWorkerDetailAddress.setText("");
		}

		System.out.println(mCurrentCityName
				+ "==============mCurrentCityName============================");
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener((OnWheelChangedListener) this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				WantDayWorkerActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == StatusTool.BOOS_SELECT_PLACE) {

		switch (resultCode) {
		case StatusTool.SELECT_PLACE:
			tvDayWorkerDetailAddress.setText(data.getExtras().getString(
					"address"));

			// if (data.getStringExtra("state").equals("ProjectImg")) {
			// tvPersonalPhoto.setText(Bimp.imgsHashMap.get("ProjectImg")
			// .size()
			// + getString(R.string.Images_has_Uploading_suceessful));
			// }
			break;

		case RESULT_OK:
			list.clear();
			mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			// 把数据转化成Bitmap,放选中的图片
			listBitMap = new ArrayList<Bitmap>();
			for (String path : mSelectPath) {
				Log.e("1", "本地路径" + path);
				// 压缩到1/20大小
				Bitmap bitmap = createThumbnail(path, 20);
				// 把bitmap放进去list中
				listBitMap.add(bitmap);
			}
			// 清空所有数据
			jiaListBitMap.clear();
			bindJia();
			// 把加号和图片放一起
			jiaListBitMap.addAll(0, listBitMap);
			// 当选择9张图片时，删除加号
			if (jiaListBitMap.size() == 10) {
				// 第10张图片下标是9
				jiaListBitMap.remove(9);
			}
			hengping();
			// 把数据绑定一下
			adapter.notifyDataSetChanged();
			break;

		case 6:
			tvDayWorkerUseMaterial.setText(data.getStringExtra("positionlist"));
			if (tvDayWorkerUseMaterial.getText().toString().trim() != null
					&& !tvDayWorkerUseMaterial.getText().toString().trim()
							.equals("")) {
				tvDayWorkerUseMaterial.setVisibility(View.VISIBLE);
			}
			break;
		case 7:

			tvDayWorkerConstructionPart.setText(data
					.getStringExtra("conspartlist"));
			if (tvDayWorkerConstructionPart.getText().toString().trim() != null
					&& !tvDayWorkerConstructionPart.getText().toString().trim()
							.equals("")) {
				tvDayWorkerConstructionPart.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	// }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		if (SELECTMAP) {
			tvDayWorkerDetailAddress.setText(preferences.getString("address",
					""));
			SELECTMAP = !SELECTMAP;

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.latLonPoint = null;
		SELECTMAP = false;
		MaterialRequestActivity.hashMap.clear();
		ConstructionPartActivity.hashMap.clear();
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

	class UploadPhoto {
		private Integer infoId;
		private String photoTitle;
		private String photoUrl;
		private Integer photoLength;
		private Integer photoWidth;

		public Integer getInfoId() {
			return infoId;
		}

		public void setInfoId(Integer infoId) {
			this.infoId = infoId;
		}

		public String getPhotoTitle() {
			return photoTitle;
		}

		public void setPhotoTitle(String photoTitle) {
			this.photoTitle = photoTitle;
		}

		public String getPhotoUrl() {
			return photoUrl;
		}

		public void setPhotoUrl(String photoUrl) {
			this.photoUrl = photoUrl;
		}

		public Integer getPhotoLength() {
			return photoLength;
		}

		public void setPhotoLength(Integer photoLength) {
			this.photoLength = photoLength;
		}

		public Integer getPhotoWidth() {
			return photoWidth;
		}

		public void setPhotoWidth(Integer photoWidth) {
			this.photoWidth = photoWidth;
		}

	}

	class upload extends AsyncTask<String, Integer, Result> {

		@Override
		protected Result doInBackground(final String... params) {
			// TODO Auto-generated method stub

			String PhotoName = UUID.randomUUID() + "";

			UploadManager uploadManager = new UploadManager();
			uploadManager.put(params[0], PhotoName, QiniuToken,
					new UpCompletionHandler() {

						@Override
						public void complete(String arg0, ResponseInfo arg1,
								JSONObject arg2) {
							// TODO Auto-generated method stub
							if (arg1.isOK()) {

								BitmapFactory.Options options = new BitmapFactory.Options();

								/**
								 * 最关键在此，把options.inJustDecodeBounds = true;
								 * 这里再decodeFile()，
								 * 返回的bitmap为空，但此时调用options.outHeight时
								 * ，已经包含了图片的高了
								 */
								options.inJustDecodeBounds = true;
								Bitmap bitmap = BitmapFactory.decodeFile(
										params[0], options); // 此时返回的bitmap为null

								UploadPhoto uploadPhoto = new UploadPhoto();
								uploadPhoto.setInfoId(dayWorker.getId());
								uploadPhoto
										.setPhotoUrl("http://7xoiij.com2.z0.glb.qiniucdn.com/"
												+ arg0);
								uploadPhoto.setPhotoTitle(arg0);
								uploadPhoto.setPhotoWidth(options.outWidth);
								uploadPhoto.setPhotoLength(options.outHeight);
								list.add(uploadPhoto);
								if (list.size() == mSelectPath.size()) {
									Gson gson = new Gson();

									if (id != null && !id.equals("0.101")
											&& !id.equals("0")) {
										UpdateRecruitmentPhotoListInfo(gson
												.toJson(list));// 当前登录用户编辑找天工信息
									} else if (id.equals("0.101")) {
										CreateRecruitmentPhotoListInfo(gson
												.toJson(list));
									}

								}

							}
						}
					}, new UploadOptions(null, null, false,
							new UpProgressHandler() {

								@Override
								public void progress(String arg0, double arg1) {
									// TODO Auto-generated method stub

								}
							}, null));
			return null;
		}

	}

	/**
	 * 获取七牛上传TOKEN
	 * 
	 * @author Johnson
	 */
	private void getQiniuToken() {
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
						String Token = interfaceReturn.getResults().get(0)
								.getToken();
						System.out.println(Token);
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
									WantDayWorkerActivity.this);
							builder.setMessage("您还没有实名认证,是否去认证 ?");
							builder.setTitle(R.string.Hint);
							builder.setNegativeButton(
									R.string.Yes,
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													WantDayWorkerActivity.this,
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

}
