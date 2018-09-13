
<%@page import="org.json.JSONArray"%>
<%@page import="control.*"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.InputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	
	InputStream in = request.getInputStream();
	String json;// = in.toString()+request.getParameter("json"); //in.toString()+
	//System.out.print(json);
	
	StringBuffer sb = new StringBuffer();
    byte[] b = new byte[4096];
    for (int n; (n = in.read(b)) != -1;) {
        sb.append(new String(b, 0, n));
    }
    json = sb.toString();

    System.out.println(json);

	JSONObject jobj = new JSONObject(json);
	
	String result = new FrontController().control(jobj).toString();
	
 
 
	out.print(new JSONObject(result).getString("result"));
	System.out.println(result + "  "+json);
%>
