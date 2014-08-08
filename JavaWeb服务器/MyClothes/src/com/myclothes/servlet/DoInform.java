package com.myclothes.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.myclothes.bean.ResultBean;
import com.myclothes.proxy.ExeProxy;
import com.myclothes.proxy.FilePrepProxy;

public class DoInform extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public DoInform() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String fileNakedName=(String) request.getParameter("fileNakedName");
		long fileSize = Integer.parseInt((String) request
				.getParameter("fileSize"));
		String reqWeb = (String) request.getParameter("reqWeb");

		String retVal = "ERR";
		if (fileNakedName != null) {
            //正确取得参数
			
			//尝试获取bean
			ResultBean ret = null;
			try {
				ret = new ExeProxy(fileNakedName, fileSize).invoke();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret=null;
				retVal="ERR";
			}
			
			
			if (reqWeb == null) {
				//请求方式1：直接返回JSON；
				try
				{
				  retVal = JSON.toJSONString(ret);
				}
				catch (Exception e)
				{
					retVal="ERR";
				}
				out.println(retVal);
				out.flush();
				out.close();
			}
			
			else
			{
				response.setContentType("text/html;charset=utf-8");
				request.setCharacterEncoding("utf-8");
				//请求方式2：转入JSP页面；
				try
				{
					new FilePrepProxy(ret).invoke(request.getRealPath(File.separator));
					request.setAttribute("clothesName", ret.getClothesName());
					request.getRequestDispatcher("ShowResult.jsp").forward(request, response);
				}
				catch (Exception e)
				{
					retVal="ERR";
					out.println(retVal);
					out.flush();
					out.close();
				}
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
