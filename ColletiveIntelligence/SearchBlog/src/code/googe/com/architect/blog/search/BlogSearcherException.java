package code.googe.com.architect.blog.search;


// 서비스 루틴 예외 처리 
public class BlogSearcherException extends Exception {
	public BlogSearcherException(String message, Throwable cause) {
		super(message, cause);
	}
	public BlogSearcherException(String message) {
		super(message);
	}
}
