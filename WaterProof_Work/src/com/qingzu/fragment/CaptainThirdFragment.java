package com.qingzu.fragment;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.MallOrderUrlModel;
import com.qingzu.entity.OrderCountModel;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.WebViewActivity;
import com.qingzu.waterproof_work.WorkOrderActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工长第三Fragment
 * 
 * @author Johnson
 * 
 */
public class CaptainThirdFragment extends Fragment implements OnClickListener {

	private View v = null;
	private TextView ft_tv_w_see_all = null;
	private ImageView ft_iv_wait_affirm = null;
	private ImageView ft_iv_yet_affirm = null;
	private ImageView ft_iv_underway = null;
	private ImageView ft_iv_w_wait_evaluate = null;
	private TextView ft_tv_m_see_all = null;
	private ImageView ft_iv_wait_pay = null;
	private ImageView ft_iv_wait_deliver = null;
	private ImageView ft_iv_wait_reception = null;
	private ImageView ft_iv_m_wait_evaluate = null;

	private TextView ft_iv_wait_affirm_counts_tv = null;// 待确认数目
	private TextView ft_iv_yet_affirm_counts_tv = null;// 已确认数目
	private TextView ft_iv_underway_counts_tv = null;// 进行中数目
	private TextView ft_iv_w_wait_evaluate_counts_tv = null;// 待评价数目
	private TextView ft_tv_order = null;
	private String UserToken = null;
	private boolean MallOrderS = false;
	private InterfaceReturn<MallOrderUrlModel> interfaceReturn = null;
	private int state;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_third, container, false);
		initView();
		return v;
	}

	private void initView() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		interfaceReturn = new InterfaceReturn<MallOrderUrlModel>();
		state = preferences.getInt("identity", 0);

		ft_tv_order = (TextView) v.findViewById(R.id.ft_tv_order);
		ft_tv_w_see_all = (TextView) v.findViewById(R.id.ft_tv_w_see_all);
		ft_iv_wait_affirm = (ImageView) v.findViewById(R.id.ft_iv_wait_affirm);
		ft_iv_yet_affirm = (ImageView) v.findViewById(R.id.ft_iv_yet_affirm);
		ft_iv_underway = (ImageView) v.findViewById(R.id.ft_iv_underway);
		ft_iv_w_wait_evaluate = (ImageView) v
				.findViewById(R.id.ft_iv_w_wait_evaluate);
		ft_iv_wait_affirm_counts_tv = (TextView) v
				.findViewById(R.id.ft_iv_wait_affirm_counts_tv);
		ft_iv_yet_affirm_counts_tv = (TextView) v
				.findViewById(R.id.ft_iv_yet_affirm_counts_tv);
		ft_iv_underway_counts_tv = (TextView) v
				.findViewById(R.id.ft_iv_underway_counts_tv);
		ft_iv_w_wait_evaluate_counts_tv = (TextView) v
				.findViewById(R.id.ft_iv_w_wait_evaluate_counts_tv);
		ft_tv_m_see_all = (TextView) v.findViewById(R.id.ft_tv_m_see_all);
		ft_iv_wait_pay = (ImageView) v.findViewById(R.id.ft_iv_wait_pay);
		ft_iv_wait_deliver = (ImageView) v
				.findViewById(R.id.ft_iv_wait_deliver);
		ft_iv_wait_reception = (ImageView) v
				.findViewById(R.id.ft_iv_wait_reception);
		ft_iv_m_wait_evaluate = (ImageView) v
				.findViewById(R.id.ft_iv_m_wait_evaluate);

		if (state == 0) {
			ft_tv_order.setText("用工订单");
			getLeaderOrderCount();
		} else if (state == 1 || state == 2) {
			ft_tv_order.setText("干活订单");
			getWorkerOrderCount();
		}

		ft_tv_w_see_all.setOnClickListener(this);
		ft_iv_wait_affirm.setOnClickListener(this);
		ft_iv_yet_affirm.setOnClickListener(this);
		ft_iv_underway.setOnClickListener(this);
		ft_iv_w_wait_evaluate.setOnClickListener(this);
		ft_tv_m_see_all.setOnClickListener(this);
		ft_iv_wait_pay.setOnClickListener(this);
		ft_iv_wait_deliver.setOnClickListener(this);
		ft_iv_wait_reception.setOnClickListener(this);
		ft_iv_m_wait_evaluate.setOnClickListener(this);
		MallOrderUrl();
	}
	
	
	
