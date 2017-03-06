package com.qingzu.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.droid.SelectCityActivity;
import com.google.gson.Gson;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.AppNewsUrl;
import com.qingzu.entity.ExtensionNumber;
import com.qingzu.entity.Information;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListNewsModel;
import com.qingzu.entity.MemberMessage;
import com.qingzu.entity.News;
import com.qingzu.entity.Order;
import com.qingzu.entity.RecruitModel;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.FifthMyMessageActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.SelectRoleActivity;
import com.qingzu.waterproof_work.WantDayWorkerActivity;
import com.qingzu.waterproof_work.WebViewActivity;
import com.qingzu.waterproof_work.MainActivity.OnCityListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工长第一Fragment
 * 
 * @author Johnson
 * 
 */
public class CaptainFirstFragment extends Fragment implements OnClickListener {
	private View v = null;
	private MyTitleView cff_home_title = null;
	private ZListView mListView = null;
	private LayoutInflater layoutInflater;
	private List<RecruitModel> list = null;
	private MyAdapter<RecruitModel> myAdapter = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int BIND_ADAPTER = 7;// 绑定适配器
	private RecruitModel itemS = null;
	private int State = 0; // 0:没拨打电话 1:拨打电话了
	private boolean fState = false;
	private int messgitem = 0;
	private List<Conversation> mDatas = new ArrayList<Conversation>();
	private String nowCity = null;
	private String UserToken = null;
	private String UserName = null;
	private int MemberId;
	private AppNewsUrl newsUrl;
	private InterfaceReturn<ListNewsModel> interfaceReturn = new InterfaceReturn<ListNewsModel>();
	private InterfaceReturn<AppNewsUrl> URLinterfaceReturn = new InterfaceReturn<AppNewsUrl>();
	private InterfaceReturn<ExtensionNumber> PHONGinterfaceReturn = new InterfaceReturn<ExtensionNumber>();

	private ExtensionNumber extensionNumber;
	/**
	 * // 劳保用品
	 */
	private LinearLayout safetyProduct_lv;
	private TextView safetyProduct_newsTitle = null;
	private TextView safetyProduct_subTitle = null;
	private ImageView safetyProduct_newsPhoto = null;

	/**
	 * 平台资讯
	 */
	private TextView text_message = null;
	private LinearLayout platform_message_ll = null;

	/**
	 * // 防水工具
	 */
	private LinearLayout toolProduct_lv;
	private TextView toolProduct_newsTitle = null;
	private TextView toolProduct_subTitle = null;
	private ImageView toolProduct_newsPhoto = null;

	private News safetyProduct_news;
	private News toolProduct_news;

	private Button safetyProduct_bt = null; // 劳保工具按钮

	private String MessageURL = null;

	private String InformationTitle = null;
	private String Message = null;
	private Boolean certification;//实名认证

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					mListView.setStateNotData(true);
				}
				break;
			case LOAD_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {
					System.out.println("Count==========================="
							+ Count);
					System.out.println("PageCount======================="
							+ PageCount);
					mListView.setStateNotData(false);
				} else {
					mListView.stopLoadMore(); // 加载更多完成
				}

				break;
			default:
				break;
			}

		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_first, container, false);
		initView();
		getNewMessage();
		return v;
	}

	/**
	 * 获取是否有新消息
	 */
	private void getNewMessage() {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.NewMessage.replace(
				"{bizType}", "0"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MemberMessage> interfaceReturn = new InterfaceReturn<MemberMessage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberMessage.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						cff_home_title.setRightImg(getResources().getDrawable(
    							R.drawable.home_message2));
						return;
					}else{
					
						if (JMessageClient.getMyInfo() != null) {
							mDatas = JMessageClient.getConversationList();
							for (int i = 0; i < mDatas.size(); i++) {
								messgitem = messgitem+mDatas.get(i).getUnReadMsgCnt();
							}
						if (messgitem>0) {
							cff_home_title.setRightImg(getResources().getDrawable(
	    							R.drawable.home_message2));
						}else{
							cff_home_title.setRightImg(getResources().getDrawable(
									R.drawable.home_message));
						}
						}else{
							cff_home_title.setRightImg(getResources().getDrawable(
									R.drawable.home_message));
						}
					}
				} else {
					cff_home_title.setRightImg(getResources().getDrawable(
							R.drawable.home_message));
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

	public OnCityListener onCityListener = new OnCityListener() {

		@Override
		public void OnCityAddress(String city) {
			// TODO Auto-generated method stub
			cff_home_title.setLeftText(city);
			nowCity = city;
			list.clear();
			InfoListByCityName(nowCity, "1", "10", REFRESH_DATA_FINISH);
			Count = 1;
		}
	};

	private void initView() {
		fState = true;
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		UserName = preferences.getString("UserName", "");
		MemberId = preferences.getInt("MemberId", 0);
		list = new ArrayList<RecruitModel>();
		layoutInflater = LayoutInflater.from(getActivity());
		cff_home_title = (MyTitleView) v.findViewById(R.id.cff_home_title);
		mListView = (ZListView) v.findViewById(R.id.cff_lv_home);

		cff_home_title.setCenterImg(R.drawable.captain_1);


		mListView.setPullLoadEnable(true);
		// addHead();
		cff_home_title.setOnImgCenterClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),
						SelectRoleActivity.class));
			}
		});
