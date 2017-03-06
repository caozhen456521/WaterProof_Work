package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 付费会员
 * @author Administrator
 *
 */
public class PayingMembersActivity extends Activity implements OnClickListener {
	private MyTitleView pay_members_title = null;// 付费会员
	private Button btApply=null;//一键申请
	private String UserToken = null;
	

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paying_members);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}


	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PayingMembersActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		pay_members_title=(MyTitleView) findViewById(R.id.paying_members_title);
		btApply=(Button) findViewById(R.id.apply_bt);
		
		pay_members_title.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		btApply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CreateVIPApply();
			}

			
		});
		
	}
	
	
/**
 * 提交申请成为VIP付费会员记录
 */
	private void CreateVIPApply() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.CreateVIPApply);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<VIPApply> interfaceReturn = new InterfaceReturn<VIPApply>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						VIPApply.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							Toast.makeText(PayingMembersActivity.this,
									interfaceReturn.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					} else {
						Toast.makeText(PayingMembersActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	
	class VIPApply{
		
		private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		public String getMemberName() {
			return memberName;
		}
		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}
		public int getInfoType() {
			return InfoType;
		}
		public void setInfoType(int infoType) {
			InfoType = infoType;
		}
		public int getStateId() {
			return stateId;
		}
		public void setStateId(int stateId) {
			this.stateId = stateId;
		}
		private int memberId;
		private String memberName;
		private int InfoType;
		private  int stateId;
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
