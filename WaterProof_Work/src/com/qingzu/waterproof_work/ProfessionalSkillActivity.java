package com.qingzu.waterproof_work;


import java.util.HashMap;

import com.qingzu.adapter.MaterialRequestLvAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class ProfessionalSkillActivity extends Activity {

	private MyTitleView mtvMaterialRequestTitle=null;//材料要求标题
	private ListView listView=null;//材料列表
	private HashMap<Integer, String> adapterlist = null;// 接收打钩的值
	private HashMap<Integer, Boolean> selectHM = null;
	private MaterialRequestLvAdapter adapter = null;
	
	private String[] str = { "SBS防水卷材施工",
			"自粘卷材施工",
			"橡胶共混卷材施工",
			"涂料类防水材料施工",
			"聚乙烯丙纶类材料施工",
			"高分子卷材施工",
			"注浆补漏类防水施工"};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_professional_skill);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	@SuppressLint("UseSparseArrays") private void initView() {
		// TODO Auto-generated method stub
		mtvMaterialRequestTitle=(MyTitleView) findViewById(R.id.professional_skill_title);
		listView=(ListView) findViewById(R.id.professional_skill_lv);
		
		
		
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
				selectHM = MaterialRequestLvAdapter.state;
				String selectText = "";
				if (selectHM != null
						&& !selectHM.toString().trim().equals("{}")) {

					for (int i = 0; i < adapterlist.size(); i++) {
						Boolean selectB = selectHM.get(i);
						if (selectB != null) {
							if (selectB == true) {
								selectText += adapterlist.get(i) + ",";
							}
						}
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
		adapter = new MaterialRequestLvAdapter(ProfessionalSkillActivity.this,
				str);
		listView.setAdapter(adapter);
		adapterlist = new HashMap<Integer, String>();
		for (int i = 0; i < str.length; i++) {
			adapterlist.put(i, str[i]);
		}
	}

}