//		cff_home_title.setOnCenterClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				startActivity(new Intent(getActivity(),
//						SelectRoleActivity.class));
//			}
//		});
		SharedPreferences preferences1 = getActivity().getSharedPreferences(
				"city", Activity.MODE_PRIVATE);
		nowCity = preferences1.getString("city", "未知");
		cff_home_title.setLeftText(nowCity);
		InfoListByCityName(nowCity, "1", "10", BIND_ADAPTER);

		MainActivity.setOnCityListener(onCityListener);

		cff_home_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectCityActivity.class);
				intent.putExtra("code", StatusTool.SELECT_CITY);
				startActivityForResult(intent, StatusTool.SELECT_CITY);
			}
		});
		cff_home_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),
						FifthMyMessageActivity.class));
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				InfoListByCityName(nowCity, "1", "10", REFRESH_DATA_FINISH);
				Count = 1;
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				InfoListByCityName(nowCity, CountStr, "10", LOAD_DATA_FINISH);
			}
		});
		if (Tools.isConnectingToInternet()) {
		
			getNews();
			getAppNewsUrl();
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

		myAdapter = new MyAdapter<RecruitModel>(getActivity(), list,
				R.layout.layout_captain_project_item) {
			ImageView no_certification_img = null;// 未认证图片
			RelativeLayout lcpi_room = null;
			ImageView lcpi_iv_photo = null;
			TextView lcpi_tv_title = null;
			TextView lcpi_tv_location = null;
			TextView lcpi_tv_material = null;
			TextView lcpi_tv_start_time = null;
			TextView lcpi_tv_site = null;
			Button lcpi_iv_call_phone = null;
			ImageView lcpi_has_completed_img = null;
			TextView lcpi_tv_distance = null;

			@Override
			public void convert(ViewHolder view, int position,
					final RecruitModel item) {
				no_certification_img=view.getView(R.id.no_certification_img);
				lcpi_room = view.getView(R.id.lcpi_room);
				lcpi_iv_photo = view.getView(R.id.lcpi_iv_photo);
				lcpi_tv_title = view.getView(R.id.lcpi_tv_title);
				lcpi_tv_location = view.getView(R.id.lcpi_tv_location);
				lcpi_tv_material = view.getView(R.id.lcpi_tv_material);
				lcpi_tv_start_time = view.getView(R.id.lcpi_tv_start_time);
				lcpi_tv_site = view.getView(R.id.lcpi_tv_site);
				lcpi_iv_call_phone = view.getView(R.id.lcpi_iv_call_phone);
				lcpi_has_completed_img = view
						.getView(R.id.lcpi_has_completed_img);
				lcpi_tv_distance = view.getView(R.id.lcpi_tv_distance);

				
	

				
				
				certification=list.get(position).getMember().isRealName();
				if(certification){
					no_certification_img.setVisibility(View.GONE);
				}else{
					no_certification_img.setVisibility(View.VISIBLE);
				}
				

				ImageLoaderUtil.loadNoXUtilImage(item.getRecruitmentPhoto()
						.getPhotoUrl(), lcpi_iv_photo);
				lcpi_tv_title.setText(item.getRecruitmentInfo().getInfoTitle());
				lcpi_tv_location.setText("施工部位:"
						+ item.getRecruitmentInfo().getConstructionParts());
				lcpi_tv_material.setText("施工材料:"
						+ item.getRecruitmentInfo().getClaimMaterial());
				lcpi_tv_start_time.setText("开工时间:"
						+ Tools.dateToStrLong(Tools.strToDateT(item
								.getRecruitmentInfo().getStartTime())));
				lcpi_tv_site.setText("地点:"
						+ item.getRecruitmentInfo().getAddress());

				if (item.getRecruitmentInfo().getInfoState() == 2) {
					lcpi_iv_call_phone.setVisibility(View.GONE);
					lcpi_has_completed_img.setVisibility(View.VISIBLE);
				} else {
					lcpi_iv_call_phone.setVisibility(View.VISIBLE);
					lcpi_has_completed_img.setVisibility(View.GONE);
				}

				lcpi_room.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),
								RecruitDetailActivity.class);
						// 添加传送数据
						intent.putExtra("recruit", item.getRecruitmentInfo()
								.getId() + "");
						intent.putExtra("JPush", "");
						getActivity().startActivity(intent);
					}
				});
				lcpi_iv_call_phone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (Tools.isConnectingToInternet()) {
							itemS = item;
							callPhone(item);
//							getExtensionNumberByInfoIdandInfoType(item
//									.getRecruitmentInfo().getId() + "", "1");
						} else {
							T.showToast(getActivity(), "请检查网络");
						}
					}
				});
				final RecruitmentInfo recruitmentInfo = item
						.getRecruitmentInfo();
				if ((!recruitmentInfo.getLat().equals("0.0"))) {
					// viewHoler.distance.setVisibility(View.GONE);
					float distance = AMapUtils.calculateLineDistance(
							new LatLng(MyApplication.Latitude,
									MyApplication.Longitude),
							new LatLng(
									Double.valueOf(recruitmentInfo.getLat()),
									Double.valueOf(recruitmentInfo.getLon())));
					double sc = Double.valueOf(recruitmentInfo.getLat());
					System.out.println(sc);
					lcpi_tv_distance.setSelected(true);
					if (distance > 1000) {
						String s = distance / 1000 + "";
						lcpi_tv_distance.setText(s.substring(0,
								s.indexOf(".") + 3) + "公里");
					} else if (distance < 1000) {
						String s = distance + "";
						lcpi_tv_distance.setText(s.substring(0, s.indexOf("."))
								+ "米");
					}
				} else if (recruitmentInfo.getLat().equals("0.0")) {

					lcpi_tv_distance.setVisibility(View.GONE);
				}
			}
		};
		mListView.setAdapter(myAdapter);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	private void getExtensionNumberByInfoIdandInfoType(String InfoId,
			String InfoType) {
		RequestParams params = new RequestParams(
				HttpUtil.ExtensionNumberByInfoIdandInfoType.replace("{InfoId}",
						InfoId).replace("{InfoType}", InfoType));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				PHONGinterfaceReturn = InterfaceReturn.fromJson(result,
						ExtensionNumber.class);
				if (PHONGinterfaceReturn != null) {

					if (PHONGinterfaceReturn.isStatus()) {
						extensionNumber = new ExtensionNumber();
						if (PHONGinterfaceReturn.getResults() != null) {
							extensionNumber = PHONGinterfaceReturn.getResults()
									.get(0);

							if (extensionNumber.getInfoState() != 1) {

								HintDialog.Builder builder = new HintDialog.Builder(
										getActivity());
								// builder.setMessage("该用户的手机号是" +
								// Mobile.substring(0,
								// 3) +
								// "****"
								// + Mobile.substring(7, 11) + "是否拔打");
								builder.setMessage("确认拨打电话?"
								// + "(如有异常,手动拨打电话:4000163235+分机号:"
								// + extensionNumber.getNumber().trim()
								// + ")"
								);
								builder.setTitle(R.string.Hint);
								builder.setNegativeButton(
										R.string.Yes,
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (Tools
														.isConnectingToInternet()) {

													String str = "tel:"
															+ extensionNumber
																	.getContactTel()
																	.trim();
													Intent intent = new Intent(
															Intent.ACTION_CALL,
															Uri.parse(str));
													startActivityForResult(
															intent, 123);
													State = 1; // 判断作用
												} else {
													T.showToast(getActivity(),
															"请检查网络");
												}
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

						} else {
							T.showToast(getActivity(), "话务忙,请稍候再试");
						}
					} else {
						T.showToast(getActivity(),
								PHONGinterfaceReturn.getMessage());
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
	private void callPhone(final RecruitModel item) {
		HintDialog.Builder builder = new HintDialog.Builder(getActivity());
		// builder.setMessage("该用户的手机号是" +
		// Mobile.substring(0,
		// 3) +
		// "****"
		// + Mobile.substring(7, 11) + "是否拔打");
		builder.setMessage("确认拨打电话?"
		// + "(如有异常,手动拨打电话:4000163235+分机号:"
		// + extensionNumber.getNumber().trim()
		// + ")"
		);
		builder.setTitle(R.string.Hint);
		builder.setNegativeButton(R.string.Yes,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (Tools.isConnectingToInternet()) {

							String str = "tel:"
									+ item.getRecruitmentInfo().getContactTel()
											.trim();
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse(str));
							startActivityForResult(intent, 123);
							State = 1; // 判断作用
						} else {
							T.showToast(getActivity(), "请检查网络");
						}
						dialog.dismiss();
					}
				});
		builder.setPositiveButton(R.string.No,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		builder.create().show();
	}


	/**
	 * APP主页商品外链集合信息
	 */

	
	private void getAppNewsUrl() {
		RequestParams params = new RequestParams(HttpUtil.AppNewsUrl);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				URLinterfaceReturn = InterfaceReturn.fromJson(result,
						AppNewsUrl.class);
				if (URLinterfaceReturn != null) {

					if (URLinterfaceReturn.isStatus()) {

						if (URLinterfaceReturn.getResults() != null) {
							newsUrl = URLinterfaceReturn.getResults().get(0);
						}
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
	 * APP主页商品集合信息
	 */
	private void getNews() {
		RequestParams params = new RequestParams(HttpUtil.AppNewsList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				interfaceReturn = InterfaceReturn.fromJson(result,
						ListNewsModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							addHead(Message, getActivity(), 
									InformationTitle);
						}
					} else {

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
	 * 当前登录用户工人/工长请求创建承接天工订单 POST
	 * 
	 * @author Johnson
	 */
	private void CreateInfoOrder(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateInfoOrder);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(), interfaceReturn.getMessage());
						itemS = null;
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 获取找天工数据信息列表(根据城市名称) GET
	 * 
	 * @param CityName
	 * @param Page
	 * @param Size
	 * @author Johnson
	 */
	private void InfoListByCityName(String CityName, String Page, String Size,
			final int State) {
		String urlCityName = null;
		try {
			urlCityName = URLEncoder.encode(CityName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(HttpUtil.InfoListByCityName
				.replace("{CityName}", urlCityName).replace("{Page}", Page)
				.replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<RecruitModel> interfaceReturn = new InterfaceReturn<RecruitModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitModel.class);
				if (interfaceReturn != null) {
					PageCount = interfaceReturn.getTotalCount();
					if (interfaceReturn.isStatus()) {
						// list=interfaceReturn.getResults();
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));
						}
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
					if (State == REFRESH_DATA_FINISH) {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
					} else if (State == LOAD_DATA_FINISH) {
						mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
					} else if (State == BIND_ADAPTER) {
						// mListView.setAdapter(myAdapter);
					} else {
						myAdapter.notifyDataSetChanged();
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
	 * 按类别获取通知(平台资讯)get
	 */
	private void Information(String TypeId, String State, String Page,
			String Size) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.Information
				.replace("{TypeId}", TypeId).replace("{State}", State)
				.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<Information> interfaceReturn = new InterfaceReturn<Information>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						Information.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						InformationTitle = interfaceReturn.getResults().get(0)
								.getInformationTitle();




						if (interfaceReturn.getResults().get(0).getIsOutUrl()
								==true) {
							//IsOutUrl = true;

							MessageURL = interfaceReturn.getResults().get(0)
									.getOutUrl();
						}
						// Message=interfaceReturn.getResults().get(0).getInformationRemark().replace("&lt;p&gt;",
						// "").replace("&lt;/p&gt;", "");

						Message = interfaceReturn.getResults().get(0)
								.getInformationRemark();
						text_message.setText(Message);
						//
						// addHead(interfaceReturn.getResults().get(0)
						// .getInformationRemark()
						// .replace("&lt;p&gt;", "")
						// .replace("&lt;/p&gt;", ""), getActivity(),
						// MessageURL, InformationTitle);

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
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

	private void addHead(String tvTextMessage, final Context context,
			 final String InformationTitle) {
		// TODO Auto-generated method stub
		View view = layoutInflater.inflate(R.layout.layout_home_view, null);
		safetyProduct_newsPhoto = (ImageView) view
				.findViewById(R.id.safetyProduct_newsPhoto);
		safetyProduct_bt = (Button) view.findViewById(R.id.safetyProduct_bt);
		toolProduct_newsPhoto = (ImageView) view
				.findViewById(R.id.toolProduct_newsPhoto);
		safetyProduct_lv = (LinearLayout) view
				.findViewById(R.id.safetyProduct_lv);
		toolProduct_lv = (LinearLayout) view.findViewById(R.id.toolProduct_lv);
		platform_message_ll = (LinearLayout) view
				.findViewById(R.id.platform_message_ll);
		text_message = (TextView) view.findViewById(R.id.text_message);

		safetyProduct_bt.setOnClickListener(this);
		safetyProduct_lv.setOnClickListener(this);
		toolProduct_lv.setOnClickListener(this);
		text_message.setSelected(true);
		Information("6", "1", "1", "1");
	//	text_message.setText(tvTextMessage);
		platform_message_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if (MessageURL!=null) {

					Intent intent = new Intent();
					intent.setClass(getActivity(), WebViewActivity.class);
					intent.putExtra("url", MessageURL);
					intent.putExtra("title", InformationTitle);
					getActivity().startActivity(intent);
				}
				

			}
		});
		safetyProduct_news = interfaceReturn.getResults().get(0)
				.getSafetyProduct().get(0);
		toolProduct_news = interfaceReturn.getResults().get(0).getToolProduct()
				.get(0);

		ImageLoaderUtil.loadNoXUtilImage(safetyProduct_news.getNewsPhoto(),
				safetyProduct_newsPhoto);

		ImageLoaderUtil.loadNoXUtilImage(toolProduct_news.getNewsPhoto(),
				toolProduct_newsPhoto);

		mListView.addHeaderView(view);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.safetyProduct_bt:
			intentWebViewActivity(newsUrl.getTool() + "", "劳保用品");
			break;
		case R.id.safetyProduct_lv:
			intentWebViewActivity(safetyProduct_news.getLinkUrl() + "",
					safetyProduct_news.getNewsTitle() + "");
			System.out.println("safetyProduct_news.getLinkUrl()==========="+ safetyProduct_news.getLinkUrl() + "");
			break;
		case R.id.toolProduct_lv:
			intentWebViewActivity(toolProduct_news.getLinkUrl() + "",
					toolProduct_news.getNewsTitle() + "");
			System.out.println("toolProduct_news.getLinkUrl()==========="+ toolProduct_news.getLinkUrl() + "");
			break;
		default:
			break;
		}
	}

	private void intentWebViewActivity(String url, String title) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), WebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		getActivity().startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == StatusTool.SELECT_CITY) {
			cff_home_title.setLeftText(data.getExtras().getString("city"));
			nowCity = data.getExtras().getString("city");
			list.clear();
			InfoListByCityName(nowCity, "1", "10", REFRESH_DATA_FINISH);
			Count = 1;
		}
		if (requestCode == 123) {
			HintDialog.Builder builder = new HintDialog.Builder(getActivity());
			builder.setMessage("是否谈好成交 ?");
			builder.setTitle(R.string.Hint);
			builder.setNegativeButton(R.string.Yes,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Order order = new Order();
							order.setInfoId(itemS.getRecruitmentInfo().getId());
							order.setFromTelephone(UserName);
							order.setToTelephone(itemS.getMember()
									.getContactTel());
							order.setMemberId(MemberId);
							order.setToMemberId(itemS.getMember().getId());
							CreateInfoOrder(new Gson().toJson(order));
							// State = 0;
							dialog.dismiss();

						}
					});
			builder.setPositiveButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							State = 0;
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		getNewMessage();
		// State = State + 1;

		super.onResume();
	}

}