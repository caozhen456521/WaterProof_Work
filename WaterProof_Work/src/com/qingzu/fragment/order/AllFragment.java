package com.qingzu.fragment.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.OrderListModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.entity.UserInfoModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.AllotWorkerActivity;
import com.qingzu.waterproof_work.EvaluateBossActivity;
import com.qingzu.waterproof_work.EvaluateConstructionTeamActivity;
import com.qingzu.waterproof_work.EvaluateWorkerActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.WorkOrderActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 全部订单
 * 
 * @author Johnson
 * 
 */
public class AllFragment extends Fragment {

	private View v = null;
	private ZListView mListView = null;
	private List<OrderListModel> list = null;
	private MyAdapter<OrderListModel> myAdapter = null;

	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int BIND_ADAPTER = 7;// 绑定适配器

	private static String UserToken = null;
	private int roleState;
	private Integer iOrderId = null;
	private Integer iPosition = null;

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
		v = inflater.inflate(R.layout.fragment_all, container, false);
		initView();
		return v;
	}

	private void initView() {
		UserToken = ((WorkOrderActivity) getActivity()).UserToken;
		roleState = ((WorkOrderActivity) getActivity()).state;
		list = new ArrayList<OrderListModel>();

		mListView = (ZListView) v.findViewById(R.id.fall_listview);
		mListView.setPullLoadEnable(true);
		list.clear();
		ListByPage("1", "10", BIND_ADAPTER);

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				ListByPage("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				ListByPage(CountStr, "10", LOAD_DATA_FINISH);
			}
		});

		myAdapter = new MyAdapter<OrderListModel>(getActivity(), list,
				R.layout.layout_work_order_item) {
			RelativeLayout lwoi_room = null;
			ImageView lwoi_iv_photo = null;
			TextView lwoi_tv_title = null;
			TextView lwoi_tv_contacts = null;
			TextView lwoi_tv_order_time = null;
			TextView lwoi_tv = null;
			Button lwoi_bt = null;
			Button lwoi_bt2 = null;
			Button lwoi_chat_bt = null;

			@Override
			public void convert(ViewHolder view, final int position,
					final OrderListModel item) {
				lwoi_chat_bt = view.getView(R.id.lwoi_chat_bt);
				lwoi_room = view.getView(R.id.lwoi_room);
				lwoi_iv_photo = view.getView(R.id.lwoi_iv_photo);
				lwoi_tv_title = view.getView(R.id.lwoi_tv_title);
				lwoi_tv_contacts = view.getView(R.id.lwoi_tv_contacts);
				lwoi_tv_order_time = view.getView(R.id.lwoi_tv_order_time);
				lwoi_tv = view.getView(R.id.lwoi_tv);
				lwoi_bt = view.getView(R.id.lwoi_bt);
				lwoi_bt2 = view.getView(R.id.lwoi_bt2);
				ImageLoaderUtil.loadNoXUtilImage(item.getRecruitmentPhoto()
						.getPhotoUrl(), lwoi_iv_photo);
				lwoi_tv_title.setText(item.getRecruitmentInfo().getInfoTitle());
				if (roleState == 0) {// 老板
					lwoi_tv_contacts.setText("接单人:"
							+ item.getMember().getNickName());
					lwoi_bt.setVisibility(View.VISIBLE);
					lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
					lwoi_bt.setEnabled(true);
					if (item.getInfoOrder().getStateId() == 0) {
						lwoi_tv.setText("状态:待确认");
						lwoi_bt.setText("确认");
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt2.setText("取消");
						lwoi_bt2.setVisibility(View.VISIBLE);
						lwoi_bt.setVisibility(View.VISIBLE);
					} else if (item.getInfoOrder().getStateId() == 1) {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:已确认");
						lwoi_bt.setText("开始工程");
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt.setVisibility(View.VISIBLE);
					} else if (item.getInfoOrder().getStateId() == 2) {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:进行中");
						lwoi_bt.setVisibility(View.GONE);
					} else if (item.getInfoOrder().getStateId() == 3) {
						if (item.getInfoOrder().getBossIsAppraise() == 0) {
							lwoi_bt2.setVisibility(View.GONE);
							lwoi_tv.setText("状态:待评价");
							lwoi_bt.setVisibility(View.VISIBLE);
							if (item.getInfoOrder().getTeamId() == 0) {
								lwoi_bt.setText("评价工人");
								lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
								lwoi_bt.setEnabled(true);
							} else {
								lwoi_bt.setText("评价施工队");
								lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
								lwoi_bt.setEnabled(true);
							}
						} else if (item.getInfoOrder().getWorkerIsAppraise() == 0) {
							lwoi_bt2.setVisibility(View.GONE);
							lwoi_tv.setText("状态:对方未评价");
							lwoi_bt.setVisibility(View.GONE);
						}
					} else {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:已完成");
						lwoi_bt.setVisibility(View.GONE);
					}
				} else if (roleState == 1) {// 工长
					lwoi_tv_contacts.setText("联系人:"
							+ item.getRecruitmentInfo().getContactName());
					if (item.getInfoOrder().getStateId() == 0) {
						lwoi_tv.setText("状态:待确认");
						lwoi_bt.setText("提醒老板");
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt2.setVisibility(View.GONE);
					} else if (item.getInfoOrder().getStateId() == 1) {
						lwoi_bt2.setVisibility(View.GONE);
						if (item.getInfoOrder().getDispatchMan() <= 0) {
							lwoi_tv.setText("状态:已确认");
							lwoi_bt.setText("指派工人");
							lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
							lwoi_bt.setEnabled(true);
							lwoi_bt.setVisibility(View.VISIBLE);
						} else {
							lwoi_tv.setText("指派工人:"
									+ item.getInfoOrder().getDispatchMan()
									+ "人");
							lwoi_bt.setEnabled(false);
							lwoi_bt.setBackground(null);
							lwoi_bt.setText(residueTime(item
									.getRecruitmentInfo().getStartTime()));
							lwoi_bt.setVisibility(View.VISIBLE);
						}
					} else if (item.getInfoOrder().getStateId() == 2) {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:进行中");
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt.setVisibility(View.VISIBLE);
						lwoi_bt.setText("工程结束");
					} else if (item.getInfoOrder().getStateId() == 3) {
						lwoi_bt2.setVisibility(View.GONE);
						if (item.getInfoOrder().getWorkerIsAppraise() == 0) {
							lwoi_tv.setText("状态:待评价");
							lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
							lwoi_bt.setEnabled(true);
							lwoi_bt.setText("评价老板");
							lwoi_bt.setVisibility(View.VISIBLE);
						} else if (item.getInfoOrder().getBossIsAppraise() == 0) {
							lwoi_bt2.setVisibility(View.GONE);
							lwoi_tv.setText("状态:对方未评价");
							lwoi_bt.setVisibility(View.GONE);
						}
					} else {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:已完成");
						lwoi_bt.setVisibility(View.GONE);
					}
				} else if (roleState == 2) {// 工人
					lwoi_tv_contacts.setText("联系人:"
							+ item.getRecruitmentInfo().getContactName());
					if (item.getInfoOrder().getStateId() == 0) {
						lwoi_tv.setText("状态:待确认");
						lwoi_bt.setText("提醒老板");
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt.setVisibility(View.VISIBLE);
					} else if (item.getInfoOrder().getStateId() == 1) {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:已确认");
						lwoi_bt.setEnabled(false);
						lwoi_bt.setBackground(null);
						lwoi_bt.setVisibility(View.VISIBLE);
						lwoi_bt.setText(residueTime(item.getRecruitmentInfo()
								.getStartTime()));
					} else if (item.getInfoOrder().getStateId() == 2) {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:进行中");
						lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
						lwoi_bt.setEnabled(true);
						lwoi_bt.setText("工程结束");
						lwoi_bt.setVisibility(View.VISIBLE);
					} else if (item.getInfoOrder().getStateId() == 3) {
						lwoi_bt2.setVisibility(View.GONE);
						if (item.getInfoOrder().getWorkerIsAppraise() == 0) {
							lwoi_tv.setText("状态:待评价");
							lwoi_bt.setBackgroundResource(R.drawable.shape_gray_line);
							lwoi_bt.setEnabled(true);
							lwoi_bt.setText("评价老板");
							lwoi_bt.setVisibility(View.VISIBLE);
						} else if (item.getInfoOrder().getBossIsAppraise() == 0) {
							lwoi_bt2.setVisibility(View.GONE);
							lwoi_tv.setText("状态:对方未评价");
							lwoi_bt.setVisibility(View.GONE);
						}
					} else {
						lwoi_bt2.setVisibility(View.GONE);
						lwoi_tv.setText("状态:已完成");
						lwoi_bt.setVisibility(View.GONE);
					}
				}
				lwoi_tv_order_time.setText("下单时间:"
						+ Tools.dateToStrLong(Tools.strToDateT(item
								.getInfoOrder().getIssueTime())));
				lwoi_room.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),
								RecruitDetailActivity.class);
						iOrderId = item.getInfoOrder().getId();
						iPosition = position;
						// 添加传送数据
						intent.putExtra("recruit", item.getRecruitmentInfo()
								.getId() + "");
						getActivity().startActivity(intent);
					}
				});
				lwoi_bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (roleState == 0) {// 老板
							if (item.getInfoOrder().getStateId() == 0) {
								PassInfoOrder(item.getInfoOrder().getId(),
										position);
							} else if (item.getInfoOrder().getStateId() == 1) {
								// 已确认
								if (item.getInfoOrder().getTeamId() == 0) {
									// 开始
									StartInfoOrder(item.getInfoOrder().getId(),
											position);
								} else {
									if (item.getInfoOrder().getDispatchMan() > 0) {
										// 开始
										StartInfoOrder(item.getInfoOrder()
												.getId(), position);
									} else {
										T.showToast(getActivity(), "请等待工长派遣工人");
									}
								}
							} else if (item.getInfoOrder().getStateId() == 2) {
								// 进行中
							} else if (item.getInfoOrder().getStateId() == 3) {
								// 待评价
								if (item.getInfoOrder().getTeamId() == 0) {
									// 评价工人
									getActivity()
											.startActivity(
													new Intent(
															getActivity(),
															EvaluateWorkerActivity.class)
															.putExtra(
																	"MEMBER_ID",
																	item.getInfoOrder()
																			.getMemberId())
															.putExtra(
																	"ORDER_ID",
																	item.getInfoOrder()
																			.getId()));
									iOrderId = item.getInfoOrder().getId();
									iPosition = position;
								} else {
									// 评价施工队
									getActivity()
											.startActivity(
													new Intent(
															getActivity(),
															EvaluateConstructionTeamActivity.class)
															.putExtra(
																	"TEAM_ID",
																	item.getInfoOrder()
																			.getTeamId())
															.putExtra(
																	"ORDER_ID",
																	item.getInfoOrder()
																			.getId()));
									iOrderId = item.getInfoOrder().getId();
									iPosition = position;
								}
							}
						} else if (roleState == 1) {// 工长
							if (item.getInfoOrder().getStateId() == 0) {
								RemindBossById(item.getInfoOrder().getId());
							} else if (item.getInfoOrder().getStateId() == 1) {
								if (item.getInfoOrder().getDispatchMan() <= 0) {
									startActivity(new Intent(getActivity(),
											AllotWorkerActivity.class)
											.putExtra("INFO_ORDER_ID", item
													.getInfoOrder().getId()));
									iOrderId = item.getInfoOrder().getId();
									iPosition = position;
								}
							} else if (item.getInfoOrder().getStateId() == 2) {
								hintDialog("提示", "工程是否已完成", item.getInfoOrder()
										.getId(), position);
							} else if (item.getInfoOrder().getStateId() == 3) {
								// 评价老板
								getActivity()
										.startActivity(
												new Intent(
														getActivity(),
														EvaluateBossActivity.class)
														.putExtra(
																"ORDER_ID",
																item.getInfoOrder()
																		.getId())
														.putExtra(
																"MEMBER_ID",
																item.getInfoOrder()
																		.getToMemberId()));
								iOrderId = item.getInfoOrder().getId();
								iPosition = position;
							}
						} else if (roleState == 2) {// 工人
							if (item.getInfoOrder().getStateId() == 0) {
								RemindBossById(item.getInfoOrder().getId());
							} else if (item.getInfoOrder().getStateId() == 1) {
								//
							} else if (item.getInfoOrder().getStateId() == 2) {
								hintDialog("提示", "工程是否已完成", item.getInfoOrder()
										.getId(), position);
							} else if (item.getInfoOrder().getStateId() == 3) {
								// 评价老板
								// 评价老板
								getActivity()
										.startActivity(
												new Intent(
														getActivity(),
														EvaluateBossActivity.class)
														.putExtra(
																"ORDER_ID",
																item.getInfoOrder()
																		.getId())
														.putExtra(
																"MEMBER_ID",
																item.getInfoOrder()
																		.getToMemberId()));
								iOrderId = item.getInfoOrder().getId();
								iPosition = position;
							}
						}
					}
				});
				lwoi_bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						DeleteInfoOrderByID(item.getInfoOrder().getId(),
								position);
					}
				});

				// 聊天
				lwoi_chat_bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (JMessageClient.getMyInfo() != null) {
							if (roleState == 0) {
								if (item.getMember().getUserName() != null) {
									Tools.getUserInfo(item.getMember().getUserName()
										, getActivity());
								}
							}else{

							if (item.getInfoOrder().getToMemberId() != 0) {
								UserInfoByID(item.getInfoOrder()
										.getToMemberId(), getActivity());
							}}
						} else {
							T.showToast(getActivity(), "未知错误,请重新登录");
						}
					}
				});
			}
		};
	}

	/**
	 * 按照用户ID获取用户信息 GET
	 * 
	 * @param id
	 */
	public static void UserInfoByID(int id, final Context context) {
		RequestParams params = new RequestParams(HttpUtil.UserInfoByID.replace(
				"{userID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<UserInfoModel> interfaceReturn = new InterfaceReturn<UserInfoModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						UserInfoModel.class);
				if (interfaceReturn.isStatus()) {
					
					Tools.getUserInfo(interfaceReturn.getResults().get(0)
							.getMember().getUserName(), context);
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
	 * 根据ID获取订单信息 GET
	 * 
	 * @param id
	 * @param position
	 * @author Johnson
	 */
	private void OrderInfoByID(int id, final int position) {
		RequestParams params = new RequestParams(
				HttpUtil.OrderInfoByID.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<OrderListModel> interfaceReturn = new InterfaceReturn<OrderListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						OrderListModel.class);
				if (interfaceReturn.isStatus()) {
					list.set(position, interfaceReturn.getResults().get(0));
					myAdapter.notifyDataSetChanged();
				} else {
					list.clear();
					ListByPage("1", "10", REFRESH_DATA_FINISH);
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

	/**
	 * 当前登录用户(老板)确认天工订单 PUT
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void PassInfoOrder(final int id, final int position) {
		RequestParams params = new RequestParams(
				HttpUtil.PassInfoOrder.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						OrderInfoByID(id, position);
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 当前登录用户根据ID删除承接天工订单信息 DELETE
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void DeleteInfoOrderByID(final int id, final int position) {
		RequestParams params = new RequestParams(
				HttpUtil.DeleteInfoOrderByID.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Delete(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						OrderInfoByID(id, position);
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 当前登录用户(老板)开始天工订单 PUT
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void StartInfoOrder(final int id, final int position) {
		RequestParams params = new RequestParams(
				HttpUtil.StartInfoOrder.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						OrderInfoByID(id, position);
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 提示dialog
	 * 
	 * @param title
	 * @param message
	 * @param status
	 * @author Johnson
	 */
	private void hintDialog(String title, String message, final int Id,
			final int position) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.Yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 完成
						EndInfoOrder(Id, position);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.No,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 当前登录用户结束承接天工订单 PUT
	 * 
	 * @param ID
	 * @author Johnson
	 */
	private void EndInfoOrder(final int id, final int position) {
		RequestParams params = new RequestParams(HttpUtil.EndInfoOrder.replace(
				"{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(getActivity(), interfaceReturn.getMessage());
					OrderInfoByID(id, position);
				} else {
					T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 剩余时间(天)
	 * 
	 * @param time
	 * @return
	 * @author Johnson
	 */
	public static String residueTime(String time) {
		Date date = new Date();
		long l = date.getTime();
		date = Tools.strToDateT(time);
		long n = date.getTime();
		long day = (n - l) / 1000 / 60 / 60 / 24;
		String value = null;
		// if (ss <= 60) {
		// value = "剩余时间  < 1分钟";
		// } else if (ss / 60 < 60) {
		// value = "剩余" + ss / 60 + "分钟";
		// } else if (ss / 60 / 60 < 24) {
		// value = "剩余" + ss / 60 / 60 + "小时"+(ss-(60*60))/60+"分钟";
		// } else
		if (day > 0) {
			value = "剩余" + day + "天开工";
		} else {
			value = "即将开工";
		}
		return value;
	}

	/**
	 * 提醒老板信息推送 PUT
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void RemindBossById(int id) {
		RequestParams params = new RequestParams(
				HttpUtil.RemindBossById.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 当前登录用户工人/工长获取承接天工订单列表信息 GET
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 * @author Johnson
	 */
	private void ListByPage(String Page, String Size, final int State) {
		RequestParams params = new RequestParams(
				(roleState == 0 ? HttpUtil.LeaderListByPage
						: HttpUtil.WorkerListByPage).replace("{Page}", Page)
						.replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<OrderListModel> interfaceReturn = new InterfaceReturn<OrderListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						OrderListModel.class);
				if (interfaceReturn != null) {
					PageCount = interfaceReturn.getTotalCount();
					if (interfaceReturn.isStatus()) {
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
						mListView.setAdapter(myAdapter);
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

	@Override
	public void onResume() {
		super.onResume();
		if (iOrderId != null && iPosition != null) {
			OrderInfoByID(iOrderId, iPosition);
			iOrderId = null;
			iPosition = null;
		}

	}
}