package com.qingzu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jmessage.android.uikit.BaseFragment;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

import com.qingzu.application.AppManager;
import com.qingzu.callback.OnPushStateListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.JPushListShow;
import com.qingzu.entity.MemberMessage;
import com.qingzu.entity.RecruitModel;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.FifthMyMessageActivity;
import com.qingzu.waterproof_work.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends BaseFragment {

	private String UserToken = null;
	private int state;
	private ListView mListView;
	private List<JPushListShow> list;
	private MyAdapter<JPushListShow> messageAdapter = null;
	private LayoutInflater layoutInflater;
	List<Conversation> mDatas = new ArrayList<Conversation>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_left_fragment, null);
		inViews( view);

		return view;

	}

	public void  addFooterView(){
		ImageView megss_ico = null; // 图标
		RelativeLayout push_show_tiem_re = null; // 整个item
		TextView push_item_name = null; // 名称 类别
		TextView push_item_content = null;// 推送内容
		TextView push_item_unread = null; // 未读条数
		 
		View view = layoutInflater.inflate(R.layout.push_show_listview_item, null);
		push_item_name = (TextView) view.findViewById(R.id.push_item_name);
		push_show_tiem_re = (RelativeLayout) view.findViewById(R.id.push_show_tiem_re);
		megss_ico = (ImageView) view.findViewById(R.id.megss_ico);
		push_item_content = (TextView) view.findViewById(R.id.push_item_content);
		push_item_unread = (TextView) view.findViewById(R.id.push_item_unread);
		megss_ico.setBackgroundResource(R.drawable.messagechat);
		push_item_name.setText("聊天消息");
	//	if (JMessageClient.getMyInfo()!=null) {
		 int messgitem=0;
			mDatas = JMessageClient.getConversationList();
			for (int i = 0; i < mDatas.size(); i++) {
				messgitem= messgitem+ mDatas.get(i).getUnReadMsgCnt();
			}
			if (messgitem!=0) {
				push_item_content.setText("您有新的消息");
				push_item_unread.setVisibility(View.VISIBLE);
				if (messgitem>99) {
					push_item_unread.setText("99+");
				}else
				push_item_unread.setText(messgitem+ "");
			} else{
				push_item_content.setText("您暂无新消息");
				push_item_unread.setVisibility(View.GONE);
			}			
	//	}

		push_show_tiem_re.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment newContent = null;
				newContent=new ConversationListFragment();
				switchFragment(newContent, "会话");
			}
		});
		mListView.addFooterView(view);
	}
	
	public void inViews(View view) {
		SharedPreferences sp = getActivity().getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = sp.getString("UserToken", "");
		state = sp.getInt("identity", 0);
		layoutInflater = LayoutInflater.from(getActivity());
		list = new ArrayList<JPushListShow>();
		switchFragment(new MessageSystemFragment(), "系统消息");
		mListView = (ListView) view.findViewById(R.id.left_frament_listview);
		getMessage();
		messageAdapter = new MyAdapter<JPushListShow>(getActivity(), list,
				R.layout.push_show_listview_item) {

			ImageView megss_ico = null; // 图标
			RelativeLayout push_show_tiem_re = null; // 整个item
			TextView push_item_name = null; // 名称 类别
			TextView push_item_content = null;// 推送内容
			TextView push_item_unread = null; // 未读条数

			@Override
			public void convert(ViewHolder view, int position,
					final JPushListShow item) {
				// TODO Auto-generated method stub
				push_item_name = view.getView(R.id.push_item_name);
				push_show_tiem_re = view.getView(R.id.push_show_tiem_re);
				megss_ico = view.getView(R.id.megss_ico);
				push_item_content = view.getView(R.id.push_item_content);
				push_item_unread = view.getView(R.id.push_item_unread);

				push_item_name.setText(item.getBizTypeName());
				if (item.getNewMsgCount() != 0) {
					push_item_unread.setVisibility(View.VISIBLE);
					if (item.getNewMsgCount()>99) {
						push_item_unread.setText("99+");
					}else
					push_item_unread.setText(item.getNewMsgCount() + "");
				} else
					push_item_unread.setVisibility(View.GONE);
				if (item.getListMessage().size() != 0) {
					push_item_content.setText(item.getListMessage().get(0)
							.getTitle());
				}

				switch (item.getBizTypeID()) {
				case 1:
					megss_ico.setBackgroundResource(R.drawable.messagesystem);
					break;
				case 3:
					if (state == 0) {
						push_show_tiem_re.setVisibility(View.GONE);
					} else if (state == 1 || state == 2) {
						push_show_tiem_re.setVisibility(View.VISIBLE);
					}
					
					megss_ico.setBackgroundResource(R.drawable.messagework);
					// push_item_name.setText(item.getBizTypeName());
				
					break;
				case 4:
					megss_ico.setBackgroundResource(R.drawable.messagenews);
					break;
				case 5:
					if (state == 0) {
						push_item_name.setText("用工消息订单");
					} else if (state == 1 || state == 2) {
						push_item_name.setText("干活消息订单");
					}
					megss_ico.setBackgroundResource(R.drawable.messageanswer);
					break;
				case 6:
					megss_ico.setBackgroundResource(R.drawable.messagegoods);
					break;
				default:
					break;
				}
				push_show_tiem_re.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Fragment newContent = null;
						String title = null;
						switch (item.getBizTypeID()) {
						case 1:
							newContent = new MessageSystemFragment();
							title = "系统消息";
							break;
						case 3:
							newContent = new MessageWorkFragment();
						
							break;
						case 4:
							newContent = new MessageAnswerFragment();
							title = "问答消息";
							break;
						case 5:
							newContent = new MessageNewFragment();
							if (state == 0) {
								title = "用工消息订单";
							} else if (state == 1 || state == 2) {
								title = "干活消息订单";
							}
							break;
						case 6:
							newContent = new MessageGoodsFragment();
							title = "商品订单";
							break;
						default:
							break;

						}
						if (newContent != null) {
							switchFragment(newContent, title);
						}
					}
				});
			}
		};
		mListView.setAdapter(messageAdapter);
		addFooterView();
		


	}

	private void getMessage() {
		RequestParams params = new RequestParams(
				HttpUtil.GetMyMemberMsg.replace("{topMsgCount}", "1"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<JPushListShow> interfaceReturn = new InterfaceReturn<JPushListShow>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						JPushListShow.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getBizTypeID() == 2) {
								list.remove(i);
							}
						}
						int a = list.size();
						System.out.println(a);
						messageAdapter.notifyDataSetChanged();
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
	 * 获取系统消息是否有新消息
	 */
	private void getNewMessage1() {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.NewMessage.replace(
				"{bizType}", "1"));
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

						String Counts = Tools.formatString(interfaceReturn
								.getTotalCount());
						if (!Counts.equals("0") && !Counts.equals("")) {
							// tvFifthMyMessageSystemCount
							// .setVisibility(View.VISIBLE);
							// tvFifthMyMessageSystemCount.setText(Counts);
						}

					} else {
						// T.showToast(getActivity(),
						// interfaceReturn.getMessage());
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
	 * 切换fragment
	 * 
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof FifthMyMessageActivity) {
			FifthMyMessageActivity fca = (FifthMyMessageActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// Fragment newContent = null;
	// String title = null;
	// switch (v.getId()) {
	//
	// case R.id.fifth_my_message_system_rl:
	// newContent = new MessageSystemFragment();
	// title = "系统消息";
	// break;
	// case R.id.fifth_my_message_work_rl:
	// newContent = new MessageWorkFragment();
	// if (state == 0) {
	// title = "用工消息";
	// } else if (state == 1 || state == 2) {
	// title = "干活消息";
	// }
	//
	// break;
	// case R.id.fifth_my_message_evaluate_rl:
	// newContent = new MessageEvaluateFragment();
	// title = "报名消息";
	// break;
	// case R.id.fifth_my_message_answer_rl:
	// newContent = new MessageAnswerFragment();
	// title = "问答消息";
	// break;
	// case R.id.fifth_my_news_order_rl:
	// newContent = new MessageNewFragment();
	// title = "消息订单";
	// break;
	// case R.id.fifth_my_goods_order_rl:
	// newContent = new MessageGoodsFragment();
	// title = "商品订单";
	// break;
	//
	// default:
	// break;
	//
	// }
	//
	// if (newContent != null) {
	// switchFragment(newContent, title);
	// }
	//
	// }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// getNewMessage1();
		// getNewMessage2();
		// getNewMessage3();
		// getNewMessage4();
		if (JMessageClient.getMyInfo()!=null) {
			mDatas = JMessageClient.getConversationList();
		}	
		super.onResume();
	}

}