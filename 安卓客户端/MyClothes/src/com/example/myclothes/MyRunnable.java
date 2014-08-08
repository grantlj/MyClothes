//FTP后台上传线程

package com.example.myclothes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.util.ftp.FTPOperator;
import com.util.image.ImgCompressor;

public class MyRunnable implements Runnable{

	
	private final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private String filePath = null;
	private String fileNakedName = null;
	
	private int requestCode;
	private Context applicationContext;
	private Handler myHandler;
	private long fileSize;
	
	
	public MyRunnable(String filePath, String fileNakedName, long fileSize,
			int requestCode,
			Context applicationContext, Context baseContext,Handler myHandler) {
		super();
		this.filePath = filePath;
		this.fileNakedName = fileNakedName;
		this.fileSize = fileSize;
		this.requestCode = requestCode;
		this.applicationContext = applicationContext;
	
		this.myHandler=myHandler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub 
		 Looper.prepare();
boolean okFlag=false;
		
		switch (requestCode) //判断图片来源，是相册选择还是拍照获得
	    {
		  case (PHOTO_REQUEST_TAKEPHOTO):
		  {
			     try
		            {
		            	fileSize=doCompress(fileNakedName);
		            	okFlag=true;
		            }
		            catch (Exception e)
		            {
		            	okFlag=false;
		            }
					break;
		  
		  }//CASE 1 END;
		  
		  case (PHOTO_REQUEST_GALLERY):
		  {
			  try {
				fileSize=doCompress(fileNakedName);
				okFlag=true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				okFlag=false;
			}
			  
			 break;
		  } //CASE 2 END;
		  
		  default:
		  {
				Toast.makeText(applicationContext, "操作异常！",
						Toast.LENGTH_LONG).show();
				okFlag=false;
		  }
	    }
		
		if (okFlag)
		{
			Toast.makeText(applicationContext, "拍照成功，正在上传服务器检测。。。",
					Toast.LENGTH_SHORT).show();
	        
			Message msg0=new Message();
			msg0.what=0; //Start upload. disable all operations.
			myHandler.sendMessage(msg0);
			
			
			FTPOperator ftp = new FTPOperator();
			
			String ret=ftp.ftpUpload(filePath, fileNakedName);
			
			if (ret.equals("1"))
			{
	        
			   //Send MainActivity a message.
				Message msg = new Message();
				msg.what=1;
				Bundle data=new Bundle();
				data.putLong("fileSize", fileSize);
				myHandler.sendMessage(msg);
				
			}
			
			else
			{
				Message msg=new Message();
				msg.what=2;
				myHandler.sendMessage(msg);
				//Toast.makeText(applicationContext, "连接服务器异常！请稍后再试！", Toast.LENGTH_LONG).show();
			}
		        
		}
	 }
	

//图片压缩服务
private long doCompress(String fileNakedName) throws FileNotFoundException
{
	   Bitmap bitmap = new ImgCompressor().getSmallBitmap(Environment
				.getExternalStorageDirectory().getPath()
				+ "/MyClothes/"
				+ fileNakedName + ".jpg");
		        CompressFormat format = Bitmap.CompressFormat.JPEG;
		        int quality = 100;
	          	OutputStream stream = null;
		     
			         stream = new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath()
					+ "/MyClothes/"
					+ fileNakedName + ".jpg");
					
					
					bitmap.compress(format, quality, stream);
					File tmpFile = new File(Environment.getExternalStorageDirectory()
							.getPath() + "/MyClothes/" + fileNakedName + ".jpg");
					return tmpFile.length();
}
	

}
