package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.AnswersListByQuestionsId;
import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MessageSpan;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.fragment.MyIssueFragment;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.LinkMovementMethodExt;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 选择答案
 * 
 * @author Johnson
 * 
 */
public class SelectRewardActivity extends Activity {

	private MyTitleView mtvTitle = null;
	private ZListView mListView = null;
	public static final int ANSWERS_MODEL = 9;
	private String UserToken = null;
	private QuestionsListByPage questions = null;
	private WindowManager wManager;// 窗口管理者
	private WindowManager.LayoutParams mParams;// 窗口的属性
	private List<AnswersListByQuestionsId> list = new ArrayList<AnswersListByQuestionsId>();
	private ArrayList<CreateFindWorkPhotoInfo> RemarkList = new ArrayList<CreateFindWorkPhotoInfo>();
	private MyAdapter<AnswersListByQuestionsId> messageAdapter = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int MY_SPANNED = 22;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private Spanned spanned;
	private LinearLayout layout;
	private int POSITION;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (messageAdapter != null) {
					messageAdapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					mListView.setStateNotData(true);
				}
				break;
			case LOAD_DATA_FINISH:
				if (messageAdapter != null) {
					messageAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {
					mListView.setStateNotData(false);
				} else {
					mListView.stopLoadMore(); // 加载更多完成
				}
				break;
			case MY_SPANNED:
				froumTopic(spanned);
				mListView.setAdapter(messageAdapter);

				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_reward);
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
		SharedPreferences preferences = SelectRewardActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		Intent intent = getIntent();
		POSITION = intent.getIntExtra("POSITION", 0);
		Bundle bundle = intent.getExtras();
		questions = (QuestionsListByPage) bundle.get("QuestionsListByPage");
		mtvTitle = (MyTitleView) findViewById(R.id.asr_title);
		mListView = (ZListView) findViewById(R.id.asr_comment);

