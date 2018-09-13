package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import db.BoardBean;
import static db.DBConnection.*;

public class BoardDao {
	
	Connection con;
	PreparedStatement pstmt; // 한번의 쿼리문장 분석, 컴파일 후 캐시에 담아 재사용
	ResultSet rs;  // select쿼리 실행 시 executeQuery() 메서드를 사용하며, 실행 결과로 java.sql.ResultSet형으로 리턴
	DataSource ds; // Connection pool 관리
	
	public BoardDao() {
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  	
		}catch(Exception ex){
			System.out.println("DB 연결 실패(BoardDao) : " + ex);
			return;
		}
	}
	
	/*
	//글의 개수 구하기.
	public int getListCount() {
		int x= 0;
		
		try{
			con=ds.getConnection();
			pstmt=con.prepareStatement("select count(*) from memberboard");
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				x=rs.getInt(1);
			}
		}catch(Exception ex){
			System.out.println("getListCount 에러: " + ex);			
		}finally{
			if(rs!=null) try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex){}
			if(con!=null) try{con.close();}catch(SQLException ex){}
		}
		return x;
	}
	*/
	
	//글 목록 가져오기
	public ArrayList<BoardBean> getBoardList( int page, int limit )
	{
		String board_list_sql = "select * from board";
				//+ "where rnum>=? and rnum<=?";
		
		ArrayList list = new ArrayList();
		
		int startrow = ( ( page - 1 ) * 10 ) + 1; //읽기 시작할 row 번호.
		int endrow = startrow + limit - 1; //읽을 마지막 row 번호.		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(board_list_sql);
			//pstmt.setInt(1, startrow);
			//pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				// 코멘트 정보 저장
				BoardBean board = new BoardBean();
				board.setBoardNum(rs.getInt("board_num"));
				board.setBoardContent(rs.getString("board_content"));
				board.setDateBoard(rs.getString("date_board"));
				board.setGood(rs.getInt("good"));
				board.setHits(rs.getInt("hits"));
				board.setBoardTag(rs.getString("board_tag"));
				board.setLatitude(rs.getDouble("board_latitude"));
				board.setLongitude(rs.getDouble("board_longitude"));
				board.setId(rs.getString("id"));
				list.add(board);
			}		
			return list;
		}catch(Exception ex){
			System.out.println("getBoardList 에러 : " + ex);
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}
	
	
	//글 내용 보기. ( 자세히 보기 )
	public BoardBean getBoardDetail( int num ) throws Exception{
		
		BoardBean board = null;
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(
					"select * from board where BOARD_NUM = ?");
			pstmt.setInt(1, num);		
			rs= pstmt.executeQuery();
			
			if(rs.next()){
				board = new BoardBean();
				board.setBoardNum(rs.getInt("board_num"));
				board.setBoardContent(rs.getString("board_content"));
				board.setDateBoard(rs.getString("date_board"));
				board.setGood(rs.getInt("good"));
				board.setHits(rs.getInt("hits"));
				board.setBoardTag(rs.getString("board_tag"));
				board.setLatitude(rs.getDouble("board_latitude"));
				board.setLongitude(rs.getDouble("board_longitude"));
				board.setId(rs.getString("id"));
			}
			return board;
			
		}catch(Exception ex){
			System.out.println("getBoardDetail 에러 : " + ex);
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}
	
	// 글쓴이의 프로필 사진 가져오기
	public String getWriterImg( String id ) throws SQLException
	{
		con = ds.getConnection();
		String board_list_sql = "select user_photo from user_info where id = ?";		
		String img = null;
				
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(board_list_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if( rs.getString("user_photo") == null)
				{
					img = "icon.jpg";
				}
				else
				{
					img = rs.getString("user_photo");
				}
			}
			
			

			return img;
		}catch(Exception ex){
			System.out.println("getBoardList 에러 : " + ex);
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}	
	
	//글 등록.
	public boolean boardInsert(BoardBean board){
		String sql="";
		
		int result=0;
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(
					"select max(board_num) from memberboard");
			rs = pstmt.executeQuery();
			
			sql="insert into memberboard (board_content,date_board";
			sql+=",good,hits,board_tag,board_latitude,board_longitude"+
				",id,category_num,comment_cnt"+
				"BOARD_DATE) values(?,sysdate,0,0,?,?,?,?,?,0)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getBoardContent());
			pstmt.setString(2, board.getBoardTag());
			pstmt.setDouble(3, board.getLatitude());
			pstmt.setDouble(4, board.getLongitude());
			pstmt.setString(5, board.getId());
			pstmt.setInt(6, board.getCategory_num());
			
			result=pstmt.executeUpdate();
			if(result==0)return false;
			
			return true;
		}catch(Exception ex){
			System.out.println("boardInsert 에러 : "+ex);
		}finally{
			if(rs!=null) try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex){}
			if(con!=null) try{con.close();}catch(SQLException ex){}
		}
		return false;
	}
	
	/*//글 답변.
	public int boardReply(BoardBean board){
		String board_max_sql="select max(board_num) from memberboard";
		String sql="";
		int num=0;
		int result=0;
		
		int re_ref=board.getBOARD_RE_REF();
		int re_lev=board.getBOARD_RE_LEV();
		int re_seq=board.getBOARD_RE_SEQ();
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			if(rs.next())num =rs.getInt(1)+1;
			else num=1;
			
			sql="update memberboard set BOARD_RE_SEQ=BOARD_RE_SEQ+1 ";
			sql+="where BOARD_RE_REF=? and BOARD_RE_SEQ>?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,re_ref);
			pstmt.setInt(2,re_seq);
			result=pstmt.executeUpdate();
			
			re_seq = re_seq + 1;
			re_lev = re_lev+1;
			
			sql="insert into memberboard (BOARD_NUM,BOARD_ID,BOARD_SUBJECT,";
			sql+="BOARD_CONTENT,BOARD_FILE,BOARD_RE_REF,BOARD_RE_LEV,";
			sql+="BOARD_RE_SEQ,BOARD_READCOUNT,BOARD_DATE) ";
			sql+="values(?,?,?,?,?,?,?,?,?,sysdate)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, board.getBOARD_ID());
			pstmt.setString(3, board.getBOARD_SUBJECT());
			pstmt.setString(4, board.getBOARD_CONTENT());
			pstmt.setString(5, ""); //답장에는 파일을 업로드하지 않음.
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0);
			pstmt.executeUpdate();
			return num;
		}catch(SQLException ex){
			System.out.println("boardReply 에러 : "+ex);
		}finally{
			if(rs!=null)try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null)try{pstmt.close();}catch(SQLException ex){}
			if(con!=null)try{con.close();}catch(SQLException ex){}
		}
		return 0;
	}
	
	//글 수정.
	public boolean boardModify(BoardBean modifyboard) throws Exception{
		String sql="update memberboard set BOARD_SUBJECT=?,";
		sql+="BOARD_CONTENT=? where BOARD_NUM=?";
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, modifyboard.getBOARD_SUBJECT());
			pstmt.setString(2, modifyboard.getBOARD_CONTENT());
			pstmt.setInt(3, modifyboard.getBOARD_NUM());
			pstmt.executeUpdate();
			return true;
		}catch(Exception ex){
			System.out.println("boardModify 에러 : " + ex);
		}finally{
			if(rs!=null)try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null)try{pstmt.close();}catch(SQLException ex){}
			if(con!=null)try{con.close();}catch(SQLException ex){}
			}
		return false;
	}
	
	//글 삭제.
	public boolean boardDelete(int num){
		String board_delete_sql=
			"delete from memberboard where BOARD_num=?";
		
		int result=0;
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(board_delete_sql);
			pstmt.setInt(1, num);
			result=pstmt.executeUpdate();
			if(result==0)return false;
			
			return true;
		}catch(Exception ex){
			System.out.println("boardDelete 에러 : "+ex);
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(con!=null)con.close();
			}catch(Exception ex) {}
		}
		
		return false;
	}
	
	//조회수 업데이트.
	public void setReadCountUpdate(int num) throws Exception{
		String sql="update memberboard set BOARD_READCOUNT = "+
			"BOARD_READCOUNT+1 where BOARD_NUM = "+num;
		
		try{
			con=ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.executeUpdate();
		}catch(SQLException ex){
			System.out.println("setReadCountUpdate 에러 : "+ex);
		}
		finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(con!=null)con.close();
			}catch(Exception ex) {}
		}
	}
	
	//글쓴이인지 확인.
	public boolean isBoardWriter(int num,String id){
		System.out.println("id="+id);
		String board_sql=
			"select * from memberboard where BOARD_NUM=?";
		
		try{
			con=ds.getConnection();
			pstmt=con.prepareStatement(board_sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			rs.next();
			
			if(id.equals(rs.getString("BOARD_ID"))){
				return true;
			}
		}catch(SQLException ex){
			System.out.println("isBoardWriter 에러 : "+ex);
		}
		finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(con!=null)con.close();
			}catch(Exception ex) {}
		}
		return false;
	}*/
	
}
