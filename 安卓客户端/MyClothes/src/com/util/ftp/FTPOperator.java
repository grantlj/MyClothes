package com.util.ftp;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPOperator
{
  private static final String url="grantlj.gicp.net";
  private static final String port="21";
  private static final String username="user";
  private static final String password="940414";
  private String fileName;
  private String fileNamePath;
  private String ret;
  
  public String ftpUpload(String fileNamePath,String fileName) {  
  this.fileNamePath=fileNamePath;
  this.fileName=fileName;
  new Thread(runnable).start();
  
  while (ret==null)
  {
	  
  }
  return ret;
} 
  
  Runnable runnable=new Runnable(){

	  public  void run(){
		  FTPClient ftpClient = new FTPClient();  
		  ftpClient.setConnectTimeout(10000);
		  FileInputStream fis = null;  
		  // ret = "0";  
		  try {  
			  
		      ftpClient.connect(url, Integer.parseInt(port));  
		      boolean loginResult = ftpClient.login(username, password);  
		      int returnCode = ftpClient.getReplyCode();  
		      if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功  
		         // ftpClient.makeDirectory(remotePath);  
		          // 设置上传目录  
		         // ftpClient.changeWorkingDirectory(remotePath);  
		          ftpClient.setBufferSize(8192);  
		          ftpClient.setControlEncoding("GBK");  
		          ftpClient.enterLocalPassiveMode();  
		          ftpClient.setFileType(2);
		          ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
		          fis = new FileInputStream(fileNamePath + fileName+".jpg");  
		          //fis=new FileInputStream(fileNamePath+"de_urgent_file.txt");
		          System.out.println(fileNamePath+fileName+".jpg");
		          ftpClient.storeFile(fileName+".jpg", fis);  
		         // ftpClient.storeFile("1.txt", fis);
		             
		          ret = "1";   //上传成功        
		            } else {
		            	// 如果登录失败  
		                 ret = "0";  
		               //  throw new RuntimeException("FTP客户端出错!！");  
		               }  
		                 
		    
		  } catch (IOException e) {  
		      e.printStackTrace();  
		      ret="0";
		      //throw new RuntimeException("FTP客户端出错！", e);  
		  } 
		  
		  finally {  
		      //IOUtils.closeQuietly(fis);  
		  try {  
		      ftpClient.disconnect();  
		  } catch (IOException e) {  
		         e.printStackTrace();  
		         ret="0";
		         //throw new RuntimeException("关闭FTP连接发生异常！", e);  
		   }  
		  }  
		
	  }
 };

}
