package com.qingzu.fragment;

import java.util.ArrayList;
import java.util.List;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.droid.MyGridView;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProjectCaseAlbum;
import com.qingzu.entity.ShowProjectTeam;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.SysUtils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.ConstructionDetailActivity;
import com.qingzu.waterproof_work.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 队员详情 晒工程
 * 
 * @author Administrator
 * 
 */
public class TeamMemberDetailPhotoShowFragment extends Fragment implements
		OnClickListener {

	private View v = null;
	private ZListView mListView = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private String UserToken = null;
	private List<ShowProjectTeam> list = new ArrayList<ShowProjectTeam>();
	private InterfaceReturn<ShowProjectTeam> interfaceReturn = new InterfaceReturn<ShowProjectTeam>();
	private MyAdapter<ShowProjectTeam> myAdapter = null;
	private MyAdapter<ProjectCaseAlbum> gvAdapter = null;
	private List<ProjectCaseAlbum> projectCaseAlbum = new ArrayList<ProjectCaseAlbum>();
	private int wh;
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
		v = inflater.inflate(R.layout.fragment_team_member_detail_photoshow,
				container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView = (ZListView) v.findViewById(R.id.construction_team_list_zlv);
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mListView.setPullLoadEnable(true);
		wh = (SysUtils.getScreenWidth(getActivity()) - SysUtils.Dp2Px(
				getActivity(), 99)) / 3;

		getProjectCaseListByTeamId(ConstructionDetailActivity.id,"1", "10",
				REFRESH_DATA_FINISH);

		myAdapter = new MyAdapter<ShowProjectTeam>(getActivity(), list,
				R.layout.construction_detail_pho_list_item) {

			TextView proiect_introduce;
			MyGridView myGridView;
			TextView completion_date;

			@Override
			public void convert(ViewHolder view, int position,
					ShowProjectTeam item) {
				// TODO Auto-generated method stub
				proiect_introduce = view.getView(R.id.proiect_introduce);
				myGridView = view.getView(R.id.gv_images);
				completion_date = view.getView(R.id.completion_date);
				proiect_introduce.setText(item.getProjectCase()
						.getCaseDescribe() + "");
				completion_date.setText(Tools.dateToStrLong(Tools
						.strToDateT(item.getProjectCase().getIssueTime())));
				projectCaseAlbum = list.get(position).getProjectCaseAlbum();
				if (projectCaseAlbum.size() != 0) {
					intiGridView(myGridView, projectCaseAlbum);
				}

			}
		};
		mListView.setAdapter(myAdapter);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getProjectCaseListByTeamId(ConstructionDetailActivity.id, "1",
						"10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getProjectCaseListByTeamId(ConstructionDetailActivity.id,
						CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});

	}

	public void intiGridView(MyGridView gridView,
			final List<ProjectCaseAlbum> projectCaseAlbum) {

		int w = 0;

		switch (projectCaseAlbum.size()) {
		case 1:
			w = wh;
			gridView.setNumColumns(1);
			break;
		case 4:
			w = 2 * wh + SysUtils.Dp2Px(getActivity(), 2);
			gridView.setNumColumns(2);
			break;
		case 6:
			w = 3 * wh + SysUtils.Dp2Px(getActivity(), 3);
			gridView.setNumColumns(3);
			break;

		default:
			break;
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(lp);
		gvAdapter = new MyAdapter<ProjectCaseAlbum>(getActivity(),
				projectCaseAlbum, R.layout.construction_detail_pho_grid_item) {

			ImageView imageView;

			@Override
			public void convert(ViewHolder view, int position,
					ProjectCaseAlbum item) {
				// TODO Auto-generated method stub
				imageView = view.getView(R.id.imageView);
				ImageLoaderUtil.loadXUtilImage(projectCaseAlbum.get(position)
						.getCasePhoto(), imageView);

			}
		};
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "点击了第" + (position + 1) + "张图片",
						Toast.LENGTH_SHORT).show();

			}
		});
		gridView.setAdapter(gvAdapter);
	}

	private void getProjectCaseListByTeamId(String TeamId, String Page,
			String Size, final int State) {
		RequestParams params = new RequestParams(
				HttpUtil.ProjectCaseListByTeamId.replace("{TeamId}", TeamId)
						.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						ShowProjectTeam.class);
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
		}

		);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
