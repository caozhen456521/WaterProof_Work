package com.qingzu.waterproof_work.wxapi;

import java.util.Timer;
import java.util.TimerTask;

import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.WebViewActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler,
		OnClickListener {
	private LinearLayout llPaySuccess = null;
	private LinearLayout llPayDefeat = null;
	private Button pay_success_completed_bt = null;
	private Button pay_defeat_completed_bt = null;
	private Timer tm;
	private TimerTask tk;
	private int count = 5;
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ceshi1);
		into();
		api = WXAPIFactory.createWXAPI(this, "wx9336db16646936e5");
		api.handleIntent(getIntent(), this);

	}

	private void into() {
		// TODO Auto-generated method stub
		llPaySuccess = (LinearLayout) findViewById(R.id.pay_success_ll);
		llPayDefeat = (LinearLayout) findViewById(R.id.pay_defeat_ll);
		pay_success_completed_bt = (Button) findViewById(R.id.pay_success_completed_bt);
		pay_defeat_completed_bt = (Button) findViewById(R.id.pay_defeat_completed_bt);

		pay_success_completed_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stuba

				finish();
			}
		});
		pay_defeat_completed_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				llPaySuccess.setVisibility(View.VISIBLE);
				llPayDefeat.setVisibility(View.GONE);
				WebViewActivity.ifPay = true;
				startCount();
			} else if (resp.errCode == -1) {
				llPaySuccess.setVisibility(View.GONE);
				llPayDefeat.setVisibility(View.VISIBLE);
				WebViewActivity.ifPay = true;
			} else {
				llPaySuccess.setVisibility(View.GONE);
				llPayDefeat.setVisibility(View.VISIBLE);
				WebViewActivity.ifPay = true;
			}
		}
	}

	private void startCount() {
		tm = new Timer();
		tk = new TimerTask() {

			@Override
			public void run() {
				Message msg = mHandler.obtainMessage();
				count--;
				if (count > 0) {
					msg.arg1 = count;
					msg.what = 0;
					msg.obj = count;
					mHandler.sendMessage(msg);
				} else {
					// 时间读完 初始秒数
					mHandler.sendEmptyMessage(11);
					count = 30;
				}

			}
		};
		tm.schedule(tk, 0, 1000);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				System.out.println();
				pay_success_completed_bt.setEnabled(false);

				pay_success_completed_bt.setText(msg.arg1 + "秒");
				break;
			case 11:
				pay_success_completed_bt.setEnabled(true);
				// pay_success_completed_bt.setBackgroundResource(R.color.white);
				pay_success_completed_bt.setText("完成");
				tk.cancel();
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}