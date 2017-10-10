package com.cryallen.androidlib.widgets.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cryallen.androidlib.R;
import com.cryallen.androidlib.widgets.CircleImage.CircleImageView;
import com.cryallen.androidlib.widgets.DilatingDot.DilatingDotsProgressBar;
/**
 * @ClassName: MProgressDialog
 * @Description: 自定义加载框
 * @author: chenran3
 * @date: 2017/9/26 19:04
 */
public class MProgressDialog implements View.OnClickListener {

	private Dialog mDialog;
	private Context mContext;
	private Builder mBuilder;

	//布局
	private RelativeLayout dialog_window_background;
	private RelativeLayout dialog_view_bg;
	private CircleImageView dialog_circleImage;
	private DilatingDotsProgressBar dialog_dot_progressbar;

	public MProgressDialog(Context context) {
		this(context, new Builder(context));
	}

	public MProgressDialog(Context context, Builder builder) {
		mContext = context;
		mBuilder = builder;
		//初始化
		initDialog();
	}

	private void initDialog() {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mProgressDialogView = inflater.inflate(R.layout.progress_dialog_layout, null);// 得到加载view
		mDialog = new Dialog(mContext, R.style.CustomProgressDialog);// 创建自定义样式dialog
		mDialog.setCancelable(false);// 不可以用“返回键”取消
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(mProgressDialogView);// 设置布局

		//设置整个Dialog的宽高
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		windowManager.getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		int screenH = dm.heightPixels;

		//设置整个Dialog的参数
		WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
		layoutParams.width = screenW;
		layoutParams.height = screenH;
		mDialog.getWindow().setAttributes(layoutParams);

		//布局相关
		dialog_window_background = (RelativeLayout) mProgressDialogView.findViewById(R.id.dialog_window_background);
		dialog_view_bg = (RelativeLayout) mProgressDialogView.findViewById(R.id.dialog_view_bg);
		dialog_circleImage = (CircleImageView) mProgressDialogView.findViewById(R.id.dialog_circleImage);
		dialog_dot_progressbar = (DilatingDotsProgressBar) mProgressDialogView.findViewById(R.id.dialog_dot_progressbar);

		//点击事件
		dialog_window_background.setOnClickListener(this);

		//显示视图
		dialog_dot_progressbar.show();

		//设置默认配置
		configView();
	}

	private void configView() {
		mDialog.setCanceledOnTouchOutside(mBuilder.canceledOnTouchOutside);
		dialog_window_background.setBackgroundColor(mBuilder.backgroundWindowColor);

		GradientDrawable myGrad = (GradientDrawable) dialog_view_bg.getBackground();
		myGrad.setColor(mBuilder.backgroundViewColor);
		myGrad.setStroke(dip2px(mContext, mBuilder.strokeWidth), mBuilder.strokeColor);
		myGrad.setCornerRadius(dip2px(mContext, mBuilder.cornerRadius));
		dialog_view_bg.setBackground(myGrad);

		if(mBuilder.drawable != null){
			dialog_circleImage.setImageDrawable(mBuilder.drawable);
		}
	}

	public void show() {
		dismiss();
		if (mDialog != null) {
			mDialog.show();
		}
	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			if (mBuilder.dialogDismissListener != null) {
				mBuilder.dialogDismissListener.dismiss();
			}
		}
	}

	public void refreshBuilder(Builder builder) {
		mBuilder = builder;
		configView();
	}

	/**
	 * dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.dialog_window_background) {
			//取消Dialog
			if (mBuilder.canceledOnTouchOutside) {
				dismiss();
			}
		}
	}

	public interface OnDialogDismissListener {
		void dismiss();
	}

	//---------------------构建者模式--------------------
	public static final class Builder {

		private Context mContext;

		//点击外部可以取消
		boolean canceledOnTouchOutside;
		//窗体背景色
		int backgroundWindowColor;
		//View背景色
		int backgroundViewColor;
		//View边框的颜色
		int strokeColor;
		//View背景圆角
		float cornerRadius;
		//View边框的宽度
		float strokeWidth;
		//自定义圆形图
		Drawable drawable;
		//消失的监听
		OnDialogDismissListener dialogDismissListener;


		public Builder(Context context) {
			mContext = context;
			//默认参数
			canceledOnTouchOutside = false;
			backgroundWindowColor = mContext.getResources().getColor(R.color.colorDialogWindowBg);
			backgroundViewColor = mContext.getResources().getColor(R.color.colorDialogViewBg);
			strokeColor = mContext.getResources().getColor(R.color.colorDialogTrans);
			cornerRadius = 0;
			strokeWidth = 0;
			drawable = null;
			dialogDismissListener = null;
		}

		public MProgressDialog build() {
			return new MProgressDialog(mContext, this);
		}

		public Builder isCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
			this.canceledOnTouchOutside = canceledOnTouchOutside;
			return this;
		}

		public Builder setBackgroundWindowColor(int backgroundWindowColor) {
			this.backgroundWindowColor = backgroundWindowColor;
			return this;
		}

		public Builder setBackgroundViewColor(int backgroundViewColor) {
			this.backgroundViewColor = backgroundViewColor;
			return this;
		}

		public Builder setStrokeColor(int strokeColor) {
			this.strokeColor = strokeColor;
			return this;
		}

		public Builder setStrokeWidth(float strokeWidth) {
			this.strokeWidth = strokeWidth;
			return this;
		}

		public Builder setCornerRadius(float cornerRadius) {
			this.cornerRadius = cornerRadius;
			return this;
		}

		public Builder setDrawable(Drawable drawable) {
			this.drawable = drawable;
			return this;
		}

		public Builder setOnDialogDismissListener(OnDialogDismissListener dialogDismissListener) {
			this.dialogDismissListener = dialogDismissListener;
			return this;
		}

	}
}
