package com.qingzu.waterproof_work;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.OrderFormListModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.SignUtils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig.Weixin;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends Activity {
	private WebView view = null;
	private ProgressBar progressBar;
	private MyTitleView myTitleView;
	private String UserToken;
	private IWXAPI api;
	public static boolean ifPay = false;
	private String url = null;
	private String mOrderCode = null;
	private ShowProgressDialog dialog = null;
	private int count = 2;
	private Thread thread;

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		AppManager.getAppManager().addActivity(this);

		api = WXAPIFactory.createWXAPI(this, "wx9336db16646936e5");
		api.registerApp("wx9336db16646936e5");

		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);

		UserToken = preferences.getString("UserToken", "");
		// dialog = getProDialog();
		view = (WebView) findViewById(R.id.web_view);
		// 支持js
		view.setWebChromeClient(new WebChromeClient());
		// 设置本地调用对象及其接口
		view.addJavascriptInterface(new JavaScriptObject(), "android");

		progressBar = (ProgressBar) findViewById(R.id.myProgressBar);
		myTitleView = (MyTitleView) findViewById(R.id.webview_title);
		WebSettings settings = view.getSettings();

		// settings.set
		settings.setSupportZoom(true); // 支持缩放
		// settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		if (UserToken.equals("")) {
			view.loadUrl(url);

		} else {
			try {
				view.postUrl(HttpUtil.WebLogonByToken, ("token=" + UserToken
						+ "&returnURL=" + URLEncoder.encode(url, "utf-8"))
						.getBytes());

				Log.e("viewurl+++++++++++++++++", view.getUrl());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		myTitleView.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (view.canGoBack() == true) {
				// view.goBack();
				// Log.e("viewurl====================", view.getUrl()); //
				// view.getUrl()
				// } else
				//
				// finish();
				if (url.equals("http://m.5ifangshui.com/help/")) {
					finish();
					return;
				}
				Dialog();
			}
		});

		view.setWebViewClient(new webViewClient() {
		});

		view.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				} else {
					if (progressBar.getVisibility() == View.GONE) {
						progressBar.setVisibility(View.VISIBLE);
						progressBar.setProgress(newProgress);
					}

				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
				myTitleView.setText(title);
			}

		});

	}

	private static final String INJECTION_TOKEN = "**injection**";
	private static final String TAG = "WEIXINPAY";

	// Web视图
	private class webViewClient extends WebViewClient {
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		//
		// // if (url.startsWith("http:") || url.startsWith("https:")) {
		// // return false;
		// // }
		// // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		// // startActivity( intent );
		// // return true;
		//
		// /iew.loadUrl(url);
		// return false;
		// }

		//

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			WebResourceResponse response = super.shouldInterceptRequest(view,
					url);
			if (url != null && url.contains(INJECTION_TOKEN)) {
				String assetPath = url.substring(url.indexOf(INJECTION_TOKEN)
						+ INJECTION_TOKEN.length(), url.length());
				try {
					response = new WebResourceResponse(
							"application/javascript", "UTF8", getAssets().open(
									assetPath));
				} catch (IOException e) {
					e.printStackTrace(); // Failed to load asset file
				}
			}
			return response;
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && view.canGoBack()) {
			// Log.e("viewurl-1111111111111111", view.getUrl());
			view.goBack(); // goBack()表示返回WebView的上一页面

			return true;
		} else {

			if (url.equals("http://m.5ifangshui.com/help/")) {
				finish();

			} else
				Dialog();

			// finish();// 结束退出程序
		}

		return super.onKeyDown(keyCode, event);
	}

	class JavaScriptObject {

		@JavascriptInterface
		public void InAppPay(String orderCode) {
			mOrderCode = orderCode;
			MallOrderFormByOrderCode(orderCode, 1);
		}
	}

	/**
	 * 根据订单编码获取订单内商品详细信息
	 * 
	 * @param orderCode
	 * @author Johnson
	 */
	public void MallOrderFormByOrderCode(final String orderCode, final int State) {
		RequestParams params = new RequestParams(
				HttpUtil.MallOrderFormByOrderCode.replace("{OrderCode}",
						orderCode));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<OrderFormListModel> interfaceReturn = new InterfaceReturn<OrderFormListModel>();
				interfaceReturn = interfaceReturn.fromJson(result,
						OrderFormListModel.class);
				if (interfaceReturn.isStatus()) {
					if (State == 1) {
						weiXinPay(interfaceReturn.getResults().get(0));
					} else if (State == 2) {
						if (interfaceReturn.getResults().get(0).getOrderForm()
								.getTradingStatus() == 1) {
							view.reload();
						}
					}
				} else {
					T.showToast(WebViewActivity.this,
							interfaceReturn.getMessage());
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

	private void weiXinPay(OrderFormListModel formListModel) {
		String nonce_str = UUID.randomUUID().toString().replace("-", "");
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", "wx9336db16646936e5");
		map.put("mch_id", "1385005402");
		map.put("device_info", "WEB");
		map.put("body", formListModel.getListDetail().get(0).getProduct()
				.getProductName()
				+ "*"
				+ formListModel.getListDetail().get(0).getOrderFormDetails()
						.getBuyNumber());
		map.put("nonce_str", nonce_str);
		map.put("notify_url",
				"http://mall.5ifangshui.com/pay/WxAppPay/ResultNotify");
		map.put("out_trade_no", formListModel.getOrderForm().getOrderCode());
		map.put("spbill_create_ip",
				Tools.getLocalIpAddress(WebViewActivity.this));
		map.put("total_fee", ((int) (formListModel.getOrderForm()
				.getTotalAmount() * 100)) + "");
		map.put("trade_type", "APP");
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
				// return (o2.getValue() - o1.getValue());
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		String stringA = "";
		// 排序后
		for (int i = 0; i < infoIds.size(); i++) {
			if (i == infoIds.size() - 1) {
				stringA += infoIds.get(i).toString();
			} else {
				stringA += infoIds.get(i).toString() + "&";
			}

		}
		stringA += "&key=CH6NmrxE6x9E7xO9pACbASExFovBlCWQ";
		System.out.println(stringA);
		// 第一种方式：创建文档，并创建根元素
		// 创建文档:使用了一个Helper类
		Document document = DocumentHelper.createDocument();

		// 创建根节点并添加进文档
		Element root = DocumentHelper.createElement("xml");
		document.setRootElement(root);
		// // 添加属性
		// root.addAttribute("name", "zhangsan");
		// 添加子节点:add之后就返回这个元素
		root.addElement("appid").setText("wx9336db16646936e5");// 应用ID
		root.addElement("body").setText(
				formListModel.getListDetail().get(0).getProduct()
						.getProductName()
						+ "*"
						+ formListModel.getListDetail().get(0)
								.getOrderFormDetails().getBuyNumber());// 商品描述
		root.addElement("mch_id").setText("1385005402");// 商户号
		root.addElement("nonce_str").setText(nonce_str);// 随机字符串
		root.addElement("notify_url").setText(
				"http://mall.5ifangshui.com/pay/WxAppPay/ResultNotify");// 通知地址
		root.addElement("device_info").setText("WEB");// 设备号
		root.addElement("out_trade_no").setText(
				formListModel.getOrderForm().getOrderCode());// 商户订单号
		root.addElement("spbill_create_ip").setText(
				Tools.getLocalIpAddress(WebViewActivity.this));// 终端IP
		root.addElement("total_fee").setText(
				((int) (formListModel.getOrderForm().getTotalAmount() * 100))
						+ "");// 总金额
		root.addElement("trade_type").setText("APP");// 交易类型
		root.addElement("sign").setText(getMessageDigest(stringA.getBytes()));// 签名
		// 输出
		// 输出到控制台
		try {
			// 输出到文件
			// 格式
			OutputFormat format = new OutputFormat(null, false);// 设置缩进为4个空格，并且另起一行为true
			StringWriter writer = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			xmlWriter.write(document);
			System.out.println(writer.toString());
			payUnifiedorder(writer.toString());
			xmlWriter.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 统一下单 xml
	 * 
	 * @author Johnson
	 */
	private void payUnifiedorder(final String xml) {
		RequestParams params = new RequestParams(HttpUtil.payUnifiedorder);
		params.setCharset("utf-8");
		params.setBodyContent(xml);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				Document document = null;
				try {
					document = DocumentHelper.parseText(result);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取根节点元素对象
				Element root = document.getRootElement();
				String time_stamp = System.currentTimeMillis() + "";
				List<NameValuePair> signParams = new ArrayList<NameValuePair>();
				signParams.add(new BasicNameValuePair("appid", root.element(
						"appid").getTextTrim()));
				signParams.add(new BasicNameValuePair("noncestr", root.element(
						"nonce_str").getTextTrim()));
				signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
				signParams.add(new BasicNameValuePair("partnerid", root
						.element("mch_id").getTextTrim()));
				signParams.add(new BasicNameValuePair("prepayid", root.element(
						"prepay_id").getTextTrim()));
				signParams.add(new BasicNameValuePair("timestamp", time_stamp));
				PayReq request = new PayReq();
				request.appId = root.element("appid").getTextTrim();
				request.partnerId = root.element("mch_id").getTextTrim();
				request.prepayId = root.element("prepay_id").getTextTrim();
				request.packageValue = "Sign=WXPay";
				request.nonceStr = root.element("nonce_str").getTextTrim();
				request.timeStamp = time_stamp;
				request.sign = SignUtils.genAppSign(signParams);
				request.extData = "app data";
				api.sendReq(request);
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

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public void Dialog() {

		HintDialog.Builder builder = new HintDialog.Builder(
				WebViewActivity.this);
		builder.setMessage("你要关闭浏览的页面吗?");
		builder.setTitle("页面提示");
		// builder.setSingleChoiceItems
		builder.setNegativeButton(R.string.Yes,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// onCityListener.OnCityAddress(currentCity);
						// edt.putString("city", currentCity);
						// edt.commit();
						// dialog.dismiss();
						// StatusTool.isWelcome = false;
						// delete();
						finish();
					}
				});
		builder.setPositiveButton("再看看", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				// delete();
				dialog.dismiss();
				// 设置你的操作事项

				// into();
			}
		});

		builder.create().show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (ifPay) {
			// dialog.show();
			view.reload();
			// MallOrderFormByOrderCode(mOrderCode,2);
			ifPay = false;
		}

	}

}
