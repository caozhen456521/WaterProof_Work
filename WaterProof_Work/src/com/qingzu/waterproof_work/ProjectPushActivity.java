package com.qingzu.waterproof_work;

import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ExtensionNumber;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.ProjectPushListModel;
import com.qingzu.entity.ProjectPushRecord;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectPushActivity extends Activity implements OnClickListener {

	private MyTitleView project_push_title = null;
	private ZListView mListView = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	public static final int SELECT_CITY = 20;
	private String UserToken = null;
	private List<ProjectPushListModel> list = new ArrayList<ProjectPushListModel>();
	private InterfaceReturn<ProjectPushListModel> interfaceReturn = new InterfaceReturn<ProjectPushListModel>();
	private MyAdapter<ProjectPushListModel> myAdapter = null;
	private Integer ProjectId = null;// 工程信息Id
	private Integer ProPosition = null;// 位置
	private String DealState = null;// 成交状态（0待处理,1已接单,2未谈成）
	private InterfaceReturn<ExtensionNumber> PHONGinterfaceReturn = new InterfaceReturn<ExtensionNumber>();
	private ExtensionNumber extensionNumber;

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

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_push);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = ProjectPushActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		project_push_title = (MyTitleView) findViewById(R.id.project_push_title);
		mListView = (ZListView) findViewById(R.id.lv_project_push);
		mListView.setPullLoadEnable(true);

		getMyListByPage("1", "10", MainActivity.LOAD_DATA);

		myAdapter = new MyAdapter<ProjectPushListModel>(
				ProjectPushActivity.this, list,
				R.layout.layout_project_push_item) {
			
			RelativeLayout rlProjectPush = null;
			TextView tvProjectInfoTitle = null;// 工程标题
			TextView tvProjectInfoStartTime = null;// 工程开工时间
			TextView tvProjectInfoWays = null;// 承包方式
			Button btAcceptProject = null;// 接受工程
			Button btRefuseProject = null;// 取消订单
			Button btCompleted = null;// 已接单
			Button btNoCompleted = null;// 未谈成
			Button btCallPhone = null;// 拨打电话
			Button btChat=null;//聊天

			@Override
			public void convert(ViewHolder view, final int position,
					final ProjectPushListModel item) {
				// TODO Auto-generated method stub
				btChat=view.getView(R.id.pro_push_chat_bt);
				btCallPhone = view.getView(R.id.pro_push_call_phone);
				rlProjectPush = view.getView(R.id.project_push_room_rl);
				tvProjectInfoTitle = view.getView(R.id.project_push_title_tv);
				tvProjectInfoStartTime = view
						.getView(R.id.project_push_start_time_tv);
				tvProjectInfoWays = view.getView(R.id.project_push_ways_tv);
				btAcceptProject = view.getView(R.id.accept_project_bt);
				btRefuseProject = view.getView(R.id.refuse_project_bt);
				btCompleted = view.getView(R.id.completed_bt);
				btNoCompleted = view.getView(R.id.no_completed_bt);

				tvProjectInfoTitle.setText(list.get(position).getProjectInfo()
						.getInfoTitle());
				String starttime = dateToStrLong(strToDate(list.get(position)
						.getProjectInfo().getStartTime()));
				String wt;
				String st = starttime;
				wt = st.replace("年", "-").replace("月", "-").replace("日", "");
				tvProjectInfoStartTime.setText(wt + "开工" + " ");
				tvProjectInfoWays
						.setText("承包方式:"
								+ list.get(position).getProjectInfo()
										.getContractMode());

				DealState = list.get(position).getProjectPushRecord()
						.getIsDeal()
						+ "";
				if (DealState.equals("0")) {
					btAcceptProject.setVisibility(View.VISIBLE);
					btRefuseProject.setVisibility(View.VISIBLE);
					btCompleted.setVisibility(View.GONE);
					btNoCompleted.setVisibility(View.GONE);

				} else if (DealState.equals("1")) {
					btCompleted.setVisibility(View.VISIBLE);
					btAcceptProject.setVisibility(View.GONE);
					btRefuseProject.setVisibility(View.GONE);
					btNoCompleted.setVisibility(View.GONE);

				} else if (DealState.equals("2")) {
					btNoCompleted.setVisibility(View.VISIBLE);
					btCompleted.setVisibility(View.GONE);
					btAcceptProject.setVisibility(View.GONE);
					btRefuseProject.setVisibility(View.GONE);
				}

				ProjectId = list.get(position).getProjectPushRecord()
						.getProjectId();

				btCallPhone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						HintDialog.Builder builder = new HintDialog.Builder(
								ProjectPushActivity.this);
						// builder.setMessage("该用户的手机号是" + Mobile.substring(0,
						// 3) +
						// "****"
						// + Mobile.substring(7, 11) + "是否拔打");
						builder.setMessage("确认拨打电话?");
						builder.setTitle(R.string.Hint);
						builder.setNegativeButton(
								R.string.Yes,
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (Tools.isConnectingToInternet()) {
											// getExtensionNumberByInfoIdandInfoType(
											// item.getProjectInfo().getId() +
											// "", "2");
											// ...
											Intent intent = new Intent(Intent.ACTION_CALL,
													Uri.parse("tel:" + item.getProjectInfo().getContactTel().trim()));
											startActivity(intent);
										} else {
											T.showToast(x.app(), "请检查网络");
										}
										dialog.dismiss();
									}
								});
						builder.setPositiveButton(R.string.No,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.dismiss();
									}
								});
						builder.create().show();
					}

				});
				
				//聊天按钮
				btChat.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (JMessageClient.getMyInfo()!=null) {
						if (list.get(position).getProjectInfo().getContactTel()!=null) {
							Tools.getUserInfo(list.get(position).getProjectInfo().getContactTel(), ProjectPushActivity.this);	
						}}else {
							T.showToast(ProjectPushActivity.this, "未知错误,请重新登录");
						}
					
					}
				});

				btAcceptProject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ProjectId = list.get(position).getProjectPushRecord()
								.getProjectId();
						ProPosition = position;
						PassProjectPushRecord(ProjectId, position);
					}

				});

				btRefuseProject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ProjectId = list.get(position).getProjectPushRecord()
								.getProjectId();
						ProPosition = position;
						NoPassProjectPushRecord(ProjectId);

					}
				});

				rlProjectPush.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(ProjectPushActivity.this,
								ProjectDetailActivity.class);

						// 添加传送数据

						intent.putExtra("project", list.get(position)
								.getProjectPushRecord().getProjectId()
								+ "");

						// String str=list.get(position).getProjectPushRecord()
						// .getProjectId();
						// intent.putExtra("lol", "2");
						startActivity(intent);

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
		project_push_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

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
					T.showToast(ProjectPushActivity.this, "请检查网络");
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
					T.showToast(ProjectPushActivity.this, "请检查网络");
				}

			}
		});

	}

	/**
	 * 根据InfoId和InfoType获取分机号信息
	 * 
	 * @param InfoId
	 * @param InfoType
	 */
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
								String str = "tel:" + "4000163235,,,,"
										+ extensionNumber.getNumber().trim()
										+ URLEncoder.encode("#");
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse(str));
								startActivity(intent);
							} else {
								T.showToast(x.app(), "话务忙,请稍候再试");
							}
						}

						T.showToast(x.app(), interfaceReturn.getMessage());
					}
				} else {
					T.showToast(x.app(), interfaceReturn.getMessage());
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
	 * 当前登录用户工长确认工程订单
	 * 
	 * @param ProjectId
	 */

	private void PassProjectPushRecord(final int ProjectId, final int position) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.PassProjectPushRecord.replace("{ProjectId}", ProjectId
						+ ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ProjectPushRecord> interfaceReturn = new InterfaceReturn<ProjectPushRecord>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectPushRecord.class);
				if (interfaceReturn.isStatus()) {
					list.get(position).setProjectPushRecord(
							interfaceReturn.getResults().get(0));
					myAdapter.notifyDataSetChanged();
					ProPosition = null;
				} else {
					list.clear();
					getMyListByPage("1", "10", REFRESH_DATA_FINISH);
					Count = 1;
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
	 * 当前登录用户工长拒绝工程订单 PUT
	 * 
	 * @param ProjectId
	 */

	private void NoPassProjectPushRecord(int ProjectId) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.NoPassProjectPushRecord.replace("{ProjectId}",
						ProjectId + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<ProjectPushRecord> interfaceReturn = new InterfaceReturn<ProjectPushRecord>();
				interfaceReturn = interfaceReturn.fromJson(result,
						ProjectPushRecord.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						list.get(ProPosition).setProjectPushRecord(
								interfaceReturn.getResults().get(0));
						myAdapter.notifyDataSetChanged();
						ProPosition = null;
						T.showToast(ProjectPushActivity.this,
								interfaceReturn.getMessage());

					} else {
						list.clear();
						getMyListByPage("1", "10", REFRESH_DATA_FINISH);
						Count = 1;
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
	 * 当前登录用户获取推送给施工队记录列表信息 get
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getMyListByPage(String Page, String Size, final int State) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.ProPushListByPage
				.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectPushListModel.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));

						}

						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
							// mListView.onShowText();
							// System.out.println(Count);
							// mListView.setStateNotData();
						} else if (State == LOAD_DATA_FINISH) {
							//

							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else {
							myAdapter.notifyDataSetChanged();
						}
					} else {
						T.showToast(ProjectPushActivity.this,
								interfaceReturn.getMessage());
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
	 * 根据ID获取工程信息推送给施工队记录信息
	 * 
	 * @param id
	 * @param position
	 */
	private void getPushRecordInfoByID(int id, final int position) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(
				HttpUtil.PushRecordInfo.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ProjectPushListModel> interfaceReturn = new InterfaceReturn<ProjectPushListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectPushListModel.class);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		if (ProjectId != null && ProPosition != null) {
			getPushRecordInfoByID(ProjectId, ProPosition);
			ProjectId = null;
			ProPosition = null;
		}
		super.onResume();
	}

}
