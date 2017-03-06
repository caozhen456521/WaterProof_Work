package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.qingzu.application.AppManager;
import com.qingzu.entity.GroupListModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.CustomDialog.Builder;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class TeamManageActivity extends Activity {

	protected static final int ADD_TEAM_MEMBER = 7;

	protected static final int DATA_FINISH = 8;

	private ExpandableListView atm_elv_list = null;

	private List<GroupListModel> list = null;
	private LayoutInflater layoutInflater;
	private MyTitleView atm_title = null;

	private String UserToken = null;
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case DATA_FINISH:
				if (adapter != null) {
					((BaseExpandableListAdapter) adapter)
							.notifyDataSetChanged();
				}
				break;
			}
		};
	};

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_manage);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = TeamManageActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(this);
		list = new ArrayList<GroupListModel>();
		atm_elv_list = (ExpandableListView) findViewById(R.id.atm_elv_list);
		atm_title = (MyTitleView) findViewById(R.id.atm_title);

		atm_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		atm_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(TeamManageActivity.this,
						AddTeamMemberActivity.class), ADD_TEAM_MEMBER);
			}
		});

		list = new ArrayList<GroupListModel>();
		// 设置默认图标为不显示状态
		atm_elv_list.setGroupIndicator(null);

		listViewAddHead();

		ListTeamMember(true);

		// atm_elv_list.setAdapter(adapter);
		// 设置一级item点击的监听器
		atm_elv_list.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// 刷新界面
				mhandler.sendEmptyMessage(DATA_FINISH);
				return false;
			}
		});

		// 设置二级item点击的监听器
		atm_elv_list.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// 刷新界面
				mhandler.sendEmptyMessage(DATA_FINISH);
				return false;
			}
		});
	}

	/**
	 * 获取当前登录用户的施工队队员列表信息
	 * 
	 * @author Johnson
	 */
	private void ListTeamMember(final boolean state) {
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
						list.addAll(interfaceReturn.getResults());
						if (state) {
							atm_elv_list.setAdapter(adapter);
						} else {
							if (!atm_elv_list.collapseGroup(0)) {
								atm_elv_list.expandGroup(0);
								atm_elv_list.collapseGroup(0);
							} else {
								atm_elv_list.expandGroup(0);
							}
						}

					} else {
						T.showToast(TeamManageActivity.this,
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
	 * 添加头部
	 * 
	 * @author Johnson
	 */
	private void listViewAddHead() {
		View view = layoutInflater.inflate(R.layout.layout_team_manage_head,
				null);
		ImageView ltmh_iv_create_grouping = (ImageView) view
				.findViewById(R.id.ltmh_iv_create_grouping);
		ImageView ltmg_iv_check_team_member = (ImageView) view
				.findViewById(R.id.ltmg_iv_check_team_member);
		ImageView ltmh_iv_grouping_manage = (ImageView) view
				.findViewById(R.id.ltmh_iv_grouping_manage);

		ltmh_iv_create_grouping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextDialog();
			}
		});
		ltmg_iv_check_team_member.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(TeamManageActivity.this,
						AuditTeamMemberActivity.class), ADD_TEAM_MEMBER);
			}
		});
		ltmh_iv_grouping_manage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(TeamManageActivity.this,
						ManageGroupActivity.class), ADD_TEAM_MEMBER);
			}
		});
		atm_elv_list.addHeaderView(view);
	}

	/**
	 * TextDialog
	 * 
	 * @author Johnson
	 */
	protected void TextDialog() {
		View view = layoutInflater.inflate(R.layout.layout_custom_dialog, null);
		final EditText lcd_et = (EditText) view.findViewById(R.id.lcd_et);
		lcd_et.setVisibility(View.VISIBLE);
		final Builder builder = new CustomDialog.Builder(
				TeamManageActivity.this)
				.setTitle("请输入要创建的分组名称")
				.setContentView(view)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (lcd_et.getText().toString().trim().length() <= 0) {
									T.showToast(TeamManageActivity.this,
											"请输入分组名称");
									return;
								}
								CreateGroups(lcd_et.getText().toString().trim());
								dialog.dismiss();
								return;
							}

						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
		builder.create().show();
	}

	/**
	 * 当前登录用户施工队创建组名信息 POST
	 * 
	 * @param GroupName
	 * @author Johnson
	 */
	protected void CreateGroups(String GroupName) {
		RequestParams params = new RequestParams(HttpUtil.CreateGroups);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("groupName", GroupName);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(TeamManageActivity.this,
								interfaceReturn.getMessage());
						list.clear();
						ListTeamMember(false);
					} else {
						T.showToast(TeamManageActivity.this,
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
		public View getGroupView(int groupPosition, boolean isExpanded,
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
				convertView.setTag(groupViewHolder);
			} else {
				groupViewHolder = (GroupViewHolder) convertView.getTag();
			}
			groupViewHolder.group_title.setText(list.get(groupPosition)
					.getGroup().getGroupName());
			groupViewHolder.ltmg_tv_number.setText(list.get(groupPosition)
					.getList().size()
					+ "");
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
		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
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
				childViewHolder.btChat=(Button) convertView.findViewById(R.id.ltmc_bt_chat);
				childViewHolder.ltmc_room=(RelativeLayout) convertView.findViewById(R.id.ltmc_room);
				convertView.setTag(childViewHolder);
			} else {
				childViewHolder = (ChildViewHolder) convertView.getTag();
			}
			if (list.get(groupPosition).getList().get(childPosition)
					.getMember().getContactName().length() > 0) {
				childViewHolder.group_title.setText(list.get(groupPosition)
						.getList().get(childPosition).getMember()
						.getContactName());
			} else {
				childViewHolder.group_title
						.setText(list.get(groupPosition).getList()
								.get(childPosition).getMember().getNickName());
			}
			ImageLoaderUtil.loadXUtilImage(list.get(groupPosition).getList()
					.get(childPosition).getMember().getMemberPhoto(),
					childViewHolder.ltmc_iv_member_photo);
			childViewHolder.ltmc_rb_evaluate.setRating((float) list
					.get(groupPosition).getList().get(childPosition)
					.getMember().getWorkerLevel());
			
			
			
			childViewHolder.ltmc_room.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (JMessageClient.getMyInfo()!=null) {
						if (list.get(groupPosition).getList()
						.get(childPosition).getMember().getUserName()!=null) {
							Tools.getUserInfo(list.get(groupPosition).getList()
									.get(childPosition).getMember().getUserName(), TeamManageActivity.this);	
						}}else{
							T.showToast(TeamManageActivity.this, "未知错误,请重新登录");
						}
					
				}
			});
			//聊天
			childViewHolder.btChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (JMessageClient.getMyInfo()!=null) {
					if (list.get(groupPosition).getList()
					.get(childPosition).getMember().getUserName()!=null) {
						Tools.getUserInfo(list.get(groupPosition).getList()
								.get(childPosition).getMember().getUserName(), TeamManageActivity.this);	
					}}else{
						T.showToast(TeamManageActivity.this, "未知错误,请重新登录");
					}
				}
			});

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
	}

	class ChildViewHolder {
		TextView group_title;
		ImageView ltmc_iv_member_photo;
		RatingBar ltmc_rb_evaluate;
		Button btChat;
		RelativeLayout ltmc_room;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case ADD_TEAM_MEMBER:
			list.clear();
			ListTeamMember(false);
			break;

		default:
			break;
		}
	};

}
