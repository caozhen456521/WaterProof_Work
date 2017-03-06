package com.qingzu.fragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.OrderListModel;
import com.qingzu.entity.RecruitDayWorker;
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
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.WantDayWorkerActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老板第二个fragment
 * 
 * @author Johnson
 * 
 */
public class BossSecondFragment extends Fragment implements OnClickListener {
	public static boolean SELECTMAP = false;
	private View v = null;
	private MyTitleView mtvBossSecondTitle = null;// 老板招工信息标题
	private ZListView mListView = null;
	private LayoutInflater layoutInflater;
	private List<RecruitModel> list = new ArrayList<RecruitModel>();
	private InterfaceReturn<RecruitModel> interfaceReturn = new InterfaceReturn<RecruitModel>();
	private MyAdapter<RecruitModel> myAdapter = null;
	private int PageCount = 0; // 总条数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	public static final int SELECT_CITY = 20;
	private String UserToken = null;
	private String infoState;
	private Integer RecruitId = null;// 招工信息编号
	private Integer RecPosition = null;// 位置
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
				// else if (PageCount == 0) {
				// mListView.setNoData();
				// }
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
		v = inflater.inflate(R.layout.fragment_boss_second, container, false);
		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(getActivity());
		mtvBossSecondTitle = (MyTitleView) v
				.findViewById(R.id.boss_second_title);
		mListView = (ZListView) v.findViewById(R.id.lv_recruit_information);
		mListView.setPullLoadEnable(true);

		if (Tools.isConnectingToInternet()) {
			getMyListByPage("1", "10", MainActivity.LOAD_DATA);// 当前登录用户获取找天工
																// 数据信息列表
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

		myAdapter = new MyAdapter<RecruitModel>(getActivity(), list,
				R.layout.layout_recruit_information_item) {
			ImageView no_certification_img = null;// 未认证图片
			ImageView imgRecruitInfo = null;// 招工信息图片
			TextView tvRecruitInfoTitle = null;// 标题
			TextView tvStartTime = null;// 开工时间
			TextView tvPeopleNumber = null;// 用工人数
			TextView tvNotConfirmNumber = null;// 待确认人数
			Button btModifyInfo = null;// 修改信息按钮
			Button btOverInfo = null;// 结束招工按钮
			Button btCompleted = null;// 完成招工
			LinearLayout llRecruitInfo = null;

			@Override
			public void convert(ViewHolder view, final int position,
					RecruitModel item) {
				no_certification_img=view.getView(R.id.no_certification_img);
				imgRecruitInfo = view.getView(R.id.recruit_info_img);
				tvRecruitInfoTitle = view.getView(R.id.recruit_info_title_tv);
				tvStartTime = view.getView(R.id.recruit_info_start_time_tv);
				tvPeopleNumber = view
						.getView(R.id.recruit_info_people_number_tv);
				tvNotConfirmNumber = view
						.getView(R.id.recruit_info_not_confirm_number_tv);
				btModifyInfo = view.getView(R.id.recruit_info_modify_bt);
				btOverInfo = view.getView(R.id.recruit_info_over_bt);
				btCompleted = view.getView(R.id.recruit_info_completed_bt);
				llRecruitInfo = view.getView(R.id.recruit_information_ll_room);
				
				
				certification=list.get(position).getMember().isRealName();
				if(certification){
					no_certification_img.setVisibility(View.GONE);
				}else{
					no_certification_img.setVisibility(View.VISIBLE);
				}

				infoState = list.get(position).getRecruitmentInfo()
						.getInfoState()
						+ "";
				if (infoState.equals("0")) {
					btModifyInfo.setVisibility(View.VISIBLE);
					btOverInfo.setVisibility(View.GONE);
					btCompleted.setVisibility(View.GONE);
				} else if (infoState.equals("1")) {
					btModifyInfo.setVisibility(View.GONE);
					btOverInfo.setVisibility(View.VISIBLE);
					btCompleted.setVisibility(View.GONE);
				} else if (infoState.equals("2")) {
					btOverInfo.setVisibility(View.GONE);
					btCompleted.setVisibility(View.VISIBLE);
					btModifyInfo.setVisibility(View.GONE);
				}

				final String id = Tools.formatString(list.get(position)
						.getRecruitmentInfo().getId());

				btModifyInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(getActivity(),
								WantDayWorkerActivity.class);
						intent.putExtra("id", id);

						getActivity().startActivity(intent);

						RecruitId = list.get(position).getRecruitmentInfo()
								.getId();
						RecPosition = position;
					}
				});

				btOverInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HintDialog.Builder builder = new HintDialog.Builder(
								getActivity());
						builder.setTitle(R.string.Hint);
						builder.setMessage("是否结束招工!");
						builder.setPositiveButton(R.string.Yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										EndRecruitment(id);
										dialog.dismiss();
									}

