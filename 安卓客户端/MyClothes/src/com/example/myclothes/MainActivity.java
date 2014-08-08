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
	
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static String filePath = null;
	private static String fileName = null;
	private static String fileNakedName = null;
	private static long fileSize = 0;
	private static boolean okFlag=true;
	private ImageView img;
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// �ȱ�����Сԭͼ
		
		if (new File(Environment.getExternalStorageDirectory()
										.getPath() + "/MyClothes/" + fileNakedName + ".jpg").exists())
		{
		MyRunnable myrunnable=new MyRunnable(filePath, fileNakedName, fileSize, requestCode,getApplicationContext(), getBaseContext(),myHandler);
	    new Thread(myrunnable).start();
		}
		else
			Toast.makeText(getApplicationContext(), "�����쳣��",
					Toast.LENGTH_SHORT).show();
		
	 }
	
	//������������ʾѡ�������/�����ѡ��
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("ͼƬѡ��")
				.setPositiveButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// ����ϵͳ�����չ���
						takePic();
					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {

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
	
	//ѡ������
	private void takePic() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

	    initializeFileInfo(intent);
	

		startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	}
    
	//�����ļ���Ϣ����������
	private void initializeFileInfo(Intent intent) {
		// TODO Auto-generated method stub
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/MyClothes/");
		file.mkdirs();// �����ļ��У�����Ϊmyimage

		// ��Ƭ��������Ŀ���ļ����£��Ե�ǰʱ�����ִ�Ϊ���ƣ�����ȷ��ÿ����Ƭ���Ʋ���ͬ��
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
				Toast.makeText(getApplicationContext(), "��ǰ�����������ڴ������Ժ�", Toast.LENGTH_LONG).show();
		}

	};

 //������Handler����������FTP,HTTP�̷߳��͵���Ϣ
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
					Toast.makeText(getApplicationContext(), "���ӷ������쳣�����Ժ����ԣ�", Toast.LENGTH_LONG).show();
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
