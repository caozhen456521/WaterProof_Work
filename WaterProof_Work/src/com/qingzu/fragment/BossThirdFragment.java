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
import com.qingzu.entity.ProjectInfo;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.ReleaseProjectActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老板第三个fragment
 * 
 * @author Administrator
 * 
 */
public class BossThirdFragment extends Fragment implements OnClickListener {

	private View v = null;
	private MyTitleView mtvBossThirdTitle = null;// 老板工程信息标题
	private ZListView mListView = null;
	private LayoutInflater layoutInflater;
	private List<ProjectModel> list = new ArrayList<ProjectModel>();
	private MyAdapter<ProjectModel> myAdapter = null;
	// private InterfaceReturn<ProjectInfo> interfaceReturn = new
	// InterfaceReturn<ProjectInfo>();

	private InterfaceReturn<ProjectModel> interfaceReturn = new InterfaceReturn<ProjectModel>();
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	public static final int SELECT_CITY = 20;
	private String UserToken = null;
	private boolean stateF = false;

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
		v = inflater.inflate(R.layout.fragment_boss_third, container, false);
		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(getActivity());
		mtvBossThirdTitle = (MyTitleView) v.findViewById(R.id.boss_third_title);
		mListView = (ZListView) v.findViewById(R.id.lv_project_information);
		mListView.setPullLoadEnable(true);

		if (Tools.isConnectingToInternet()) {
			getMyListByPage("1", "10", MainActivity.LOAD_DATA);
		} else {
			T.showToast(getActivity(), "请检查网络");
		}
		myAdapter = new MyAdapter<ProjectModel>(getActivity(), list,
				R.layout.layout_project_information_item) {
			TextView tvProjectInfoTitle = null;// 工程标题
			TextView tvProjectInfoStartTime = null;// 工程开工时间
			TextView tvProjectInfoWays = null;// 承包方式
			TextView tvProjectInfoState = null;// 工程信息处理状态
			RelativeLayout rlProjectInfoRoom = null;

			@Override
			public void convert(ViewHolder view, int position, ProjectModel item) {
				tvProjectInfoTitle = view.getView(R.id.project_info_title_tv);
				tvProjectInfoStartTime = view
						.getView(R.id.project_info_start_time_tv);
				tvProjectInfoWays = view.getView(R.id.project_info_ways_tv);
				tvProjectInfoState = view.getView(R.id.project_info_state_tv);
				rlProjectInfoRoom = view.getView(R.id.project_info_room_rl);

				tvProjectInfoTitle.setText(list.get(position).getProjectInfo()
						.getInfoTitle());
				String starttime = dateToStrLong(strToDate(list.get(position)
						.getProjectInfo().getStartTime()));
				String wt;
				String st = starttime;

				// int b = 5;
				// 去掉字符串的前i个字符：
				// str=str.Remove(0,i); // or
				// str=str.Substring(i);
				// st = st.substring(b);
				wt = st.replace("年", "-").replace("月", "-").replace("日", "");
				tvProjectInfoStartTime.setText(wt + "开工" + " ");
				tvProjectInfoWays
						.setText("承包方式："
								+ list.get(position).getProjectInfo()
										.getContractMode());
				if (list.get(position).getProjectInfo().getInfoState()
						.equals("0")) {
					tvProjectInfoState.setText("正在审核中");
				} else if (list.get(position).getProjectInfo().getInfoState()
						.equals("1")) {
					tvProjectInfoState.setText("审核通过");
				} else if (list.get(position).getProjectInfo().getInfoState()
						.equals("2")) {
					tvProjectInfoState.setText("对接成功");
				} else if (list.get(position).getProjectInfo().getInfoState()
						.equals("3")) {
					tvProjectInfoState.setText("已完成");
				} else if (list.get(position).getProjectInfo().getInfoState()
						.equals("-1")) {
					list.remove(position);
				}

				final String Id = Tools.formatString(list.get(position)
						.getProjectInfo().getId());
				rlProjectInfoRoom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// getActivity().startActivity(new
						// Intent(getActivity(),ProjectDetailActivity.class));
						Intent intent = new Intent(getActivity(),
								ProjectDetailActivity.class);

						// 添加传送数据
						intent.putExtra("project", Id);
						// intent.putExtra("lol", "1");
						getActivity().startActivity(intent);
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
		mtvBossThirdTitle.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity()
						.startActivity(
								new Intent(getActivity(),
										ReleaseProjectActivity.class));
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
	 * 当前登录用户获取防水工程项目列表信息
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getMyListByPage(String Page, String Size, final int State) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.ProjectMyList
				.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectModel.class);
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// getMyListByPage("1", "10", MainActivity.LOAD_DATA);
		if (stateF) {
			list.clear();
			getMyListByPage("1", "10", REFRESH_DATA_FINISH);
			Count = 1;
			mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
		} else {
			stateF = true;
		}
		super.onResume();
	}

}