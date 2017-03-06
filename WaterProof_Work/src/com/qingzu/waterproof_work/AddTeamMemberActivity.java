package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.droid.PingYinUtil;
import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.entity.Contacts;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyLetterListView;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.MyLetterListView.OnTouchingLetterChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddTeamMemberActivity extends Activity implements OnScrollListener {

	private List<HashMap<String, String>> list = null;

	private MyTitleView aatm_title = null;
	private LinearLayout aatm_ll_compile = null;
	private LinearLayout aatm_ll_commit = null;
	private EditText aatm_et_name = null;
	private EditText aatm_et_phone = null;
	private Button aatm_bt_manual_add = null;
	private Button aatm_bt_cancel = null;
	private Button aatm_bt_commit = null;
	private BaseAdapter adapter;
	private ListView personList;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread; // 显示首字母对话框
	private ArrayList<Contacts> allContacts_lists;
	private ArrayList<Contacts> contacts_lists;
	private HashMap<Integer, Contacts> checkeds;

	private List<teamM> teamList = null;
	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_team_member);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = AddTeamMemberActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		aatm_title = (MyTitleView) findViewById(R.id.aatm_title);
		personList = (ListView) findViewById(R.id.aatm_lv_list_view);
		aatm_ll_compile = (LinearLayout) findViewById(R.id.aatm_ll_compile);
		aatm_ll_commit = (LinearLayout) findViewById(R.id.aatm_ll_commit);
		aatm_et_name = (EditText) findViewById(R.id.aatm_et_name);
		aatm_et_phone = (EditText) findViewById(R.id.aatm_et_phone);
		aatm_bt_manual_add = (Button) findViewById(R.id.aatm_bt_manual_add);
		aatm_bt_cancel = (Button) findViewById(R.id.aatm_bt_cancel);
		aatm_bt_commit = (Button) findViewById(R.id.aatm_bt_commit);

		aatm_bt_manual_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aatm_ll_compile.setVisibility(View.VISIBLE);
				aatm_ll_commit.setVisibility(View.VISIBLE);
				aatm_bt_manual_add.setVisibility(View.GONE);
			}
		});
		aatm_bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aatm_ll_compile.setVisibility(View.GONE);
				aatm_ll_commit.setVisibility(View.GONE);
				aatm_bt_manual_add.setVisibility(View.VISIBLE);
			}
		});
		aatm_bt_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				manualAddCommit();
			}
		});

		aatm_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		aatm_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				commit();
			}
		});

		allContacts_lists = new ArrayList<Contacts>();

		letterListView = (MyLetterListView) findViewById(R.id.aatm_MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		checkeds = new HashMap<Integer, Contacts>();

		personList.setAdapter(adapter);
		personList.setOnScrollListener(this);
		initOverlay();
		contacts_lists = getPeopleInPhone();
		allContacts_lists.addAll(contacts_lists);
		adapter = new ListAdapter(this, allContacts_lists);
		personList.setAdapter(adapter);
	}

	class teamM implements Serializable {
		private int teamId;
		private String teamName;
		private String contactTel;
		private String contactName;

		public int getTeamId() {
			return teamId;
		}

		public void setTeamId(int teamId) {
			this.teamId = teamId;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public String getContactTel() {
			return contactTel;
		}

		public void setContactTel(String contactTel) {
			this.contactTel = contactTel;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public teamM(int teamId, String teamName, String contactTel,
				String contactName) {
			super();
			this.teamId = teamId;
			this.teamName = teamName;
			this.contactTel = contactTel;
			this.contactName = contactName;
		}

		public teamM() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	/**
	 * 手动添加提交
	 * 
	 * @author Johnson
	 */
	protected void manualAddCommit() {
		if (aatm_et_name.getText().toString().trim().length() <= 0) {
			T.showToast(this, "请输入姓名");
			return;
		} else if (aatm_et_phone.getText().toString().trim().length() <= 0) {
			T.showToast(this, "请输入手机号码");
			return;
		} else if (!Tools.isMobileNO(aatm_et_phone.getText().toString().trim())) {
			T.showToast(this, "手机号码不正确");
			return;
		} else {
			teamM m = new teamM();
			m.setTeamId(MainActivity.constructionTeam.getId());
			m.setTeamName(MainActivity.constructionTeam.getTeamName());
			m.setContactName(aatm_et_name.getText().toString().trim());
			m.setContactTel(aatm_et_phone.getText().toString().trim());
			teamList = new ArrayList<AddTeamMemberActivity.teamM>();
			teamList.add(0, m);
			Gson gson = new Gson();
			CreateInviteJoinTeam(gson.toJson(teamList));
		}
	}

	/**
	 * 当前登录用户提交工长邀请工人入队记录信息
	 * 
	 * @param json
	 * @author Johnson
	 */
	private void CreateInviteJoinTeam(String json) {
		RequestParams params = new RequestParams(HttpUtil.CreateInviteJoinTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setCharset("utf-8");
		params.setBodyContent(json);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(AddTeamMemberActivity.this,
								interfaceReturn.getMessage());
						setResult(TeamManageActivity.ADD_TEAM_MEMBER);
						finish();
					} else {
						T.showToast(AddTeamMemberActivity.this,
								interfaceReturn.getMessage());
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

	/**
	 * 通讯录选择提交
	 * 
	 * @author Johnson
	 */
	protected void commit() {
		if (checkeds != null && checkeds.size() > 0) {
			teamM m = null;
			teamList = new ArrayList<AddTeamMemberActivity.teamM>();
			for (Integer key : checkeds.keySet()) {
				m = new teamM();
				m.setTeamId(MainActivity.constructionTeam.getId());
				m.setTeamName(MainActivity.constructionTeam.getTeamName());
				m.setContactName(checkeds.get(key).getName());
				m.setContactTel(checkeds.get(key).getPhone());
				teamList.add(0, m);
			}
			Gson gson = new Gson();
			CreateInviteJoinTeam(gson.toJson(teamList));
		} else {
			T.showToast(AddTeamMemberActivity.this, "请勾选或手动添加联系人");
		}
	}

	public class ListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<Contacts> list;

		public ListAdapter(Context context, List<Contacts> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];// /
			for (int i = 0; i < list.size(); i++) {
				String currentStr = getAlpha(list.get(i).getPinyi());
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.layout_select_contacts_list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkbox);
				// holder.item = (RelativeLayout) convertView
				// .findViewById(R.id.rl_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.phone.setText(list.get(position).getPhone());
			holder.name.setText(list.get(position).getName());
			holder.checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							if (arg1) {
								checkeds.put(position,
										new Contacts(list.get(position)
												.getName(), list.get(position)
												.getPhone(), list.get(position)
												.getPinyi()));
							} else {
								checkeds.remove(position);
							}
						}
					});
			holder.checkbox.setChecked(checkeds.get(position) != null ? true
					: false);
			String currentStr = getAlpha(list.get(position).getPinyi());
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
					position - 1).getPinyi()) : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
			TextView phone;
			CheckBox checkbox;
			RelativeLayout item;
		}
	}

	private boolean isScroll = false;

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1000);
			}
		}
	}

	// 获得汉语拼音首字母
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("unchecked")
	private ArrayList<Contacts> getPeopleInPhone() {
		ArrayList<Contacts> list = new ArrayList<Contacts>();

		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null); // 获取手机联系人
		Contacts contacts = null;
		while (cursor.moveToNext()) {
			contacts = new Contacts();
			int indexPeopleName = cursor.getColumnIndex(Phone.DISPLAY_NAME);
			int indexPhoneNum = cursor.getColumnIndex(Phone.NUMBER);

			String strPeopleName = cursor.getString(indexPeopleName);
			String strPhoneNum = cursor.getString(indexPhoneNum);

			if (strPeopleName != null && !strPeopleName.equals("")) {
				if (strPhoneNum != null && !strPhoneNum.equals("")) {
					String str = Tools.getIntger(strPhoneNum);
					if (Tools.validatePhone(str)) {
						contacts.setName(strPeopleName);
						contacts.setPhone(str);
						contacts.setPinyi(Tools.getPYIndexStr(strPeopleName,
								true).toUpperCase());
						list.add(contacts);
					}
				}
			}
		}
		if (!cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * a-z
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<Contacts>() {
		@Override
		public int compare(Contacts lhs, Contacts rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	private boolean mReady;

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(
				R.layout.layout_select_contacts_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		if (mReady) {
			String text;
			String pinyin = allContacts_lists.get(firstVisibleItem).getPinyi();
			text = PingYinUtil.converterToFirstSpell(pinyin).substring(0, 1)
					.toUpperCase();
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// 延迟一秒后执行，让overlay为不可见
			handler.postDelayed(overlayThread, 1000);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL
				|| scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

}
