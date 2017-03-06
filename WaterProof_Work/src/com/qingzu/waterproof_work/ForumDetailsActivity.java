package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.xutils.x;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.Answers;
import com.qingzu.entity.AnswersListByQuestionsId;
import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.MessageSpan;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.LinkMovementMethodExt;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 问题详情
 * 
 * @author Johnson
 * 
 */
public class ForumDetailsActivity extends Activity implements OnClickListener {
	private MyTitleView FDTitle = null;
	private ZListView mListView = null;
	private Button btSend = null;
	public static final int ANSWERS_MODEL = 9;
	private WindowManager wManager;// 窗口管理者
	private WindowManager.LayoutParams mParams;// 窗口的属性
	private ShowProgressDialog dialog = null;
	private ConnectionDetector cd;
	private String UserToken = null;
	private QuestionsListByPage questions = null;
	private LayoutInflater layoutInflater = null;
	private List<AnswersListByQuestionsId> list = new ArrayList<AnswersListByQuestionsId>();
	private ArrayList<CreateFindWorkPhotoInfo> RemarkList = new ArrayList<CreateFindWorkPhotoInfo>();

	private MyAdapter<AnswersListByQuestionsId> myAdapter = null;

	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int MY_SPANNED = 22;
	public static final int SET_RESULT = 7;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private Spanned spanned;
	private LinearLayout layout;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					mListView.setStateNotData(true);
				}

				break;
			case LOAD_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {

					System.out.println("Count==========================="
							+ Count);
					System.out.println("PageCount==========================="
							+ PageCount);
					mListView.setStateNotData(false);
				} else {
					mListView.stopLoadMore(); // 加载更多完成
				}
				break;
			case MY_SPANNED:
				froumTopic(spanned);
				mListView.setAdapter(myAdapter);

				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum_details);
		AppManager.getAppManager().addActivity(this);
		setView();
		initView();

	}

	public void setView() {
		layout = new LinearLayout(this);
		wManager = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		mParams = new WindowManager.LayoutParams();
		mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
		mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
		// mParams.format = PixelFormat.RGBA_8888;
		mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
		mParams.width = this.getWindowManager().getDefaultDisplay().getWidth();// 窗口的宽和高
		mParams.height = this.getWindowManager().getDefaultDisplay()
				.getHeight();
		mParams.x = 0;// 窗口位置的偏移量
		mParams.y = 0;
		wManager.addView(layout, mParams);
	}

	private void initView() {
		SharedPreferences preferences = ForumDetailsActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		cd = new ConnectionDetector(this);
		dialog = getProDialog();
		layoutInflater = LayoutInflater.from(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		questions = (QuestionsListByPage) bundle.get("QuestionsListByPage");
		FDTitle = (MyTitleView) findViewById(R.id.fd_title);
		mListView = (ZListView) findViewById(R.id.fd_comment);
		btSend = (Button) findViewById(R.id.fd_bt_send);
		mListView.setPullLoadEnable(true);
		btSend.setOnClickListener(this);

		myAdapter = new MyAdapter<AnswersListByQuestionsId>(
				ForumDetailsActivity.this, list, R.layout.forum_answer_item) {

			ImageView UserPhoto;
			TextView UserName, Time, tvPraise, Comment, Content;
			Button btPraise;
			LinearLayout llRoom;

			@Override
			public void convert(ViewHolder view, final int position,
					AnswersListByQuestionsId item) {
				// TODO Auto-generated method stub
				UserPhoto = view.getView(R.id.fai_riv_user_photo);
				UserName = view.getView(R.id.fai_tv_user_name);
				Time = view.getView(R.id.fai_tv_time);
				tvPraise = view.getView(R.id.fai_tv_praise);
				Comment = view.getView(R.id.fai_tv_comment);
				Content = view.getView(R.id.fai_tv_content);
				btPraise = view.getView(R.id.fai_bt_praise);
				llRoom = view.getView(R.id.fai_ll_room);

				ImageLoaderUtil.loadXUtilImage(list.get(position).getMember()
						.getMemberPhoto(), UserPhoto);

				UserName.setText(list.get(position).getMember().getNickName());
				Time.setText(Tools.getTimestampString(Tools.strToDateT(list
						.get(position).getAnswers().getIssueTime())));

				tvPraise.setText(list.get(position).getAnswers().getLikeCount()
						+ "");

				Content.setText(Html
						.fromHtml(list.get(position).getAnswers().getaRemark())
						.toString().replaceAll("\t", "").replaceAll("\n", ""));
				final AnswersListByQuestionsId answers = list.get(position);

				if (list.get(position).isLike()) {
					btPraise.setBackgroundResource(R.drawable.praise);
				} else {
					btPraise.setBackgroundResource(R.drawable.unpraise);
				}

				btPraise.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (UserToken != null && !UserToken.equals("")) {
							if (answers.isLike()) {
								AnswersInfoByCancelLikeCount(answers
										.getAnswers().getId() + "", position);
							} else {
								AnswersInfoByLikeCount(answers.getAnswers()
										.getId() + "", position);
							}
						} else {
							HintDialog.Builder builder = new HintDialog.Builder(
									ForumDetailsActivity.this);
							builder.setTitle(R.string.Hint);
							builder.setMessage("您还没有登录!是否登录");
							builder.setPositiveButton(R.string.Yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											startActivity(new Intent(
													ForumDetailsActivity.this,
													LoginActivity.class));
											dialog.dismiss();
										}
									});
							builder.setNegativeButton(R.string.No,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.dismiss();
										}
									});
							builder.create().show();
						}

					}
				});
				Comment.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("AnswersListByQuestionsId",
								answers);
						Intent intent = new Intent(ForumDetailsActivity.this,
								CommentActivity.class);
						intent.putExtras(bundle);
						intent.putExtra("POSITION", position);
						startActivityForResult(intent, ANSWERS_MODEL);
					}
				});

				llRoom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Bundle bundle = new Bundle();
						bundle.putSerializable("AnswersListByQuestionsId",
								answers);
						Intent intent = new Intent(ForumDetailsActivity.this,
								CommentActivity.class);
						intent.putExtras(bundle);
						intent.putExtra("POSITION", position);
						startActivityForResult(intent, ANSWERS_MODEL);
					}
				});

			}

		};

		FDTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMessage("1", "10", questions.getQuestions().getId() + "",
						REFRESH_DATA_FINISH);
				Count = 1;
				// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// TODO 加载更多
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", questions.getQuestions().getId()
						+ "", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				myAdapter.notifyDataSetChanged();
			}
		});
		dialog();
		getMessage("1", "10", questions.getQuestions().getId() + "",
				MainActivity.LOAD_DATA);
	}

	public void dialog() {
		// mListView.sethide();
		new Thread() {
			public void run() {
				// 访问网络代码
				spanned = Html.fromHtml(questions.getQuestions().getqRemark(),
						getter, null);
				mhandler.sendEmptyMessage(MY_SPANNED);
			}
		}.start();

	}

	private void froumTopic(Spanned spanned) {
		LayoutInflater layoutInflater = LayoutInflater
				.from(ForumDetailsActivity.this);
		View view = layoutInflater.inflate(R.layout.layout_forum_topic, null);
		ViewHolderTopic holderTopic = new ViewHolderTopic();
		holderTopic.Browse = (TextView) view.findViewById(R.id.lft_tv_browse);
		holderTopic.Comment = (TextView) view.findViewById(R.id.lft_tv_comment);
		holderTopic.Content = (TextView) view.findViewById(R.id.lft_tv_content);
		holderTopic.Time = (TextView) view.findViewById(R.id.lft_tv_time);
		holderTopic.Title = (TextView) view.findViewById(R.id.lft_tv_title);
		holderTopic.UserName = (TextView) view
				.findViewById(R.id.lft_tv_user_name);
		holderTopic.UserPhoto = (ImageView) view
				.findViewById(R.id.lft_riv_user_photo);

		view.setTag(holderTopic);

		ImageLoaderUtil.loadXUtilImage(questions.getMember().getMemberPhoto(),
				holderTopic.UserPhoto);
		holderTopic.UserName.setText(questions.getMember().getNickName());
		holderTopic.Comment.setText(questions.getQuestions().getAnswerCount()
				+ "");
		holderTopic.Browse.setText(questions.getQuestions().getBrowseCount()
				+ "");
		holderTopic.Title.setText(questions.getQuestions().getqTitle());
		holderTopic.Content.setMovementMethod(LinkMovementMethod.getInstance());

		holderTopic.Content.setText(spanned);
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				int what = msg.what;
				if (what == 200) {
					MessageSpan ms = (MessageSpan) msg.obj;
					Object[] spans = (Object[]) ms.getObj();

					for (Object span : spans) {
						if (span instanceof ImageSpan) {
							RemarkList.clear();
							CreateFindWorkPhotoInfo workPhotoInfo = new CreateFindWorkPhotoInfo();
							workPhotoInfo.setPhotoUrl(((ImageSpan) span)
									.getSource());
							RemarkList.add(workPhotoInfo);
							Tools.imageBrower(ForumDetailsActivity.this, 1,
									RemarkList);
						}
					}
				}
			};
		};
		holderTopic.Content.setMovementMethod(LinkMovementMethodExt
				.getInstance(handler, ImageSpan.class));

		holderTopic.Time.setText(Tools.getTimestampString(Tools
				.strToDateT(questions.getQuestions().getIssueTime())));
		wManager.removeView(layout);
		mListView.addHeaderView(view);
	}

	class ViewHolderTopic {
		TextView Title, Browse, Comment, UserName, Time, Content;
		ImageView UserPhoto;
	}

	ImageGetter getter = new ImageGetter() {

		@Override
		public Drawable getDrawable(String arg0) {
			Bitmap bitmap = ImageLoader.getInstance().loadImageSync(arg0,
					MyApplication.getInstance().getOptions(R.drawable.defa));

			Drawable drawable = new BitmapDrawable(getResources(), bitmap);
			WindowManager wm = (WindowManager) ForumDetailsActivity.this
					.getSystemService(Context.WINDOW_SERVICE);
			double Width = drawable.getIntrinsicWidth();
			double Height = drawable.getIntrinsicHeight();
			double Tw = wm.getDefaultDisplay().getWidth();
			double w = Width / Tw;
			if (Width > Tw) {
				Width = Tw - 100;
				Height = Height / w - 100;
			}
			drawable.setBounds(0, 0, (int) Width, (int) Height);
			return drawable;
		}
	};

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (arg0.getId()) {
		case R.id.fd_bt_send:
			if (UserToken != null && !UserToken.equals("")) {
				intent.setClass(ForumDetailsActivity.this,
						ReleaseIssueActivity.class);
				intent.putExtra("FROM_ACTIVITY", "FORUM_DETAILS");
				bundle.putSerializable("QuestionsListByPage", questions);
				intent.putExtras(bundle);
				startActivityForResult(intent, SET_RESULT);
			} else {
				HintDialog.Builder builder = new HintDialog.Builder(
						ForumDetailsActivity.this);
				builder.setTitle(R.string.Hint);
				builder.setMessage("您还没有登录!是否登录");
				builder.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								startActivity(new Intent(
										ForumDetailsActivity.this,
										LoginActivity.class));
								dialog.dismiss();
							}
						});
				builder.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 加载数据
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void getMessage(String Page, String Size, String QuestionsId,
			final int State) {
		RequestParams params = new RequestParams(
				HttpUtil.AnswersListByQuestionsId.replace("{Page}", Page)
						.replace("{Size}", Size)
						.replace("{QuestionsId}", QuestionsId));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<AnswersListByQuestionsId> interfaceReturn = new InterfaceReturn<AnswersListByQuestionsId>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						AnswersListByQuestionsId.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));

						}

						if (State == REFRESH_DATA_FINISH) {
							mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
							// mListView.onShowText();
							// System.out.println(Count);
							// mListView.setStateNotData();
						} else if (State == LOAD_DATA_FINISH) {
							//

							mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
						} else {
							myAdapter.notifyDataSetChanged();
						}
					} else {
						T.showToast(ForumDetailsActivity.this,
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ANSWERS_MODEL) {
			Bundle bundle = data.getExtras();
			AnswersListByQuestionsId answersListByQuestionsId = (AnswersListByQuestionsId) bundle
					.get("AnswersListByQuestionsId");
			list.get(data.getIntExtra("POSITION", 0)).setAnswers(
					answersListByQuestionsId.getAnswers());
			list.get(data.getIntExtra("POSITION", 0)).setLike(
					answersListByQuestionsId.isLike());
			myAdapter.notifyDataSetChanged();
		}
		if (resultCode == SET_RESULT) {
			list.clear();
			getMessage("1", "10", questions.getQuestions().getId() + "",
					REFRESH_DATA_FINISH);
			Count = 1;
			// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
		}
	}

	/**
	 * 当前登录用户对答案数据信息进行点赞
	 * 
	 * @param Id
	 */
	public void AnswersInfoByLikeCount(String ID, final int Position) {

		RequestParams params = new RequestParams(
				HttpUtil.AnswersInfoByLikeCount.replace("{ID}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ForumDetailsActivity.this,
								interfaceReturn.getMessage());
						list.get(Position).setLike(true);
						list.get(Position)
								.getAnswers()
								.setLikeCount(
										list.get(Position).getAnswers()
												.getLikeCount() + 1);
						myAdapter.notifyDataSetChanged();
					} else {
						T.showToast(ForumDetailsActivity.this,
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
	 * 当前登录用户对答案数据信息取消点赞
	 * 
	 * @param Id
	 */
	public void AnswersInfoByCancelLikeCount(String ID, final int Position) {

		RequestParams params = new RequestParams(
				HttpUtil.AnswersInfoByCancelLikeCount.replace("{ID}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ForumDetailsActivity.this,
								interfaceReturn.getMessage());
						list.get(Position).setLike(false);
						list.get(Position)
								.getAnswers()
								.setLikeCount(
										list.get(Position).getAnswers()
												.getLikeCount() - 1);
						myAdapter.notifyDataSetChanged();
					} else {
						T.showToast(ForumDetailsActivity.this,
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
				ForumDetailsActivity.this, R.style.progress_dialog,
				getString(R.string.Loading));

		return dialog;
	}

}
