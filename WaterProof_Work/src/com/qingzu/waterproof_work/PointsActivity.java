package com.qingzu.waterproof_work;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.MemberIntegralDetailed;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PointsActivity extends Activity implements OnClickListener {

	private MyTitleView mtvPointsTitle = null;// 积分标题
	private ImageButton imbtnSign = null;// 签到按钮
	private TextView tvCurrentPoints = null;// 当前积分
	private ZListView lvPoints = null;// 积分列表
	private String UserToken = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private int IntegralNumber;
	private String NickName = null;

	private List<MemberIntegralDetailed> list = new ArrayList<MemberIntegralDetailed>();
	private InterfaceReturn<MemberIntegralDetailed> interfaceReturn = new InterfaceReturn<MemberIntegralDetailed>();
	private MyAdapter<MemberIntegralDetailed> myAdapter = null;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				lvPoints.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					lvPoints.setStateNotData(true);
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
					lvPoints.setStateNotData(false);
				} else {
					lvPoints.stopLoadMore(); // 加载更多完成
				}

				break;
			default:
				break;
			}

		};
	};

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_points);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
		getUserInfo();

	}

	

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PointsActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		NickName = preferences.getString("NickName", "");
		// IntegralNumber=preferences.getInt("IntegralNumber ", 0);
//		IntegralNumber = LoginActivity.integralNumber;

		mtvPointsTitle = (MyTitleView) findViewById(R.id.points_title);
		imbtnSign = (ImageButton) findViewById(R.id.sign_imgbtn);
		tvCurrentPoints = (TextView) findViewById(R.id.current_points_tv);
		lvPoints = (ZListView) findViewById(R.id.points_zlv);
		lvPoints.setPullLoadEnable(true);

		imbtnSign.setOnClickListener(this);
		
//		tvCurrentPoints.setText(IntegralNumber + "");
		
		
		CanAddDailyIntegral();
		getIntegral("1", "10", MainActivity.LOAD_DATA);

		myAdapter = new MyAdapter<MemberIntegralDetailed>(PointsActivity.this,
				list, R.layout.layout_points_item) {

			TextView tvPoints = null;// 签到积分
			RelativeLayout rlPointsItem = null;

			@Override
			public void convert(ViewHolder view, int position,
					MemberIntegralDetailed item) {
				// TODO Auto-generated method stub

				tvPoints = view.getView(R.id.points_tv);
				rlPointsItem = view.getView(R.id.points_item_rl);

				tvPoints.setText(list.get(position).getTypeName() + "获得积分"
						+ list.get(position).getIntegralNumber());

			}

		};
		lvPoints.setAdapter(myAdapter);
		mtvPointsTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		mtvPointsTitle.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		lvPoints.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				getIntegral("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				getIntegral(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});

	}
/**
 * 获取用户信息（获得用户的积分）
 */
	private void getUserInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn
						.fromJson(result, LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						IntegralNumber=interfaceReturn.getResults().get(0).getMember().getIntegralNumber();
						tvCurrentPoints.setText(IntegralNumber+"");
						
					

					} else {
						T.showToast(PointsActivity.this, interfaceReturn.getMessage());
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
	 * 判断登录用户 每日签到 是否已经完成.Status=true,可以签到，否则，当天已经签到过了。
	 */
	private void CanAddDailyIntegral() {
		// TODO Auto-generated method stub
		
		RequestParams params = new RequestParams(HttpUtil.CanAddDailyIntegral);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberIntegralDetailed.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						
						imbtnSign.setBackground(getResources().getDrawable(R.drawable.sign));
					} else {
//						T.showToast(PointsActivity.this,
//								interfaceReturn.getMessage());
						imbtnSign.setBackground(getResources().getDrawable(R.drawable.have_sign));
						
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
	 * 当前登录用户获取积分明细列表信息
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getIntegral(String Page, String Size, final int State) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.Integral.replace(
				"{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberIntegralDetailed.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
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
						T.showToast(PointsActivity.this,
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_imgbtn:
			getSign();
			break;

		default:
			break;
		}

	}
	
	
/**
 * 登录用户 每日签到 提交
 */
	private void getSign() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.DailyIntegral);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(PointsActivity.this,
								interfaceReturn.getMessage());
						
						imbtnSign.setBackground(getResources().getDrawable(R.drawable.have_sign));
						getUserInfo();
						

					
					} else {
						T.showToast(PointsActivity.this,
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		if (list!=null) {
//			list.clear();
//			initView();
//		}
		
		getUserInfo();

		super.onResume();
	}

}
