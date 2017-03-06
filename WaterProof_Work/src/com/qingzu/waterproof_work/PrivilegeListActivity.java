package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.CityModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProvinceModel;
import com.qingzu.entity.Tag;
import com.qingzu.entity.VIPServiceItem;
import com.qingzu.entity.VIPServiceItemListModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

public class PrivilegeListActivity extends Activity {

	private MyTitleView apl_title = null;
	private ExpandableListView apl_listView = null;
	private SharedPreferences preferences = null;
	private List<VIPServiceItemListModel> list = null;
	private ExampleAdapter adapter = null;
	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privilege_list);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		preferences = getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		list = new ArrayList<VIPServiceItemListModel>();
		apl_title = (MyTitleView) findViewById(R.id.apl_title);
		apl_listView = (ExpandableListView) findViewById(R.id.apl_listView);
		apl_listView.setGroupIndicator(null);
		apl_listView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		apl_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		VIPServiceItemList();

	}

	/**
	 * 当前登录用户获取vip服务项列表信息 GET
	 * 
	 * @author Johnson
	 */
	private void VIPServiceItemList() {
		RequestParams params = new RequestParams(HttpUtil.VIPServiceItemList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<VIPServiceItemListModel> interfaceReturn = new InterfaceReturn<VIPServiceItemListModel>();
				interfaceReturn = interfaceReturn.fromJson(result,
						VIPServiceItemListModel.class);
				if (interfaceReturn.isStatus()) {
					list.addAll(interfaceReturn.getResults());
					adapter = new ExampleAdapter(PrivilegeListActivity.this);
					adapter.setData(list);
					apl_listView.setAdapter(adapter);
				} else {
					T.showToast(PrivilegeListActivity.this,
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
				}
			}
		});
	}

	private static class ChildHolder {
		RelativeLayout room;
		TextView title;
		CheckBox select;
	}

	private static class GroupHolder {
		TextView title;
		ImageView arrows;
	}

	private class ExampleAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater;

		private List<VIPServiceItemListModel> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<VIPServiceItemListModel> items) {
			this.items = items;
		}

		@Override
		public VIPServiceItem getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).getList().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public VIPServiceItemListModel getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items != null ? items.size() : 0;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder holder;
			VIPServiceItemListModel item = getGroup(groupPosition);
			if (convertView == null) {
				holder = new GroupHolder();
				convertView = inflater.inflate(
						R.layout.layout_serve_city_group_item, parent, false);
				holder.title = (TextView) convertView
						.findViewById(R.id.lscgi_tv_province);
				holder.arrows = (ImageView) convertView
						.findViewById(R.id.lscgi_iv_arrows);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}
			holder.title.setText(item.getVipServiceItem().getServiceName());
			if (isExpanded) {
				holder.arrows.setBackgroundResource(R.drawable.below_arrows);
			} else {
				holder.arrows.setBackgroundResource(R.drawable.rigth_arrows);
			}
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			final ChildHolder holder;
			final VIPServiceItem item = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ChildHolder();
				convertView = inflater.inflate(
						R.layout.layout_serve_city_child_item, parent, false);
				holder.title = (TextView) convertView
						.findViewById(R.id.lscci_tv_city);
				holder.select = (CheckBox) convertView
						.findViewById(R.id.lscci_cb_select);
				holder.room = (RelativeLayout) convertView
						.findViewById(R.id.lscci_room);
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}
			holder.title.setText(item.getServiceName());
			holder.room.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.putExtra("SERVICE_ID", item.getId());
					intent.putExtra("SERVICE_NAME", item.getServiceName());
					setResult(VipCenterActivity.SELECT_PRIVILEGE, intent);
					finish();
				}
			});
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return items.get(groupPosition).getList() != null ? items
					.get(groupPosition).getList().size() : 0;
		}

	}

}
