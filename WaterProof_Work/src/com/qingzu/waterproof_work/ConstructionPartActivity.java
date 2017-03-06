package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.adapter.ConstructionPartLvAdapter;
import com.qingzu.adapter.MaterialRequestLvAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.entity.ConstructionPosition;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.MaterialRequestActivity.ConstructionMaterial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ConstructionPartActivity extends Activity implements
		OnClickListener {

	private MyTitleView mtvConstructionPartTitle = null;// 施工部位标题
	private ListView listView = null;// 施工部位列表
	private MyAdapter<ConstructionPosition> adapter = null;
	private List<ConstructionPosition> list = null;
	public static HashMap<Integer, ConstructionPosition> hashMap = new HashMap<Integer, ConstructionPartActivity.ConstructionPosition>();

	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_construction_part);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		list = new ArrayList<ConstructionPartActivity.ConstructionPosition>();
		mtvConstructionPartTitle = (MyTitleView) findViewById(R.id.construction_part_title);
		listView = (ListView) findViewById(R.id.construction_part_lv);

		adapter = new MyAdapter<ConstructionPartActivity.ConstructionPosition>(
				this, list, R.layout.layout_construction_part_item) {

			TextView constructionPart = null; // 施工部位
			CheckBox cb = null; // checkbox
			LinearLayout constructionPart_item = null; // item

			@Override
			public void convert(ViewHolder view, final int position,
					final ConstructionPosition item) {
				constructionPart = view.getView(R.id.construction_part_item_tv);
				cb = view.getView(R.id.construction_part_item_cb);
				constructionPart_item = view
						.getView(R.id.construction_part_item_ll);

				constructionPart.setText(item.getPositionName());

				constructionPart_item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (hashMap.get(position) != null) {
							hashMap.remove(position);
						} else {
							hashMap.put(position, item);
						}
						adapter.notifyDataSetChanged();
					}
				});

				cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							hashMap.put(position, item);
						} else {
							hashMap.remove(position);
						}

					}

				});
				cb.setChecked(hashMap.get(position) == null ? false : true);
			}
		};

		mtvConstructionPartTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mtvConstructionPartTitle.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				String selectText = "";
				if (hashMap != null && hashMap.size() > 0) {

					for (Integer key : hashMap.keySet()) {
						selectText += hashMap.get(key).getPositionName() + ",";
					}

					intent.putExtra("conspartlist",
							selectText.substring(0, (selectText.length()) - 1));
					setResult(7, intent);
					finish();
				}
				intent.putExtra("conspartlist", selectText);
				setResult(7, intent);
				finish();

			}
		});

		ConstructionPartList();

	}

	/**
	 * 当前登录用户获取施工部位列表信息
	 * 
	 * @author Johnson
	 */
	private void ConstructionPartList() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.ConstructionPositionList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionPosition> interfaceReturn = new InterfaceReturn<ConstructionPartActivity.ConstructionPosition>();
				interfaceReturn = interfaceReturn.fromJson(result,
						ConstructionPosition.class);
				if (interfaceReturn.isStatus()) {
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						list.add(interfaceReturn.getResults().get(i));
					}
					listView.setAdapter(adapter);
				} else {

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

	public class ConstructionPosition implements Serializable {

		private int id;
		private String positionName;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getPositionName() {
			return positionName;
		}

		public void setPositionName(String positionName) {
			this.positionName = positionName;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
