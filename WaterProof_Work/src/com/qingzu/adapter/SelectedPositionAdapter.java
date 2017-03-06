package com.qingzu.adapter;

import java.util.List;

import com.qingzu.entity.AddressItem;
import com.qingzu.waterproof_work.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author 曹振 选择地点 listview adapter
 */

public class SelectedPositionAdapter extends BaseAdapter {

	private List<AddressItem> listdata = null;

	private LayoutInflater inflater = null;

	private Context context = null;

	private int type = 0;

	public SelectedPositionAdapter(Context context, List<AddressItem> listdata,
			int type) {
		this.type = type;
		this.context = context;
		this.listdata = listdata;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listdata != null) {
			return listdata.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
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
		ViewHoler viewHoler = null;

		viewHoler = new ViewHoler();
		convertView = inflater.inflate(
				R.layout.selected_position_listview_item, null);
		viewHoler.address = (TextView) convertView
				.findViewById(R.id.listview_item_address);
		viewHoler.addressDetails = (TextView) convertView
				.findViewById(R.id.listview_item_addressDetails);
		viewHoler.dangqian = (TextView) convertView.findViewById(R.id.dangqian);

		AddressItem item = listdata.get(position);

		viewHoler.address.setText(item.getAddress());
		if (type == 0) {

			if (position == 0) {
				viewHoler.address.setTextColor(android.graphics.Color
						.parseColor("#FF8000"));
				viewHoler.dangqian.setVisibility(View.VISIBLE);
			} else {
				viewHoler.dangqian.setVisibility(View.GONE);
			}
		}
		viewHoler.addressDetails.setText(item.getAddressDetails());

		return convertView;
	}

	private class ViewHoler {

		TextView dangqian = null;
		TextView address = null;
		TextView addressDetails = null;

	}
}
