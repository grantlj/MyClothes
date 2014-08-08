package com.example.myclothes;

import java.io.File;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button doPhoto = null;
	
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static String filePath = null;
	private static String fileName = null;
	private static String fileNakedName = null;
	private static long fileSize = 0;
	private static boolean okFlag=true;
	private ImageView img;
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 等比例缩小原图
		
		if (new File(Environment.getExternalStorageDirectory()
										.getPath() + "/MyClothes/" + fileNakedName + ".jpg").exists())
		{
		MyRunnable myrunnable=new MyRunnable(filePath, fileNakedName, fileSize, requestCode,getApplicationContext(), getBaseContext(),myHandler);
	    new Thread(myrunnable).start();
		}
		else
			Toast.makeText(getApplicationContext(), "操作异常！",
					Toast.LENGTH_SHORT).show();
		
	 }
	
	//按键操作，显示选择框，拍照/从相册选择
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("图片选择")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 调用系统的拍照功能
						takePic();
					}
				})
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						/*
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						initializeFileInfo(intent);
						startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
						*/
						
						 Intent intent=new Intent(Intent.ACTION_PICK,null);
						 intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  "image/*");  
					     intent.putExtra("crop","true");
					     //intent.putExtra("aspectX",3);
					     //intent.putExtra("aspectY",4);
					     initializeFileInfo(intent);
					     intent.putExtra("output",Uri.fromFile(new File(Environment.getExternalStorageDirectory()
										.getPath() + "/MyClothes/" + fileNakedName + ".jpg")));
					     intent.putExtra("outputFormat", "JPEG");
					     startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
					}
				}).show();
	}
	
	//选择拍照
	private void takePic() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

	    initializeFileInfo(intent);
	

		startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	}
    
	//生成文件信息！！！！！
	private void initializeFileInfo(Intent intent) {
		// TODO Auto-generated method stub
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/MyClothes/");
		file.mkdirs();// 创建文件夹，名称为myimage

		// 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。
		String str = null;
		str = UUID.randomUUID().toString();
		filePath = Environment.getExternalStorageDirectory().getPath()
				+ "/MyClothes/";
		fileName = filePath + str + ".jpg";
		fileNakedName = str;
		Uri uri = Uri.fromFile(new File(fileName));
		intent.putExtra("output", uri);
		intent.putExtra("fileName", fileName);
		intent.putExtra("android.intent.extra.screenOrientation", false);
		intent.putExtra("fileNakedName", fileNakedName);
	}

	private Button.OnClickListener l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
//			doPhoto.setBackgroundResource(R.drawable.takephoto_2);
			if (okFlag)
			showDialog();
			else
				Toast.makeText(getApplicationContext(), "当前已有请求正在处理，请稍后！", Toast.LENGTH_LONG).show();
		}

	};

 //主界面Handler，用来接收FTP,HTTP线程发送的消息
  private  Handler myHandler;
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		addListener();
		
		 myHandler=new Handler(){

			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if (msg.what==0) //disable operations.
				{
					okFlag=false; //Not allow to take photos anymore.
					
				}
				
				if (msg.what==1)
				{
					img.setScaleType(ImageView.ScaleType.FIT_XY );
					
	                fileSize=msg.getData().getLong("fileSize");
				
					
				    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://grantlj.gicp.net:8080/MyClothes/DoInform?fileNakedName="+fileNakedName+"&fileSize="+fileSize+"&reqWeb=11"));  
			        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");  
			        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        getBaseContext().startActivity(it);  
			        okFlag=true;   //Reset state.
			        
			        File del=new File(Environment.getExternalStorageDirectory()
							.getPath() + "/MyClothes/" + fileNakedName + ".jpg");
			        if (del.exists())
			        	del.delete();
			        
				}
				
				if (msg.what==2)
				{
					//System.out.println("in");
					Toast.makeText(getApplicationContext(), "连接服务器异常！请稍后再试！", Toast.LENGTH_LONG).show();
				//	pg.seftProgress(pg.getProgress()+msg.getData().getInt("go"));
					okFlag=true;
					   File del=new File(Environment.getExternalStorageDirectory()
								.getPath() + "/MyClothes/" + fileNakedName + ".jpg");
				        if (del.exists())
				        	del.delete();
				}
				
			}
			
		};
	}

	private void addListener() {
		// TODO Auto-generated method stub
		doPhoto.setOnClickListener(l);
	}
    
	//private GifView gf1;
	
	
	
	private void findViews() {
		// TODO Auto-generated method stub
		doPhoto = (Button) findViewById(R.id.button1);
	
	  
		img = (ImageView) findViewById(R.id.imgPic);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
