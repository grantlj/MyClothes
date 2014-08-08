package com.myclothes.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.myclothes.bean.ResultBean;

public class ExeProxy {

  private static final String exeFilePath="D:\\MyClothes\\FtpServer\\core.exe"; // 识别文件核心exe
  private static final String workingPath="D:/MyClothes/FtpServer/uploaded/";   // jpg文件默认存放目录
  private static final String dbInfoPath="D:/MyClothes/FtpServer/db/db.info"; //数据库信息文件 
  private static final String dbMainPath="D:/MyClothes/FtpServer/db/db.dat";     //数据库核心文件
  private static final String dbFileIndex="D:/MyClothes/FtpServer/db/db.fn";    //图片索引文件
  private String fileNakedName=null;
  private long fileSize=0;
  
  public ExeProxy(String fileNakedName,long fileSize)
  {
	  this.fileNakedName=fileNakedName;
	  this.fileSize=fileSize;
  }

public ResultBean invoke() throws FileNotFoundException, InterruptedException
{
	

	File file2=new File(workingPath+this.fileNakedName+".jpg");
	
	while ((!file2.exists()) || (file2.length()<fileSize) )
	{
		//System.out.println("wait");
	}
	
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	Runtime rt=null;
	ResultBean ret=new ResultBean();
	try
	{
		rt=Runtime.getRuntime();
		String tmp=exeFilePath+" "+workingPath+this.fileNakedName+".jpg"+" "+dbInfoPath+" "+dbMainPath+" "+dbFileIndex+" "+workingPath+this.fileNakedName+".txt";
		System.out.println(tmp);
		rt.exec(tmp);
	}
	catch (Exception e)
	{
		e.printStackTrace();
		rt=null;
		return null;
	}
	
	File file=new File(workingPath+this.fileNakedName+".txt");
	
	int t=0;
	while (!file.exists() && t<=100)
	{
		t++;
		Thread.sleep(400);
	}
	
	if (t>=100) return null;
	
	Scanner sc=new Scanner(file);
	ArrayList<String> clothesName=new ArrayList<String>();
	
	while (sc.hasNext())
	{
		clothesName.add(sc.nextLine());
	}
	
	sc.close();
	ret.setClothesName(clothesName);
	
	return ret;
	
}
}
