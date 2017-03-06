package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.adapter.MaterialRequestLvAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.R.integer;
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

/**
 * 材料要求
 * 
 * @author Administrator
 * 
 */
public class MaterialRequestActivity extends Activity {

	private MyTitleView mtvMaterialRequestTitle = null;// 材料要求标题
	private ListView listView = null;// 材料列表
	private MyAdapter<ConstructionMaterial> adapter = null;
	private List<ConstructionMaterial> list = null;
	public static HashMap<Integer, ConstructionMaterial> hashMap = new HashMap<Integer, MaterialRequestActivity.ConstructionMaterial>();

	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_request);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		list = new ArrayList<MaterialRequestActivity.ConstructionMaterial>();
		mtvMaterialRequestTitle = (MyTitleView) findViewById(R.id.material_request_title);
		listView = (ListView) findViewById(R.id.material_request_lv);

		adapter = new MyAdapter<MaterialRequestActivity.ConstructionMaterial>(
				this, list, R.layout.layout_material_request_item) {

			TextView material = null; // 材料类别
			CheckBox cb = null; // checkbox
			LinearLayout material_item = null; // item

			@Override
			public void convert(ViewHolder view, final int position,
					final ConstructionMaterial item) {
				material = view.getView(R.id.material_item_tv);
				cb = view.getView(R.id.material_item_cb);
				material_item = view.getView(R.id.material_request_item_ll);

				material.setText(item.getMaterialName());

				material_item.setOnClickListener(new OnClickListener() {

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

		mtvMaterialRequestTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mtvMaterialRequestTitle.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				String selectText = "";
				if (hashMap != null && hashMap.size() > 0) {

					for (Integer key : hashMap.keySet()) {
						selectText += hashMap.get(key).getMaterialName() + ",";
					}

					intent.putExtra("positionlist",
							selectText.substring(0, (selectText.length()) - 1));
					setResult(6, intent);
					finish();
				}
				intent.putExtra("positionlist", selectText);
				setResult(6, intent);
				finish();

			}
		});
		ConstructionMaterialList();
	}

	/**
	 * 当前登录用户获取施工材料列表信息 GET
	 * 
	 * @author Johnson
	 */
	private void ConstructionMaterialList() {
		RequestParams params = new RequestParams(
				HttpUtil.ConstructionMaterialList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionMaterial> interfaceReturn = new InterfaceReturn<MaterialRequestActivity.ConstructionMaterial>();
				interfaceReturn = interfaceReturn.fromJson(result,
						ConstructionMaterial.class);
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

	class ConstructionMaterial implements Serializable {
		private int id;
		private String materialName;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getMaterialName() {
			return materialName;
		}

		public void setMaterialName(String materialName) {
			this.materialName = materialName;
		}

	}
}
