package com.qingzu.fragment;

import io.jchat.android.application.JChatDemoApplication;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import me.multi_image_selector.view.HorizontalListView;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import cn.jmessage.android.uikit.BaseFragment;
import cn.jmessage.android.uikit.chatting.ChatActivity;
import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import com.droid.SelectCityActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.adapter.CeShiAdapter;
import com.qingzu.adapter.CeShiAdapter.OnItemClickListener;
import com.qingzu.adapter.FirstHorizontaiListViewAdapter;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.AppNewsUrl;
import com.qingzu.entity.FindWorkInfoListModels;
import com.qingzu.entity.Information;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListNewsModel;
import com.qingzu.entity.Member;
import com.qingzu.entity.MemberMessage;
import com.qingzu.entity.News;
import com.qingzu.entity.Order;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
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
import com.qingzu.waterproof_work.MainActivity.OnCityListener;
import com.qingzu.waterproof_work.FifthMyMessageActivity;
import com.qingzu.waterproof_work.InviteWorkerListActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity;
import com.qingzu.waterproof_work.PointsActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.ReleaseProjectActivity;
import com.qingzu.waterproof_work.SelectRewardActivity;
import com.qingzu.waterproof_work.SelectRoleActivity;
import com.qingzu.waterproof_work.WantDayWorkerActivity;
import com.qingzu.waterproof_work.WebViewActivity;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX;
import android.view.WindowManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老板第一
 * 
 * @author Johnson
 * 
 */
public class BossFirstFragment extends Fragment implements OnClickListener {

	private View v = null;
	private MyTitleView cff_home_title = null;
	private ZListView mListView = null;
	private LayoutInflater layoutInflater;
	// private List<String> list = new ArrayList<String>();
	// private MyAdapter<String> myAdapter = null;
	private List<Conversation> mDatas = new ArrayList<Conversation>();
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private boolean IsRealName = false;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int ONLOOAD = 13;//显示heardview
	public static final int SELECT_CITY = 20;
	private static final int BIND_ADAPTER = 7;// 绑定适配器
	private String UserToken = null;

	private CeShiAdapter ceShiAdapter;
	private FirstHorizontaiListViewAdapter horizontaiListViewAdapter;
	private InterfaceReturn<ListNewsModel> interfaceReturn = new InterfaceReturn<ListNewsModel>();
	private InterfaceReturn<AppNewsUrl> URLinterfaceReturn = new InterfaceReturn<AppNewsUrl>();
	private AppNewsUrl newsUrl;
	private RecyclerView noScrollgridview;
	private Button material_bt = null; // 防水材料
	private View view ;
	private RelativeLayout rec_worker_rl = null;// 找工人
	private RelativeLayout rel_project_rl = null; // 发工程
	private RelativeLayout go_sign_rl = null; // 去签到
	private String strCity = null;

	private MyAdapter<FindWorkInfoListModels> myAdapter = null;
	private List<FindWorkInfoListModels> list = new ArrayList<FindWorkInfoListModels>();
	/**
	 * // 顶置商品
	 */

	private TextView topProduct_newsTitle = null;
	private TextView topProduct_subTitle = null;

	/**
	 * // 热销商品
	 */
	private TextView PopularProduct_newsTitle_1 = null;
	private TextView PopularProduct_subTitle_1 = null;

	/**
	 * // 热销商品
	 */
	private TextView PopularProduct_newsTitle_2 = null;
	private TextView PopularProduct_subTitle_2 = null;
	/**
	 * // 优质商品
	 */
	private TextView goodProduct_newsTitle = null;
	private TextView goodProduct_subTitle = null;

	/**
	 * // 劳保用品
	 */
	private TextView safetyProduct_newsTitle = null;
	private TextView safetyProduct_subTitle = null;

	/**
	 * // 防水工具
	 */
	private TextView toolProduct_newsTitle = null;
	private TextView toolProduct_subTitle = null;
	/**
	 * 平台资讯
	 */
	private TextView text_message = null;
	private LinearLayout platform_message_ll = null;

	private News popularProduct_news_1;
	private News popularProduct_news_2;
	private News topProduct_news;
	private News goodProduct_news;

	private News safetyProduct_news;
	private News toolProduct_news;

	private String MessageURL = null;
	private boolean IsOutUrl = false;
	private String InformationTitle = null;
	private String Message = null;
	private int messgitem = 0;
   
	public OnLoad mLoad;//数据加载完毕 ,显示heardview
	
	public  interface OnLoad{
		void State(int state);
	}
	
