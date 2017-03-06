package com.qingzu.waterproof_work;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.entity.CityModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProvinceModel;
import com.qingzu.entity.Tag;
import com.qingzu.entity.TeamServeCity;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.TagListView;
import com.qingzu.uitest.view.TagListView.OnTagClickListener;
import com.qingzu.uitest.view.TagView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.XmlParserHandler;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * 施工队服务城市
 * 
 * @author Johnson
 * 
 */
public class ServeCityActivity extends Activity {

	private List<ProvinceModel> provinceList = null;
	private MyTitleView asc_title = null;
	private ExpandableListView listView = null;
	private ExampleAdapter adapter;
	private List<Tag> mTags = null;
	private Button lnsc_bt_compile = null;
	private TagListView lnsc_tagview = null;
	private HashMap<String, Tag> serveCity = null;
	private boolean IsDelete = false;
	private LayoutInflater layoutInflater = null;
	private List<ServeCityList> cityLists = null;
	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serve_city);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = ServeCityActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		serveCity = new HashMap<String, Tag>();
		cityLists = new ArrayList<ServeCityActivity.ServeCityList>();
		mTags = new ArrayList<Tag>();
		layoutInflater = LayoutInflater.from(this);
		asc_title = (MyTitleView) findViewById(R.id.asc_title);
		listView = (ExpandableListView) findViewById(R.id.asc_listView);

		getData();
		adapter = new ExampleAdapter(this);
		adapter.setData(provinceList);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				adapter.notifyDataSetChanged();
				return false;
			}
		});

		lnsc_tagview = (TagListView) findViewById(R.id.lnsc_tagview);
		lnsc_bt_compile = (Button) findViewById(R.id.lnsc_bt_compile);
		ServeCityInfoByTeamId(MainActivity.constructionTeam.getId() + "");
		lnsc_bt_compile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IsDelete) {
					lnsc_tagview.setDeleteMode(false);
					lnsc_tagview.setTags(mTags);
					IsDelete = !IsDelete;
				} else {
					lnsc_tagview.setDeleteMode(true);
					lnsc_tagview.setTags(mTags);
					IsDelete = !IsDelete;
				}

			}
		});
		lnsc_tagview.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(TagView tagView, Tag tag) {
				if (IsDelete) {
					lnsc_tagview.removeTag(tag);
					serveCity.remove(tag.getId() + "");
					mTags.clear();
					for (String key : serveCity.keySet()) {
						mTags.add(serveCity.get(key));
					}
				}
			}
		});

		asc_title.setOnLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		asc_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				commit();
			}
		});
	}

	/**
	 * 根据施工队Id获取施工队服务城市信息列表 GET
	 * 
	 * @author Johnson
	 */
	private void ServeCityInfoByTeamId(String Id) {
		RequestParams params = new RequestParams(
				HttpUtil.ServeCityInfoByTeamId.replace("{TeamId}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ServeCityList> interfaceReturn = new InterfaceReturn<ServeCityList>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ServeCityList.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Tag tag = null;
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							tag = new Tag();
							tag.setId(interfaceReturn.getResults().get(i)
									.getRegionId());
							tag.setTitle(interfaceReturn.getResults().get(i)
									.getRegionName());
							tag.setChecked(true);
							serveCity.put(interfaceReturn.getResults().get(i)
									.getRegionId()
									+ "", tag);
							mTags.add(tag);
						}
						lnsc_tagview.setTags(mTags);
					} else {
						T.showToast(ServeCityActivity.this,
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
	 * 获取省市区
	 */
	private void getData() {
		if (XmlParserHandler.provinceList.size() <= 0) {
			AssetManager asset = getAssets();
			try {
				InputStream input = asset.open("province_data.xml");
				// 创建一个解析xml的工厂对象
				SAXParserFactory spf = SAXParserFactory.newInstance();
				// 解析xml
				SAXParser parser = spf.newSAXParser();
				XmlParserHandler handler = new XmlParserHandler();
				parser.parse(input, handler);
				input.close();
				// 获取解析出来的数据
				provinceList = handler.getDataList();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		} else {
			provinceList = XmlParserHandler.provinceList;
		}

	}

	protected void commit() {
		ServeCityList cityList = null;
		for (String key : serveCity.keySet()) {
			cityList = new ServeCityList();
			cityList.setTeamId(MainActivity.constructionTeam.getId());
			cityList.setRegionId(Integer.parseInt(key));
			cityList.setRegionName(serveCity.get(key).getTitle());
			cityLists.add(cityList);
		}
		if (cityLists.size() > 0) {
			Gson gson = new Gson();
			CreateServeCityList(gson.toJson(cityLists));
		} else {
			T.showToast(ServeCityActivity.this, "请选择至少一个城市");
		}
	}

	/**
	 * 提交施工队服务城市列表信息 POST
	 * 
	 * @param json
	 * @author Johnson
	 */
	private void CreateServeCityList(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateServeCityList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setCharset("utf-8");
		params.setBodyContent(json);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ServeCityActivity.this,
								interfaceReturn.getMessage());
						finish();
					} else {
						T.showToast(ServeCityActivity.this,
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

		private List<ProvinceModel> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<ProvinceModel> items) {
			this.items = items;
		}

		@Override
		public CityModel getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).getCityList().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public ProvinceModel getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder holder;
			ProvinceModel item = getGroup(groupPosition);
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
			holder.title.setText(item.getName());
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
			final CityModel item = getChild(groupPosition, childPosition);
			// if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(
					R.layout.layout_serve_city_child_item, parent, false);
			holder.title = (TextView) convertView
					.findViewById(R.id.lscci_tv_city);
			holder.select = (CheckBox) convertView
					.findViewById(R.id.lscci_cb_select);
			holder.room = (RelativeLayout) convertView
					.findViewById(R.id.lscci_room);
			// convertView.setTag(holder);
			// } else {
			// holder = (ChildHolder) convertView.getTag();
			// }
			holder.title.setText(item.getName());
			holder.room.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// if (holder.select.isChecked()) {
					// holder.select.setChecked(false);
					// } else if (!holder.select.isChecked()) {
					// holder.select.setChecked(true);
					// }
					if (serveCity.get(item.getId()) == null) {
						if (serveCity.size() < 10) {
							Tag tag = new Tag();
							tag.setId(Integer.parseInt(item.getId()));
							tag.setChecked(true);
							tag.setTitle(item.getName());
							serveCity.put(item.getId(), tag);
							mTags.add(tag);
							lnsc_tagview.setTags(mTags);
						} else {
							T.showToast(ServeCityActivity.this, "最多只能先择10个城市");
						}
					}
				}
			});
			// holder.select
			// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// @Override
			// public void onCheckedChanged(CompoundButton arg0,
			// boolean arg1) {
			// if (arg1) {
			// Tag tag = new Tag();
			// tag.setId(Integer.parseInt(item.getId()));
			// tag.setChecked(true);
			// tag.setTitle(item.getName());
			// serveCity.put(item.getId(), tag);
			// } else {
			// serveCity.remove(item.getId());
			// }
			// }
			// });
			// holder.select.setChecked(serveCity.get(item.getId()) != null ?
			// true
			// : false);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return items.get(groupPosition).getCityList().size();
		}

	}

	/**
	 * 施工队服务城市信息
	 * 
	 * @author Johnson
	 */
	class ServeCityList {
		private int teamId;
		private int regionId;
		private String regionName;
		private String regionPath;

		public int getTeamId() {
			return teamId;
		}

		public void setTeamId(int teamId) {
			this.teamId = teamId;
		}

		public int getRegionId() {
			return regionId;
		}

		public void setRegionId(int regionId) {
			this.regionId = regionId;
		}

		public String getRegionName() {
			return regionName;
		}

		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}

		public String getRegionPath() {
			return regionPath;
		}

		public void setRegionPath(String regionPath) {
			this.regionPath = regionPath;
		}
	}

}
