package com.qingzu.waterproof_work;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

//import javax.swing.text.html.HTML;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.QuestionsClass;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.fragment.ForumFragment;
//import com.qingzu.photo.util.FileUtils;
import com.qingzu.photo.util.FileUtils;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
//import com.qingzu.utils.tools.JsonUtil;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.CommentActivity.UserInfoByID;
import com.qingzu.waterproof_work.MyInformationActivity.getQiNiuToken;
import com.qingzu.waterproof_work.R.color;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.StringUtils;
import com.umeng.socialize.PlatformConfig.Laiwang;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 论坛发布页面
 * 
 * @author Johnson
 * 
 */
public class ReleaseIssueActivity extends Activity implements OnClickListener {

	private Button btBack, btSend, btAddimg;
	private EditText etIssueTitle, etContent;
	private TextView tvIssueType;
	private static final int PICK_FROM_GALLERY = 101;
	private String QiNiuToken = null;
	private String UserToken = null;
	private int IntegralNumber;
	private ShowProgressDialog dialog;
	private QuestionsListByPage questions = null;
	private int state = 0;// 0:发问题/1:发答案
	private String Reward = null;
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private boolean PassBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release_issue);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = ReleaseIssueActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog();
		btBack = (Button) findViewById(R.id.ri_bt_back);
		btSend = (Button) findViewById(R.id.ri_bt_send);
		btAddimg = (Button) findViewById(R.id.ri_bt_addimg);
		etContent = (EditText) findViewById(R.id.ri_et_content);
		etIssueTitle = (EditText) findViewById(R.id.ri_et_issue_title);
		tvIssueType = (TextView) findViewById(R.id.ri_tv_issue_type);
		Intent intent = getIntent();
		if (intent.getStringExtra("FROM_ACTIVITY").equals("FORUM_DETAILS")) {
			Bundle bundle = intent.getExtras();
			questions = (QuestionsListByPage) bundle.get("QuestionsListByPage");
			state = 1;
			tvIssueType.setVisibility(View.GONE);
			etIssueTitle.setVisibility(View.GONE);
			etContent.setHint("请输入答案...");
			PassBack = true;
		} else if (intent.getStringExtra("FROM_ACTIVITY").equals("FORUM")) {
			state = 0;
			tvIssueType.setVisibility(View.VISIBLE);
			etIssueTitle.setVisibility(View.VISIBLE);

		}

		btBack.setOnClickListener(this);
		btSend.setOnClickListener(this);
		btAddimg.setOnClickListener(this);
		tvIssueType.setOnClickListener(this);
		QiniuToken();
		UserInfo();
	}

	/**
	 * 获取用户信息
	 * 
	 */
	private void UserInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						IntegralNumber = interfaceReturn.getResults().get(0)
								.getMember().getIntegralNumber();
					} else {
						T.showToast(ReleaseIssueActivity.this,
								interfaceReturn.getMessage());
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ri_bt_back:
			finish();
			break;
		case R.id.ri_bt_send:
			if (state == 0) {
				MyTextDialog("悬  赏", null);
			} else if (state == 1) {
				sendAnswer();
			}
			break;
		case R.id.ri_bt_addimg:
			openGallery();
			break;
		case R.id.ri_tv_issue_type:
			MyTextDialog("请选择问题类型", tvIssueType);
			break;

		}
	}

	private void sendAnswer() {
		SharedPreferences preferences = ReleaseIssueActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		CreateAnswersInfo answersInfo = new CreateAnswersInfo();

		if (!Html.toHtml(etContent.getText()).equals("")
				&& questions.getQuestions().getId() > 0
				&& preferences.getInt("MemberId", 0) != 0) {
			answersInfo.setaRemark(Html.toHtml(etContent.getText()));
			answersInfo.setQuestionsId(questions.getQuestions().getId());
			answersInfo.setMemberId(preferences.getInt("MemberId", 0));
			Gson gson = new GsonBuilder().serializeNulls().create();
			String JsonStr = gson.toJson(answersInfo);
			CreateAnswersInfo(JsonStr);
		} else {
			T.showToast(ReleaseIssueActivity.this, "缺少参数");
		}

	}

	public void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("return-data", true);
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				PICK_FROM_GALLERY);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Bitmap bitmap = null;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), uri);
				Bitmap mLogPhoto = Tools.comp(bitmap);

				String fileName = String.valueOf(System.currentTimeMillis());
				File file = FileUtils.saveBitmap(mLogPhoto, fileName);
				uploadImg(file, mLogPhoto);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	private void uploadImg(final File file, final Bitmap bitmap) {
		new Thread(new Runnable() {
			String fileName = String.valueOf(System.currentTimeMillis());

			@Override
			public void run() {
				fileName = String.valueOf(System.currentTimeMillis());
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, fileName, QiNiuToken,
						new UpCompletionHandler() {

							@Override
							public void complete(String arg0,
									com.qiniu.android.http.ResponseInfo arg1,
									JSONObject arg2) {
								// TODO Auto-generated method stub
								if (arg1.isOK()) {
									BitmapFactory.Options options = new BitmapFactory.Options();

									/**
									 * 最关键在此，把options.inJustDecodeBounds = true;
									 * 这里再decodeFile()，
									 * 返回的bitmap为空，但此时调用options.
									 * outHeight时，已经包含了图片的高了
									 */
									options.inJustDecodeBounds = true;
									/**
									 * options.outHeight为原始图片的高
									 */
									// Log.e("Test", "Bitmap Height == "
									// + options.outHeight);
									// Log.e("Test", "Bitmap outWidth == "
									// + options.outWidth);
									addImage(
											"http://7xoiij.com2.z0.glb.qiniucdn.com/"
													+ arg0, bitmap);
								}
							}

						}, null);

			}
		}).start();

	}

	/**
	 * 添加图片
	 * 
	 * @param ImgName
	 * @param bitmap
	 */
	private void addImage(String ImgName, Bitmap bitmap) {

		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
				bitmap);
		double Width = bitmapDrawable.getIntrinsicWidth();
		double Height = bitmapDrawable.getIntrinsicHeight();
		double Tw = etContent.getWidth();
		double w = Width / Tw;
		if (Width > Tw) {
			Width = Tw - 100;
			Height = Height / w - 100;
		}
		bitmapDrawable.setBounds(0, 0, (int) Width, (int) Height);
		String Key = ImgName;
		ImageSpan imageSpan = new ImageSpan(bitmapDrawable, ImgName,
				ImageSpan.ALIGN_BOTTOM);
		SpannableString spannableString = new SpannableString(Key);
		spannableString.setSpan(imageSpan, 0, Key.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		int index = etContent.getSelectionStart();
		Editable edit = etContent.getEditableText();
		if (index < 0 || index >= edit.length()) {
			edit.append(" \n");
			edit.append(spannableString);
		} else {
			edit.insert(index, " \n");
			edit.insert(index + " \n".length(), spannableString);
		}

	}

	/**
	 * 获取七牛Token
	 * 
	 * @author Johnson
	 * @date 2016-1-5
	 * @param mobile
	 * @param cateID
	 */
	public void QiniuToken() {

		RequestParams params = new RequestParams(HttpUtil.QiniuToken);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {
				InterfaceReturn<getQiNiuToken> interfaceReturn = new InterfaceReturn<getQiNiuToken>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						getQiNiuToken.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						QiNiuToken = interfaceReturn.getResults().get(0)
								.getToken();
						System.out.println(QiNiuToken);
					}
				}
			};

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

	/**
	 * dialog
	 * 
	 * @author Johnson
	 * @date 2016-1-5
	 * @param Hint
	 * @param textView
	 * @param Text
	 */
	public void MyTextDialog(final String Hint, final TextView textView) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		final RadioGroup group = (RadioGroup) layout.findViewById(R.id.null_rg);
		EditText editText = (EditText) layout.findViewById(R.id.et_nickname);
		ScrollView null_rg_sv = (ScrollView) layout
				.findViewById(R.id.null_rg_sv);
		ScrollView det_radio_sv = (ScrollView) layout
				.findViewById(R.id.det_radio_sv);
		if (Hint.equals("请选择问题类型")) {
			editText.setVisibility(View.GONE);
			det_radio_sv.setVisibility(View.GONE);
			null_rg_sv.setVisibility(View.VISIBLE);
			for (int i = 0; i < ForumFragment.classes.size(); i++) {
				if (i > 0) {
					RadioButton button = new RadioButton(this);
					button.setText(ForumFragment.classes.get(i).getQcTitle());
					button.setId(i + 1);
					button.setTextColor(color.black);
					group.addView(button);
					hashMap.put(ForumFragment.classes.get(i).getQcTitle(),
							ForumFragment.classes.get(i).getId());
				}
			}
		}
		final EditText det_et_issue_type = (EditText) layout
				.findViewById(R.id.det_et_reward);
		if (Hint.equals("悬  赏")) {
			null_rg_sv.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			det_radio_sv.setVisibility(View.VISIBLE);
			TextView textView2 = (TextView) layout
					.findViewById(R.id.det_tv_reward);
			textView2.setText("您当前积分" + IntegralNumber);
			final RadioGroup det_radio = (RadioGroup) layout
					.findViewById(R.id.det_radio);

			det_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					RadioButton button = (RadioButton) layout
							.findViewById(arg1);
					det_et_issue_type.setText(button.getText().toString()
							.replace("分", ""));
					button.setPressed(true);
				}
			});

		}
		det_et_issue_type.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().trim().length() > 0) {
					if (Integer.parseInt(arg0 + "") > IntegralNumber) {
						det_et_issue_type.setText(IntegralNumber + "");
						T.showToast(ReleaseIssueActivity.this, "没有更多的积分");
						if (Integer.parseInt(arg0 + "") > 1000) {
							det_et_issue_type.setText("1000");
							T.showToast(ReleaseIssueActivity.this,
									"土豪!!!悬赏分数最多1000分");
						}
					}
				}
			}
		});

		new HintDialog.Builder(layout.getContext())

		.setTitle(Hint).setContentView(layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (Hint.equals("请选择问题类型")) {
							int i = group.getCheckedRadioButtonId();
							RadioButton button = (RadioButton) layout
									.findViewById(i);
							textView.setText(button.getText().toString().trim());
						} else if (Hint.equals("悬  赏")) {
							CreateQuestionsInfo createQuestionsInfo = new CreateQuestionsInfo();
							if (!tvIssueType.getText().toString().trim()
									.equals("")
									&& !etIssueTitle.getText().toString()
											.trim().equals("")
									&& !Html.toHtml(etContent.getText())
											.equals("")
									&& !det_et_issue_type.getText().toString()
											.trim().equals("")) {
								createQuestionsInfo.setClassId(hashMap
										.get(tvIssueType.getText().toString()
												.trim()));
								createQuestionsInfo.setqTitle(etIssueTitle
										.getText().toString().trim());
								createQuestionsInfo.setqRemark(Html
										.toHtml(etContent.getText()));
								createQuestionsInfo.setRewardPoints(Integer
										.parseInt(det_et_issue_type.getText()
												.toString().trim()));
								Gson gson = new GsonBuilder().serializeNulls()
										.create();
								String JsonStr = gson
										.toJson(createQuestionsInfo);
								CreateQuestionsInfo(JsonStr);
							} else {
								T.showToast(ReleaseIssueActivity.this, "缺少参数");
							}

						}
						dialog.dismiss();
						return;

					}
				}).create().show();

	}

	/**
	 * 当前登录用户提交问题答案信息
	 * 
	 * @param json
	 */
	public void CreateAnswersInfo(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateAnswersInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ReleaseIssueActivity.this,
								interfaceReturn.getMessage());
						if (PassBack) {
							setResult(ForumDetailsActivity.SET_RESULT);
						}
						finish();
					} else {
						T.showToast(ReleaseIssueActivity.this,
								interfaceReturn.getMessage());
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
	 * 当前登录用户发布问题数据信息
	 * 
	 * @param json
	 */
	public void CreateQuestionsInfo(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateQuestionsInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ReleaseIssueActivity.this,
								interfaceReturn.getMessage());
						finish();
					} else {
						T.showToast(ReleaseIssueActivity.this,
								interfaceReturn.getMessage());
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

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				ReleaseIssueActivity.this, R.style.progress_dialog,
				getString(R.string.Loading));

		return dialog;
	}

	private class CreateQuestionsInfo implements Serializable {
		private String qTitle;// 标题
		private String qRemark;// 内容
		private int rewardPoints;// 悬赏分
		private int classId;// 问题分类ID
		private String qImages;// 图片
		private String qTag;// 标签
		private String qKeyWord;// 关键字

		public String getqTitle() {
			return qTitle;
		}

		public void setqTitle(String qTitle) {
			this.qTitle = qTitle;
		}

		public String getqRemark() {
			return qRemark;
		}

		public void setqRemark(String qRemark) {
			this.qRemark = qRemark;
		}

		public int getRewardPoints() {
			return rewardPoints;
		}

		public void setRewardPoints(int rewardPoints) {
			this.rewardPoints = rewardPoints;
		}

		public int getClassId() {
			return classId;
		}

		public void setClassId(int classId) {
			this.classId = classId;
		}

		public String getqImages() {
			return qImages;
		}

		public void setqImages(String qImages) {
			this.qImages = qImages;
		}

		public String getqTag() {
			return qTag;
		}

		public void setqTag(String qTag) {
			this.qTag = qTag;
		}

		public String getqKeyWord() {
			return qKeyWord;
		}

		public void setqKeyWord(String qKeyWord) {
			this.qKeyWord = qKeyWord;
		}

	}

	class CreateAnswersInfo {
		private int questionsId;
		private int memberId;
		private String aRemark;
		private String aImages;
		private String aTag;
		private String aKeyWord;

		public int getQuestionsId() {
			return questionsId;
		}

		public void setQuestionsId(int questionsId) {
			this.questionsId = questionsId;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public String getaRemark() {
			return aRemark;
		}

		public void setaRemark(String aRemark) {
			this.aRemark = aRemark;
		}

		public String getaImages() {
			return aImages;
		}

		public void setaImages(String aImages) {
			this.aImages = aImages;
		}

		public String getaTag() {
			return aTag;
		}

		public void setaTag(String aTag) {
			this.aTag = aTag;
		}

		public String getaKeyWord() {
			return aKeyWord;
		}

		public void setaKeyWord(String aKeyWord) {
			this.aKeyWord = aKeyWord;
		}

	}
}