	public void setOnLoad(OnLoad load){
		load=mLoad;
	}
	
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
				
	//		startActivity(new Intent(packageContext, cls))	
		//		onCreate(null);	
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
		into();
		initView();
		getNewMessage();

		return v;
	}

	private void into() {
		// TODO Auto-generated method stub
		SharedPreferences preferences2 = getActivity().getSharedPreferences(
				"city", Activity.MODE_PRIVATE);
		strCity = preferences2.getString("city", "");

	}

	/**
	 * 获取是否有新消息
	 */
	private void getNewMessage() {
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
					} else {

						if (JMessageClient.getMyInfo() != null) {
							mDatas = JMessageClient.getConversationList();
							for (int i = 0; i < mDatas.size(); i++) {
								messgitem = messgitem
										+ mDatas.get(i).getUnReadMsgCnt();
							}
							if (messgitem > 0) {
								cff_home_title.setRightImg(getResources()
										.getDrawable(R.drawable.home_message2));
							} else {
								cff_home_title.setRightImg(getResources()
										.getDrawable(R.drawable.home_message));
							}
						} else {
							cff_home_title.setRightImg(getResources()
									.getDrawable(R.drawable.home_message));
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
		}
	};

	private void initView() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(getActivity());
		cff_home_title = (MyTitleView) v.findViewById(R.id.cff_home_title);
		mListView = (ZListView) v.findViewById(R.id.cff_lv_home);
		mListView.setPullLoadEnable(true);
		cff_home_title.setCenterImg(R.drawable.boss_1);

		SharedPreferences preferences1 = getActivity().getSharedPreferences(
				"city", Activity.MODE_PRIVATE);
		cff_home_title.setLeftText(preferences1.getString("city", ""));
		MainActivity.setOnCityListener(onCityListener);
		getMessage("1", "10", strCity, BIND_ADAPTER);
		// Information("7", "1", "1", "1");
		// addHead();
		myAdapter = new MyAdapter<FindWorkInfoListModels>(getActivity(), list,
				R.layout.layout_find_worker_item) {

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
						HintDialog.Builder builder = new HintDialog.Builder(
								getActivity());
						builder.setMessage("请点击招天工，发布工程后联系工人 ");
						builder.setTitle("温馨提示");
						builder.setNegativeButton(
								R.string.Yes,
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												getActivity(),
												WantDayWorkerActivity.class);
										intent.putExtra("id", "0.101");
										startActivity(intent);
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
				find_worker_rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});

			}
		};

		cff_home_title.setOnImgCenterClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),
						SelectRoleActivity.class));
			}
		});

		// cff_home_title.setOnCenterClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// startActivity(new Intent(getActivity(),
		// SelectRoleActivity.class));
		// }
		// });
		mListView.setAdapter(myAdapter);
		cff_home_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectCityActivity.class);
				intent.putExtra("code", SELECT_CITY);
				startActivityForResult(intent, SELECT_CITY);
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

				if (Tools.isConnectingToInternet()) {
					list.clear();
					getMessage("1", "10", strCity, REFRESH_DATA_FINISH);
					Count = 1;
				//	getNews();
					getAppNewsUrl();
				} else {
					T.showToast(getActivity(), "请检查网络");
				}
			}

			@Override
			public void onLoadMore() {

				if (Tools.isConnectingToInternet()) {
					Count = Count + 1;
					CountStr = Count + "";
					getMessage(CountStr, "10", strCity, LOAD_DATA_FINISH);
				} else {
					T.showToast(getActivity(), "请检查网络");
				}

			}
		});
		if (Tools.isConnectingToInternet()) {
			getNews();
			getAppNewsUrl();
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

	}

	public void getUserInfo(final Member member) {
		final Intent intent = new Intent();
		JMessageClient.getUserInfo(member.getUserName(),
				new GetUserInfoCallback() {
					@Override
					public void gotResult(final int status, String desc,
							final UserInfo userInfo) {
						// mLoadingDialog.dismiss();

						if (status == 0) {
							Conversation conv = Conversation
									.createSingleConversation(member
											.getUserName());
							if (conv != null) {
								String targetId = ((UserInfo) conv
										.getTargetInfo()).getUserName();
								intent.putExtra(JChatDemoApplication.TARGET_ID,
										targetId);
								intent.putExtra(
										JChatDemoApplication.TARGET_APP_KEY,
										conv.getTargetAppKey());
								Log.d("ConversationList",
										"Target app key from conversation: "
												+ conv.getTargetAppKey());
								// intent.putExtra(JChatDemoApplication.DRAFT,
								// member.getNickName());
								intent.setClass(getActivity(),
										ChatActivity.class);
								getActivity().startActivity(intent);
							}
							// getAdapter().getDraft(conv.getId())
							// Conversation conv =
							// Conversation.createSingleConversation(targetId);
							// if (!TextUtils.isEmpty(userInfo.getAvatar())) {
							// userInfo.getAvatarBitmap(new
							// GetAvatarBitmapCallback() {
							// @Override
							// public void gotResult(int status, String desc,
							// Bitmap bitmap) {
							// if (status == 0) {
							// //
							// mController.getAdapter().notifyDataSetChanged();
							// } else {
							// HandleResponseCode.onHandle(getActivity(),
							// status, false);
							// }
							// }
							// });
							// }

						} else if (status == 898002) {
							JMessageClient.register(member.getUserName(),
									"112233", new BasicCallback() {

										@Override
										public void gotResult(int arg0,
												String arg1) {
											// TODO Auto-generated method stub
											if (arg0 == 0) {
												JMessageClient.getUserInfo(
														member.getUserName(),
														new GetUserInfoCallback() {

															@Override
															public void gotResult(
																	int arg0,
																	String arg1,
																	UserInfo arg2) {
																// TODO
																// Auto-generated
																// method stub
																if (arg0 == 0) {

																	Conversation conv = Conversation
																			.createSingleConversation(member
																					.getUserName());
																	if (conv != null) {
																		String targetId = ((UserInfo) conv
																				.getTargetInfo())
																				.getUserName();
																		intent.putExtra(
																				JChatDemoApplication.TARGET_ID,
																				targetId);
																		intent.putExtra(
																				JChatDemoApplication.TARGET_APP_KEY,
																				conv.getTargetAppKey());
																		Log.d("ConversationList",
																				"Target app key from conversation: "
																						+ conv.getTargetAppKey());
																		// intent.putExtra(JChatDemoApplication.DRAFT,
																		// member.getNickName());
																		intent.setClass(
																				getActivity(),
																				ChatActivity.class);
																		getActivity()
																				.startActivity(
																						intent);
																	}
																}
															}
														});
											}
										}
									});
						}

						else {
							HandleResponseCode.onHandle(getActivity(), status,
									true);
						}
					}
				});
	}

	/**
	 * 按类别获取通知(平台资讯)get
	 * 
	 * @param TypeId
	 * @param State
	 * @param Page
	 * @param Size
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

						if (interfaceReturn.getResults().get(0).getIsOutUrl() == true) {
							// IsOutUrl = true;
							MessageURL = interfaceReturn.getResults().get(0)
									.getOutUrl();
						}
						// Message=interfaceReturn.getResults().get(0).getInformationRemark().replace("&lt;p&gt;",
						// "").replace("&lt;/p&gt;", "");

						Message = interfaceReturn.getResults().get(0)
								.getInformationRemark();
						text_message.setText(Message);
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

	/**
	 * 获取同城工人工长数据信息列表
	 * 
	 * @param Page
	 * @param Size
	 * @param strCity
	 * @param State
	 */

	private void getMessage(String Page, String Size, String strCity,
			final int State) {
		// TODO Auto-generated method stub
		String urlCityName = null;
		try {
			urlCityName = URLEncoder.encode(strCity, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(HttpUtil.FindWorkByPage
				.replace("{Page}", Page).replace("{Size}", Size)
				.replace("{CityName}", urlCityName));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				InterfaceReturn<FindWorkInfoListModels> interfaceReturn = new InterfaceReturn<FindWorkInfoListModels>();
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
						} else if (State == LOAD_DATA_FINISH) {
							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else if (State == BIND_ADAPTER) {
							mListView.setAdapter(myAdapter);
						} else {
							myAdapter.notifyDataSetChanged();
						}
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
						;
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
						
							addHead(Message, getActivity(), MessageURL,
									InformationTitle);
						}
						// System.out.println(newsTitle);
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

	private void hengping() {
		ViewGroup.LayoutParams params = noScrollgridview.getLayoutParams();
		// dishtype，welist为ArrayList
		WindowManager wm = getActivity().getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		// 图片之间的距离
		params.width = width / 20 * 20;
		Log.d("看看这个宽度", params.width + "" + 4);
		noScrollgridview.setLayoutParams(params);
		// 设置列数为得到的list长度
		// noScrollgridview.setNumColumns(4);
	}

	private void addHead(String tvTextMessage, final Context context,
			final String MessageURL, final String InformationTitle) {
		View view = layoutInflater.inflate(R.layout.boss_home_view, null);
		noScrollgridview = (RecyclerView) view
				.findViewById(R.id.recyclerView);
		 hengping();

		rec_worker_rl = (RelativeLayout) view.findViewById(R.id.rec_worker_rl);
		rel_project_rl = (RelativeLayout) view
				.findViewById(R.id.rel_project_rl);
		go_sign_rl = (RelativeLayout) view.findViewById(R.id.go_sign_rl);

		material_bt = (Button) view.findViewById(R.id.material_bt);
		platform_message_ll = (LinearLayout) view
				.findViewById(R.id.platform_message_ll);
		text_message = (TextView) view.findViewById(R.id.text_message);

		rec_worker_rl.setOnClickListener(this);
		rel_project_rl.setOnClickListener(this);
		go_sign_rl.setOnClickListener(this);
		text_message.setSelected(true);
		Information("7", "1", "1", "1");
		// text_message.setText(tvTextMessage);
		platform_message_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MessageURL != null) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), WebViewActivity.class);
					intent.putExtra("url", MessageURL);
					intent.putExtra("title", InformationTitle);
					getActivity().startActivity(intent);
				}

			}
		});

		material_bt.setOnClickListener(this);

		topProduct_news = interfaceReturn.getResults().get(0).getTopProduct()
				.get(0);
		popularProduct_news_1 = interfaceReturn.getResults().get(0)
				.getPopularProduct().get(0);
		popularProduct_news_2 = interfaceReturn.getResults().get(0)
				.getPopularProduct().get(1);
		goodProduct_news = interfaceReturn.getResults().get(0).getGoodProduct()
				.get(0);
		// goodProduct_news_2 = interfaceReturn.getResults().get(0)
		// .getGoodProduct().get(1);
		safetyProduct_news = interfaceReturn.getResults().get(0)
				.getSafetyProduct().get(0);
		toolProduct_news = interfaceReturn.getResults().get(0).getToolProduct()
				.get(0);

		// noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), WebViewActivity.class);
		// intent.putExtra("url", interfaceReturn.getResults().get(0)
		// .getMaterialBrand().get(position).getLinkUrl());
		// intent.putExtra("title", "intent");
		// getActivity().startActivity(intent);
		// }
		// });
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
		noScrollgridview.setLayoutManager(layoutManager);
		ceShiAdapter = new CeShiAdapter(interfaceReturn, getActivity());
		noScrollgridview.setAdapter(ceShiAdapter);
		ceShiAdapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent();
				 intent.setClass(getActivity(), WebViewActivity.class);
				 intent.putExtra("url", interfaceReturn.getResults().get(0)
				 .getMaterialBrand().get(position).getLinkUrl());
				 intent.putExtra("title", "intent");
				 getActivity().startActivity(intent);
			}
		});