									/**
									 * 当前登录用户找天工信息结束招工
									 * @param ID
									 * @author Johnson
									 */
									private void EndRecruitment(String ID) {
										// TODO Auto-generated method stub
										RequestParams params = new RequestParams(
												HttpUtil.EndRecruitmentInfoById
														.replace("{ID}", ID));
										params.addHeader("EZFSToken",
												Tools.getEZFSToken(UserToken));
										Xutils.Put(params,
												new MyCallBack<String>() {
													@SuppressWarnings("unchecked")
													@Override
													public void onSuccess(
															String result) {
														InterfaceReturn<RecruitmentInfo> interfaceReturn = new InterfaceReturn<RecruitmentInfo>();
														interfaceReturn = InterfaceReturn
																.fromJson(
																		result,
																		RecruitmentInfo.class);
														if (interfaceReturn != null) {
															if (interfaceReturn
																	.isStatus()) {
																T.showToast(
																		getActivity(),
																		interfaceReturn
																				.getMessage());
																btOverInfo
																		.setVisibility(View.GONE);
																btCompleted
																		.setVisibility(View.VISIBLE);

															} else {
																T.showToast(
																		getActivity(),
																		interfaceReturn
																				.getMessage());
															}
														}
													}

													@Override
													public void onError(
															Throwable ex,
															boolean isOnCallback) {
														Toast.makeText(
																x.app(),
																ex.getMessage(),
																Toast.LENGTH_LONG)
																.show();
														if (ex instanceof HttpException) { // 网络错误
															HttpException httpEx = (HttpException) ex;
															int responseCode = httpEx
																	.getCode();
															String responseMsg = httpEx
																	.getMessage();
															String errorResult = httpEx
																	.getResult();
															System.out
																	.println("responseMsg:"
																			+ responseMsg
																			+ "=====errorResult:"
																			+ errorResult
																			+ "=====responseCode"
																			+ responseCode);
															// ...
														} else { // 其他错误
															// ...
														}
													}
												});
									}

								});
						builder.setNegativeButton(R.string.No,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				});

				tvRecruitInfoTitle.setText(list.get(position)
						.getRecruitmentInfo().getInfoTitle());

				ImageLoaderUtil.loadNoXUtilImage(list.get(position)
						.getRecruitmentPhoto().getPhotoUrl(), imgRecruitInfo);

				String starttime = dateToStrLong(strToDate(list.get(position)
						.getRecruitmentInfo().getStartTime()));
				String wt;
				String st = starttime;
				// int b = 5;
				// 去掉字符串的前i个字符：
				// str=str.Remove(0,i); // or
				// str=str.Substring(i);
				// st = st.substring(b);
				wt = st.replace("年", "-").replace("月", "-").replace("日", "");
				tvStartTime.setText(wt + "开工" + " ");

				tvPeopleNumber.setText(Tools.formatString(list.get(position)
						.getRecruitmentInfo().getDealNumber())
						+ "/"
						+ Tools.formatString(list.get(position)
								.getRecruitmentInfo().getPeopleNumber()));

				tvNotConfirmNumber.setText(Tools.formatString(list
						.get(position).getRecruitmentInfo().getApplyNumber()));

				final String Id = Tools.formatString(list.get(position)
						.getRecruitmentInfo().getId());

				llRecruitInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(getActivity(),
								RecruitDetailActivity.class);
						// 添加传送数据
						intent.putExtra("recruit", Id);
						// intent.putExtra("JPush", "");
						getActivity().startActivity(intent);
						RecruitId = list.get(position).getRecruitmentInfo().getId();
						RecPosition = position;

					}
				});

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

		};
		mListView.setAdapter(myAdapter);

		mtvBossSecondTitle.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						WantDayWorkerActivity.class);

				// 添加传送数据
				intent.putExtra("id", "0.101");

				getActivity().startActivity(intent);
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				if (Tools.isConnectingToInternet()) {
					list.clear();
					getMyListByPage("1", "10", REFRESH_DATA_FINISH);
					Count = 1;
					mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
				} else {
					T.showToast(getActivity(), "请检查网络");
				}

			}

			@Override
			public void onLoadMore() {
				if (Tools.isConnectingToInternet()) {
					Count = Count + 1;
					CountStr = Count + "";
					getMyListByPage(CountStr, "10", LOAD_DATA_FINISH);
					mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				} else {
					T.showToast(getActivity(), "请检查网络");
				}

			}
		});

	}

	/**
	 * 当前登录用户获取找天工 数据信息列表
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getMyListByPage(String Page, String Size, final int State) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.MyListByPage.replace(
				"{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitModel.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));
						}
						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						} else if (State == LOAD_DATA_FINISH) {
							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else {
							myAdapter.notifyDataSetChanged();
						}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/**
	 * 根据ID获取找天工信息
	 * 
	 * @param id
	 * @param position
	 */
	private void getRecruitmentInfoByID(int id, final int position) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(
				HttpUtil.RecruitmentInfoByID.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<RecruitModel> interfaceReturn = new InterfaceReturn<RecruitModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitModel.class);
				if (interfaceReturn.isStatus()) {
					list.set(position, interfaceReturn.getResults().get(0));
					myAdapter.notifyDataSetChanged();
				} else {
					list.clear();
					getMyListByPage("1", "10", REFRESH_DATA_FINISH);
					Count = 1;
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
	public void onResume() {
		// TODO Auto-generated method stub
		// list.clear();
		// getRecruitmentInfoByID(RecruitId,RecPosition);
		// Count = 1;
		//

		if (RecruitId != null && RecPosition != null) {
			getRecruitmentInfoByID(RecruitId, RecPosition);
			RecruitId = null;
			RecPosition = null;
		}
		super.onResume();
	}

}