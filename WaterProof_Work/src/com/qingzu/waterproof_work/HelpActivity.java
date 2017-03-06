package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MyFeedBackListModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;

//import com.qingzu.utils.http.data.BaseGetDataController;
//import com.qingzu.utils.http.data.OnDataGetListener;
//import com.qingzu.utils.tools.JsonUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qiniu.android.http.ResponseInfo;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;


/**
 * 意见反馈
 * @author Administrator
 *
 */
public class HelpActivity extends Activity {

	private String UserToken = null;
	private MyTitleView mtvTitle = null;
	private ListView mListView = null;
	private EditText etText = null;
	private Button btSend = null;

	private RelativeLayout layout1 = null;
	private RelativeLayout layout2 = null;

	private ShowProgressDialog dialog = null;
	private HelpAdapter messageAdapter = new HelpAdapter();

	// private MyAdapter<MyFeedBackListModel> myAdapter = null;
	private int itemHeight = 0;
	private LayoutInflater layoutInflater = null;

	private ConnectionDetector cd;
	private List<MyFeedBackListModel> list = new ArrayList<MyFeedBackListModel>();

	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
		// initActionBar("测试");
	}

	private void initView() {
		cd = new ConnectionDetector(this);
		SharedPreferences preferences = HelpActivity.this.getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog();
		layoutInflater = LayoutInflater.from(this);
		mListView = (ListView) findViewById(R.id.feedback_reply_info);
		mtvTitle = (MyTitleView) findViewById(R.id.opsition_help_title);
		etText = (EditText) findViewById(R.id.help_et_text);
		btSend = (Button) findViewById(R.id.help_bt_send);
		layout1 = (RelativeLayout) findViewById(R.id.help_room);
		layout2 = (RelativeLayout) findViewById(R.id.re);

		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;

		layout1.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
				if (oldBottom != 0 && bottom != 0
						&& (oldBottom - bottom > keyHeight)) {
					mListView.setSelection(list.size() - 1);
				} else if (oldBottom != 0 && bottom != 0
						&& (bottom - oldBottom > keyHeight)) {

				}
			}
		});

		etText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					conceal(v);
				}
			}
		});

		layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				conceal(v);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				conceal(view);
			}
		});

		// mListView.setParentScrollView(scrollView);
		// mListView.setMaxHeight(2000);

		mtvTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etText.getText().toString().trim().equals("")
						&& etText.getText().toString().trim() != null) {
					String string = etText.getText().toString().trim();
					System.out.println(string);
					OpinionFeedback(etText.getText().toString().trim());

				} else {
					T.showToast(HelpActivity.this, "意见内容不能为空");
				}

			}
		});

		getMessage();
		mListView.setAdapter(messageAdapter);
		View view = (View) messageAdapter.getItem(0);
		// itemHeight = view.getHeight();

		// System.out.println(messageAdapter.getCount());

	}

	protected void conceal(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 当前登录用户提交意见反馈信息(发送按钮)
	 * 
	 * @param Remark
	 */
	private void OpinionFeedback(String Remark) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.CreateOpinionFeedback);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("Remark", Remark);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(HelpActivity.this,
								interfaceReturn.getMessage());
						list.clear();
						getMessage();
						mListView.deferNotifyDataSetChanged();
						etText.setText("");
					} else {
						T.showToast(HelpActivity.this,
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
	 * 当前登录用户获取自己提交的反馈信息
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void getMessage() {

		RequestParams params = new RequestParams(HttpUtil.OpinionFeedbackList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MyFeedBackListModel> interfaceReturn = new InterfaceReturn<MyFeedBackListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MyFeedBackListModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							MyFeedBackListModel help = new MyFeedBackListModel();
							if (interfaceReturn.getResults().get(i)
									.getFeedback() != null
									&& !interfaceReturn.getResults().get(i)
											.getFeedback().equals("")) {
//								String str = interfaceReturn.getResults().get(i).getFeedback().getRemark();
								
								
								help.setRemark(interfaceReturn.getResults()
										.get(i).getFeedback().getRemark());
								help.setStatus(1);
								help.setIssueTime(Tools
										.getTimestampString(Tools
												.strToDateT(interfaceReturn
														.getResults().get(i)
														.getFeedback()
														.getIssueTime())));

							}

							if (interfaceReturn.getResults().get(i).getReply() != null
									&& !interfaceReturn.getResults().get(i)
											.getReply().equals("")) {

								help.setRemark(interfaceReturn.getResults()
										.get(i).getReply().getRemark());
								help.setStatus(2);
								help.setIssueTime(Tools
										.getTimestampString(Tools
												.strToDateT(interfaceReturn
														.getResults().get(i)
														.getReply()
														.getIssueTime())));

							}
							list.add(help);

						}
						messageAdapter.notifyDataSetChanged();
						mListView.setSelection(list.size() - 1);

					} else {
						T.showToast(HelpActivity.this,
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

		// dialog.show();
		// BaseGetDataController controller = new BaseGetDataController(
		// HelpActivity.this, new OnDataGetListener() {
		//
		// @Override
		// @SuppressWarnings("unchecked")
		// public void onGetDataSuccesed(
		// ResponseInfo<JSONObject> status) {
		// dialog.dismiss();
		// String jsonStr = Tools.formatString(status.result);
		// HashMap<String, Object> data = JsonUtil
		// .parseJsonFinal(jsonStr);
		// String ResultStatus = Tools.formatString(data
		// .get("status"));
		// String str1 = Tools.formatString(data.get("message"));
		// if (ResultStatus.equals("true")) {
		// if (!data.get("results").equals("")) {
		// List<HashMap<String, Object>> resultsInfo = (List<HashMap<String,
		// Object>>) data
		// .get("results");
		// HashMap<String, Object> hashMap = null;
		// HashMap<String, Object> feedback = null;
		// HashMap<String, Object> reply = null;
		// Help help = null;
		// for (int i = 0; i < resultsInfo.size(); i++) {
		// help = new Help();
		// hashMap = resultsInfo.get(i);
		// if (hashMap.get("feedback") != null
		// && !hashMap.get("feedback").equals(
		// "")) {
		// feedback = (HashMap<String, Object>) hashMap
		// .get("feedback");
		// help.setId(Tools.formatNumInt(feedback
		// .get("id")));
		// help.setIsReply(Tools
		// .formatNumInt(feedback
		// .get("isReply")));
		// help.setIsSolve(Tools
		// .formatNumInt(feedback
		// .get("isSolve")));
		// help.setIssueTime(Tools.getTimestampString(Tools.strToDateT(Tools
		// .formatString(feedback
		// .get("issueTime")))));
		// help.setMemberId(Tools
		// .formatNumInt(feedback
		// .get("memberId")));
		// help.setMemberName(Tools
		// .formatString(feedback
		// .get("memberName")));
		// help.setOperatorId(Tools
		// .formatNumInt(feedback
		// .get("operatorId")));
		// help.setOperatorName(Tools
		// .formatString(feedback
		// .get("operatorName")));
		// help.setParentId(Tools
		// .formatNumInt(feedback
		// .get("parentId")));
		// help.setRemark(Tools
		// .formatString(feedback
		// .get("remark")));
		// help.setStatus(1);
		// help.setUpdateTime(Tools
		// .formatString(feedback
		// .get("updateTime")));
		// }
		// if (hashMap.get("reply") != null
		// && !hashMap.get("reply").equals("")) {
		// reply = (HashMap<String, Object>) hashMap
		// .get("reply");
		// help.setId(Tools.formatNumInt(reply
		// .get("id")));
		// help.setIsReply(Tools
		// .formatNumInt(reply
		// .get("isReply")));
		// help.setIsSolve(Tools
		// .formatNumInt(reply
		// .get("isSolve")));
		// help.setIssueTime(Tools.getTimestampString(Tools.strToDateT(Tools
		// .formatString(reply
		// .get("issueTime")))));
		// help.setMemberId(Tools
		// .formatNumInt(reply
		// .get("memberId")));
		// help.setMemberName(Tools
		// .formatString(reply
		// .get("memberName")));
		// help.setOperatorId(Tools
		// .formatNumInt(reply
		// .get("operatorId")));
		// help.setOperatorName(Tools
		// .formatString(reply
		// .get("operatorName")));
		// help.setParentId(Tools
		// .formatNumInt(reply
		// .get("parentId")));
		// help.setRemark(Tools.formatString(reply
		// .get("remark")));
		// help.setStatus(2);
		// help.setUpdateTime(Tools
		// .formatString(reply
		// .get("updateTime")));
		// }
		// list.add(help);
		// }
		// messageAdapter.notifyDataSetChanged();
		// mListView.setSelection(list.size() - 1);
		// }
		//
		// }
		//
		// }
		//
		// public void onGetDataFailed(HttpException status) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// T.showToast(HelpActivity.this,
		// getString(R.string.NetworkErrors));
		// }
		// });
		//
		// RequestParams params = new RequestParams();
		// params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		//
		// controller.getDatas(HttpUtil.OpinionFeedbackList, params);

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(HelpActivity.this,
				R.style.progress_dialog, getString(R.string.Loading));
		return dialog;
	}

	private LayoutParams fillParentLayoutParams = new LinearLayout.LayoutParams(

	LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private int index = -1;

	/**
	 * Help适配器
	 * 
	 * @author Johnson
	 * 
	 */
	class HelpAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (list != null && list.size() > 0) {
				return list.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			final int itemType = list.get(position).getStatus();

			if (itemType == 1) {
				convertView = layoutInflater.inflate(
						R.layout.feedback_info_item, null);

				viewHolder = new ViewHolder();
				viewHolder.Text = (TextView) convertView
						.findViewById(R.id.info_tv);
				viewHolder.Time = (TextView) convertView
						.findViewById(R.id.date_tv);
				convertView.setTag(viewHolder);

				MyFeedBackListModel help = list.get(position);
				 viewHolder.Text.setText(help.getRemark());
				 viewHolder.Time.setText(help.getIssueTime());

			} else if (itemType == 2) {
				convertView = layoutInflater.inflate(R.layout.reply_info_item,
						null);

				viewHolder = new ViewHolder();
				viewHolder.Text = (TextView) convertView
						.findViewById(R.id.info_tv);
				viewHolder.Time = (TextView) convertView
						.findViewById(R.id.date_tv);
				convertView.setTag(viewHolder);

				MyFeedBackListModel help = list.get(position);
				
				viewHolder.Text.setText(help.getRemark());
				viewHolder.Time.setText(help.getIssueTime());

			}

			// viewHolder = new ViewHolder();
			// viewHolder.Text = (TextView)
			// convertView.findViewById(R.id.info_tv);
			// viewHolder.Time = (TextView)
			// convertView.findViewById(R.id.date_tv);
			// convertView.setTag(viewHolder);
			//
			// MyFeedBackListModel help = list.get(position);
			// viewHolder.Text.setText(help.getRemark());
			// viewHolder.Time.setText(help.getIssueTime());
			return convertView;
		}

		/**
		 * 反馈
		 * 
		 * @author Johnson
		 * 
		 */
		private class ViewHolder {
			private static final int ITEM_VIEW_TYPE_1 = 1;
			TextView Text;
			TextView Time;
		}

		/**
		 * 问题
		 * 
		 * @author Johnson
		 * 
		 */
		private class ViewHolder2 {
			private static final int ITEM_VIEW_TYPE_2 = 2;
			TextView Text;
			TextView Time;
		}

	}

	class Help {
		private int Status;
		private int id;
		private int parentId;
		private int memberId;
		private String memberName;
		private String remark;
		private int isReply;
		private int isSolve;
		private String issueTime;
		private String updateTime;
		private int operatorId;
		private String operatorName;

		public int getStatus() {
			return Status;
		}

		public void setStatus(int status) {
			Status = status;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getParentId() {
			return parentId;
		}

		public void setParentId(int parentId) {
			this.parentId = parentId;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public String getMemberName() {
			return memberName;
		}

		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public int getIsReply() {
			return isReply;
		}

		public void setIsReply(int isReply) {
			this.isReply = isReply;
		}

		public int getIsSolve() {
			return isSolve;
		}

		public void setIsSolve(int isSolve) {
			this.isSolve = isSolve;
		}

		public String getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(String issueTime) {
			this.issueTime = issueTime;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

		public int getOperatorId() {
			return operatorId;
		}

		public void setOperatorId(int operatorId) {
			this.operatorId = operatorId;
		}

		public String getOperatorName() {
			return operatorName;
		}

		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}

	}

}
