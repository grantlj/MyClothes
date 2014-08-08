package com.myclothes.proxy;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.util.ArrayList;

import com.myclothes.bean.ResultBean;

public class FilePrepProxy {
	private static final String imgFileFolder = "D:/MyClothes/";
	private ArrayList<String> clothesName;

	private void copyFile(String fileFrom, String fileTo) {
		try {
			File file1 = new File(fileFrom);
			File file2 = new File(fileTo);
			InputStream is = new BufferedInputStream(new FileInputStream(file1));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(file2));
			is.available();
			byte byteArray[] = new byte[512];// 大小可调
			int count = -1;
			while ((count = is.read(byteArray)) != -1) {
				os.write(byteArray, 0, count);
			}
			os.flush();
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void invoke(String destPath) {
		// TODO Auto-generated method stub
		for (int i = 0; i < clothesName.size(); i++) {
			String tmp = imgFileFolder + clothesName.get(i) + "_01.jpg";
			File f1 = new File(tmp);
			if (f1.exists()) {
               copyFile(tmp,destPath+"images//"+clothesName.get(i)+"_01.jpg");
			}
			else
			{
				tmp=imgFileFolder+clothesName.get(i)+"_1.jpg";
			    f1=new File(tmp);
				if (f1.exists())
				{
					copyFile(tmp,destPath+"images//"+clothesName.get(i)+"_01.jpg");
				}
			}
		}
	}

	public FilePrepProxy(ResultBean ret) {
		this.clothesName = ret.getClothesName();
	}

}
