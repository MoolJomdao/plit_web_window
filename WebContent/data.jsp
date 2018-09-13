<%@page import="control.FrontController"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	int size = 100*1024*1024;
	String uploadPath = "/var/lib/tomcat7/webapps/ROOT/PlitImage";
	JSONObject jobj = null;
	String result = "-12";
	

	
	System.out.println("test Strat");

try{

	  // 파일 업로드. 폼에서 가져온 인자값을 얻기 위해request 객체 전달, 업로드 경로, 파일 최대 크기, 한글처리, 파일 중복처리

	  MultipartRequest multi = new MultipartRequest(request, uploadPath, size, "euc-kr", new DefaultFileRenamePolicy());
	 

	  // 이전 페이지의 form에서 입력한 각각의 값을 가져온다.
		
	  String json = multi.getParameter("json");
	  System.out.println(json);
	  //jobj = new JSONObject(json);
	 

	// 업로드한 파일들을 Enumeration 타입으로 반환

	// Enumeration형은 데이터를 뽑아올때 유용한 인터페이스임.  Enumeration객체는 java.util 팩키지에 정의 되어있으므로

//	     java.util.Enumeration을 import 시켜야 함.

		
	 JSONArray jrr = new JSONArray();

	// 업로드한 파일들의 이름을 얻어옴

	   for (Enumeration  e = multi.getFileNames(); e.hasMoreElements() ;) 
	   {
         String strName = (String) e.nextElement();
         String fileName= multi.getFilesystemName(strName);
         if (fileName != null) 
         {
        	 jrr.put(new JSONObject().put("photo", fileName));
         }
 	   }
	
	  //String result = new FrontController().control(jobj,jrr).toString();
	  
	 // out.print(result + "  "+json);
	 
	 jobj = new JSONObject(json);
	 
	 jobj.put("name", jrr.getJSONObject(0).getString("photo"));
	 
	 
	 
	 result = new FrontController().control(jobj).toString();
	 
	 System.out.println("test ENd");
	 System.out.println("  "+jrr.toString());//jobj.toString()+

	 }catch(Exception e){

	  // 예외처리

	  e.printStackTrace();
	  
	  out.println(result);

	 }
%>
