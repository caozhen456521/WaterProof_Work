package com.qingzu.utils.tools;

import com.qingzu.waterproof_work.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ShowProgressDialog extends Dialog {

	public ShowProgressDialog(Context context, int theme, String message) {
		super(context, theme);
		setContentView(R.layout.dialog);
		setCanceledOnTouchOutside(true);
		setCancelable(false);
		this.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView msg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		msg.setText(message);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}
}
