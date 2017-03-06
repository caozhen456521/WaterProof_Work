package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.AnswersListByQuestionsId;
import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.MemberDetails;
import com.qingzu.entity.MessageSpan;
import com.qingzu.entity.Questions;
import com.qingzu.entity.QuestionsListByPage;

import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;

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
import com.qingzu.waterproof_work.R.color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 回复详情
 * 
 * @author Johnson
 * 
 */
public class CommentActivity extends Activity implements OnClickListener {

	private MyTitleView caTitle;
	private ZListView mListView;
	private EditText etComment;
	private Button btSend;
	private AnswersListByQuestionsId answers;
	private int POSITION = 0;
	private int ReplyId = 0;
	private ArrayList<CreateFindWorkPhotoInfo> RemarkList = new ArrayList<CreateFindWorkPhotoInfo>();

	private ShowProgressDialog dialog = null;
	private ConnectionDetector cd;
	private String UserToken = null;

	private MyAdapter<CommentsListByReplyId> myAdapter = null;
	private List<CommentsListByReplyId> list = new ArrayList<CommentActivity.CommentsListByReplyId>();
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int MY_SPANNED = 22;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private LayoutInflater layoutInflater;
	private Spanned spanned;
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
				// else if (PageCount == 0) {
				// // mListView.setNoData();
				// }
				break;
			case LOAD_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {
					// mListView.onShowText();
					// System.out.println(Count);
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
		setContentView(R.layout.activity_comment);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		cd = new ConnectionDetector(this);
		SharedPreferences preferences = CommentActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(this);
		dialog = getProDialog();
		Intent intent = getIntent();
		POSITION = intent.getIntExtra("POSITION", 0);
		Bundle bundle = intent.getExtras();
		answers = (com.qingzu.entity.AnswersListByQuestionsId) bundle
				.get("AnswersListByQuestionsId");
		caTitle = (MyTitleView) findViewById(R.id.ac_mtv_title);
		etComment = (EditText) findViewById(R.id.ac_et_text);
		btSend = (Button) findViewById(R.id.ac_bt_send);
		mListView = (ZListView) findViewById(R.id.ac_reply_info);
		mListView.setPullLoadEnable(true);
		btSend.setOnClickListener(this);
		caTitle.setText((POSITION + 1) + "楼");

		myAdapter = new MyAdapter<CommentsListByReplyId>(CommentActivity.this,
				list, R.layout.comment_item) {
			TextView time;
			TextView textView;
			LinearLayout llItem;

			@Override
			public void convert(com.qingzu.utils.tools.ViewHolder view,
					final int position, CommentsListByReplyId item) {
				time = view.getView(R.id.ci_tv_time);
				textView = view.getView(R.id.ci_tv_text);
				llItem = view.getView(R.id.ci_ll_room);
				time.setText(Tools.getTimestampString(Tools.strToDateT(list
						.get(position).getComments().getCreateTime())));
				if (list.get(position).getReplyMember() != null) {
					String str1 = list.get(position).getMember().getNickName()
							+ ": ";
					String str2 = list.get(position).getReplyMember()
							.getNickName()
							+ ": ";
					SpannableStringBuilder builder = new SpannableStringBuilder();
					SpannableString spanString = new SpannableString(str1
							+ "回复 ");
					ForegroundColorSpan span = new ForegroundColorSpan(
							color.comment_color);
					spanString.setSpan(span, 0, str1.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(spanString);

					SpannableString spannableString = new SpannableString(str2);
					ForegroundColorSpan span2 = new ForegroundColorSpan(
							color.comment_color);
					spannableString.setSpan(span2, 0, str2.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(spannableString);
					builder.append(list.get(position).getComments()
							.getCommentsContent());
					textView.setText(builder);
				} else {
					SpannableString spanString = new SpannableString(list
							.get(position).getMember().getNickName()
							+ ": "
							+ list.get(position).getComments()
									.getCommentsContent());
					ForegroundColorSpan span = new ForegroundColorSpan(
							color.comment_color);
					spanString.setSpan(span, 0, list.get(position).getMember()
							.getNickName().length() + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textView.setText(spanString);
				}

				llItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						etComment.setHint("回复:"
								+ list.get(position).getMember().getNickName()
								+ "...");
						etComment.setFocusable(true);
						etComment.requestFocus();
						ReplyId = list.get(position).getComments().getId();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				});

			}

		};

		caTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("AnswersListByQuestionsId", answers);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.putExtra("POSITION", POSITION);
				setResult(ForumDetailsActivity.ANSWERS_MODEL, intent);
				finish();
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMessage("1", "10", answers.getAnswers().getId() + "",
						REFRESH_DATA_FINISH);
				Count = 1;
				// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", answers.getAnswers().getId() + "",
						LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				myAdapter.notifyDataSetChanged();

			}
		});

		new Thread() {
			public void run() {
				// 访问网络代码
				spanned = Html.fromHtml(answers.getAnswers().getaRemark(),
						getter, null);
				mhandler.sendEmptyMessage(MY_SPANNED);
			}
		}.start();
		getMessage("1", "10", answers.getAnswers().getId() + "",
				MainActivity.LOAD_DATA);

	}

	class UserInfoByID implements Serializable {
		private Member member;
		private MemberDetails memberDetails;

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}

		public MemberDetails getMemberDetails() {
			return memberDetails;
		}

		public void setMemberDetails(MemberDetails memberDetails) {
			this.memberDetails = memberDetails;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ac_bt_send:
			// 登录
			if (UserToken != null && !UserToken.equals("")) {
				CreateComments(arg0);
			} else {
				HintDialog.Builder builder = new HintDialog.Builder(
						CommentActivity.this);
				builder.setTitle(R.string.Hint);
				builder.setMessage("您还没有登录!是否登录");
				builder.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								startActivity(new Intent(CommentActivity.this,
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

	private void CreateComments(View view) {
		if (etComment.getText().toString().trim().length() > 0) {
			CreateCommentsInfo commentsInfo = new CreateCommentsInfo();
			commentsInfo.setTypeId(2);
			commentsInfo.setInfoId(answers.getAnswers().getId());
			commentsInfo.setReplyId(ReplyId);
			commentsInfo.setCommentsContent(etComment.getText().toString());
			Gson gson = new GsonBuilder().serializeNulls().create();
			String JsonStr = gson.toJson(commentsInfo);
			CreateCommentsInfo(JsonStr, view);
		} else {
			T.showToast(CommentActivity.this, "回复内容不能为空");
		}
	}

	/**
	 * 当前登录用户提交评论信息
	 * 
	 * @param json
	 */
	public void CreateCommentsInfo(String json, final View view) {

		RequestParams params = new RequestParams(HttpUtil.CreateComments);
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
						T.showToast(CommentActivity.this,
								interfaceReturn.getMessage());
						etComment.setText("");
						conceal(view);
						list.clear();
						getMessage("1", "10",
								answers.getAnswers().getId() + "",
								MainActivity.LOAD_DATA);
						Count = 1;
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
					} else {
						T.showToast(CommentActivity.this,
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
	 * 
	 * @param v
	 */
	protected void conceal(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		etComment.setHint("回复内容...");
		ReplyId = 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("AnswersListByQuestionsId", answers);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.putExtra("POSITION", POSITION);
			setResult(ForumDetailsActivity.ANSWERS_MODEL, intent);
			finish();
		}
		return false;
	}

	private void froumTopic(Spanned spanned) {
		LayoutInflater layoutInflater = LayoutInflater
				.from(CommentActivity.this);
		View view = layoutInflater.inflate(R.layout.forum_answer_item, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.UserPhoto = (ImageView) view
				.findViewById(R.id.fai_riv_user_photo);
		viewHolder.Time = (TextView) view.findViewById(R.id.fai_tv_time);
		viewHolder.tvPraise = (TextView) view.findViewById(R.id.fai_tv_praise);
		viewHolder.btPraise = (Button) view.findViewById(R.id.fai_bt_praise);
		viewHolder.Comment = (TextView) view.findViewById(R.id.fai_tv_comment);
		viewHolder.Content = (TextView) view.findViewById(R.id.fai_tv_content);
		viewHolder.UserName = (TextView) view
				.findViewById(R.id.fai_tv_user_name);
		viewHolder.llRoom = (LinearLayout) view.findViewById(R.id.fai_ll_room);
		view.setTag(viewHolder);

		ImageLoaderUtil.loadXUtilImage(answers.getMember().getMemberPhoto(),
				viewHolder.UserPhoto);

		viewHolder.UserName.setText(answers.getMember().getNickName());
		viewHolder.Time.setText(Tools.getTimestampString(Tools
				.strToDateT(answers.getAnswers().getIssueTime())));
		viewHolder.tvPraise.setText(answers.getAnswers().getLikeCount() + "");
		// viewHolder.Comment
		// .setText(answers.getAnswers().getCommentsCount() + "");
		if (answers.isLike()) {
			viewHolder.btPraise.setBackgroundResource(R.drawable.praise);
		} else {
			viewHolder.btPraise.setBackgroundResource(R.drawable.unpraise);
		}
		viewHolder.btPraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (UserToken != null && !UserToken.equals("")) {
					if (answers.isLike()) {
						AnswersInfoByCancelLikeCount(answers.getAnswers()
								.getId() + "", viewHolder.btPraise,
								viewHolder.tvPraise);
					} else {
						AnswersInfoByLikeCount(answers.getAnswers().getId()
								+ "", viewHolder.btPraise, viewHolder.tvPraise);
					}
				} else {
					HintDialog.Builder builder = new HintDialog.Builder(
							CommentActivity.this);
					builder.setTitle(R.string.Hint);
					builder.setMessage("您还没有登录!是否登录");
					builder.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									startActivity(new Intent(
											CommentActivity.this,
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
			}
		});
		viewHolder.Content.setMovementMethod(LinkMovementMethod.getInstance());
		viewHolder.Content.setText(spanned);
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
							Tools.imageBrower(CommentActivity.this, 1,
									RemarkList);
						}
					}
				}
			};
		};
		viewHolder.Content.setMovementMethod(LinkMovementMethodExt.getInstance(
				handler, ImageSpan.class));
		mListView.addHeaderView(view);
	}

	class ViewHolder {
		ImageView UserPhoto;
		TextView UserName, Time, tvPraise, Comment, Content;
		Button btPraise;
		LinearLayout llRoom;
	}

	ImageGetter getter = new ImageGetter() {

		@Override
		public Drawable getDrawable(String arg0) {
			Bitmap bitmap = ImageLoader.getInstance().loadImageSync(arg0,
					MyApplication.getInstance().getOptions(R.drawable.defa));

			Drawable drawable = new BitmapDrawable(getResources(), bitmap);
			WindowManager wm = (WindowManager) CommentActivity.this
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

	/**
	 * 当前登录用户对答案数据信息进行点赞
	 * 
	 * @param Id
	 */
	public void AnswersInfoByLikeCount(String Id, final Button button,
			final TextView textView) {

		RequestParams params = new RequestParams(
				HttpUtil.AnswersInfoByLikeCount.replace("{ID}", Id));
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
						T.showToast(CommentActivity.this,
								interfaceReturn.getMessage());
						button.setBackgroundResource(R.drawable.praise);
						answers.setLike(true);
						answers.getAnswers().setLikeCount(
								answers.getAnswers().getLikeCount() + 1);
						textView.setText(answers.getAnswers().getLikeCount()
								+ "");
					} else {
						T.showToast(CommentActivity.this,
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
	public void AnswersInfoByCancelLikeCount(String Id, final Button button,
			final TextView textView) {

		RequestParams params = new RequestParams(
				HttpUtil.AnswersInfoByCancelLikeCount.replace("{ID}", Id));
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
						T.showToast(CommentActivity.this,
								interfaceReturn.getMessage());
						button.setBackgroundResource(R.drawable.unpraise);
						answers.setLike(false);
						answers.getAnswers().setLikeCount(
								answers.getAnswers().getLikeCount() - 1);
						textView.setText(answers.getAnswers().getLikeCount()
								+ "");
					} else {
						T.showToast(CommentActivity.this,
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
	 * 根据信息类型和信息ID获取评论列表
	 * 
	 * @param Page
	 * @param Size
	 * @param InfoID
	 * @param State
	 */
	public void getMessage(String Page, String Size, String InfoID,
			final int State) {

		RequestParams params = new RequestParams(HttpUtil.CommentsPageList
				.replace("{Page}", Page).replace("{Size}", Size)
				.replace("{InfoID}", InfoID).replace("{TypeID}", "2"));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<CommentsListByReplyId> interfaceReturn = new InterfaceReturn<CommentsListByReplyId>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						CommentsListByReplyId.class);
				PageCount = interfaceReturn.getResultsCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
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
						T.showToast(CommentActivity.this,
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

	class CommentsListByReplyId {
		private Comments comments;
		private Member member;
		private Member replyMember;

		public Member getReplyMember() {
			return replyMember;
		}

		public void setReplyMember(Member replyMember) {
			this.replyMember = replyMember;
		}

		public Comments getComments() {
			return comments;
		}

		public void setComments(Comments comments) {
			this.comments = comments;
		}

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				CommentActivity.this, R.style.progress_dialog,
				getString(R.string.Loading));
		return dialog;
	}

	class CreateCommentsInfo {
		private int typeId;
		private int infoId;
		private String commentsContent;
		private int replyId;

		public int getTypeId() {
			return typeId;
		}

		public void setTypeId(int typeId) {
			this.typeId = typeId;
		}

		public int getInfoId() {
			return infoId;
		}

		public void setInfoId(int infoId) {
			this.infoId = infoId;
		}

		public String getCommentsContent() {
			return commentsContent;
		}

		public void setCommentsContent(String commentsContent) {
			this.commentsContent = commentsContent;
		}

		public int getReplyId() {
			return replyId;
		}

		public void setReplyId(int replyId) {
			this.replyId = replyId;
		}

	}

	class Comments {
		private int id;
		private int typeId;
		private int infoId;
		private String commentsContent;
		private int memberId;
		private String createTime;
		private int replyId;
		private int likeCount;
		private boolean state;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getTypeId() {
			return typeId;
		}

		public void setTypeId(int typeId) {
			this.typeId = typeId;
		}

		public int getInfoId() {
			return infoId;
		}

		public void setInfoId(int infoId) {
			this.infoId = infoId;
		}

		public String getCommentsContent() {
			return commentsContent;
		}

		public void setCommentsContent(String commentsContent) {
			this.commentsContent = commentsContent;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public int getReplyId() {
			return replyId;
		}

		public void setReplyId(int replyId) {
			this.replyId = replyId;
		}

		public int getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(int likeCount) {
			this.likeCount = likeCount;
		}

		public boolean isState() {
			return state;
		}

		public void setState(boolean state) {
			this.state = state;
		}
	}

}