//		if (view!=null) {
//			mListView.removeView(view);
//		
//		}
	
		mListView.addHeaderView(view);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.material_bt:
			intentWebViewActivity(newsUrl.getMaterial() + "", "防水材料");
			break;

		case R.id.rec_worker_rl:

			Intent intent = new Intent(getActivity(),
					WantDayWorkerActivity.class);
			// 添加传送数据
			intent.putExtra("id", "0.101");
			intent.putExtra("FROM", "MAIN_ACTIVITY");
			getActivity().startActivityForResult(intent,
					MainActivity.MAIN_ISSUE_WORK);
			// getCertification();
			break;
		case R.id.rel_project_rl:
			getActivity()
					.startActivityForResult(
							new Intent(getActivity(),
									ReleaseProjectActivity.class).putExtra(
									"FROM", "MAIN_ACTIVITY"),
							MainActivity.MAIN_ISSUE_WORK);
			// getCertification();
			break;
		case R.id.go_sign_rl:
			startActivity(new Intent(getActivity(), PointsActivity.class));
			break;
		default:
			break;
		}

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
									getActivity());
							builder.setMessage("您还没有实名认证,是否去认证 ?");
							builder.setTitle(R.string.Hint);
							builder.setNegativeButton(
									R.string.Yes,
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													getActivity(),
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
		if (resultCode == SELECT_CITY) {
			cff_home_title.setLeftText(data.getExtras().getString("city"));
			strCity = data.getExtras().getString("city");
			list.clear();
			getMessage("1", "10", strCity, BIND_ADAPTER);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		getNewMessage();
		v.invalidate();
		super.onResume();
	}
}