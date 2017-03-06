package com.qingzu.waterproof_work;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.entity.Tag;
import com.qingzu.uitest.view.CustomListView;
import com.qingzu.uitest.view.TagListView;
import com.qingzu.uitest.view.CustomListView.OnLoadListener;
import com.qingzu.uitest.view.TagListView.OnTagClickListener;
import com.qingzu.uitest.view.TagView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 问题搜索
 * 
 * @author Johnson
 * 
 */
public class ForumSearchActivity extends Activity implements OnClickListener {

	private ImageView ivFSBack = null;
	private EditText etFSFind = null;
	private Button btFSIssue = null;
	private TagListView TagView = null;
	private CustomListView mListView = null;
	private LinearLayout llTagview = null;

	private final List<Tag> mTags = new ArrayList<Tag>();
	private String[] titles = {};
	private SharedPreferences preferences = null;

	private String UserToken = null;

	private List<QuestionsListByPage> list = new ArrayList<QuestionsListByPage>();
	private MyAdapter<QuestionsListByPage> myAdapter = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int MY_SPANNED = 22;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				if (CountStr.equals(PageCount)) {
					mListView.onShowText();
				} else {
					mListView.onLoadComplete(); // 加载更多完成
				}
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum_search);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		titles = new String[0];
		mTags.clear();
		TagView.removeAllViews();
		preferences = this.getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		if (!preferences.getString("Tags", "").equals("")
				&& preferences.getString("Tags", "") != null) {
			titles = preferences.getString("Tags", "").split(",");
		}
		setUpData();
		if (mTags.size() > 0) {
			TagView.setTags(mTags);
		}
	}

	private void initView() {
		preferences = this.getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		if (!preferences.getString("Tags", "").equals("")) {
			titles = preferences.getString("Tags", "").split(",");
		}
		ivFSBack = (ImageView) findViewById(R.id.fs_back_img);
		etFSFind = (EditText) findViewById(R.id.fs_find);
		btFSIssue = (Button) findViewById(R.id.fs_issue);
		TagView = (TagListView) findViewById(R.id.tagview);
		mListView = (CustomListView) findViewById(R.id.fs_listview);
		llTagview = (LinearLayout) findViewById(R.id.fs_ll_tagview);

		myAdapter = new MyAdapter<QuestionsListByPage>(
				ForumSearchActivity.this, list, R.layout.forum_item) {

			ImageView UserPhoto;
			TextView UserName;
			TextView BrowseNumber;
			TextView Comment;
			TextView Title;
			TextView Content;
			TextView Time;
			LinearLayout Item;

			@Override
			public void convert(ViewHolder view, int position,
					QuestionsListByPage item) {
				UserPhoto = view.getView(R.id.fi_riv_user_photo);
				UserName = view.getView(R.id.fi_tv_user_name);
				BrowseNumber = view.getView(R.id.fi_tv_browse);
				Comment = view.getView(R.id.fi_tv_comment);
				Title = view.getView(R.id.fi_tv_title);
				Content = view.getView(R.id.fi_tv_content);
				Time = view.getView(R.id.fi_tv_time);
				Item = view.getView(R.id.fi_room);
				ImageLoaderUtil.loadXUtilImage(list.get(position).getMember()
						.getMemberPhoto(), UserPhoto);
				UserName.setText(list.get(position).getMember().getNickName());
				BrowseNumber.setText(list.get(position).getQuestions()
						.getBrowseCount()
						+ "");
				Comment.setText(list.get(position).getQuestions()
						.getAnswerCount()
						+ "");
				Title.setText(list.get(position).getQuestions().getqTitle());
				Content.setText(Html.fromHtml(list.get(position).getQuestions()
						.getqRemark().toString().replaceAll("\t", "")
						.replaceAll("\n", "")));
				Time.setText(Tools.getTimestampString(Tools.strToDateT(list
						.get(position).getQuestions().getIssueTime())));
				final QuestionsListByPage questions = list.get(position);
				Item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Commit(questions.getQuestions().getqTitle());
						Bundle bundle = new Bundle();
						bundle.putSerializable("QuestionsListByPage", questions);
						Intent intent = new Intent(ForumSearchActivity.this,
								ForumDetailsActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});

			}

		};

		etFSFind.addTextChangedListener(new TextWatcher() {

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
				if (arg0 != null && !arg0.toString().equals("")) {
					llTagview.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					list.clear();
					getMessage("1", "10", arg0 + "");
					mListView.setAdapter(myAdapter);
				} else {
					llTagview.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}

			}
		});

		mListView.setonLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// TODO 加载更多
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", etFSFind.getText().toString());
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				myAdapter.notifyDataSetChanged();
			}

		});

		setUpData();
		if (mTags.size() > 0) {
			TagView.setTags(mTags);
		}
		TagView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(TagView tagView, Tag tag) {
				etFSFind.setText(tagView.getText().toString().trim());
			}
		});
		ivFSBack.setOnClickListener(this);
		btFSIssue.setOnClickListener(this);
	}

	private void setUpData() {
		Tag tag = null;
		for (int i = 0; i < titles.length; i++) {
			tag = new Tag();
			tag.setId(i);
			tag.setChecked(true);
			tag.setTitle(titles[i]);
			mTags.add(tag);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.fs_back_img:
			finish();
			break;
		case R.id.fs_issue:
			TagView.removeAllViews();
			SharedPreferences.Editor editor = preferences.edit();
			editor.remove("Tags");
			editor.commit();
			break;

		default:
			break;
		}
	}

	/**
	 * 搜索问题列表
	 * 
	 * @param Page
	 * @param Size
	 * @param KeyWord
	 *            关键词
	 * @param ClassID
	 *            问题类别ID,传0表示所有类别
	 * @param SortType
	 *            排序类型：1，最新倒排；2，热门倒排
	 */
	public void getMessage(String Page, String Size, String Keyword) {
		String keyword = null;
		try {
			keyword = URLEncoder.encode(Keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(HttpUtil.QuestionsSearch
				.replace("{Page}", Page).replace("{ClassID}", "0")
				.replace("{SortType}", "2").replace("{Size}", Size)
				.replace("{Keyword}", keyword));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		// http://api.5ifangshui.com/api/Questions/ListByKeyWord/%E5%91%B5/1/10
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<QuestionsListByPage> interfaceReturn = new InterfaceReturn<QuestionsListByPage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						QuestionsListByPage.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));

						}

						myAdapter.notifyDataSetChanged();

					} else {
						T.showToast(ForumSearchActivity.this,
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

	protected void Commit(String Message) {
		if (!Message.equals("")) {
			Editor edit = preferences.edit();
			String Tags = preferences.getString("Tags", "");
			if (!Tags.equals("")) {
				String[] a = Tags.split(",");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < a.length; i++) {
					list.add(a[i].trim());
				}
				if (list.size() > 9) {
					list.remove(9);
				}
				boolean haha = true;
				int hehe = list.size();
				for (int i = 0; i < hehe; i++) {
					if (list.get(i).trim().equals(Message.trim())) {
						haha = false;
					}
				}
				if (haha) {
					list.add(0, Message.trim());
				}
				edit.putString(
						"Tags",
						list.toString().substring(1,
								list.toString().length() - 2));
			} else {
				edit.putString("Tags", Message.trim() + ",");
			}
			edit.commit();
		}
	}
}
