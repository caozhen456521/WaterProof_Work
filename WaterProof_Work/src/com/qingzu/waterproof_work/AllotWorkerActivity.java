package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;



import com.qingzu.application.AppManager;
import com.qingzu.entity.GroupListModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.TeamMemberListModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ManageGroupActivity.ChildViewHolder;
import com.qingzu.waterproof_work.ManageGroupActivity.GroupViewHolder;

import android.R.integer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class AllotWorkerActivity extends Activity {

	private ExpandableListView aaw_elv_list = null;

	private List<GroupListModel> list = null;
	private LayoutInflater layoutInflater;
	private MyTitleView aaw_title = null;
	private HashMap<String, String> checkedMap = null;
	private List<DispatchMember> dispatchMembers = null;

	private String UserToken = null;
	private int infoOrderId;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allot_worker);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = AllotWorkerActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(this);
		checkedMap = new HashMap<String, String>();
		dispatchMembers = new ArrayList<AllotWorkerActivity.DispatchMember>();
		list = new ArrayList<GroupListModel>();
		Intent intent = getIntent();
		infoOrderId = intent.getIntExtra("INFO_ORDER_ID", 0);
		aaw_elv_list = (ExpandableListView) findViewById(R.id.aaw_elv_list);
		aaw_title = (MyTitleView) findViewById(R.id.aaw_title);

		aaw_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(TeamManageActivity.ADD_TEAM_MEMBER);
				finish();
			}
		});
		aaw_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkedMap.size() > 0) {
					DispatchMember dispatchMember = null;
					for (String key : checkedMap.keySet()) {
						dispatchMember = new DispatchMember();
						dispatchMember.setInfoOrderId(infoOrderId);
						dispatchMember.setMemberId(Integer.parseInt(key));
						dispatchMembers.add(dispatchMember);
					}
					CreateDispatchMember(InterfaceReturn.getGson().toJson(
							dispatchMembers));
				}else {
					T.showToast(AllotWorkerActivity.this, "请要派遣的工人");
				}
			}
		});

		list = new ArrayList<GroupListModel>();
		// 设置默认图标为不显示状态
		aaw_elv_list.setGroupIndicator(null);

		ListTeamMember(true, 0);

		// aaw_elv_list.setAdapter(adapter);
		// 设置一级item点击的监听器
		aaw_elv_list.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// 刷新界面
				if (adapter != null) {
					((BaseExpandableListAdapter) adapter)
							.notifyDataSetChanged();
				}
				return false;
			}
		});

		// 设置二级item点击的监听器
		aaw_elv_list.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// 刷新界面
				if (adapter != null) {
					((BaseExpandableListAdapter) adapter)
							.notifyDataSetChanged();
				}
				return false;
			}
		});
	}

	/**
	 * 当前登录用户创建派遣施工人员信息 POST
	 * 
	 * @param json
	 * @author Johnson
	 */
	private void CreateDispatchMember(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateDispatchMember);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(AllotWorkerActivity.this,
							interfaceReturn.getMessage());
					finish();
				} else {
					T.showToast(AllotWorkerActivity.this,
							interfaceReturn.getMessage());
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
	 * 获取当前登录用户的施工队队员列表信息
	 * 
	 * @author Johnson
	 */
	private void ListTeamMember(final boolean state, final int groupPosition) {
		RequestParams params = new RequestParams(HttpUtil.ListTeamMember);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<GroupListModel> interfaceReturn = new InterfaceReturn<GroupListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						GroupListModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						GroupListModel groupListModel = null;
						List<TeamMemberListModel> listModels = null;
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							groupListModel = new GroupListModel();
							listModels = new ArrayList<TeamMemberListModel>();
							for (int j = 0; j < interfaceReturn.getResults()
									.get(i).getList().size(); j++) {
								if (interfaceReturn.getResults().get(i)
										.getList().get(j).getWorkState() == 1) {
									listModels.add(interfaceReturn.getResults()
											.get(i).getList().get(j));
								}
							}
							groupListModel.setGroup(interfaceReturn
									.getResults().get(i).getGroup());
							groupListModel.setList(listModels);
							list.add(groupListModel);
						}
						// list.addAll(interfaceReturn.getResults());
						if (state) {
							aaw_elv_list.setAdapter(adapter);
						} else {
							// ((BaseExpandableListAdapter) adapter)
							// .notifyDataSetChanged();
							if (!aaw_elv_list.collapseGroup(groupPosition)) {
								aaw_elv_list.expandGroup(groupPosition);
								aaw_elv_list.collapseGroup(groupPosition);
							} else {
								aaw_elv_list.expandGroup(groupPosition);
							}
						}
					} else {
						T.showToast(AllotWorkerActivity.this,
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

	/**
	 * 适配器
	 */
	ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {

		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {

		}

		@Override
		public void onGroupExpanded(int arg0) {

		}

		@Override
		public void onGroupCollapsed(int arg0) {

		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		/**
		 * 对一级标签进行设置
		 */
		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupViewHolder = null;
			if (convertView == null) {
				groupViewHolder = new GroupViewHolder();
				// 为视图对象指定布局
				convertView = layoutInflater.inflate(
						R.layout.layout_team_manage_group, null);
				groupViewHolder.group_title = (TextView) convertView
						.findViewById(R.id.ltmg_tv_title);
				groupViewHolder.ltmg_arrows = (ImageView) convertView
						.findViewById(R.id.ltmg_arrows);
				groupViewHolder.ltmg_tv_number = (TextView) convertView
						.findViewById(R.id.ltmg_tv_number);
				groupViewHolder.ltmg_iv_delete = (ImageView) convertView
						.findViewById(R.id.ltmg_iv_delete);
				convertView.setTag(groupViewHolder);
			} else {
				groupViewHolder = (GroupViewHolder) convertView.getTag();
			}
			if (list.size() > 0) {
				groupViewHolder.group_title.setText(list.get(groupPosition)
						.getGroup().getGroupName());
				groupViewHolder.ltmg_tv_number.setText(list.get(groupPosition)
						.getList().size()
						+ "");
			}
			if (isExpanded) {
				groupViewHolder.ltmg_arrows
						.setImageResource(R.drawable.below_arrows);
			} else {
				groupViewHolder.ltmg_arrows
						.setImageResource(R.drawable.rigth_arrows);
			}
			return convertView;
		}

		/**
		 * 获取一级标签的ID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		/**
		 * 获取一级标签总数
		 */
		@Override
		public int getGroupCount() {
			return list != null ? list.size() : 0;
		}

		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return list.get(groupPosition);
		}

		@Override
		public long getCombinedGroupId(long arg0) {
			return 0;
		}

		@Override
		public long getCombinedChildId(long arg0, long arg1) {
			return 0;
		}

		/**
		 * 获取一级标签下二级标签的总数
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return list.get(groupPosition).getList() != null ? list
					.get(groupPosition).getList().size() : 0;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildViewHolder childViewHolder = null;
			if (convertView == null) {
				childViewHolder = new ChildViewHolder();
				// 为视图对象指定布局
				convertView = layoutInflater.inflate(
						R.layout.layout_team_manage_child, null);
				childViewHolder.group_title = (TextView) convertView
						.findViewById(R.id.ltmc_tv_title);
				childViewHolder.ltmc_iv_member_photo = (ImageView) convertView
						.findViewById(R.id.ltmc_iv_member_photo);
				childViewHolder.ltmc_rb_evaluate = (RatingBar) convertView
						.findViewById(R.id.ltmc_rb_evaluate);
				childViewHolder.ltmc_iv_delete_member = (ImageView) convertView
						.findViewById(R.id.ltmc_iv_delete_member);
				childViewHolder.ltmc_iv_move_group = (ImageView) convertView
						.findViewById(R.id.ltmc_iv_move_group);
				childViewHolder.ltmc_checkbox = (CheckBox) convertView
						.findViewById(R.id.ltmc_checkbox);
				childViewHolder.ltmc_bt_chat=(Button) convertView.findViewById(R.id.ltmc_bt_chat);
				
				convertView.setTag(childViewHolder);
			} else {
				childViewHolder = (ChildViewHolder) convertView.getTag();
			}
			if (list.size() > 0) {
				childViewHolder.ltmc_iv_delete_member.setVisibility(View.GONE);
				childViewHolder.ltmc_iv_move_group.setVisibility(View.GONE);
				if (list.get(groupPosition).getList().get(childPosition)
						.getMember().getContactName().length() > 0) {
					childViewHolder.group_title.setText(list.get(groupPosition)
							.getList().get(childPosition).getMember()
							.getContactName());
				} else {
					childViewHolder.group_title.setText(list.get(groupPosition)
							.getList().get(childPosition).getMember()
							.getNickName());
				}
				ImageLoaderUtil
						.loadXUtilImage(
								list.get(groupPosition).getList()
										.get(childPosition).getMember()
										.getMemberPhoto(),
								childViewHolder.ltmc_iv_member_photo);
			}
			childViewHolder.ltmc_checkbox.setVisibility(View.VISIBLE);
			childViewHolder.ltmc_bt_chat.setVisibility(View.GONE);
			childViewHolder.ltmc_checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							if (arg1) {
								checkedMap.put(list.get(groupPosition)
										.getList().get(childPosition)
										.getMember().getId()
										+ "", list.get(groupPosition).getList()
										.get(childPosition).getMember()
										.getNickName()
										+ "");
							} else {
								checkedMap.remove(list.get(groupPosition)
										.getList().get(childPosition)
										.getMember().getId()
										+ "");
							}
						}
					});
			childViewHolder.ltmc_checkbox.setChecked(checkedMap.get(list
					.get(groupPosition).getList().get(childPosition)
					.getMember().getId()
					+ "") != null ? true : false);
			return convertView;
		}

		/**
		 * 获取二级标签的ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		/**
		 * 获取一级标签下二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return list.get(groupPosition).getList().get(childPosition);
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
	};

	class GroupViewHolder {
		TextView group_title;
		ImageView ltmg_arrows;
		TextView ltmg_tv_number;
		ImageView ltmg_iv_delete;
	}

	class ChildViewHolder {
		TextView group_title;
		ImageView ltmc_iv_member_photo;
		RatingBar ltmc_rb_evaluate;
		ImageView ltmc_iv_delete_member;
		ImageView ltmc_iv_move_group;
		CheckBox ltmc_checkbox;
		Button ltmc_bt_chat;
	}

	class DispatchMember {
		private int id;
		private int teamId;
		private int infoOrderId;
		private int memberId;
		private String memberName;
		private String issueTime;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getTeamId() {
			return teamId;
		}

		public void setTeamId(int teamId) {
			this.teamId = teamId;
		}

		public int getInfoOrderId() {
			return infoOrderId;
		}

		public void setInfoOrderId(int infoOrderId) {
			this.infoOrderId = infoOrderId;
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

		public String getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(String issueTime) {
			this.issueTime = issueTime;
		}

		public DispatchMember(int id, int teamId, int infoOrderId,
				int memberId, String memberName, String issueTime) {
			super();
			this.id = id;
			this.teamId = teamId;
			this.infoOrderId = infoOrderId;
			this.memberId = memberId;
			this.memberName = memberName;
			this.issueTime = issueTime;
		}

		public DispatchMember() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

}
