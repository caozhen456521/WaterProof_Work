package com.qingzu.fragment;

import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jmessage.android.uikit.BaseFragment;
import cn.jpush.im.android.api.JMessageClient;

import com.amap.api.mapcore.util.bu;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.XListView;
import com.qingzu.custom.control.XListView.IXListViewListener;
import com.qingzu.entity.ExtensionNumber;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.InviteJoinTeamRecord;
import com.qingzu.entity.ListNewsModel;
import com.qingzu.entity.Order;
import com.qingzu.entity.OrderListModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.FindWorkerActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.SelectedPositionActivity;
import com.qingzu.waterproof_work.ShowMapAddressActivity;
import com.qingzu.waterproof_work.WorkerDetailActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 招工详情 基本信息
 * 
 * @author Administrator
 * 
 */
public class RecruitDetailBasciInfoFragment extends Fragment implements
		OnClickListener {
	private View v = null;
	private TextView tvRecruitArea = null;// 施工面积
	private TextView tvRecruitBrowerNumber = null;// 浏览次数
	private TextView tvRecruitConstructionNumber = null;// 已确认人数
	private TextView tvDaiQueRenNumber = null;// 待确认人数
	private TextView tvRecruitStartTime = null;// 开工时间
	private TextView tvRecruitConstructionPart = null;// 施工部位
	private TextView tvRecruitMaterialRequest = null;// 材料要求
	private RoundImageView rivRecruitImg = null;// 联系人头像
	private TextView tvRecruitContactName = null;// 联系人名字
	private TextView tvRecruitReleaseNumber = null;// 发布次数
	private Button recruit_chat_bt=null;//聊天
	private RelativeLayout recruit_info_rl=null;
	private RatingBar recruit_detail_assess_ratingbar = null;// 小星星
	private TextView tvRecruitPoints = null;// 分数
	private TextView tvRecruitWorkingPlace = null;// 工作地点
	private ImageView recruit_detail_img = null;// 地图
	private Button btOverRecruit = null;// 结束招工
	private Button btCompletedRecruit = null;// 完成招工
	private Button call_phone_bt = null; // 拨打电话
	private XListView mListView = null;// listview
	private RelativeLayout contact_worker_rl=null;
	private TextView contact_worker_tv = null;// 急需工人点这里
	private TextView wait_project_tv=null;//请等待审核工程
	private LinearLayout boss_ll = null;// 老板
	private LinearLayout worker_captain_ll = null;// 工人，工长
	private InterfaceReturn<ExtensionNumber> PHONGinterfaceReturn = new InterfaceReturn<ExtensionNumber>();
	private int State = 0; // 0:没拨打电话 1:拨打电话了
	private ExtensionNumber extensionNumber;
	private String id;
	private String UserToken = null;
	private RecruitmentInfo recruitmentInfo = null;
	private boolean fState = false;
	private int state = 0; // 0:老板 1:工长 2: 工人
	private InterfaceReturn<RecruitModel> interfaceReturn = new InterfaceReturn<RecruitModel>();
	private List<OrderListModel> list = new ArrayList<OrderListModel>();
	private MyAdapter<OrderListModel> myAdapter = null;
	private RecruitModel itemS = null;
    private String  chatUserName=null;
	// private LatLng latLng = new LatLng(34.341568, 108.940174);
	// private LatLng latLng1 = new LatLng(34.341568, 107.940174);
	// private String lat = null;
	// private String lon = null;
	private String UserName = null;
	private int MemberId;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int BIND_ADAPTER = 7;// 绑定适配器
	private ConnectionDetector cd;


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
		v = inflater.inflate(R.layout.fragment_recruit_detail_basic_info,
				container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		cd = new ConnectionDetector(getActivity());
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		UserName = preferences.getString("UserName", "");
		MemberId = preferences.getInt("MemberId", 0);
		state = preferences.getInt("identity", 0);
		fState = true;
		wait_project_tv=(TextView) v.findViewById(R.id.wait_project_tv);
		contact_worker_rl=(RelativeLayout) v.findViewById(R.id.contact_worker_rl);
		recruit_info_rl=(RelativeLayout) v.findViewById(R.id.recruit_info_rl);
		recruit_chat_bt=(Button) v.findViewById(R.id.recruit_chat_bt);
		tvRecruitArea = (TextView) v
				.findViewById(R.id.recruit_construcyion_area_tv);
		tvRecruitBrowerNumber = (TextView) v
				.findViewById(R.id.recruit_brower_number_tv);
		tvRecruitConstructionNumber = (TextView) v
				.findViewById(R.id.recruit_construction_number_tv);
		tvDaiQueRenNumber = (TextView) v
				.findViewById(R.id.dai_que_ren_number_tv);
		tvRecruitStartTime = (TextView) v
				.findViewById(R.id.recruit_construction_start_time_tv);
		tvRecruitConstructionPart = (TextView) v
				.findViewById(R.id.recruit_construction_part_tv);
		tvRecruitMaterialRequest = (TextView) v
				.findViewById(R.id.recruit_material_request_tv);
		rivRecruitImg = (RoundImageView) v.findViewById(R.id.riv_recruit_img);
		tvRecruitContactName = (TextView) v
				.findViewById(R.id.recruit_contact_name_tv);
		tvRecruitReleaseNumber = (TextView) v
				.findViewById(R.id.recruit_release_number_tv);
		recruit_detail_assess_ratingbar = (RatingBar) v
				.findViewById(R.id.recruit_detail_assess_ratingbar);
		tvRecruitPoints = (TextView) v.findViewById(R.id.recruit_points_tv);
		tvRecruitWorkingPlace = (TextView) v
				.findViewById(R.id.recruit_working_place_tv);
		recruit_detail_img = (ImageView) v
				.findViewById(R.id.recruit_detail_img);
		btOverRecruit = (Button) v.findViewById(R.id.over_recruit_bt);
		btCompletedRecruit = (Button) v.findViewById(R.id.completed_recruit_bt);
		call_phone_bt = (Button) v.findViewById(R.id.call_phone_bt);
		mListView = (XListView) v.findViewById(R.id.dai_que_ren__lv);
		contact_worker_tv = (TextView) v.findViewById(R.id.contact_worker_tv);
		boss_ll = (LinearLayout) v.findViewById(R.id.boss_ll);
		worker_captain_ll = (LinearLayout) v
				.findViewById(R.id.worker_captain_ll);
		
		recruit_info_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (JMessageClient.getMyInfo()!=null) {
					if (chatUserName!=null) {
						Tools.getUserInfo(chatUserName, getActivity());	
					}	
				}else {
					T.showToast(getActivity(), "未知错误,请重新登录");
				}
			}
		});
	
		//聊天
		recruit_chat_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (JMessageClient.getMyInfo()!=null) {
					if (chatUserName!=null) {
						Tools.getUserInfo(chatUserName, getActivity());	
					}	
				}else {
					T.showToast(getActivity(), "未知错误,请重新登录");
				}
		//		
			}
		});

		id = RecruitDetailActivity.id;
		
		if (state == 0) {
			RecruitmentMember(id, "1", "10", BIND_ADAPTER);
			boss_ll.setVisibility(View.GONE);
			worker_captain_ll.setVisibility(View.VISIBLE);
		}
		if (state == 1 || state == 2) {
			boss_ll.setVisibility(View.VISIBLE);
			worker_captain_ll.setVisibility(View.GONE);
		}

		myAdapter = new MyAdapter<OrderListModel>(getActivity(), list,
				R.layout.layout_dai_que_ren_worker_item) {
			RelativeLayout no_confirmed_room_rl = null;
			RoundImageView workerPhoto;
			TextView contactName;
			RatingBar rbNumbr;
			ImageView accept;
			ImageView refuse;

			@Override
			public void convert(ViewHolder view, final int position,
					OrderListModel item) {
				// TODO Auto-generated method stub
				no_confirmed_room_rl = view.getView(R.id.no_confirmed_room_rl);
				workerPhoto = view.getView(R.id.ltmc_iv_member_photo);
				contactName = view.getView(R.id.ltmc_tv_title);
				rbNumbr = view.getView(R.id.ltmc_rb_evaluate);
				accept = view.getView(R.id.ltmc_iv_move_group);
				refuse = view.getView(R.id.ltmc_iv_delete_member);

				ImageLoader
						.getInstance()
						.displayImage(
								list.get(position).getMember().getMemberPhoto(),
								workerPhoto,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

				contactName.setText(list.get(position).getMember()
						.getContactName());

				String str = list.get(position).getMember().getContactName();
				final String workerId = item.getMember().getId()+"";
                //System.out.println(workerId);
				rbNumbr.setRating((float) Double.parseDouble(Tools
						.formatString(list.get(position).getMember()
								.getWorkerLevel())));

				accept.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						accept(list.get(position).getInfoOrder().getId()+"",position);//当前登录用户(老板)确认天工订单
					}

				});

				refuse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						refuse(list.get(position).getInfoOrder().getId()+"",position);

					}

					
				});

				no_confirmed_room_rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(getActivity(),
								WorkerDetailActivity.class);
						intent.putExtra("id", workerId);
						intent.putExtra("InfoId", id);
						System.out.println(workerId);
						startActivity(intent);
						

					}
				});

			}

		};
		
		
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				RecruitmentMember(id, "1", "10", REFRESH_DATA_FINISH);
				Count = 1;
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				RecruitmentMember(id, CountStr, "10", LOAD_DATA_FINISH);
			}
		});

		mListView.setAdapter(myAdapter);
