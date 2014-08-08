<%@ page language="java" import="java.util.*"  contentType="text/html; charset=utf-8"pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
  String imgRootPath="images/";
  String searchRootStr="http://s.taobao.com/search?q=";
  
  ArrayList<String> clothesName=(ArrayList<String>) request.getAttribute("clothesName");
  
  ArrayList<String> imgPath=new ArrayList<String>();
  ArrayList<String> searchUrl=new ArrayList<String>();
  
  for (int i=0;i<clothesName.size();i++)
  {
     String tmp=imgRootPath+clothesName.get(i)+"_01.jpg";
     String tmp2=searchRootStr+clothesName.get(i);
     imgPath.add(tmp);
     searchUrl.add(tmp2);
  }

 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>匹配结果</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="识别结果">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	
    <link rel="stylesheet" type="text/css" href="./css/table.css"/>
  </head>
  
  <body>
  <center>
    <h1>识别结果</h1>
    <table class="table">
      <tr>
       <th><font size="2" color="red">序号</th> <th><font size="2" color="red">品名</th> <th><font size="2" color="red">参考图</th> <th><font size="2" color="red">购买链接</th>
        <%
          for (int i=0;i<clothesName.size();i++)
          {
            out.println("<tr>");
            out.println("<td>"+(i+1)+"</td>");
            out.println("<td>"+clothesName.get(i)+"</td>");
            out.println("<td><img height=\"280\" width=\"200\" src=\""+imgPath.get(i)+"\"></img></td>");
            out.println("<td><a target=\"blank\" href=\""+searchUrl.get(i)+"\">点我购买</a></td>");
            out.println("</tr>");
          }
         %>
      </tr>
    </table>
  </center>
  </body>
</html>
