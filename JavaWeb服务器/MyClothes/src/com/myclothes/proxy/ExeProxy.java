package com.myclothes.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.myclothes.bean.ResultBean;

public class ExeProxy {

  private static final String exeFilePath="D:\\MyClothes\\FtpServer\\core.exe"; // ʶ���ļ�����exe
  private static final String workingPath="D:/MyClothes/FtpServer/uploaded/";   // jpg�ļ�Ĭ�ϴ��Ŀ¼
  private static final String dbInfoPath="D:/MyClothes/FtpServer/db/db.info"; //���ݿ���Ϣ�ļ� 
  private static final String dbMainPath="D:/MyClothes/FtpServer/db/db.dat";     //���ݿ�����ļ�
  private static final String dbFileIndex="D:/MyClothes/FtpServer/db/db.fn";    //ͼƬ�����ļ�
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
