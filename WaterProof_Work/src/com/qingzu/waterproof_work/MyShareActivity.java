package com.qingzu.waterproof_work;
import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.BitmapUtil;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MyShareActivity extends Activity {
	protected int mScreenWidth;
	private String uri = "http://passport.5ifangshui.com/user/register?uid=";
	private ImageView iv_qr_image = null;
	private String twoCode = "twoCode.png"; // 本地二维码名称
	private MyTitleView mtvTitle = null;
	private String NickName = null;
	private String UserID = null;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_share);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
	

		iv_qr_image = (ImageView) findViewById(R.id.my_two_code);
		mtvTitle = (MyTitleView) findViewById(R.id.my_share_title);
		mtvTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		intviews();

	}

	public void intviews() {
		SharedPreferences sharedPreferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserID = sharedPreferences.getInt("MemberId", 123456)+"";
		System.out.println(UserID);

		NickName = sharedPreferences.getString("NickName", "");
		if (NickName.equals("")) {
			NickName = getString(R.string.Mystery_Men);
		}

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels * 3 / 5;
		Bitmap bitmap;
		if (!UserID.equals("")) {

			bitmap = BitmapUtil.create2DCoderBitmap(MyShareActivity.this, uri+ UserID, mScreenWidth, mScreenWidth);
			if (bitmap != null) {
				iv_qr_image.setImageBitmap(bitmap);
				FileUtils.saveBitmap(bitmap, "twoCode");
			}
		} else {
			T.showToast(this, getString(R.string.Lack_Parame_Login_Again));
		}
	}

	public void onClick(View view) {
		UMImage image = new UMImage(MyShareActivity.this,
				BitmapFactory.decodeFile(FileUtils.SDPATH + twoCode));
		// UMusic music = new UMusic(
		// "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
		// music.setTitle("sdasdasd");
		// music.setThumb(new UMImage(MyShareActivity.this,
		// "http://www.umeng.com/images/pic/social/chart_1.png"));
		// UMVideo video = new UMVideo(
		// "http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");

		switch (view.getId()) {
		case R.id.app_share_wx:
			if (!UserID.equals("") && UserID != null) {
				new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
						.setCallback(umShareListener)
						.withText(getString(R.string.app_name))
						.withMedia(image)
						.withTitle(NickName + getString(R.string.Share))
						.withTargetUrl(uri + UserID).share();
			}
			break;
		case R.id.app_share_wx_circle:
			if (!UserID.equals("") && UserID != null) {
				new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
						.setCallback(umShareListener)
						.withText(getString(R.string.app_name))
						.withMedia(image)
						.withTitle(NickName + getString(R.string.Share))
						.withTargetUrl(uri + UserID).share();
			}
			break;
		case R.id.app_share_sms:
			// new ShareAction(this)
			// .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ,
			// SHARE_MEDIA.QZONE)
			// .setContentList(new ShareContent(), new ShareContent())
			// .withMedia(image)
			// .setListenerList(umShareListener, umShareListener).open();
			if (!UserID.equals("") && UserID != null) {
				new ShareAction(this).setPlatform(SHARE_MEDIA.SMS)
						.setCallback(umShareListener)
						.withText(getString(R.string.app_name))
						.withMedia(image)
						.withTitle(NickName + getString(R.string.Share))
						.withTargetUrl(uri + UserID).share();
			}
			break;
		case R.id.app_share_qq:
			if (!UserID.equals("") && UserID != null) {
				new ShareAction(this).setPlatform(SHARE_MEDIA.QQ)
						.setCallback(umShareListener)
						.withText(getString(R.string.app_name))
						.withMedia(image)
						.withTitle(NickName + getString(R.string.Share))
						.withTargetUrl(uri + UserID).share();
			}

			break;
		}

	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(MyShareActivity.this,
					platform + getString(R.string.Share_Successful),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(MyShareActivity.this,
					platform + getString(R.string.Share_Defeat),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(MyShareActivity.this,
					platform + getString(R.string.Share_Cancel),
					Toast.LENGTH_SHORT).show();
		}
	};
	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(MyShareActivity.this).setPlatform(share_media)
					.setCallback(umShareListener)
					.withText(getString(R.string.More_Platform_Share)).share();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this **/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}

}