		messageAdapter = new MyAdapter<AnswersListByQuestionsId>(this, list,
				R.layout.item_set_reward) {

			RoundImageView UserPhoto;
			TextView UserName, Time, Content;
			Button btSetScore;
			LinearLayout llRoom;

			@Override
			public void convert(ViewHolder view, final int position,
					final AnswersListByQuestionsId item) {
				UserPhoto = view.getView(R.id.isr_riv_user_photo);
				Time = view.getView(R.id.isr_tv_time);
				Content = view.getView(R.id.isr_tv_content);
				UserName = view.getView(R.id.isr_tv_user_name);
				llRoom = view.getView(R.id.isr_ll_room);
				btSetScore = view.getView(R.id.isr_bt_set_score);

				ImageLoader.getInstance()
						.displayImage(
								item.getMember().getMemberPhoto(),
								UserPhoto,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
				UserName.setText(item.getMember().getNickName());
				Time.setText(Tools.getTimestampString(Tools.strToDateT(item
						.getAnswers().getIssueTime())));
				Content.setText(Html.fromHtml(item.getAnswers().getaRemark())
						.toString().replaceAll("\t", "").replaceAll("\n", ""));
				btSetScore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						MyTextDialog(item.getAnswers().getId(), position);
					}
				});
				if (item.getAnswers().getIsAdoption() == 1) {
					btSetScore.setText(item.getAnswers().getScore() + "分 采纳");
					btSetScore.setEnabled(false);
					btSetScore.setVisibility(View.VISIBLE);
				} else if (item.getAnswers().getIsAdoption() == 0) {
					if (questions.getQuestions().getRewardPointsLeft() > 0) {
						btSetScore.setText("设置分数");
						btSetScore.setEnabled(true);
						btSetScore.setVisibility(View.VISIBLE);
					} else if (questions.getQuestions().getRewardPointsLeft() == 0) {
						btSetScore.setVisibility(View.GONE);
					}
				}
				llRoom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("AnswersListByQuestionsId",
								list.get(position));
						Intent intent = new Intent(SelectRewardActivity.this,
								CommentActivity.class);
						intent.putExtras(bundle);
						intent.putExtra("POSITION", position);
						startActivityForResult(intent, ANSWERS_MODEL);
					}
				});

			}
		};
		mListView.setPullLoadEnable(true);
		mtvTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSetResult();
				finish();
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				getMessage("1", "10", questions.getQuestions().getId() + "",
						REFRESH_DATA_FINISH);
				Count = 1;

			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", questions.getQuestions().getId()
						+ "", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});

		new Thread() {
			public void run() {
				// 访问网络代码
				spanned = Html.fromHtml(questions.getQuestions().getqRemark(),
						getter, null);
				mhandler.sendEmptyMessage(MY_SPANNED);
			}
		}.start();
		getMessage("1", "10", questions.getQuestions().getId() + "",
				MainActivity.LOAD_DATA);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mSetResult();
			finish();
		}
		return false;
	}

	private void mSetResult() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("QuestionsListByPage", questions);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.putExtra("POSITION", POSITION);
		setResult(MyIssueFragment.MY_ISSUE, intent);
	}

	private void froumTopic(Spanned spanned) {
		LayoutInflater layoutInflater = LayoutInflater
				.from(SelectRewardActivity.this);
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
		if (questions.getQuestions().getRewardPointsLeft() > 0) {
			mtvTitle.setText("剩余"
					+ questions.getQuestions().getRewardPointsLeft() + "分");
		} else if (questions.getQuestions().getRewardPointsLeft() == 0) {
			mtvTitle.setText("悬赏完毕");
		}
		ImageLoader.getInstance().displayImage(
				questions.getMember().getMemberPhoto(), holderTopic.UserPhoto,
				MyApplication.getInstance().getOptions(R.drawable.defa));
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
							Tools.imageBrower(SelectRewardActivity.this, 1,
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
			WindowManager wm = (WindowManager) SelectRewardActivity.this
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
	 * 加载数据
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void getMessage(String Page, String Size, String QuestionsId,
			final int State) {
		RequestParams params = new RequestParams(
				HttpUtil.AnswersListByQuestionsId.replace("{Size}", Size)
						.replace("{Page}", Page)
						.replace("{QuestionsId}", QuestionsId));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<AnswersListByQuestionsId> interfaceReturn = new InterfaceReturn<AnswersListByQuestionsId>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						AnswersListByQuestionsId.class);
				if (interfaceReturn.isStatus()) {
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						list.add(interfaceReturn.getResults().get(i));
					}
					messageAdapter.notifyDataSetChanged();
				}
				if (State == REFRESH_DATA_FINISH) {
					mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
				} else if (State == LOAD_DATA_FINISH) {
					mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				} else {
					messageAdapter.notifyDataSetChanged();
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

	/**
	 * Dialog
	 * 
	 * @param Id
	 * @param textView
	 */
	public void MyTextDialog(final int Id, final int position) {
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		LinearLayout linearLayout = (LinearLayout) layout
				.findViewById(R.id.ll_set_score);
		linearLayout.setVisibility(View.VISIBLE);
		final EditText et = (EditText) layout.findViewById(R.id.et_set_score);
		et.setHint("请输入悬赏分数");
		final TextView tv = (TextView) layout.findViewById(R.id.tv_set_score);
		tv.setText(questions.getQuestions().getRewardPointsLeft() + "");
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().trim().length() > 0) {
					if (Integer.parseInt(arg0.toString()) > questions
							.getQuestions().getRewardPointsLeft()) {
						T.showToast(SelectRewardActivity.this, "没有更多分数");
						et.setText(questions.getQuestions()
								.getRewardPointsLeft() + "");
					}
				}
				if (et.getText().toString().trim().length() > 0) {
					tv.setText(questions.getQuestions().getRewardPointsLeft()
							- Integer.parseInt(et.getText().toString()) + "");
				} else {
					tv.setText(questions.getQuestions().getRewardPointsLeft()
							+ "");
				}
			}
		});
		new HintDialog.Builder(layout.getContext())
				.setTitle("提示")
				.setContentView(layout)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String Text = null;
								if (et.getText().toString().trim().length() > 0) {
									Text = et.getText().toString().trim();
								} else {
									Text = "0";
								}
								AnswersSetInfo answersSetInfo = new AnswersSetInfo();
								ListAnswerSet answerSet = new ListAnswerSet();
								List<ListAnswerSet> answerSets = new ArrayList<ListAnswerSet>();
								answerSet.setAnswersID(Id);
								answerSet.setScore(Integer.parseInt(Text));
								answerSets.add(answerSet);
								answersSetInfo.setQuestionsID(questions
										.getQuestions().getId());
								answersSetInfo.setListAnswerSet(answerSets);
								Gson gson = new GsonBuilder().serializeNulls()
										.create();
								String JsonStr = gson.toJson(answersSetInfo);
								AnswersSetInfo(JsonStr, Integer.parseInt(Text),
										position);
								dialog.dismiss();
								return;
							}
						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create().show();
	}

	/**
	 * 当前登录用户设置最佳答案信息
	 * 
	 * @param json
	 */
	public void AnswersSetInfo(String json, final int Score, final int position) {
		RequestParams params = new RequestParams(HttpUtil.AnswersSetInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setCharset("utf-8");
		params.setBodyContent(json);
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<String> interfaceReturn = new InterfaceReturn<String>();
				Gson gson = new GsonBuilder().serializeNulls().create();
				interfaceReturn = gson.fromJson(result,
						interfaceReturn.getClass());
				if (interfaceReturn.isStatus()) {
					T.showToast(SelectRewardActivity.this,
							interfaceReturn.getMessage());
					questions.getQuestions().setRewardPointsLeft(
							questions.getQuestions().getRewardPointsLeft()
									- Score);
					list.get(position).getAnswers().setScore(Score);
					list.get(position).getAnswers().setIsAdoption(1);
					messageAdapter.notifyDataSetChanged();
					if (questions.getQuestions().getRewardPointsLeft() > 0) {
						mtvTitle.setText("剩余"
								+ questions.getQuestions()
										.getRewardPointsLeft() + "分");
					} else if (questions.getQuestions().getRewardPointsLeft() == 0) {
						mtvTitle.setText("悬赏完毕");
					}
				} else {
					T.showToast(SelectRewardActivity.this,
							interfaceReturn.getMessage());
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}
		});
	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				SelectRewardActivity.this, R.style.progress_dialog,
				getString(R.string.Loading));

		return dialog;
	}

	class AnswersSetInfo implements Serializable {
		private int questionsID;
		private List<ListAnswerSet> listAnswerSet;

		public int getQuestionsID() {
			return questionsID;
		}

		public void setQuestionsID(int questionsID) {
			this.questionsID = questionsID;
		}

		public List<ListAnswerSet> getListAnswerSet() {
			return listAnswerSet;
		}

		public void setListAnswerSet(List<ListAnswerSet> listAnswerSet) {
			this.listAnswerSet = listAnswerSet;
		}

	}

	class ListAnswerSet implements Serializable {
		private int answersID;
		private int score;

		public int getAnswersID() {
			return answersID;
		}

		public void setAnswersID(int answersID) {
			this.answersID = answersID;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
	}

}
