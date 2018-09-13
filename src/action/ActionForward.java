package action;

public class ActionForward 
{
	// redirect : URL이 변경되며 요청이 바뀌게 된다. 서블릿 request 영역에 공유한 속성 갓에 접근 x
	private boolean isRedirect = false;
	private String path = null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect = b;
	}
	
	public void setPath(String string){
		path = string;
	}
}