package com.qingzu.adapter;

import java.util.HashMap;

import com.qingzu.waterproof_work.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MaterialRequestLvAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context mcontext = null;
	private String[] str;
	public static HashMap<Integer, String> Positionlist = null;
	public static HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public MaterialRequestLvAdapter(Context context, String[] str) {
		this.str = str;
		this.mcontext = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (str != null && str.length > 0) {
			return str.length;
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
		// TODO Auto-generated method stub
		final ViewHolder viewHolder = new ViewHolder();
		convertView = layoutInflater.inflate(
				R.layout.layout_material_request_item, null);
		viewHolder.material = (TextView) convertView
				.findViewById(R.id.material_item_tv);
		viewHolder.cb = (CheckBox) convertView
				.findViewById(R.id.material_item_cb);
		viewHolder.material_item = (LinearLayout) convertView
				.findViewById(R.id.material_request_item_ll);
		convertView.setTag(viewHolder);
		final String skill = str[position];
		final int index = position;
		viewHolder.material.setText(skill);

		viewHolder.material_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (viewHolder.cb.isChecked()) {
					viewHolder.cb.setChecked(false);
				} else if (!viewHolder.cb.isChecked()) {
					viewHolder.cb.setChecked(true);
				}
			}
		});

		viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					state.put(index, arg1);
				} else {
					state.remove(index);
				}

			}

		});
		viewHolder.cb.setChecked(state.get(position) == null ? false : true);
		return convertView;
	}

	private class ViewHolder {

		TextView material = null; // 材料类别
		CheckBox cb = null; // checkbox
		LinearLayout material_item = null; // item
	}

}
