package com.qingzu.waterproof_work;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jmessage.android.uikit.chatting.BaseActivity;
import cn.jpush.im.android.api.JMessageClient;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.FindWorkInfoListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Order;
import com.qingzu.entity.OrderListModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindWorkerActivity extends BaseActivity implements OnClickListener {
	private String strCity = null;
	private MyTitleView find_worker_title = null;
	private ZListView mListView = null;
	private LayoutInflater layoutInflater;
	private Button btChange = null;
	private InterfaceReturn<FindWorkInfoListModels> interfaceReturn = new InterfaceReturn<FindWorkInfoListModels>();
	private MyAdapter<FindWorkInfoListModels> myAdapter = null;
	private List<FindWorkInfoListModels> list = new ArrayList<FindWorkInfoListModels>();
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private String UserToken = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private String infoId;
	private String UserName = null;
	private int MemberId;
	private FindWorkInfoListModels itemS;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					// mListView.setStateNotData(true);
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
		setContentView(R.layout.activity_find_worker);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		into();
		initView();
	}

	public void into() {
		SharedPreferences preferences2 = getSharedPreferences("city",
				Activity.MODE_PRIVATE);
		strCity = preferences2.getString("city", "");

	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		layoutInflater = LayoutInflater.from(FindWorkerActivity.this);
		UserToken = preferences.getString("UserToken", "");
		UserName = preferences.getString("UserName", "");
		MemberId = preferences.getInt("MemberId", 0);
		find_worker_title = (MyTitleView) findViewById(R.id.find_worker_title);
		mListView = (ZListView) findViewById(R.id.find_worker_listview);
		btChange = (Button) findViewById(R.id.change_bt);
		mListView.setPullLoadEnable(false);
	
		addFooter();

		Intent intent = getIntent();
		infoId = intent.getStringExtra("InfoId");
		getMessage("1", "30", strCity, REFRESH_DATA_FINISH,infoId);
		// mListView.addFooterView(v)
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (Tools.isConnectingToInternet()) {
					list.clear();
					getMessage("1", "30", strCity, REFRESH_DATA_FINISH,infoId);
					Count = 1;
					mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
				} else {
					T.showToast(FindWorkerActivity.this, "请检查网络");
				}

			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub

			}
		});

		myAdapter = new MyAdapter<FindWorkInfoListModels>(
				FindWorkerActivity.this, list, R.layout.layout_find_worker_item) {
			Button find_worker_chat_bt=null;
			RelativeLayout find_worker_rl = null;
			RoundImageView riv_find_worker_img = null;
			TextView find_worker_contact_name_tv = null;
			ImageView find_worker_certification_img = null;
			TextView find_worker_jiedan_number_tv = null;
			RatingBar find_worker_assess_ratingbar = null;
			ImageButton find_worker_imgbutton = null;

			@Override
			public void convert(ViewHolder view, final int position,
					final FindWorkInfoListModels item) {
				// TODO Auto-generated method stub
				find_worker_rl = view.getView(R.id.find_worker_rl);
				riv_find_worker_img = view.getView(R.id.riv_find_worker_img);
				find_worker_contact_name_tv = view
						.getView(R.id.find_worker_contact_name_tv);
				find_worker_certification_img = view
						.getView(R.id.find_worker_certification_img);
				find_worker_jiedan_number_tv = view
						.getView(R.id.find_worker_jiedan_number_tv);
				find_worker_assess_ratingbar = view
						.getView(R.id.find_worker_assess_ratingbar);
				find_worker_imgbutton = view
						.getView(R.id.find_worker_imgbutton);
				
				find_worker_chat_bt=view.getView(R.id.find_worker_chat_bt);
				find_worker_chat_bt.setVisibility(View.VISIBLE);
				ImageLoader
						.getInstance()
						.displayImage(
								list.get(position).getMember().getMemberPhoto(),
								riv_find_worker_img,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

				find_worker_contact_name_tv.setText(list.get(position)
						.getMember().getNickName());

				if (list.get(position).getMember().isRealName()) {
					find_worker_certification_img.setVisibility(View.VISIBLE);
				} else {
					find_worker_certification_img.setVisibility(View.GONE);
				}

//				find_worker_jiedan_number_tv.setText("接单次数："
//						+ list.get(position).getFindWorkInfo().getWorkCount());
				if (list.get(position).getFindWorkInfo().getIsCertificate() == true) {
					find_worker_jiedan_number_tv.setText("技能证书：" + "有");
				} else {
					find_worker_jiedan_number_tv.setText("技能证书：" + "无");
				}
				find_worker_assess_ratingbar.setRating((float) Double
						.parseDouble(Tools.formatString(list.get(position)
								.getMember().getWorkerLevel())));

				find_worker_imgbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						itemS = item;
						callphone(item);
					}
				});
				
				//聊天
				find_worker_chat_bt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (JMessageClient.getMyInfo()!=null) {
						if (list.get(position).getMember().getUserName() != null) {
							Tools.getUserInfo(list.get(position).getMember().getUserName(), FindWorkerActivity.this);	
						}
					}else{
						T.showToast(FindWorkerActivity.this, "未知错误,请重新登录");
					}	
					}
				});

				final String id = list.get(position).getFindWorkInfo()
						.getMemberId()
						+ "";
				find_worker_rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(FindWorkerActivity.this,
								WorkerDetailActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("InfoId", infoId);
						startActivity(intent);
					}
				});
			}

		};

		find_worker_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mListView.setAdapter(myAdapter);

	}

	private void addFooter() {
		View view = layoutInflater.inflate(R.layout.chage_button, null);
		Button button = (Button) view.findViewById(R.id.change_bt);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.clear();
				Count = Count + 1;
				CountStr = Count + "";
				if (Count * 30 >= PageCount) {
					getMessage("1", "30", strCity, REFRESH_DATA_FINISH,infoId);
					Count = 1;

				} else
					getMessage(CountStr, "30", strCity, REFRESH_DATA_FINISH,infoId);
				mListView.setSelection(0);
			}
		});
		mListView.addFooterView(view);

	}

	/**
	 * 获取同城推荐工人数据信息列表
	 * 
	 * @param Start
	 * @param Num
	 * @param strCity
	 */
	private void getMessage(String Page, String Size, String strCity,
			final int State,String Id) {
		// TODO Auto-generated method stub
		String urlCityName = null;
		try {
			urlCityName = URLEncoder.encode(strCity, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(HttpUtil.FindWork.replace("{Id}", Id)
				.replace("{Page}", Page).replace("{Size}", Size)
				.replace("{CityName}", urlCityName));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfoListModels.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));
						}

						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						} else {
							myAdapter.notifyDataSetChanged();
						}
					} else {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						T.showToast(FindWorkerActivity.this,
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

	private void CreateInfoOrderByLeader(String json) {

		RequestParams params = new RequestParams(
				HttpUtil.CreateInfoOrderByLeader);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(FindWorkerActivity.this,
								interfaceReturn.getMessage());
						itemS = null;
					} else {
						T.showToast(FindWorkerActivity.this,
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

	};

	public void callphone(final FindWorkInfoListModels workInfoListModels) {
		HintDialog.Builder builder = new HintDialog.Builder(
				FindWorkerActivity.this);
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
									+ workInfoListModels.getFindWorkInfo()
											.getMemberName().trim();
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse(str));
							startActivityForResult(intent, 123);
							// State = 1; // 判断作用
						} else {
							T.showToast(FindWorkerActivity.this, "请检查网络");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 123) {
			HintDialog.Builder builder = new HintDialog.Builder(
					FindWorkerActivity.this);
			builder.setMessage("是否谈好成交 ?");
			builder.setTitle(R.string.Hint);
			builder.setNegativeButton(R.string.Yes,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Order order = new Order();
							order.setInfoId(Integer.parseInt(infoId));
							order.setFromTelephone(UserName);
							order.setToTelephone(itemS.getMember()
									.getContactTel());
							order.setMemberId(MemberId);
							order.setToMemberId(itemS.getMember().getId());
							CreateInfoOrderByLeader(new Gson().toJson(order));
							// State = 0;
							dialog.dismiss();

						}
					});
			builder.setPositiveButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// State = 0;
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
