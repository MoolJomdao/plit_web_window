package db;

import java.sql.*;

// DB에 연결의 담당하는 메소드
// DB 작업을 할 때 필요한 작업들의 정의


/*
 * 1) JDBC 드라이버를 적재
 * 2) 데이터베이스 연결
 * 3) SQL 문장 작성 및 전송
 * 4) 결과 집합 사용 후 연결 해제
 */
public class DBConnection {

	/*// JDBC 드라이버를 적재
    static{
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    		// Class   : 자바 API에서 제공되는 클래스
    		// forName : 파라미터로 지정된 클래스를 인스턴스 메모리에 올리는 역할을 하는 메소드
    		// DB과 애플리케이션이 연동하기 위해 DB 드라이버를 메모리로 올리는 부분
    	}
    	catch(ClassNotFoundException e){
    		e.printStackTrace();
    		System.out.println("드라이버를 찾을 수 없습니다.");
    	}
    }*/

    /*// 데이터베이스 연결
    public static Connection getConnection(){
    	Connection con=null;
    	try{
    		// getConnection( url, id, pw );
    		con = DriverManager.
    				getConnection("jdbc:mysql://localhost:3306/plit?testWhileIdle=true&validationQuery=select 1", "root", "root");
    		con.setAutoCommit(true);
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    		System.out.println(" 데이터베이스에 연결 할 수 없습니다.");
    	}
    	return con;
    }*/

    public static void close(Connection con){
    	try{
    		if( con != null )
    		{
    			con.close();
    		}		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public static void close(Statement stmt){
    	try{
    		if( stmt != null )
    			stmt.close();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public static void close(ResultSet rs){
    	try{
    		if( rs != null )
    			rs.close();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	} 	  
    }
      
    // commit
    // 작업 결과를 물리적 디스크로 저장 하고
    // 조작 작업이 정상적으로 완료 되었음을 관리자에게 알려줌
    public static void commit(Connection con){
    	try{
    		con.commit();
    		System.out.println("commit success");
    	}
    	catch(Exception e){
    		System.out.println("commit fail");
    	}
    }
    
    // rollback : 원래 상태로 복구		  
    public static void rollback(Connection con){
    	  try{
    		  con.rollback();
    		  System.out.println("rollback success");
    	  }
    	  catch(Exception e){
    		  System.out.println("rollback fail");
    	  }
      }
}