//		Tools.setListViewHeightBasedOnChildren(mListView);
//		setListViewHeightBasedOnChildren(mListView);(在setAdapter后调用自定义的方法)

		call_phone_bt.setOnClickListener(this);
		contact_worker_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getActivity(),
						FindWorkerActivity.class);
				intent.putExtra("InfoId", id);
				startActivity(intent);

				// startActivity(new Intent(getActivity(),
				// FindWorkerActivity.class));
			}
		});

		recruit_detail_img.setOnClickListener(this);
		btOverRecruit.setOnClickListener(this);

		getMessage(id);

	}
/**
 * 当前登录用户(老板)确认天工订单
 * @param ID
 */
	private void accept(String ID,final int position) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.PassInfoOrder.replace("{ID}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<Order> interfaceReturn = new InterfaceReturn<Order>();
				interfaceReturn = InterfaceReturn.fromJson(
						result, Order.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(),
								interfaceReturn.getMessage());
						list.remove(position);
						myAdapter.notifyDataSetChanged();
					
					} else {
						T.showToast(getActivity(),
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
	 * 当前登录用户根据ID删除承接天工订单信息
	 * @param id
	 * @param position
	 */
	private void refuse(String ID, final int position) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.DeleteInfoOrderByID.replace("{ID}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Delete(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<Order> interfaceReturn = new InterfaceReturn<Order>();
				interfaceReturn = InterfaceReturn.fromJson(
						result, Order.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(),
								interfaceReturn.getMessage());
						list.remove(position);
						myAdapter.notifyDataSetChanged();
					
					} else {
						T.showToast(getActivity(),
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
	 * 当前登录用户获取找天工 接单人数据信息列表(获取待确认天工)
	 * 
	 * @param Id
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void RecruitmentMember(String Id, String Page, String Size,
			final int State) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.RecruitmentMember
				.replace("{Id}", Id).replace("{Page}", Page)
				.replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<OrderListModel> interfaceReturn = new InterfaceReturn<OrderListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						OrderListModel.class);
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
						} else if(State == BIND_ADAPTER){
							mListView.setAdapter(myAdapter);
						}else {
							myAdapter.notifyDataSetChanged();
						}
					} else {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						 T.showToast(getActivity(),
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
	 * 根据ID获取找天工信息
	 * 
	 * @param Id
	 */
	private void getMessage(String Id) {
		RequestParams params = new RequestParams(
				HttpUtil.RecruitmentInfoByID.replace("{ID}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						RecruitModel.class);

				recruitmentInfo = new RecruitmentInfo();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						chatUserName= interfaceReturn.getResults().get(0).getMember().getUserName();
						itemS = interfaceReturn.getResults().get(0);
						tvRecruitArea.setText(interfaceReturn.getResults()
								.get(0).getRecruitmentInfo()
								.getConstructionArea()
								+ "平米");
						tvRecruitBrowerNumber.setText("浏览次数："
								+ interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getBrowseCount()
								+ "");

						String str = interfaceReturn.getResults().get(0)
								.getRecruitmentInfo().getBrowseCount()
								+ "";

						tvRecruitConstructionNumber.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getDealNumber()
								+ "/"
								+ interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getPeopleNumber()
								+ "");
						tvDaiQueRenNumber.setText("待确认天工:"
								+ interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getApplyNumber()
								+ "");

						String starttime = dateToStrLong(strToDate(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getStartTime()));
						tvRecruitStartTime.setText(starttime);
						tvRecruitConstructionPart.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getConstructionParts());
						tvRecruitMaterialRequest.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getClaimMaterial());

						ImageLoader.getInstance().displayImage(
								interfaceReturn.getResults().get(0).getMember()
										.getMemberPhoto(),
								rivRecruitImg,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

						tvRecruitContactName.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());
						tvRecruitReleaseNumber.setText(interfaceReturn
								.getResults().get(0).getBossInfo()
								.getEmployCount()
								+ "");
						recruit_detail_assess_ratingbar
								.setRating((float) Double.parseDouble(Tools
										.formatString(interfaceReturn
												.getResults().get(0)
												.getMember().getBossLevel())));
						tvRecruitPoints.setText(Tools
								.formatString(interfaceReturn.getResults()
										.get(0).getMember().getBossLevel())
								+ "分");
						tvRecruitWorkingPlace.setText(interfaceReturn
								.getResults().get(0).getRecruitmentInfo()
								.getAddress());
						recruitmentInfo = interfaceReturn.getResults().get(0)
								.getRecruitmentInfo();

						String str9 = interfaceReturn.getResults().get(0)
								.getRecruitmentInfo().getLon();

						if (!interfaceReturn.getResults().get(0)
								.getRecruitmentInfo().getLon().equals("0.0")
								|| !interfaceReturn.getResults().get(0)
										.getRecruitmentInfo().getLat()
										.equals("0.0")) {
							recruit_detail_img.setVisibility(View.VISIBLE);

						}

						if (state != 0) {
							btOverRecruit.setVisibility(View.GONE);
							if (interfaceReturn.getResults().get(0)
									.getRecruitmentInfo().getInfoState() == 2) {
								btCompletedRecruit.setVisibility(View.VISIBLE);
								call_phone_bt.setVisibility(View.GONE);

							}
							if (interfaceReturn.getResults().get(0)
									.getRecruitmentInfo().getInfoState() == 1) {
								btCompletedRecruit.setVisibility(View.GONE);
								call_phone_bt.setVisibility(View.VISIBLE);
							}
						} else if (state == 0) {
							String infoState = interfaceReturn.getResults()
									.get(0).getRecruitmentInfo().getInfoState()
									+ "";
							if (infoState.equals("2")) {
								btOverRecruit.setVisibility(View.GONE);
								btCompletedRecruit.setVisibility(View.VISIBLE);
								call_phone_bt.setVisibility(View.GONE);
								contact_worker_rl.setVisibility(View.GONE);

							}
							if (infoState.equals("0")) {
								wait_project_tv.setVisibility(View.VISIBLE);
								contact_worker_rl.setVisibility(View.GONE);
								btOverRecruit.setVisibility(View.GONE);
								btCompletedRecruit.setVisibility(View.GONE);
								call_phone_bt.setVisibility(View.GONE);
							}
						}

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
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
		case R.id.recruit_detail_img:

			Intent intent = new Intent(getActivity(),
					ShowMapAddressActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(SelectedPositionActivity.SEARCH,
					recruitmentInfo);
			intent.putExtra("type", SelectedPositionActivity.SEARCH);
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.over_recruit_bt:
			HintDialog.Builder builder = new HintDialog.Builder(getActivity());
			builder.setTitle(R.string.Hint);
			builder.setMessage("是否结束招工!");
			builder.setPositiveButton(R.string.Yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							EndRecruitment(id);
							dialog.dismiss();
						}

						private void EndRecruitment(String ID) {
							// TODO Auto-generated method stub
							RequestParams params = new RequestParams(
									HttpUtil.EndRecruitmentInfoById.replace(
											"{ID}", ID));
							params.addHeader("EZFSToken",
									Tools.getEZFSToken(UserToken));
							Xutils.Put(params, new MyCallBack<String>() {
								@SuppressWarnings("unchecked")
								@Override
								public void onSuccess(String result) {
									InterfaceReturn<RecruitmentInfo> interfaceReturn = new InterfaceReturn<RecruitmentInfo>();
									interfaceReturn = InterfaceReturn.fromJson(
											result, RecruitmentInfo.class);
									if (interfaceReturn != null) {
										if (interfaceReturn.isStatus()) {
											T.showToast(getActivity(),
													interfaceReturn
															.getMessage());

											//
											// btOverRecruit.setVisibility(View.GONE);
											// btCompletedRecruit.setVisibility(View.VISIBLE);

										} else {
											T.showToast(getActivity(),
													interfaceReturn
															.getMessage());
										}
									}
								}

								@Override
								public void onError(Throwable ex,
										boolean isOnCallback) {
									Toast.makeText(x.app(), ex.getMessage(),
											Toast.LENGTH_LONG).show();
									if (ex instanceof HttpException) { // 网络错误
										HttpException httpEx = (HttpException) ex;
										int responseCode = httpEx.getCode();
										String responseMsg = httpEx
												.getMessage();
										String errorResult = httpEx.getResult();
										System.out.println("responseMsg:"
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
			break;
		case R.id.call_phone_bt:
			if (itemS != null) {
				if (Tools.isConnectingToInternet()) {
					callPhone(itemS);
//					getExtensionNumberByInfoIdandInfoType(itemS
//							.getRecruitmentInfo().getId() + "", "1");
				} else {
					T.showToast(getActivity(), "请检查网络");
				}

			} else
				T.showToast(getActivity(), "订单数据有误");
			break;

		default:
			break;
		}
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
													// + URLEncoder
													// .encode("#");
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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
	
	
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		list.clear();
//		if (cd.isConnectingToInternet()) {
//			RecruitmentMember(id, "1", "10", REFRESH_DATA_FINISH);
//		} else {
//			// 无网无数据,提示信息
//			T.showToast(getActivity(), getString(R.string.LinkNetwork));
//		}
//
//	}

}
