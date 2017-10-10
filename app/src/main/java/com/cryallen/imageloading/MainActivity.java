package com.cryallen.imageloading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cryallen.androidlib.widgets.Dialog.MProgressDialog;

public class MainActivity extends AppCompatActivity {

	private Button mButton;

	private MProgressDialog mMProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mButton = (Button) findViewById(R.id.btn);

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showLoadDialog();
			}
		});
	}

	private void configDialogDefault() {
		//新建一个Dialog
		mMProgressDialog = new MProgressDialog.Builder(this)
				.isCanceledOnTouchOutside(true)
				.setOnDialogDismissListener(new MProgressDialog.OnDialogDismissListener() {
					@Override
					public void dismiss() {
						Toast.makeText(MainActivity.this, "弹出框消失", Toast.LENGTH_SHORT).show();
					}
				})
				.build();
	}

	private void showLoadDialog(){
		if(mMProgressDialog == null){
			configDialogDefault();
		}
		mMProgressDialog.show();
	}

	private void hideLoadDialog(){
		if(mMProgressDialog != null){
			mMProgressDialog.dismiss();
		}
	}

	@Override
	public void onDestroy() {
		if(mMProgressDialog != null){
			mMProgressDialog.dismiss();
			mMProgressDialog = null;
		}

		super.onDestroy();
	}
}
