package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.droid.MyGridView;
import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MemberMessage;
import com.qingzu.entity.ProjectCaseAlbum;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.entity.ShowProjectTeam;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.SysUtils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Administrator 晒工程
 */

@SuppressLint("ResourceAsColor") public class SunEngineeringActitvty extends Activity {

	private ZListView mListView = null;
	private MyTitleView myTitleView = null;
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
	// private FinalBitmap finalImageLoader;
	private int wh;
	private ArrayList<CreateFindWorkPhotoInfo> RemarkList;
	private String proCaseId = null;// itemID
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_sun_engineering_actitvty);
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView = (ZListView) findViewById(R.id.sun_engineering_list_zlv);
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		myTitleView = (MyTitleView) findViewById(R.id.sun_engineering_title);
		UserToken = preferences.getString("UserToken", "");
		mListView.setPullLoadEnable(true);
		// finalImageLoader = FinalBitmap.create(getActivity());
		wh = (SysUtils.getScreenWidth(this) - SysUtils.Dp2Px(this, 99)) / 3;

		getMyConstructionTeam("1", "10", REFRESH_DATA_FINISH);

		myAdapter = new MyAdapter<ShowProjectTeam>(this, list,
				R.layout.construction_detail_pho_list_item) {

			TextView proiect_introduce;
			MyGridView myGridView;
			TextView completion_date;
			Button delete_bt;

			@Override
			public void convert(ViewHolder view, final int position,
					ShowProjectTeam item) {
				// TODO Auto-generated method stub
				delete_bt = view.getView(R.id.delete_bt);
				proiect_introduce = view.getView(R.id.proiect_introduce);
				myGridView = view.getView(R.id.gv_images);
				completion_date = view.getView(R.id.completion_date);
			
				proiect_introduce.setText(item.getProjectCase()
						.getCaseDescribe() + "");
				completion_date.setText(Tools.dateToStrLong(Tools
						.strToDateT(item.getProjectCase().getIssueTime())));
				projectCaseAlbum = list.get(position).getProjectCaseAlbum();
				
			

				proCaseId = list.get(position).getProjectCase().getId() + "";

				if (projectCaseAlbum.size() != 0) {
					intiGridView(myGridView, projectCaseAlbum);
				}

				delete_bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						HintDialog.Builder builder = new HintDialog.Builder(
								SunEngineeringActitvty.this);
						builder.setTitle(R.string.Hint);
						builder.setMessage("是否删除工程记录!");
						builder.setPositiveButton(R.string.Yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										DeleteCase(proCaseId, position);
										dialog.dismiss();
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

			}
		};
		mListView.setAdapter(myAdapter);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMyConstructionTeam("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getMyConstructionTeam(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});
		myTitleView.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		myTitleView.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(SunEngineeringActitvty.this,
						ReleaseActivity.class));

			}
		});
	}

	/**
	 * 当前登录用户根据ID删除施工队/个人工程案例信息 DELETE
	 */
	private void DeleteCase(String ID, final int Position) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.DeleteCase.replace(
				"{ID}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Delete(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						T.showToast(SunEngineeringActitvty.this,
								interfaceReturn.getMessage());
						list.remove(Position);
						myAdapter.notifyDataSetChanged();

					} else {
						T.showToast(SunEngineeringActitvty.this,
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

	public void getMyConstructionTeam(String Page, String Size, final int State) {
		RequestParams params = new RequestParams(HttpUtil.MyListProjectCase
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
						T.showToast(SunEngineeringActitvty.this,
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
		}

		);

	}

	public void intiGridView(MyGridView gridView,
			final List<ProjectCaseAlbum> projectCaseAlbum) {

		int w = 0;

		switch (projectCaseAlbum.size()) {
		case 1:
			w = wh;
			gridView.setNumColumns(1);
			break;

		case 3:
		case 5:
		case 2:
		case 4:
			w = 2 * wh + SysUtils.Dp2Px(this, 2);
			gridView.setNumColumns(2);
			break;
		case 6:
			w = 3 * wh + SysUtils.Dp2Px(this, 3);
			gridView.setNumColumns(3);
			break;

		default:
			break;
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(lp);
		gvAdapter = new MyAdapter<ProjectCaseAlbum>(this, projectCaseAlbum,
				R.layout.construction_detail_pho_grid_item) {
			ImageView imageView;

			@Override
			public void convert(ViewHolder view, int position,
					ProjectCaseAlbum item) {
				// TODO Auto-generated method stub
				imageView = view.getView(R.id.imageView);
				ImageLoaderUtil.loadNoXUtilImage(projectCaseAlbum.get(position)
						.getCasePhoto(), imageView);
				// finalImageLoader.display(imageView,
				// projectCaseAlbum.get(position).getCasePhoto());
			}
		};
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				RemarkList = new ArrayList<CreateFindWorkPhotoInfo>();
				for (int i = 0; i < projectCaseAlbum.size(); i++) {
					CreateFindWorkPhotoInfo createFindWorkPhotoInfo = new CreateFindWorkPhotoInfo();
					createFindWorkPhotoInfo.setId(projectCaseAlbum.get(i)
							.getId());
					createFindWorkPhotoInfo.setPhotoLength(projectCaseAlbum
							.get(i).getPhotoLength());
					createFindWorkPhotoInfo.setPhotoWidth(projectCaseAlbum.get(
							i).getPhotoWidth());
					createFindWorkPhotoInfo.setPhotoUrl(projectCaseAlbum.get(i)
							.getCasePhoto());
					RemarkList.add(createFindWorkPhotoInfo);

				}
				Tools.imageBrower(SunEngineeringActitvty.this, position,
						RemarkList);

			}
		});
		gridView.setAdapter(gvAdapter);
	}

	// private void getProjectCaseListByTeamId(String TeamId, String Page,
	// String Size, final int State) {
	// RequestParams params = new RequestParams(
	// HttpUtil.ProjectCaseListByTeamId.replace("{TeamId}", TeamId)
	// .replace("{Page}", Page).replace("{Size}", Size));
	// params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
	// Xutils.Get(params, new MyCallBack<String>() {
	// @SuppressWarnings("unchecked")
	// public void onSuccess(String result) {
	// interfaceReturn = InterfaceReturn.fromJson(result,
	// ShowProjectTeam.class);
	// PageCount = interfaceReturn.getTotalCount();
	// if (interfaceReturn != null) {
	// if (interfaceReturn.isStatus()) {
	// for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
	//
	// list.add(interfaceReturn.getResults().get(i));
	// }
	// if (State == REFRESH_DATA_FINISH) {
	// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
	// // mListView.onShowText();
	// // System.out.println(Count);
	// // mListView.setStateNotData();
	// } else if (State == LOAD_DATA_FINISH) {
	// //
	//
	// mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
	// } else {
	// myAdapter.notifyDataSetChanged();
	// }
	// } else {
	// T.showToast(SunEngineeringActitvty.this,
	// interfaceReturn.getMessage());
	// }
	// }
	//
	// }
	//
	// public void onError(Throwable ex, boolean isOnCallback) {
	// Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
	// .show();
	// if (ex instanceof HttpException) { // 网络错误
	// HttpException httpEx = (HttpException) ex;
	// int responseCode = httpEx.getCode();
	// String responseMsg = httpEx.getMessage();
	// String errorResult = httpEx.getResult();
	// System.out.println("responseMsg:" + responseMsg
	// + "=====errorResult:" + errorResult
	// + "=====responseCode" + responseCode);
	// // ...
	// } else { // 其他错误
	// // ...
	// }
	// }
	// }
	//
	// );
	// }

}
