package com.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultBeanGetter {
    private String retStr=null;
    private boolean okFlag=false;
    
	private static final String url="http://grantlj.gicp.net:8080/MyClothes/DoInform?fileNakedName=";
	private String fileNakedName=null;
	private long fileSize=0;
	
	public String doGetResult(String str,long fileSize)
	{
		this.fileNakedName=str;
		new Thread(runnable).start();
		while (!okFlag)
		{
			
		}
		
		return retStr;
	}
	Runnable runnable=new Runnable(){
		public void run()
		{
			 
			URL myUrl=null;
			InputStream is=null;
			HttpURLConnection conn=null;
		    
			try
			{
		     myUrl = new URL(url+fileNakedName+"&fileSize="+fileSize);
		     
			 conn = (HttpURLConnection) myUrl.openConnection();
			 conn.setReadTimeout(30000);
			 conn.setDoInput(true);
	         conn.connect();
	         is = conn.getInputStream();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	         
	         String tmp;
	         while ((tmp = reader.readLine()) != null) {
	             retStr+=tmp;
	         }
	         
	         if (retStr.indexOf("null")==0)
	        	 retStr=retStr.substring(4);
	         okFlag=true;
	         
			}
			catch (Exception e)
			{
				e.printStackTrace();
				retStr="ERR";
				okFlag=true;
			}
			finally
			{
				if (is!=null)
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	};
	
}