/**
 * 当前登录用户工人/工长获取订单列表数量信息
 */
	private void getWorkerOrderCount() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.WorkerOrderCount);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<OrderCountModel> interfaceReturn = new InterfaceReturn<OrderCountModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						OrderCountModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						if (interfaceReturn.getResults().get(0)
								.getUnConfirmOrder() > 0) {
							ft_iv_wait_affirm_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_wait_affirm_counts_tv.setText(interfaceReturn
									.getResults().get(0).getUnConfirmOrder()
									+ "");

						} else {
							ft_iv_wait_affirm_counts_tv
									.setVisibility(View.GONE);
						}
						if (interfaceReturn.getResults().get(0)
								.getFinishOrder() > 0) {
							ft_iv_yet_affirm_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_yet_affirm_counts_tv.setText(interfaceReturn
									.getResults().get(0).getFinishOrder()
									+ "");

						} else {
							ft_iv_yet_affirm_counts_tv.setVisibility(View.GONE);
						}
						if (interfaceReturn.getResults().get(0)
								.getOnGoingOrder() > 0) {
							ft_iv_underway_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_underway_counts_tv.setText(interfaceReturn
									.getResults().get(0).getOnGoingOrder()
									+ "");

						} else {
							ft_iv_underway_counts_tv.setVisibility(View.GONE);
						}

						if (interfaceReturn.getResults().get(0)
								.getNoevaluatOrder() > 0) {
							ft_iv_w_wait_evaluate_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_w_wait_evaluate_counts_tv
									.setText(interfaceReturn.getResults()
											.get(0).getNoevaluatOrder()
											+ "");

						} else {
							ft_iv_w_wait_evaluate_counts_tv
									.setVisibility(View.GONE);
						}

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

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
	 * 当前登录用户老板获取订单列表数量信息
	 */
	private void getLeaderOrderCount() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.LeaderOrderCount);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<OrderCountModel> interfaceReturn = new InterfaceReturn<OrderCountModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						OrderCountModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						if (interfaceReturn.getResults().get(0)
								.getUnConfirmOrder() > 0) {
							ft_iv_wait_affirm_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_wait_affirm_counts_tv.setText(interfaceReturn
									.getResults().get(0).getUnConfirmOrder()
									+ "");

						} else {
							ft_iv_wait_affirm_counts_tv
									.setVisibility(View.GONE);
						}
						if (interfaceReturn.getResults().get(0)
								.getFinishOrder() > 0) {
							ft_iv_yet_affirm_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_yet_affirm_counts_tv.setText(interfaceReturn
									.getResults().get(0).getFinishOrder()
									+ "");

						} else {
							ft_iv_yet_affirm_counts_tv.setVisibility(View.GONE);
						}
						if (interfaceReturn.getResults().get(0)
								.getOnGoingOrder() > 0) {
							ft_iv_underway_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_underway_counts_tv.setText(interfaceReturn
									.getResults().get(0).getOnGoingOrder()
									+ "");

						} else {
							ft_iv_underway_counts_tv.setVisibility(View.GONE);
						}

						if (interfaceReturn.getResults().get(0)
								.getNoevaluatOrder() > 0) {
							ft_iv_w_wait_evaluate_counts_tv
									.setVisibility(View.VISIBLE);
							ft_iv_w_wait_evaluate_counts_tv
									.setText(interfaceReturn.getResults()
											.get(0).getNoevaluatOrder()
											+ "");

						} else {
							ft_iv_w_wait_evaluate_counts_tv
									.setVisibility(View.GONE);
						}

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

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
	 * 商城订单外链集合 GET
	 * 
	 * @author Johnson
	 */
	private void MallOrderUrl() {
		RequestParams params = new RequestParams(HttpUtil.MallOrderUrl);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				interfaceReturn = interfaceReturn.fromJson(result,
						MallOrderUrlModel.class);
				if (interfaceReturn.isStatus()) {
					MallOrderS = true;
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

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.ft_tv_w_see_all:
			startActivity(new Intent(getActivity(), WorkOrderActivity.class)
					.putExtra("IDENTITY", state).putExtra("PAGE", 0));
			break;
		case R.id.ft_iv_wait_affirm:
			startActivity(new Intent(getActivity(), WorkOrderActivity.class)
					.putExtra("IDENTITY", state).putExtra("PAGE", 1));
			break;
		case R.id.ft_iv_yet_affirm:
			startActivity(new Intent(getActivity(), WorkOrderActivity.class)
					.putExtra("IDENTITY", state).putExtra("PAGE", 2));
			break;
		case R.id.ft_iv_underway:
			startActivity(new Intent(getActivity(), WorkOrderActivity.class)
					.putExtra("IDENTITY", state).putExtra("PAGE", 3));
			break;
		case R.id.ft_iv_w_wait_evaluate:
			startActivity(new Intent(getActivity(), WorkOrderActivity.class)
					.putExtra("IDENTITY", state).putExtra("PAGE", 4));
			break;
		case R.id.ft_tv_m_see_all:
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", interfaceReturn.getResults().get(0)
					.getAllOrder());
			getActivity().startActivity(intent);
			break;
		case R.id.ft_iv_wait_pay:
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", interfaceReturn.getResults().get(0)
					.getObligationOrder());
			getActivity().startActivity(intent);
			break;
		case R.id.ft_iv_wait_deliver:
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", interfaceReturn.getResults().get(0)
					.getUndeliveredOrder());
			getActivity().startActivity(intent);
			break;
		case R.id.ft_iv_wait_reception:
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", interfaceReturn.getResults().get(0)
					.getReceivedOrder());
			getActivity().startActivity(intent);
			break;
		case R.id.ft_iv_m_wait_evaluate:
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", interfaceReturn.getResults().get(0)
					.getFinishOrder());
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
}