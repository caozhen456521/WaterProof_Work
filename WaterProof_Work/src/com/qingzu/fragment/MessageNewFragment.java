package com.qingzu.fragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.Tools.ConnectionDetector;

import com.qingzu.callback.OnPushStateListener;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MemberMessage;

import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;

import com.qingzu.utils.tools.HintDialog;

import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import com.qingzu.waterproof_work.MainActivity;

import com.qingzu.waterproof_work.ConstructionDetailActivity;
import com.qingzu.waterproof_work.FifthMyMessageActivity;
import com.qingzu.waterproof_work.ForumActivity;
import com.qingzu.waterproof_work.HelpActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.ProjectPushActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.WebViewActivity;
import com.qingzu.waterproof_work.WorkOrderActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageNewFragment extends Fragment {

	private String UserToken = null;
	private View v = null;
	private ZListView mListView;

	private ShowProgressDialog dialog = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private ConnectionDetector cd;

	private List<MemberMessage> list = new ArrayList<MemberMessage>();// 数据源
	private MyAdapter<MemberMessage> messageAdapter = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新

	private LayoutInflater layoutInflater = null;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (messageAdapter != null) {
					messageAdapter.notifyDataSetChanged();
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
				if (messageAdapter != null) {
					messageAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {
					// mListView.onShowText();
					// System.out.println(Count);
					System.out.println("Count==========================="
							+ Count);
					System.out.println("PageCount==========================="
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.message_system, null);

		initView();

		return v;
	}

	private void initView() {

		cd = new ConnectionDetector(getActivity());

		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog();
		layoutInflater = LayoutInflater.from(getActivity());
		mListView = (ZListView) v.findViewById(R.id.ms_lv_message_system);
		mListView.setPullLoadEnable(true);

		messageAdapter = new MyAdapter<MemberMessage>(getActivity(), list,
				R.layout.member_message_item) {

			TextView Content = null;
			TextView Date = null;
			RelativeLayout Item = null;
			TextView Infostate = null;
			TextView Title = null;
			ImageView DeleteBtn = null;

			@Override
			public void convert(ViewHolder view, final int position,
					MemberMessage item) {
				// TODO Auto-generated method stub
				Content = view.getView(R.id.mm_tv_message);
				Date = view.getView(R.id.mm_tv_date);
				Item = view.getView(R.id.mm_rl_item);
				Infostate = view.getView(R.id.mm_tv_info_state);
				Title = view.getView(R.id.mm_tv_title);
				DeleteBtn = view.getView(R.id.mm_img_delete_btn);

				final MemberMessage message = list.get(position);

				Title.setText(message.getTitle() + ":");
				Content.setText(message.getAlert());
				Date.setText(message.getCreateTime());
				if (message.isRead()) {
					Infostate.setVisibility(View.GONE);
				} else {
					Infostate.setVisibility(View.VISIBLE);
				}

				DeleteBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						HintDialog.Builder builder = new HintDialog.Builder(
								getActivity());
						builder.setMessage(R.string.delete_info);
						builder.setTitle(R.string.Hint);
						builder.setNegativeButton(
								R.string.Yes,
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										DeleteItem(Tools.formatString(message
												.getId()), position);
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

					/**
					 * 删除单条消息
					 * 
					 * @param ID
					 */
					private void DeleteItem(String ID, final int Position) {
						// TODO Auto-generated method stub

						RequestParams params = new RequestParams(
								HttpUtil.DeleteSingle.replace("{msgID}", ID));
						params.addHeader("EZFSToken",
								Tools.getEZFSToken(UserToken));
						params.setCharset("utf-8");
						Xutils.Delete(params, new MyCallBack<String>() {
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String result) {
								InterfaceReturn<MemberMessage> interfaceReturn = new InterfaceReturn<MemberMessage>();
								interfaceReturn = InterfaceReturn.fromJson(
										result, MemberMessage.class);
								if (interfaceReturn != null) {
									if (interfaceReturn.isStatus()) {

										T.showToast(getActivity(),
												interfaceReturn.getMessage());
										list.remove(Position);
										messageAdapter.notifyDataSetChanged();

									} else {
										T.showToast(getActivity(),
												interfaceReturn.getMessage());
									}
								}
							}

							public void onError(Throwable ex,
									boolean isOnCallback) {
								Toast.makeText(x.app(), ex.getMessage(),
										Toast.LENGTH_LONG).show();
								if (ex instanceof HttpException) { // 网络错误
									HttpException httpEx = (HttpException) ex;
									int responseCode = httpEx.getCode();
									String responseMsg = httpEx.getMessage();
									String errorResult = httpEx.getResult();
									System.out.println("responseMsg:"
											+ responseMsg + "=====errorResult:"
											+ errorResult + "=====responseCode"
											+ responseCode);
									// ...
								} else { // 其他错误
									// ...
								}
							}
						});

					}

				});

				Item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!message.isRead()) {
							Read(Tools.formatString(list.get(position).getId()),
									Infostate);

						}
						Intent i = new Intent();
						switch (message.getMsgType()) {
						case 1:
							i.setClass(getActivity(), MainActivity.class);
							// i.putExtra("url", message.getContent());

							// i.putExtra("title", message.getTitle());
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;
						case 2:
							i.setClass(getActivity(), WebViewActivity.class);
							i.putExtra("url", message.getContent());

							// i.putExtra("title", message.getTitle());
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 5:
							i.setClass(getActivity(),
									RecruitDetailActivity.class);
							i.putExtra("recruit", message.getContent());
							i.putExtra("JPush", "JPush");
							// i.putExtra("title", message.getTitle());
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 6:
							// ForumActivity.class).putExtra("VIEWPAGE_S", 1)
							i.setClass(getActivity(), ForumActivity.class);
							i.putExtra("VIEWPAGE_S", 1);
							// i.putExtra("title", message.getTitle());
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 7:
							i.setClass(getActivity(), HelpActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 8:
							SharedPreferences preferences = getActivity()
									.getSharedPreferences("UserToken",
											Activity.MODE_PRIVATE);
							i.setClass(getActivity(), WorkOrderActivity.class);
							i.putExtra("IDENTITY", preferences.getInt("identity", 0));
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 9:
							i.setClass(getActivity(), WorkOrderActivity.class);
							i.putExtra("IDENTITY", 0);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 10:

							break;

						case 11:

							break;
						case 12:
							i.setClass(getActivity(), ProjectPushActivity.class);

							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(i);
							break;

						case 13:

							break;

						default:
							T.showToast(getActivity(), "数据类型错误");
							break;

						}
					}

					/**
					 * 标记是否已读
					 * 
					 * @param ID
					 * @param view
					 */
					private void Read(String ID, final TextView view) {
						// TODO Auto-generated method stub

						RequestParams params = new RequestParams(
								HttpUtil.SetReaded.replace("{msgID}", ID));
						params.addHeader("EZFSToken",
								Tools.getEZFSToken(UserToken));
						params.setCharset("utf-8");
						Xutils.Put(params, new MyCallBack<String>() {
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String result) {
								InterfaceReturn<MemberMessage> interfaceReturn = new InterfaceReturn<MemberMessage>();
								interfaceReturn = InterfaceReturn.fromJson(
										result, MemberMessage.class);
								if (interfaceReturn != null) {
									if (interfaceReturn.isStatus()) {

										view.setVisibility(View.GONE);

									} else {
										T.showToast(getActivity(),
												interfaceReturn.getMessage());
									}
								}
							}

							public void onError(Throwable ex,
									boolean isOnCallback) {
								Toast.makeText(x.app(), ex.getMessage(),
										Toast.LENGTH_LONG).show();
								if (ex instanceof HttpException) { // 网络错误
									HttpException httpEx = (HttpException) ex;
									int responseCode = httpEx.getCode();
									String responseMsg = httpEx.getMessage();
									String errorResult = httpEx.getResult();
									System.out.println("responseMsg:"
											+ responseMsg + "=====errorResult:"
											+ errorResult + "=====responseCode"
											+ responseCode);
									// ...
								} else { // 其他错误
									// ...
								}
							}
						});

					}

				});

			}

		};

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				if (cd.isConnectingToInternet()) {
					getMessage("1", "10", REFRESH_DATA_FINISH);
				} else {
					// 无网无数据,提示信息
					T.showToast(getActivity(), getString(R.string.LinkNetwork));
				}

				Count = 1;
				// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// TODO 加载更多
				Count = Count + 1;
				CountStr = Count + "";
				if (cd.isConnectingToInternet()) {
					getMessage(CountStr, "10", LOAD_DATA_FINISH);
				} else {
					// 无网无数据,提示信息
					T.showToast(getActivity(), getString(R.string.LinkNetwork));
				}

				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				messageAdapter.notifyDataSetChanged();
			}
		});

		// if (cd.isConnectingToInternet()) {
		// getMessage("1", "10", MainActivity.LOAD_DATA);
		// } else {
		// // 无网无数据,提示信息
		// T.showToast(getActivity(), getString(R.string.LinkNetwork));
		// }

		mListView.setAdapter(messageAdapter);

	}

	/**
	 * 加载数据
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	@SuppressWarnings("unchecked")
	public void getMessage(String PageIndex, String Size, final int State) {

		RequestParams params = new RequestParams(HttpUtil.MemberMessage
				.replace("{page}", PageIndex).replace("{pageSize}", Size)
				.replace("{bizType}", "5"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MemberMessage> interfaceReturn = new InterfaceReturn<MemberMessage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberMessage.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						MemberMessage message = null;
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							message = new MemberMessage();

							message.setAlert(interfaceReturn.getResults()
									.get(i).getAlert());
							message.setBizType(Integer.parseInt(Tools
									.formatString(interfaceReturn.getResults()
											.get(i).getBizType())));
							message.setContent(interfaceReturn.getResults()
									.get(i).getContent());

							message.setCreateTime(Tools
									.getTimestampString(Tools.strToDateT(Tools
											.formatString(interfaceReturn
													.getResults().get(i)
													.getCreateTime()))));
							message.setId(Integer.parseInt(Tools
									.formatString(interfaceReturn.getResults()
											.get(i).getId())));
							message.setMsgType(Integer.parseInt(Tools
									.formatString(interfaceReturn.getResults()
											.get(i).getMsgType())));
							message.setRead(Tools.formatBoolean(interfaceReturn
									.getResults().get(i).isRead()));
							message.setTaskID(Integer.parseInt(Tools
									.formatString(interfaceReturn.getResults()
											.get(i).getTaskID())));
							message.setTitle(Tools.formatString(interfaceReturn
									.getResults().get(i).getTitle()));
							message.setUserID(Tools
									.formatNumInt(interfaceReturn.getResults()
											.get(i).getUserID()));

							// list.add(interfaceReturn.getResults().get(i));
							list.add(message);

						}
						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						} else if (State == LOAD_DATA_FINISH) {
							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else {
							messageAdapter.notifyDataSetChanged();
						}
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						} else if (State == LOAD_DATA_FINISH) {
							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else {
							messageAdapter.notifyDataSetChanged();
						}
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

		if (PageIndex.equals("1")) {
			// 下拉刷新
		} else {
			mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
		}

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(getActivity(),
				R.style.progress_dialog, getString(R.string.Loading));

		return dialog;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		list.clear();
		if (cd.isConnectingToInternet()) {
			getMessage("1", "10", REFRESH_DATA_FINISH);
		} else {
			// 无网无数据,提示信息
			T.showToast(getActivity(), getString(R.string.LinkNetwork));
		}

		// Count = 1;

	}

}
