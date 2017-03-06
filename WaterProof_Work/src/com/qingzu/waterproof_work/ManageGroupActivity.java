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
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.CustomDialog.Builder;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ManageGroupActivity extends Activity {

	private ExpandableListView amg_elv_list = null;

	private List<GroupListModel> list = null;
	private LayoutInflater layoutInflater;
	private MyTitleView amg_title = null;
	private ArrayAdapter<String> spinnerAdapter = null;
	private List<String> spinnerList = null;
	private HashMap<String, String> spinnerMap = null;

	private String UserToken = null;
	private int MemberId;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_group);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = ManageGroupActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		MemberId = preferences.getInt("MemberId", 0);

		layoutInflater = LayoutInflater.from(this);
		list = new ArrayList<GroupListModel>();
		amg_elv_list = (ExpandableListView) findViewById(R.id.amg_elv_list);
		amg_title = (MyTitleView) findViewById(R.id.amg_title);

		amg_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(TeamManageActivity.ADD_TEAM_MEMBER);
				finish();
			}
		});

		list = new ArrayList<GroupListModel>();
		// 设置默认图标为不显示状态
		amg_elv_list.setGroupIndicator(null);

		ListTeamMember(true, 0);

		// amg_elv_list.setAdapter(adapter);
		// 设置一级item点击的监听器
		amg_elv_list.setOnGroupClickListener(new OnGroupClickListener() {

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
		amg_elv_list.setOnChildClickListener(new OnChildClickListener() {

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
	 * Dialog
	 * 
	 * @author Johnson
	 */
	protected void SpinnerDialog(final String MemberId, final int groupPosition) {
		View view = layoutInflater.inflate(R.layout.layout_custom_dialog, null);
		final Spinner lcd_spinner = (Spinner) view
				.findViewById(R.id.lcd_spinner);
		spinnerList = new ArrayList<String>();
		spinnerMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			spinnerList.add(list.get(i).getGroup().getGroupName());
			spinnerMap.put(list.get(i).getGroup().getGroupName(), list.get(i)
					.getGroup().getId()
					+ "");
		}
		spinnerAdapter = new ArrayAdapter<String>(ManageGroupActivity.this,
				android.R.layout.simple_spinner_item, spinnerList);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lcd_spinner.setAdapter(spinnerAdapter);
		lcd_spinner.setVisibility(View.VISIBLE);
		final Builder builder = new CustomDialog.Builder(
				ManageGroupActivity.this)
				.setTitle("请选择分组")
				.setContentView(view)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								UpdateTeamMember(MemberId, spinnerMap
										.get(lcd_spinner.getSelectedItem()),
										groupPosition);
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
	 * 当前登录用户编辑施工队队员信息
	 * 
	 * @param MemberId
	 * @param GroupId
	 * @author Johnson
	 */
	protected void UpdateTeamMember(String MemberId, String GroupId,
			final int groupPosition) {
		RequestParams params = new RequestParams(HttpUtil.UpdateTeamMember);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("TeamId", MainActivity.constructionTeam.getId()
				+ "");
		params.addBodyParameter("MemberId", MemberId);
		params.addBodyParameter("GroupId", GroupId);
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<GroupListModel> interfaceReturn = new InterfaceReturn<GroupListModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						GroupListModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ManageGroupActivity.this,
								interfaceReturn.getMessage());
						list.clear();
						ListTeamMember(false, groupPosition);
					} else {
						T.showToast(ManageGroupActivity.this,
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
						list.addAll(interfaceReturn.getResults());
						if (state) {
							amg_elv_list.setAdapter(adapter);
						} else {
							// ((BaseExpandableListAdapter) adapter)
							// .notifyDataSetChanged();
							if (!amg_elv_list.collapseGroup(groupPosition)) {
								amg_elv_list.expandGroup(groupPosition);
								amg_elv_list.collapseGroup(groupPosition);
							} else {
								amg_elv_list.expandGroup(groupPosition);
							}
						}
					} else {
						T.showToast(ManageGroupActivity.this,
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
	 * 根据分组编号ID删除施工队分组信息
	 * 
	 * @author Johnson
	 */
	private void DeleteGroupsByID(int Id) {
		RequestParams params = new RequestParams(
				HttpUtil.DeleteGroupsByID.replace("{Id}", Id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Delete(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ManageGroupActivity.this, "分组删除成功");
						list.clear();
						ListTeamMember(false, 0);
					} else {
						T.showToast(ManageGroupActivity.this,
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
	 * 当前登录用户（工长）辞退队员对象实体记录信息 POST
	 * 
	 * @param MemberId
	 *            31167 16
	 * @author Johnson
	 */
	private void LeaveTeamRecordByLeader(int MemberId, final int groupPosition) {
		RequestParams params = new RequestParams(
				HttpUtil.LeaveTeamRecordByLeader);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("MemberId", MemberId + "");
		params.addBodyParameter("TeamId", MainActivity.constructionTeam.getId()
				+ "");
		params.addBodyParameter("TypeId", 1 + "");
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ManageGroupActivity.this, "删除成功");
						list.clear();
						ListTeamMember(false, groupPosition);
					} else {
						T.showToast(ManageGroupActivity.this,
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
				groupViewHolder.ltmg_tv_number.setVisibility(View.GONE);
				groupViewHolder.ltmg_iv_delete
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new CustomDialog.Builder(
										ManageGroupActivity.this)
										.setTitle("提示")
										.setMessage("是否删除分组")
										.setPositiveButton(
												R.string.Yes,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {

														DeleteGroupsByID(list
																.get(groupPosition)
																.getGroup()
																.getId());

														dialog.dismiss();
														return;
													}

												})
										.setNegativeButton(
												R.string.No,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														dialog.dismiss();
													}
												}).create().show();

							}
						});
				if (list.get(groupPosition).getGroup().isDefault()) {
					groupViewHolder.ltmg_iv_delete.setVisibility(View.GONE);
				} else {
					groupViewHolder.ltmg_iv_delete.setVisibility(View.VISIBLE);
				}
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
				childViewHolder.ltmc_bt_chat=(Button) convertView.findViewById(R.id.ltmc_bt_chat);
				convertView.setTag(childViewHolder);
			} else {
				childViewHolder = (ChildViewHolder) convertView.getTag();
			}
			
			childViewHolder.ltmc_bt_chat.setVisibility(View.GONE);
			if (list.size() > 0) {
				if (list.get(groupPosition).getList().get(childPosition)
						.getMember().getId() != MemberId) {
					childViewHolder.ltmc_iv_delete_member
							.setVisibility(View.VISIBLE);
					childViewHolder.ltmc_iv_move_group
							.setVisibility(View.VISIBLE);
				} else {
					childViewHolder.ltmc_iv_delete_member
							.setVisibility(View.GONE);
					childViewHolder.ltmc_iv_move_group.setVisibility(View.GONE);
				}
				childViewHolder.ltmc_iv_delete_member
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new CustomDialog.Builder(
										ManageGroupActivity.this)
										.setTitle("提示")
										.setMessage("是否移除该队员")
										.setPositiveButton(
												R.string.Yes,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {

														LeaveTeamRecordByLeader(
																list.get(
																		groupPosition)
																		.getList()
																		.get(childPosition)
																		.getMember()
																		.getId(),
																groupPosition);

														dialog.dismiss();
														return;
													}

												})
										.setNegativeButton(
												R.string.No,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														dialog.dismiss();
													}
												}).create().show();
							}
						});
				childViewHolder.ltmc_iv_move_group
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								SpinnerDialog(list.get(groupPosition).getList()
										.get(childPosition).getMember().getId()
										+ "", groupPosition);
							}
						});
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
		Button  ltmc_bt_chat;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
			setResult(TeamManageActivity.ADD_TEAM_MEMBER);
	    }
		return super.onKeyDown(keyCode, event);
	}
}
