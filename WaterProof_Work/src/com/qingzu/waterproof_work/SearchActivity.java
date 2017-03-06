package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.qingzu.adapter.SelectedPositionAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.entity.AddressItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity implements InputtipsListener,
		TextWatcher {
	private ImageView ivDeleteText;
	private EditText etSearch;
	private List<AddressItem> addressItems = new ArrayList<AddressItem>();
	private ListView listView = null;
	private SelectedPositionAdapter adapter2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		AppManager.getAppManager().addActivity(this);
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		etSearch = (EditText) findViewById(R.id.etSearch);

		listView = (ListView) findViewById(R.id.listview2);
		adapter2 = new SelectedPositionAdapter(this, addressItems, 1);
		listView.setAdapter(adapter2);

		etSearch.addTextChangedListener(this);// 添加文本输入框监听事件
		ivDeleteText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this,
						SelectedPositionActivity.class);
				Bundle bundle = new Bundle();
				AddressItem addressItem = new AddressItem();
				addressItem.setLatitude(addressItems.get(arg2).getPoint()
						.getLatitude());
				addressItem.setLongitude(addressItems.get(arg2).getPoint()
						.getLongitude());
				addressItem.setAddress(addressItems.get(arg2).getAddress());
				bundle.putSerializable(SelectedPositionActivity.SEARCH,
						addressItem);
				intent.putExtra("type", SelectedPositionActivity.SEARCH);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

			}

		});

	}

	@Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {// 正确返回
			List<String> listString = new ArrayList<String>();
			if (addressItems != null) {
				addressItems.clear();
			}
			for (int i = 0; i < tipList.size(); i++) {
				AddressItem addressItem = new AddressItem();
				if (tipList.get(i).getPoint().getLatitude() > 0) {
				//	tipList.get(i).getDistrict()
					addressItem.setAddress(tipList.get(i).getName());
					addressItem.setAddressDetails(tipList.get(i).getDistrict());
					addressItem.setPoint(tipList.get(i).getPoint());
					addressItems.add(addressItem);
				}
			}
			System.out.println(listString);
			// ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
			// getApplicationContext(),
			// R.layout.route_inputs, listString);
			// searchText.setAdapter(aAdapter);
			adapter2.notifyDataSetChanged();
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String newText = s.toString().trim();
		if (newText.equals("") && addressItems != null) {
			addressItems.clear();
			adapter2.notifyDataSetChanged();
		}
		InputtipsQuery inputquery = new InputtipsQuery(newText, etSearch
				.getText().toString());
		inputquery.setCityLimit(true);
		inputquery.setType(getString(R.string.HangZhou_City));
		Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
		inputTips.setInputtipsListener(this);

		inputTips.requestInputtipsAsyn();
	}
}