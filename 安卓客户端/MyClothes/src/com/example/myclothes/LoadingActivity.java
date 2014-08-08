package com.example.myclothes;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class LoadingActivity extends Activity {
	
	ImageView bG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent i = new Intent(LoadingActivity.this, MainActivity.class);
				startActivity(i); 
				finish();
			}
		}, 3000); // 1s
	}

}
